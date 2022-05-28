import args.Identifier
import db.CesDb

fun listAuthorizedAreas(db: CesDb, ident: Identifier) {
    // Get the Person ID associated with the NetID
    val personId = db.getPersonId(ident)

    // If there isn't an associated Person ID, print an error
    if (personId == null) {
        printError("Person ID not found for '$ident'. Ensure that it is correct, then retry.")
        return
    }

    // Get the list of authorized areas from the database
    val authorizedAreas = db.getAuthorizedAreas(personId)

    // Print out the results (if it's empty, just print out "(NONE)")
    if (authorizedAreas.isNotEmpty()) {
        println("Authorized areas:")
        for (area in authorizedAreas) {
            println("- $area")
        }
    } else {
        println("Authorized areas: (NONE)")
    }
}

fun addAuthorizedAreas(db: CesDb, ident: Identifier, areas: List<String>) {
    // Ensure that the list of areas isn't empty
    if (areas.isEmpty()) {
        printError("Must add at least one area")
        return
    }

    // Get the Person ID associated with the NetID
    val personId = db.getPersonId(ident)

    // If there isn't an associated Person ID, print an error
    if (personId == null) {
        printError("Person ID not found for '$ident'. Ensure that it is correct, then retry.")
        return
    }

    // Add the given list of areas
    areas.forEach { area -> println("adding $area"); db.addAuthorizedArea(personId, area) }
}

fun removeAuthorizedAreas(db: CesDb, ident: Identifier, areas: List<String>) {
    if (areas.isEmpty()) {
        printError("Must remove at least one area")
        return
    }

    // Get the Person ID associated with the NetID
    val personId = db.getPersonId(ident)

    // If there isn't an associated Person ID, print an error
    if (personId == null) {
        printError("Person ID not found for '$ident'. Ensure that it is correct, then retry.")
        return
    }

    // Add the given list of areas
    areas.forEach { area -> println("removing $area"); db.removeAuthorizedArea(personId, area) }
}