import db.CesDb

fun listAuthorizedAreas(db: CesDb, netId: String) {
    // Get the
    val personId = db.getPersonId(netId)

    if (personId == null) {
        printError("Person ID not found for Net ID '$netId'. Ensure that your Net ID is correct, then retry.")
        return
    }

    val authorizedAreas = db.getAuthorizedAreas(personId)

    if (authorizedAreas.isNotEmpty()) {
        println("Authorized areas:")
        for (area in authorizedAreas) {
            println("- $area")
        }
    } else {
        println("Authorized areas: (NONE)")
    }
}