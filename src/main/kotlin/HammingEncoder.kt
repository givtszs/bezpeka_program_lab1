import kotlin.math.pow

class HammingEncoder {
    companion object {
        const val EIGHT_BIT = 8
        const val SIXTEEN_BIT = 16
    }

    fun toHamming(input: String, bits: Int): String {
        val binary = toBinary(input, bits)
        return binaryToHamming(binary)
    }

    fun toString(hamming: String): String {
        val binary = hammingToBinary(hamming)
        return binaryToString(binary)
    }

    fun toBinary(input: String, bits: Int): String {
        val binaryStringBuilder = StringBuilder()
        for (char in input) {
            var binary = Integer.toBinaryString(char.code)
            if (binary.length != bits) {
                binary = binary.padStart(bits, '0')
            }

            binaryStringBuilder
                .append(binary)
                .append(' ')
        }
        return binaryStringBuilder.trimEnd().toString()
    }

    private fun findParityBitsCount(inputLength: Int): Int {
        var i = 1
        while (2.0.pow(i) < inputLength + i + 1) {
            ++i
        }
        return i
    }

    private fun isPowerOfTwo(number: Int): Boolean = number > 0 && Integer.bitCount(number) == 1

    private fun binaryToHamming(input: String): String {
        val hammingCode: StringBuilder = StringBuilder()
        input.split(" ").forEach { str ->
            val hammingCodeTemp = calculateHammingSequence(str)
            hammingCode.append(hammingCodeTemp).append(' ')
        }

        return hammingCode.trim().toString()
    }

    private fun calculateHammingSequence(str: String): String {
        val dataBits = str.length
        val parityBits = findParityBitsCount(dataBits)
        val hammingCodeTemp = StringBuilder(dataBits + parityBits)

        // index of the current data bit
        var dataIndex = 0

        // placing bits
        for (i in 0 until dataBits + parityBits) {
            if (isPowerOfTwo(i + 1)) {
                // place a parity bit placeholder
                hammingCodeTemp.append('0')
            } else {
                hammingCodeTemp.append(str[dataIndex])
                ++dataIndex
            }
        }

        // computing parity bits
        calculateParityBits(hammingCodeTemp, parityBits)

        return hammingCodeTemp.toString()
    }

    private fun calculateParityBits(hammingCode: StringBuilder, parityBits: Int) {
        for (i in 0 until parityBits) {
            val parityBitIndex = 2.0.pow(i).toInt() - 1
            hammingCode[parityBitIndex] = computeParityBit(hammingCode, parityBitIndex)
        }
    }

    private fun computeParityBit(hammingCode: CharSequence, parityBitIndex: Int): Char {
        var parity = 0 // '0'
        for (i in parityBitIndex + 1 until hammingCode.length) {
            if ((i + 1 and (parityBitIndex + 1)) != 0) {
                parity = parity xor (hammingCode[i] - '0')
            }
        }

        return (parity + '0'.code).toChar()
    }

    fun checkAndCorrect(hammingCode: String): String {
        val restoredSequence = StringBuilder()
        hammingCode.split(' ').forEach { code ->
            val parityBits = findParityBitsCount(code.length)
            var failedBitsSum = 0

            for (i in 0 until parityBits) {
                val parityBitPosition = 2.0.pow(i).toInt()
                if (parityBitPosition > code.length) {
                    continue
                }

                val parityBit = computeParityBit(code, parityBitPosition - 1)
                if (code[parityBitPosition - 1] != parityBit) {
                    failedBitsSum += parityBitPosition
                }
            }

            // restore the corrupted bit
            val restoredCode = StringBuilder(code)
            if (failedBitsSum != 0) {
                restoredCode[failedBitsSum - 1] = if (code[failedBitsSum - 1] == '0') '1' else '0'
            }

            restoredSequence
                .append(restoredCode)
                .append(' ')
        }

        return restoredSequence.trim().toString()
    }

    fun hammingToBinary(hammingCode: String): String {
        val restoredBits = StringBuilder()
        hammingCode.split(' ').forEach { code ->
            for (i in code.indices) {
                if (!isPowerOfTwo(i + 1)) {
                    // Exclude positions that are powers of 2 (parity bits)
                    restoredBits.append(code[i])
                }
            }
            restoredBits.append(' ')
        }

        return restoredBits.trim().toString()
    }

    private fun binaryToChar(binary: String): Char {
        return Integer.parseInt(binary, 2).toChar()
    }

    fun binaryToString(binaryCode: String): String {
        if (binaryCode.isEmpty()) {
            return ""
        }

        val restoredString = StringBuilder()
        binaryCode.split(' ').forEach { code ->
            restoredString.append(binaryToChar(code))
        }
        return restoredString.toString()
    }

    fun flipBit(hammingCode: String, vararg bitIndices: Int): String {
        if (hammingCode.isEmpty()) {
            return ""
        }

        val modified = StringBuilder(hammingCode)
        bitIndices.forEach { bitIndex ->
            if (bitIndex < 1) {
                println("Bit to flip index is invalid")
                return hammingCode
            }

            val isSpace = hammingCode[bitIndex - 1] == ' '
            if (isSpace) {
                modified[bitIndex] = if (modified[bitIndex] == '1') '0' else '1'
            } else {
                modified[bitIndex - 1] = if (modified[bitIndex - 1] == '1') '0' else '1'
            }
        }

        return modified.toString()
    }
}