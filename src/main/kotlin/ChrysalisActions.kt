import db.CesDb

fun listAuthorizedAreas(db: CesDb, personId: String) {
    // Get the list of authorized areas from the database
    Log.info("Getting authorized areas")
    val authorizedAreas = db.getAuthorizedAreas(personId)

    // Print out the results (if it's empty, just print out "(NONE)")
    Log.info("Printing authorized areas")
    if (authorizedAreas.isNotEmpty()) {
        println("Authorized areas:")
        for (area in authorizedAreas) {
            println("- $area")
        }
    } else {
        println("Authorized areas: (NONE)")
    }
}

fun addAuthorizedAreas(db: CesDb, personId: String, areas: Collection<String>) {
    // Add the given list of areas
    Log.info("Adding authorized areas")
    areas.forEach { area ->
        Log.info("Adding '$area'")
        println("adding $area")
        db.addAuthorizedArea(personId, area)
    }
}

fun removeAuthorizedAreas(db: CesDb, personId: String, areas: Collection<String>) {
    // Remove the given list of areas
    Log.info("Removing authorized areas")
    areas.forEach { area ->
        Log.info("Removing '$area'")
        println("removing $area")
        db.removeAuthorizedArea(personId, area)
    }
}

fun printVersion() {
    // Print out the current version of `chrysalis` (found in `build.gradle.kts`)
    println("chrysalis v$VERSION")
}