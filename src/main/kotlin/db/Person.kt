package db

import org.jetbrains.exposed.sql.*

object Person : Table("PERSON") {
    val netId: Column<String> = varchar("NET_ID", 9)
    val personId: Column<String> = varchar("PERSON_ID", 9)
}