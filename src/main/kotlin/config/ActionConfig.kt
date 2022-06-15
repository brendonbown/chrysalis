package config

import Action
import ChrysalisArgs

sealed class ActionConfig


class ListActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get user credentials for the database
    private val dbConfig = DbConfig(args.personId, args.byuId, args.netId)
    val personId = getPersonId(dbConfig)
    val db = dbConfig.db

}

class AddActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get user credentials for the database
    private val dbConfig = DbConfig(args.personId, args.byuId, args.netId)
    val personId = getPersonId(dbConfig)
    val areas = nonEmptyAreaList(args.action, args.areas)
    val db = dbConfig.db

}

class RemoveActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get user credentials for the database
    private val dbConfig = DbConfig(args.personId, args.byuId, args.netId)
    val personId = getPersonId(dbConfig)
    val areas = nonEmptyAreaList(args.action, args.areas)
    val db = dbConfig.db

}

class ProductPermActionConfig: ActionConfig() {
    val apiConfig = ApiConfig()
}

object VersionActionConfig : ActionConfig()

/**
 * Convert from a ChrysalisArgs object to a config object
 */
fun argsToConfig(args: ChrysalisArgs) =
    when (args.action) {
        Action.LIST -> ListActionConfig(args)
        Action.ADD -> AddActionConfig(args)
        Action.REMOVE -> RemoveActionConfig(args)
        Action.PRODUCT_PERM -> ProductPermActionConfig()
        Action.VERSION -> VersionActionConfig
    }

/**
 * Check if the `areas` list is non-empty
 */
private fun nonEmptyAreaList(action: Action, areas: List<String>) =
    areas.ifEmpty {
        throw ConfigException("'$action' requires at least one area")
    }

/**
 * Get the Person ID associated with the identifier
 *
 * If there's no Person ID associated, throw an error
 */
private fun getPersonId(dbConfig: DbConfig) =
    dbConfig.db.getPersonId(dbConfig.identifier) ?:
    throw ConfigException("Person ID not found for '${dbConfig.identifier}'. Ensure that it is correct, then retry.")