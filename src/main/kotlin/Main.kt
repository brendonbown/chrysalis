import com.xenomachina.argparser.ArgParser
import db.CesDb

fun main(args: Array<String>) {
    // Parse args
    ArgParser(args).parseInto(::ChrysalisArgs).run {
        // Get the NetID from the 'CHRYSALIS_NET_ID' environment variable
        // or else prompt for it
        //
        // This will be used later to generate the database username ("oit#$netId")
        val netId =
            System.getenv("CHRYSALIS_NET_ID") ?:
            prompt("NetID: ")

        // Get the Oracle database password from the 'CHRYSALIS_DB_PASSWORD' environment
        // variable (possible insecure, need to consider this more later) or else
        // prompt for it
        val password =
            System.getenv("CHRYSALIS_DB_PASSWORD") ?:
            prompt("Password: ", hideInput = true)

        // Check that both the NetID and the database password are valid inputs
        if (netId == null || password == null) {
            // If at least one is invalid, alert the user and end the program
            println("Unable to read username and/or password, please try again later")
            return
        }

        // From the NetID, generate the database username, then log in to the database,
        // creating a 'CesDb' access object that provides an interface through which
        // the user can interact with the database
        val username = "oit#$netId"
        val db = CesDb(username, password)

        // Perform the requested action
        when (action) {
            Action.LIST -> listAuthorizedAreas(db, netId)
            else -> printError("Unimplemented!")
        }
    }
}

fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.toString() ?: readLine()
    else
        readLine()
}