package db.table

import org.jetbrains.exposed.sql.*

object WebResourceArea : Table("WEB_RESOURCE_AREA") {
    val informationalArea: Column<String> = varchar("INFORMATIONAL_AREA", 20)
    val webResourceId = varchar("WEB_RESOURCE_ID", 8)
}