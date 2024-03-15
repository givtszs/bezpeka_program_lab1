/**
 * The class implements the encryption leveraging the DES algorithm.
 * The Data Encryption Standard is a symmetric-key algorithm for the encryption of digital data.
 */
class DesEncryptor {

    companion object {
        private const val BITS_64 = 64
        private const val BITS_32 = 32
        private const val KEY_BITS_48 = 48
        private const val KEY_BITS_56 = 56
        private const val ROUNDS = 16
        private const val S_BOXES = 8
    }

    private val keyPermutation = intArrayOf(
        57,
        49,
        41,
        33,
        25,
        17,
        9,
        1,
        58,
        50,
        42,
        34,
        26,
        18,
        10,
        2,
        59,
        51,
        43,
        35,
        27,
        19,
        11,
        3,
        60,
        52,
        44,
        36,
        63,
        55,
        47,
        39,
        31,
        23,
        15,
        7,
        62,
        54,
        46,
        38,
        30,
        22,
        14,
        6,
        61,
        53,
        45,
        37,
        29,
        21,
        13,
        5,
        28,
        20,
        12,
        4
    )

    private val keyShiftsPerRound = intArrayOf(1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1)

    /** Compression of a key from 56 bits to 48 bits **/
    private val keyCompressionPermutation = intArrayOf(
        14, 17, 11, 24, 1, 5, 3, 28, 15, 6, 21, 10, 23, 19, 12, 4,
        26, 8, 16, 7, 27, 20, 13, 2, 41, 52, 31, 37, 47, 55, 30, 40,
        51, 45, 33, 48, 44, 49, 39, 56, 34, 53, 46, 42, 50, 36, 29, 32
    )

    private val initialPermutation = intArrayOf(
        58, 50, 42, 34, 26, 18, 10, 2, 60, 52, 44, 36, 28, 20, 12, 4,
        62, 54, 46, 38, 30, 22, 14, 6, 64, 56, 48, 40, 32, 24, 16, 8,
        57, 49, 41, 33, 25, 17, 9, 1, 59, 51, 43, 35, 27, 19, 11, 3,
        61, 53, 45, 37, 29, 21, 13, 5, 63, 55, 47, 39, 31, 23, 15, 7
    )

    /** Expansion Box **/
    private val eBox = intArrayOf(
        32, 1, 2, 3, 4, 5, 4, 5, 6, 7, 8, 9, 8, 9, 10, 11,
        12, 13, 12, 13, 14, 15, 16, 17, 16, 17, 18, 19, 20, 21, 20, 21,
        22, 23, 24, 25, 24, 25, 26, 27, 28, 29, 28, 29, 30, 31, 32, 1
    )

    /** Substitution box **/
    private val sBox = arrayOf(
        arrayOf(
            intArrayOf(14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7),
            intArrayOf(0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8),
            intArrayOf(4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0),
            intArrayOf(15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13)
        ),
        arrayOf(
            intArrayOf(15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10),
            intArrayOf(3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5),
            intArrayOf(0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15),
            intArrayOf(13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9)
        ),
        arrayOf(
            intArrayOf(10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8),
            intArrayOf(13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1),
            intArrayOf(13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7),
            intArrayOf(1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12)
        ),
        arrayOf(
            intArrayOf(7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15),
            intArrayOf(13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9),
            intArrayOf(10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4),
            intArrayOf(3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14)
        ),
        arrayOf(
            intArrayOf(2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9),
            intArrayOf(14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6),
            intArrayOf(4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14),
            intArrayOf(11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3)
        ),
        arrayOf(
            intArrayOf(12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11),
            intArrayOf(10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8),
            intArrayOf(9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6),
            intArrayOf(4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13)
        ),
        arrayOf(
            intArrayOf(4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1),
            intArrayOf(13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6),
            intArrayOf(1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2),
            intArrayOf(6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12)
        ),
        arrayOf(
            intArrayOf(13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7),
            intArrayOf(1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2),
            intArrayOf(7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8),
            intArrayOf(2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11)
        )
    )

    /** Straight permutation box **/
    private val pBox = intArrayOf(
        16, 7, 20, 21, 29, 12, 28, 17, 1, 15, 23, 26, 5, 18, 31, 10,
        2, 8, 24, 14, 32, 27, 3, 9, 19, 13, 30, 6, 22, 11, 4, 25
    )

    private val finalPermutation = intArrayOf(
        40, 8, 48, 16, 56, 24, 64, 32, 39, 7, 47, 15, 55, 23, 63, 31,
        38, 6, 46, 14, 54, 22, 62, 30, 37, 5, 45, 13, 53, 21, 61, 29,
        36, 4, 44, 12, 52, 20, 60, 28, 35, 3, 43, 11, 51, 19, 59, 27,
        34, 2, 42, 10, 50, 18, 58, 26, 33, 1, 41, 9, 49, 17, 57, 25
    )


    private fun hexToBin(hex: String): String {
        val hexToBinMapper = mapOf(
            '0' to "0000", '1' to "0001", '2' to "0010", '3' to "0011",
            '4' to "0100", '5' to "0101", '6' to "0110", '7' to "0111",
            '8' to "1000", '9' to "1001", 'A' to "1010", 'B' to "1011",
            'C' to "1100", 'D' to "1101", 'E' to "1110", 'F' to "1111"
        )

        val binary = StringBuilder()
        hex.forEach {
            binary.append(hexToBinMapper[it] ?: "")
        }

        return binary.toString()
    }

    private fun binToHex(binary: String): String {
        val binToHexMapper = mapOf(
            "0000" to '0', "0001" to '1', "0010" to '2', "0011" to '3',
            "0100" to '4', "0101" to '5', "0110" to '6', "0111" to '7',
            "1000" to '8', "1001" to '9', "1010" to 'A', "1011" to 'B',
            "1100" to 'C', "1101" to 'D', "1110" to 'E', "1111" to 'F'
        )
        val hex = StringBuilder()
        for (i in binary.indices step 4) {
            val ch = binary.substring(i, i + 4)
            hex.append(binToHexMapper[ch] ?: "")
        }
        return hex.toString()
    }

    /**
     * Converts the [binary] string to a decimal number.
     */
    private fun binToDec(binary: String): Int = binary.toInt(2)

    private fun decToBin(decimal: Int): String = Integer.toBinaryString(decimal)

    /**
     * Permutes the [input] block with the [permutationArr] returning a new block of size [bits].
     */
    private fun permute(input: String, permutationArr: IntArray, bits: Int): String {
        val permutation = StringBuilder()
        for (i in 0 until bits) {
            permutation.append(input[permutationArr[i] - 1])
        }
        return permutation.toString()
    }

    /**
     * Performs XOR operation on two binary strings
     *
     * @param a the first binary string
     * @param b the second binary string
     * @return the XORed binary string
     */
    private fun xor(a: String, b: String): String {
        val result = StringBuilder()
        a.indices.forEach { i ->
            if (a[i] == b[i]) {
                result.append("0")
            } else {
                result.append("1")
            }
        }

        return result.toString()
    }

    /**
     * Performs the S-Box Substitution ...
     */
    private fun sBoxSubstitution(binStr: String): String {
        val substituteResult = StringBuilder()
        repeat(S_BOXES) { i ->
            val row = binToDec("${binStr[i * 6]}${binStr[i * 6 + 5]}")
            val col = binToDec(binStr.substring(i * 6 + 1, i * 6 + 5))
            val value = sBox[i][row][col]
            substituteResult.append(decToBin(value).padStart(4, '0'))
        }
        return substituteResult.toString()
    }

    private fun roundFunction(rpt: String, roundKeyBin: String): String {
        // expand the 32-bit RPT into a 48-bit block
        val rptExpanded = permute(rpt, eBox, KEY_BITS_48)

        // XOR the expanded RPT with the current round key
        val keyXorResult = xor(rptExpanded, roundKeyBin)

        // perform the S-Box substitution on the XORed result
        val sBoxResult = sBoxSubstitution(keyXorResult)

        // perform straight permutation on the substitution result
        val straightPermutationResult = permute(sBoxResult, pBox, BITS_32)

        return straightPermutationResult
    }

    private fun shiftLeft(key: String, shifts: Int): String {
        var s = ""
        var keyTemp = key
        repeat(shifts) {
            for (j in 1 until keyTemp.length) {
                s += keyTemp[j]
            }

            s += keyTemp[0]
            keyTemp = s
            s = ""
        }
        return keyTemp
    }

    /**
     * Generates the keys for each round of the algorithm based on the input [key].
     *
     * @param key the hexadecimal key
     * @return the list of binary keys
     */
    private fun generateKeys(key: String): List<String> {
        val keyBin = hexToBin(key)

        // reduce the 64-bit key to the 56-bit key by ignoring every eighth bit
        val key56 = permute(keyBin, keyPermutation, KEY_BITS_56)

        // split the key into 28-bit halves
        var left = key56.substring(0, KEY_BITS_56 / 2)
        var right = key56.substring(KEY_BITS_56 / 2, KEY_BITS_56)

        val roundKeysBinary = mutableListOf<String>()
        repeat(ROUNDS) { i ->
            // shifting the bits by n-th shifts by checking from shift table
            left = shiftLeft(left, keyShiftsPerRound[i])
            right = shiftLeft(right, keyShiftsPerRound[i])

            // combination of left and right string
            val combineStr = left + right

            // compression of key from 56 to 48 bits
            val roundKey = permute(combineStr, keyCompressionPermutation, KEY_BITS_48)

            roundKeysBinary.add(roundKey)
        }

        return roundKeysBinary
    }

    /**
     * Encrypts the [message][text] using the DES algorithm.
     *
     * @param text the message in hexadecimal numeric system
     * @param key the hexadecimal key
     * @return the encrypted message in hexadecimal format
     */
    fun encrypt(text: String, key: String): String {
        val roundKeysBinary = generateKeys(key)
        var inputBin = hexToBin(text)

        // initial permutation
        inputBin = permute(inputBin, initialPermutation, BITS_64)
        println("After initial permutation: ${binToHex(inputBin)}")

        // split into LPT and RPT
        val lpt = inputBin.substring(0, BITS_32) // RPT - Right Plain Text
        val rpt = inputBin.substring(BITS_32, BITS_64) // LPT - Left Plain Text
        var lptTemp = lpt
        var rptTemp = rpt

        // 16 rounds of encryption
        repeat(ROUNDS) { i ->
            val functionResult = roundFunction(rptTemp, roundKeysBinary[i])

            // XOR the LPT with the straight permutation result
            lptTemp = xor(lptTemp, functionResult)

            if (i != ROUNDS - 1) {
                // if the round is < 16, switch the LPT and RPT
                lptTemp = rptTemp.also { rptTemp = lptTemp }
            }

            println("Round ${i + 1} ${binToHex(lptTemp)} ${binToHex(rptTemp)}")
        }

        // concatenate the LPT with the RPT
        val concatBlock = lptTemp + rptTemp

        // perform the final permutation on the concatenated LPT and RPT blocks
        val cipherText = permute(concatBlock, finalPermutation, BITS_64)

        return binToHex(cipherText)
    }

    object Helper {
        fun strToHex(text: String): String {
            val tmp = if (text.length < 8) text.padEnd(8, ' ') else text
            return tmp
                .toByteArray().joinToString("") { byte ->
                    "%02x".format(byte) // converts the byte to its corresponding hex value
                }
                .uppercase()
        }

    }

}