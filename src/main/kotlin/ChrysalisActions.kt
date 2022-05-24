import db.CesDb

fun listAuthorizedAreas(db: CesDb, netId: String) {
    // Get the Person ID associated with the NetID
    val personId = db.getPersonId(netId)

    // If there isn't an associated Person ID, print an error
    if (personId == null) {
        printError("Person ID not found for Net ID '$netId'. Ensure that your Net ID is correct, then retry.")
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