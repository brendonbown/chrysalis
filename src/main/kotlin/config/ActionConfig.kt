package config

import args.Action
import args.ChrysalisArgs
import db.CesDb

sealed class ActionConfig

// LIST ACTION
class ListActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get database configs
    private val dbConfig = getDbConfig(args.personId, args.byuId, args.netId)
    val db = dbConfig.first
    val personId = dbConfig.second

}

// ADD ACTION
class AddActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get database configs
    val db: CesDb
    val personId: String
    init {
        val dbConfig = getDbConfig(args.personId, args.byuId, args.netId)
        db = dbConfig.first
        personId = dbConfig.second
    }

    // Get the areas to add
    val areas = nonEmptyAreaSet(args.action, args.areas)

}

// REMOVE ACTION
class RemoveActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get database configs
    val db: CesDb
    val personId: String
    init {
        val dbConfig = getDbConfig(args.personId, args.byuId, args.netId)
        db = dbConfig.first
        personId = dbConfig.second
    }

    // Get the areas to remove
    val areas = nonEmptyAreaSet(args.action, args.areas)

}

// VERSION ACTION
object VersionActionConfig : ActionConfig()

/**
 * Convert from a args.ChrysalisArgs object to a config object
 *
 * This handles any configuration errors as well, returning
 * a Result object for the user to handle
 */
fun argsToConfig(args: ChrysalisArgs) =
    try {
        Result.success(
            when (args.action) {
                Action.LIST -> ListActionConfig(args)
                Action.ADD -> AddActionConfig(args)
                Action.REMOVE -> RemoveActionConfig(args)
                Action.VERSION -> VersionActionConfig
            }
        )
    } catch(e: ConfigException) {
        Result.failure(e)
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
            throw ConfigException("'$action' requires at least one area or product")
        }