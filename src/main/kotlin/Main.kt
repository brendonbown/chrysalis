import args.NetId
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import db.CesDb

fun main(args: Array<String>) = mainBody {
    // Parse args
    ArgParser(args).parseInto(::ChrysalisArgs).run {
        // Get the NetID from the 'CHRYSALIS_NET_ID' environment variable
        // or else prompt for it
        //
        // This will be used later to generate the database username ("oit#$netId")
        val userNetId =
            System.getenv("CHRYSALIS_NET_ID") ?: prompt("NetID: ")

        // Get the Oracle database password from the 'CHRYSALIS_DB_PASSWORD' environment
        // variable (possible insecure, need to consider this more later) or else
        // prompt for it
        val password =
            System.getenv("CHRYSALIS_DB_PASSWORD") ?: prompt("Password: ", hideInput = true)

        // Check that both the NetID and the database password are valid inputs
        if (userNetId == null || password == null)
            // If at least one is invalid, alert the user and end the program
            println("Unable to read username and/or password, please try again later")
        else {

            // From the NetID, generate the database username, then log in to the database,
            // creating a 'CesDb' access object that provides an interface through which
            // the user can interact with the database
            val username = "oit#$userNetId"
            val db = CesDb(username, password)

            // Get the identifier used to add/remove/list authorizations
            //
            // Use the program args in this order:
            //   * --person-id/-p
            //   * --byu-id/-b
            //   * --net-id/-n
            //
            // If none are provided, use the current user's NetID
            val identifier = personId ?: byuId ?: netId ?: NetId(userNetId)

            // Get the Person ID associated with the NetID
            val personId = db.getPersonId(identifier)

            if (personId == null)
                // If there isn't an associated Person ID, print an error
                printError("Person ID not found for '$identifier'. Ensure that it is correct, then retry.")
            else {

                // Ensure that the list of areas isn't empty if it is required
                val requiresAreas = when (action) {
                    Action.ADD, Action.REMOVE -> true
                    else -> false
                }

                if (requiresAreas && areas.isEmpty())
                    printError("'$action' requires at least one area")
                else
                    // Perform the requested action
                    when (action) {
                        Action.LIST -> listAuthorizedAreas(db, personId)
                        Action.ADD -> addAuthorizedAreas(db, personId, areas)
                        Action.REMOVE -> removeAuthorizedAreas(db, personId, areas)
                        else -> printError("Unimplemented!")
                    }
            }
        }
    }
}