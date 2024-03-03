import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class HammingEncoderTest {
    private val encoder = HammingEncoder()

    @Test
    fun strToBinary_withEightBitBlock_convertsStringToBinarySequence() {
        val input = "John"
        val expected = "01001010 01101111 01101000 01101110"

        val result = encoder.toBinary(input, HammingEncoder.EIGHT_BIT)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun strToBinary_withSixteenBitBlock_convertsStringToBinarySequence() {
        val input = "Іван"
        val expected = "0000010000000110 0000010000110010 0000010000110000 0000010000111101"

        val result = encoder.toBinary(input, HammingEncoder.SIXTEEN_BIT)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun strToBinary_withEmptyInput_returnsEmptyString() {
        val input = ""
        val result = encoder.toBinary(input, HammingEncoder.EIGHT_BIT)
        assertThat(result).isEmpty()
    }

    @Test
    fun toHamming_convertsLatinStringToHammingSequence() {
        val input = "Jo"
        // 01001010
        val expected = "110110001010 110111001111"

        val result = encoder.toHamming(input, HammingEncoder.EIGHT_BIT)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun toString_convertsHammingCodeToString() {
        val input = "110110001010 110111001111"
        // 01001010
        val expected = "Jo"

        val result = encoder.toString(input)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun checkAndCorrect_with1CorruptedBit_findsAndCorrectsSequence() {
        val input = "110110101010"
        val expected = "110110001010"

        val result = encoder.checkAndCorrect(input)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun checkAndCorrect_with2CorruptedBits_doesNotCorrectSequence() {
        val input = "110111101010"
        val expected = "110110001010"

        val result = encoder.checkAndCorrect(input)

        assertThat(result).isNotEqualTo(expected)
    }

    @Test
    fun hammingToBinary_convertsHammingSequenceToBinary() {
        val input = "110110001010 110111001111"
        // 01001010
        val expected = "01001010 01101111"

        val result = encoder.hammingToBinary(input)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun hammingToBinary_withEmptyInput_returnsEmptyString() {
        val input = ""
        val result = encoder.hammingToBinary(input)

        assertThat(result).isEmpty()
    }

    @Test
    fun binaryToString_convertsBinarySequenceToString() {
        val input = "01001010 01101111 01101000 01101110"
        val expected = "John"

        val result = encoder.binaryToString(input)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun binaryToString_withEmptyInput_returnsEmptyString() {
        val input = ""
        val result = encoder.binaryToString(input)

        assertThat(result).isEmpty()
    }

    @Test
    fun flipBit_invertsTheBitInHammingSequence() {
        var input = "110110001010"
        var bitToChange = 3
        var expected = "111110001010"
        var result = encoder.flipBit(input, bitToChange)

        assertThat(result).isEqualTo(expected)

        input = "110110001010 110110001010"
        bitToChange = 13
        expected = "110110001010 010110001010"
        result = encoder.flipBit(input, bitToChange)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun flipBit_withEmptyInput_returnsEmptyString() {
        val input = ""
        val bitToChange = 2
        val result = encoder.flipBit(input, bitToChange)

        assertThat(result).isEmpty()
    }
}