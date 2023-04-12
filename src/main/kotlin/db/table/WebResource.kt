package db.table

import org.jetbrains.exposed.sql.*

object WebResource : Table("WEB_RESOURCE") {
    val speedUrl = varchar("SPEED_URL", 8)
    val webResourceId = varchar("WEB_RESOURCE_ID", 8)
}