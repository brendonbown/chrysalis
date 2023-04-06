package config

import args.Action
import args.ChrysalisArgs
import args.NetId
import db.CesDb

sealed class ActionConfig

// LIST ACTION
class ListActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get database configs
    val db: CesDb
    val personId: String
    init {
        val dbConfig = getDbConfig(args.personId, args.byuId, args.netId)
        db = dbConfig.first
        personId = dbConfig.second
    }

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

// COPY ACTION
class CopyActionConfig(args: ChrysalisArgs): ActionConfig() {

    // Get database configs
    val db: CesDb
    val personId: String
    init {
        val dbConfig = getDbConfig(args.personId, args.byuId, args.netId)
        db = dbConfig.first
        personId = dbConfig.second
    }

    // Get the person to copy
    val fromNetId: String
    val fromPersonId: String
    init {
        // Uses the `areas` list because of limitations in argument parsing
        if (args.areas.size != 1) throw ConfigException("'copy' requires only one NetID")
        fromNetId = args.areas[0]
        fromPersonId = db.getPersonId(NetId(fromNetId)) ?: throw ConfigException("NetID '$fromNetId' doesn't exist")
    }
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
                Action.COPY -> CopyActionConfig(args)
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