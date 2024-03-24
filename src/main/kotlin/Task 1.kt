import DesEncryptor.Helper.toHex

fun main() {
    encryptName("Ivan")
    encryptLastName("Havrysh")
    encryptMiddleName("Vasylovych")
}

fun encryptName(name: String) {
    val encryptor = DesEncryptor(rounds = 1)
    val key = "password"

    val textHex = name.toHex()
    val keyHex = key.toHex()

    val result = encryptor.encrypt(textHex, keyHex)

    println("Name: $name")
    println("Rounds: ${encryptor.rounds}")
    println("Encrypted name: $result\n")
}

fun encryptLastName(name: String) {
    val encryptor = DesEncryptor(rounds = 1)
    val key = "password"

    val textHex = name.toHex()
    val keyHex = key.toHex()

    val result = encryptor.encrypt(textHex, keyHex)

    println("Last name: $name")
    println("Rounds: ${encryptor.rounds}")
    println("Encrypted name: $result\n")
}

fun encryptMiddleName(name: String) {
    val encryptor = DesEncryptor(rounds = 1)
    val key = "password"

    val textHex = DesEncryptor.Helper.strToHexList(name)
    val keyHex = key.toHex()

    val result = encryptor.encrypt(textHex, keyHex)

    println("Middle name: $name")
    println("Rounds: ${encryptor.rounds}")
    println("Encrypted name: $result")
}