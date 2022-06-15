import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.mainBody
import config.*
import db.ConnectionError
import java.sql.SQLException

fun main(args: Array<String>) = mainBody {
    /*
    val api = ApiAccess("8bfe1cdacb587bc2a62ccdb4b4b543")
    val webResInfo = api.getWebResourceInfo("ADV020")
    println(webResInfo)
    val webResId = webResInfo!!.content[0].webResourceId
    val webResPolicies = api.getWebResourcePolicies(webResId)
    println(webResPolicies)
     */
    try {
        // Try to get configuration from args
        val config = try {
            ArgParser(args).parseInto(::ChrysalisArgs).let(::argsToConfig)
        } catch (e: ConfigException) {
            System.err.println("ERROR: ${e.message}")
            return@mainBody
        }
        when (config) {
            is ListActionConfig ->
                listAuthorizedAreas(config.db, config.personId)
            is AddActionConfig ->
                addAuthorizedAreas(config.db, config.personId, config.areas)
            is RemoveActionConfig ->
                removeAuthorizedAreas(config.db, config.personId, config.areas)
            is ProductPermActionConfig ->
                println("Unimplemented!")//printProductPermissions()
            is VersionActionConfig ->
                printVersion()
        }
    } catch (e: SQLException) {
        val error = ConnectionError.fromErrorCode(e.errorCode)
        if (error == ConnectionError.UNKNOWN) {
            System.err.println("An unknown error has occurred with the following message:")
            System.err.print("\t") // Indent the error message
            System.err.println(e.message)
            System.err.println()
            System.err.println("Error code: ${e.errorCode}")
        } else {
            System.err.println(error.toString())
        }
    }
}