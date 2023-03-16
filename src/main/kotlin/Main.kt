import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import config.*
import db.ConnectionError
import java.sql.SQLException

fun main(args: Array<String>) = mainBody {
    try {
        // Try to get configuration from args
        Log.info("Parsing args")
        val configResult = ArgParser(args).parseInto(::ChrysalisArgs).let(::argsToConfig)

        configResult.fold(
            // If the configuration was successful, perform the requested action
            onSuccess = { config ->
                when (config) {
                    is ListActionConfig ->
                        listAuthorizedAreas(config.db, config.personId)
                    is AddActionConfig ->
                        addAuthorizedAreas(config.db, config.personId, config.areas)
                    is RemoveActionConfig ->
                        removeAuthorizedAreas(config.db, config.personId, config.areas)
                    is VersionActionConfig ->
                        printVersion()
                }
            },

            // If the configuration failed, print out the error
            onFailure = { error ->
                Log.error(error.message ?: "Unknown config error")
                System.err.println("ERROR: ${error.message}")
            }
        )
    } catch (e: SQLException) {
        val error = ConnectionError.fromErrorCode(e.errorCode)
        if (error == ConnectionError.UNKNOWN) {
            Log.error("Unknown SQL error code '${e.errorCode}': ${e.message}")
            System.err.println("An unknown error has occurred with the following message:")
            System.err.print("\t") // Indent the error message
            System.err.println(e.message)
            System.err.println()
            System.err.println("Error code: ${e.errorCode}")
        } else {
            Log.error("SQL error: ${error.toString().replace('\n', ' ')}")
            System.err.println(error.toString())
        }
    }
}