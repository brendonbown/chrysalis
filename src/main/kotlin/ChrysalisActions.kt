import db.CesDb
import user.Log
import user.Terminal

fun listAuthorizedAreas(db: CesDb, personId: String) {
    // Get the list of authorized areas from the database
    Log.info("Getting authorized areas")
    val authorizedAreas = db.getAuthorizedAreas(personId)

    // Print out the results (if it's empty, just print out "(NONE)")
    Log.info("Printing authorized areas")
    printAreas(authorizedAreas)
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

fun copyAuthorizedAreas(db: CesDb, personId: String, fromNetId: String, fromPersonId: String) {
    Log.info("Printing authorized areas for '$fromNetId' ($fromPersonId)")
    val authorizedAreas = db.getAuthorizedAreas(fromPersonId)
    printAreas(authorizedAreas, label = "Authorized areas for '$fromNetId'")

    Log.info("Confirming that the user wants to add areas")
    var answer: String?
    do {
        Log.debug("Prompting for answer")
        answer = Terminal.prompt("Continue? [Y/n] ")
    } while (answer !in setOf("", "y", "n", "Y", "N"))

    if (answer == "n" || answer == "N") {
        Log.info("User declined")
        println("areas from '$fromNetId' not copied")
        return
    }

    Log.info("Copying authorized areas from '$fromNetId' ($fromPersonId)")
    print("copying areas from '$fromNetId' ... ")
    db.copyAuthorizedArea(personId, fromPersonId)
    println("done!")
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

fun printAreas(areas: Collection<String>, label: String = "Authorized areas") {
    if (areas.isNotEmpty()) {
        println("$label:")
        for (area in areas) {
            println("- $area")
        }
    } else {
        println("$label: (NONE)")
    }
}

fun printVersion() {
    // Print out the current version of `chrysalis` (found in `build.gradle.kts`)
    println("chrysalis v$VERSION")
}