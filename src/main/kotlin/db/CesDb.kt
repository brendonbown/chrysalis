package db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

class CesDb(username: String, password: String) {
    init {
        Database.connect(
            "jdbc:oracle:thin:@ora7gdev.byu.edu:1521/cescpy1.byu.edu",
            driver = "oracle.jdbc.OracleDriver",
            username,
            password
        )
    }

    fun getPersonId(netId: String): String? {
        val personIdList = transaction {
            Person
                .slice(Person.personId)
                .select { Person.netId eq netId }
                .toList()
        }

        return personIdList.firstOrNull()?.get(Person.personId)
    }

    fun getAuthorizedAreas(personId: String): Set<String> {
        val query =  transaction {
            UserAuthorization
                .slice(UserAuthorization.informationalArea)
                .select { UserAuthorization.personId eq personId }
                .map { it[UserAuthorization.informationalArea] }
                .toSet()
        }

        return query
    }
}