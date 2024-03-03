package task2

import HammingEncoder
import HammingEncoder.Companion.EIGHT_BIT
import HammingEncoder.Companion.SIXTEEN_BIT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileNotFoundException
import kotlin.system.exitProcess
import kotlin.system.measureTimeMillis

const val inputLorem = "src/main/kotlin/task2/input_lorem.txt"
const val inputEng = "src/main/kotlin/task2/input_eng.txt"
const val inputUkr = "src/main/kotlin/task2/input_ukr.txt"
const val outputEncoded = "src/main/kotlin/task2/output_encoded.txt"
const val outputDecoded = "src/main/kotlin/task2/output_decoded.txt"

var numberOfBits: Int = -1
var input: String? = null
lateinit var inputFilePath: String

fun main() = runBlocking {
    selectBitsNumber()
    selectAction()
}

private fun selectBitsNumber() {
    println(
        "Select the size of the bit block:\n" +
                "1 - 8-bit\n" +
                "2 - 16-bit"
    )

    while (input == null) {
        input = readlnOrNull()
    }

    when (input) {
        "1" -> {
            println("Bit block: $EIGHT_BIT-bit\n")
            numberOfBits = EIGHT_BIT
            inputFilePath = inputEng
        }

        "2" -> {
            println("Bit block: $SIXTEEN_BIT-bit\n")
            numberOfBits = SIXTEEN_BIT
            inputFilePath = inputUkr
        }

        else -> {
            println("$EIGHT_BIT-bit block is selected by default\n")
            numberOfBits = EIGHT_BIT
            inputFilePath = inputLorem
        }
    }
}

private suspend fun selectAction() {
    println(
        "Select an action:\n" +
                "1 - Encode and decode a file\n" +
                "2 - Encode, corrupt and decode file"
    )

    input = null
    while (input == null) {
        input = readlnOrNull()
    }

    when (input) {
        "1" -> {
            println("You've selected Option 1\n")
            performEncodeDecode()
        }

        "2" -> {
            println("You've selected Option 2\n")
            encodeCorruptRestoreDecode()
        }

        else -> {
            println("Option 1 is selected by default\n")
            performEncodeDecode()
        }
    }
}

suspend fun performEncodeDecode() {
    encodeFile(inputFilePath, outputEncoded)
    decodeFile(outputEncoded, outputDecoded)
}

suspend fun encodeCorruptRestoreDecode() {
    encodeFile(inputFilePath, outputEncoded)
    corruptFile()
    decodeFile(outputEncoded, outputDecoded, checkAndCorrect = false)
    correctFile()
    decodeFile(outputEncoded, outputDecoded, checkAndCorrect = true)
}

suspend fun encodeFile(readFrom: String, writeInto: String) = withContext(Dispatchers.IO) {
    println("Encoding the file...")
    val encoder = HammingEncoder()
    val fileToWrite = File(writeInto).also {
        it.delete() // clear the file
    }
    try {
        File(readFrom).forEachLine { line ->
            val hamming = encoder.toHamming(line, numberOfBits)
            fileToWrite.appendText("$hamming\n")
        }
        println("Encoded the file")
    } catch (e: FileNotFoundException) {
        println(e.message)
    } catch (e: Exception) {
        println(e.message)
    }
}

suspend fun decodeFile(readFrom: String, writeInto: String, checkAndCorrect: Boolean = true) =
    withContext(Dispatchers.IO) {
        println("Decoding the file...")
        val encoder = HammingEncoder()
        val fileToWrite = File(writeInto).also {
            it.delete()
        }
        try {
            if (checkAndCorrect) {
                File(readFrom).forEachLine { line ->
                    val corrected = encoder.checkAndCorrect(line)
                    val str = encoder.toString(corrected)
                    fileToWrite.appendText("$str\n")
                }
            } else {
                File(readFrom).forEachLine { line ->
                    val str = encoder.toString(line)
                    fileToWrite.appendText("$str\n")
                }
            }
            println("Decoded the file")
        } catch (e: FileNotFoundException) {
            println(e.message)
        } catch (e: Exception) {
            println(e.message)
        }
    }

suspend fun corruptFile() = coroutineScope {
    println("Now, corrupt the encoded file! Press ENTER when you're ready")
    var input: String? = null
    while (input == null) {
        input = readlnOrNull()
    }
    println("Nicely done")
}

suspend fun correctFile() = coroutineScope {
    println("Take your time and check that the file is really corrupted\n" +
            "Press Enter when you're ready to proceed")
    var input: String? = null
    while (input == null) {
        input = readlnOrNull()
    }
    println("Now, I will do my best to restore the initial data")
}