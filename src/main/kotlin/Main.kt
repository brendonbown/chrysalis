import db.CesDb

fun main() {

    val username = System.getenv("CHRYSALIS_DB_USERNAME") ?: prompt("Username: ")
    val password = System.getenv("CHRYSALIS_DB_PASSWORD") ?: prompt("Password: ")

    if (username != null && password != null) {
        val db = CesDb(username, password)
        println("Hello $username!")
        db.getInfo()
    } else {
        println("Invalid username and/or password, please try again later")
    }
}

fun prompt(promptText: String): String? {
    print(promptText)
    return readLine()
}