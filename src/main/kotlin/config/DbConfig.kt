package config

import args.ByuId
import args.Identifier
import args.NetId
import args.PersonId
import db.CesDb
import getDebugEnv
import prompt

/*
 * Get the configuration for the database
 * (The Person ID, BYU ID, and NetID are optional - if they aren't present
 *  the user's NetID is used)
 */
fun getDbConfig(personIdArg: PersonId?, byuIdArg: ByuId?, netIdArg: NetId?): Pair<CesDb, String> {
    // Get the NetID from the 'CHRYSALIS_NET_ID' environment variable
    // or else prompt for it
    //
    // If it doesn't work, throw a config exception
    //
    // This will be used later to generate the database username ("oit#$netId")
    Log.info("Getting user NetID")
    val userNetId =
        getDebugEnv("CHRYSALIS_NET_ID") ?:
        prompt("NetID: ") ?:
        throw ConfigException("Unable to read NetID, please try again later")
    Log.info("Received user NetID $userNetId")

    // Get the Oracle database password from the 'CHRYSALIS_DB_PASSWORD' environment
    // variable (possible insecure, need to consider this more later) or else
    // prompt for it
    //
    // If it doesn't work, throw a config exception
    Log.info("Getting user password")
    val password =
        getDebugEnv("CHRYSALIS_DB_PASSWORD") ?:
        prompt("Password: ", hideInput = true) ?:
        throw ConfigException("Unable to read password, please try again later")
    Log.info("Received user password")

    println()

    // From the NetID, generate the database username, then log in to the database,
    // creating a 'CesDb' access object that provides an interface through which
    // the user can interact with the database
    val username = "oit#$userNetId"
    Log.info("Loading database connection for user '$username'")
    val db = CesDb(username, password)
    Log.info("Loaded database connection for user '$username'")

    // Get the identifier used to add/remove/list authorizations
    //
    // Use the program args in this order:
    //   * --person-id/-p
    //   * --byu-id/-b
    //   * --net-id/-n
    //
    // If none are provided, use the current user's NetID
    val identifier =
        personIdArg ?:
        byuIdArg ?:
        netIdArg ?:
        NetId(userNetId)
    Log.info("Actions will be performed for $identifier")

    val personId =
        getPersonId(db, identifier)

    return Pair(db, personId)
}

/**
 * Get the Person ID associated with the identifier
 *
 * If there's no Person ID associated, throw an error
 */
private fun getPersonId(db: CesDb, identifier: Identifier) =
    db.getPersonId(identifier) ?:
    throw ConfigException("Person ID not found for '${identifier}'. Ensure that it is correct, then retry.")