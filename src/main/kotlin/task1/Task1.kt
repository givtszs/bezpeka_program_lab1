package task1

import HammingEncoder
import HammingEncoder.Companion.EIGHT_BIT
import HammingEncoder.Companion.SIXTEEN_BIT

const val N = 6

fun main() {
    val encoder = HammingEncoder()

    val name = "John"
    println("Name: $name")

    val nameBinary8 = encoder.toBinary(name, EIGHT_BIT)
    println("8-bit binary: $nameBinary8")

    val nameHamming = encoder.toHamming(name, EIGHT_BIT)
    println("Hamming code: $nameHamming")

    val modifiedHammingCode = encoder.flipBit(nameHamming, N, 35 - N)
    println("Corrupted hamming code: $modifiedHammingCode")

    val restoredHammingCode = encoder.checkAndCorrect(modifiedHammingCode)
    println("Restored hamming code: $restoredHammingCode")

    val binFromHamming = encoder.hammingToBinary(restoredHammingCode)
    println("Hamming code to binary: $binFromHamming")

    val strFromBin = encoder.binaryToString(binFromHamming)
    println("Binary to string: $strFromBin")

    val cyrillicToHamming = encoder.toHamming("Іван", SIXTEEN_BIT)
    println("Cyrillic to Hamming: $cyrillicToHamming")

    val hammingToCyrillic = encoder.toString(cyrillicToHamming)
    println("Hamming to cyrillic: $hammingToCyrillic")
}
//
//fun String.toAscii(): List<Int> = map { it.code }
//
//fun strToHexadecimal(input: String): String =
//    if (input.isNotEmpty()) {
//        input.toAscii().joinToString("") { it.toString(16).uppercase() }
//    } else {
//        ""
//    }
//
//fun strToBinary(input: String): String {
//    val binaryStringBuilder = StringBuilder()
//    for (char in input) {
//        var binary = Integer.toBinaryString(char.code)
//        if (binary.length != EIGHT_BIT) {
//            binary = binary.padStart(EIGHT_BIT, '0')
//        }
//
//        binaryStringBuilder
//            .append(binary)
//            .append(' ')
//    }
//    return binaryStringBuilder.trimEnd().toString()
//}
//
//fun findParityBitsCount(inputLength: Int): Int {
//    var i = 1
//    while (2.0.pow(i) < inputLength + i + 1) {
//        ++i
//    }
//    return i
//}
//
//fun isPowerOfTwo(number: Int): Boolean = number > 0 && Integer.bitCount(number) == 1
//
//fun binaryToHamming(input: String): String {
//    val hammingCode: StringBuilder = StringBuilder()
//    input.split(" ").forEach { str ->
//        val hammingCodeTemp = calculateHammingSequence(str)
//        hammingCode.append(hammingCodeTemp).append(' ')
//    }
//
//    return hammingCode.trim().toString()
//}
//
//fun calculateHammingSequence(str: String): String {
//    val dataBits = str.length
//    val parityBits = findParityBitsCount(dataBits)
//    val hammingCodeTemp = StringBuilder(dataBits + parityBits)
//
//    // index of the current data bit
//    var dataIndex = 0
//
//    // placing bits
//    for (i in 0 until dataBits + parityBits) {
//        if (isPowerOfTwo(i + 1)) {
//            // place a parity bit placeholder
//            hammingCodeTemp.append('0')
//        } else {
//            hammingCodeTemp.append(str[dataIndex])
//            ++dataIndex
//        }
//    }
//
//    // computing parity bits
//    calculateParityBits(hammingCodeTemp, parityBits)
//
//    return hammingCodeTemp.toString()
//}
//
//fun calculateParityBits(hammingCode: StringBuilder, parityBits: Int) {
//    for (i in 0 until parityBits) {
//        val parityBitIndex = 2.0.pow(i).toInt() - 1
//        val parityBit = computeParityBit(hammingCode, parityBitIndex)
//        hammingCode[parityBitIndex] = parityBit
//    }
//}
//
//fun computeParityBit(hammingCode: CharSequence, parityBitIndex: Int): Char {
//    var parity = 0 // '0'
//    for (i in hammingCode.indices) {
//        if ((i + 1 and (parityBitIndex + 1)) != 0) {
//            parity = parity xor (hammingCode[i] - '0')
//        }
//    }
//
//    return (parity + '0'.code).toChar()
//}
//
//fun flipBit(hammingCode: String, vararg bitIndices: Int): String {
//    val modified = StringBuilder(hammingCode)
//    bitIndices.forEach { bitIndex ->
//        if (bitIndex < 1) {
//            println("Bit to flip index is invalid")
//            return hammingCode
//        }
//
//        val isSpace = hammingCode[bitIndex - 1] == ' '
//        if (isSpace) {
//            modified[bitIndex] = if (modified[bitIndex] == '1') '0' else '1'
//        } else {
//            modified[bitIndex - 1] = if (modified[bitIndex - 1] == '1') '0' else '1'
//        }
//    }
//
//    return modified.toString()
//}
//
//fun restoreHammingCode(hammingCode: String): String {
//    val restoredSequence = StringBuilder()
//    hammingCode.split(' ').forEach { code ->
//        val restoredCode = StringBuilder(code)
//        val parityBits = findParityBitsCount(restoredCode.length)
//        var failedBitsSum = 0
//
//        for (i in 0 until parityBits) {
//            val parityBitIndex = 2.0.pow(i).toInt()
//            if (parityBitIndex > restoredCode.length) {
//                continue
//            }
//
//            val tmp = StringBuilder(restoredCode)
//            if (tmp[parityBitIndex - 1] == '1') {
//                tmp[parityBitIndex - 1] =  '0' // for the correct calculation of a parity bit value
//            }
//
//            val parityBit = computeParityBit(tmp, parityBitIndex - 1)
//
//            if (restoredCode[parityBitIndex - 1] != parityBit) {
//                failedBitsSum += parityBitIndex
//            }
//        }
//
//        if (failedBitsSum != 0) {
//            restoredCode[failedBitsSum - 1] = if (restoredCode[failedBitsSum - 1] == '0') '1' else '0'
//        }
//
//        restoredSequence
//            .append(restoredCode)
//            .append(' ')
//    }
//
//    return restoredSequence.trim().toString()
//}
//
//fun hammingCodeToBinary(hammingCode: String): String {
//    val restoredBits = StringBuilder()
//    hammingCode.split(' ').forEach { code ->
//        for (i in code.indices) {
//            if (!isPowerOfTwo(i + 1)) {
//                // Exclude positions that are powers of 2 (parity bits)
//                restoredBits.append(code[i])
//            }
//        }
//        restoredBits.append(' ')
//    }
//
//    return restoredBits.trim().toString()
//}
//
//fun binaryToChar(binary: String): Char {
//    return Integer.parseInt(binary, 2).toChar()
//}
//
//fun binaryToString(binaryCode: String): String {
//    val restoredString = StringBuilder()
//    binaryCode.split(' ').forEach { code ->
//        restoredString.append(binaryToChar(code))
//    }
//    return restoredString.toString()
//}