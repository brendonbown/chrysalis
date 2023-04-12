import model.Action
import model.Identifier
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import db.ConnectionError
import task.Task
import task.TaskException
import user.Log
import java.sql.SQLException

fun main(args: Array<String>) = mainBody("chrysalis") {
    try {
        // Try to get configuration from args
        val argsResult = ArgParser(args).parseInto(::ChrysalisArgs)

        // Set up logging if requested
        if (argsResult.debug || argsResult.verbose >= 2) {
            Log.useDebugLevel()
            Log.info("Using debug level logging")
        } else if (argsResult.verbose == 1) {
            Log.useVerboseLevel()
            Log.info("Using verbose level logging")
        }

        // Short circuit for 'version' action (no need to connect to the database)
        if (argsResult.action == Action.VERSION) {
            printVersion()
            return@mainBody
        }

        argsResult.run {
            // Get database configuration
            val (db, personId) = Task.getDbConfig(personId, byuId, netId)

            when (action) {
                Action.LIST -> listAuthorizedAreas(db, personId)
                Action.ADD -> addAuthorizedAreas(db, personId, nonEmptyAreaSet(action, areas))
                Action.COPY -> {
                    // Uses the `areas` list because of limitations in argument parsing
                    if (areas.size != 1) throw TaskException("'copy' requires only one NetID")
                    val fromNetId = areas[0]
                    val fromPersonId =
                        db.getPersonId(Identifier.NetId(fromNetId)) ?: throw TaskException("NetID '$fromNetId' doesn't exist")

                    copyAuthorizedAreas(db, personId, fromNetId, fromPersonId)
                }
                Action.REMOVE -> removeAuthorizedAreas(db, personId, nonEmptyAreaSet(action, areas))
                Action.VERSION -> throw IllegalArgumentException("unreachable: 'version' action should already be handled")
            }
        }
    } catch (e: TaskException) {
        Log.error(e.message ?: "Unknown config error")
        System.err.println("ERROR: ${e.message}")
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

/**
 * Check if the `areas` list or 'product areas' list is non-empty
 */
private fun nonEmptyAreaSet(action: Action, vararg areas: List<String>) =
    // If the result is empty, throw a config error
    areas
        .reduce { acc, newAreas ->
            acc.plus(newAreas)
        }
        .toSet()
        .ifEmpty {
            throw TaskException("'$action' requires at least one area or product")
        }