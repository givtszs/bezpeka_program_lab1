import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Test

class DesEncryptorTest {
    private val encryptor = DesEncryptor()

    @Test
    fun test1() {
        val text = "0123456789ABCDEF"
        val key = "FEFEFEFEFEFEFEFE"
        val expected = "6DCE0DC9006556A3"
        val keys = encryptor.generateKeys(key)

        val result = encryptor.encrypt(text, keys)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun test2() {
        val text = "0000000000000000"
        val key = "0000000000000000"
        val expected = "8CA64DE9C1B123A7"
        val keys = encryptor.generateKeys(key)

        val result = encryptor.encrypt(text, keys)

        assertThat(result).isEqualTo(expected)
    }

    @Test
    fun test3() {
        val text = "0123456789ABCDEF"
        val key = "FEDCBA9876543210"
        val expected = "ED39D950FA74BCC4"
        val keys = encryptor.generateKeys(key)

        val result = encryptor.encrypt(text, keys)

        assertThat(result).isEqualTo(expected)
    }
}