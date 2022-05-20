import db.CesDb

fun main() {

    val netId =
        System.getenv("CHRYSALIS_NET_ID") ?:
        prompt("NetID: ")
    val password =
        System.getenv("CHRYSALIS_DB_PASSWORD") ?:
        prompt("Password: ", hideInput = true)

    if (netId != null && password != null) {
        val username = "oit#$netId"
        val db = CesDb(username, password)
        val personId = db.getPersonId(netId)

        if (personId != null) {
            println(db.getAuthorizedAreas(personId))
            db.addAuthorizedArea(personId, "GRADADMOGS")
            println(db.getAuthorizedAreas(personId))
//            db.removeAuthorizedArea(personId, "GRADADMOGS")
            println(db.getAuthorizedAreas(personId))
        } else
            println("NetID is invalid!")
    } else {
        println("Invalid username and/or password, please try again later")
    }
}

fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.toString() ?: readLine()
    else
        readLine()
}