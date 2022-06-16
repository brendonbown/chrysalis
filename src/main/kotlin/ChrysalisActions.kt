import api.ApiAccess
import db.CesDb

fun listAuthorizedAreas(db: CesDb, personId: String) {
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

fun addAuthorizedAreas(db: CesDb, personId: String, areas: List<String>) {
    // Add the given list of areas
    areas.forEach { area ->
        println("adding $area")
        db.addAuthorizedArea(personId, area)
    }
}

fun removeAuthorizedAreas(db: CesDb, personId: String, areas: List<String>) {
    // Remove the given list of areas
    areas.forEach { area ->
        println("removing $area")
        db.removeAuthorizedArea(personId, area)
    }
}

fun printProductPermissions(api: ApiAccess) {
    val webResInfo = api.getWebResourceInfo("ADV020")
    println(webResInfo)
    val webResId = webResInfo!!.content[0].webResourceId
    val webResPolicies = api.getWebResourcePolicies(webResId)
    println(webResPolicies)
}

fun printVersion() {
    // Print out the current version of `chrysalis` (found in `build.gradle.kts`)
    println("chrysalis v0.1.0")
}