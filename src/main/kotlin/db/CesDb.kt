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

    fun getInfo() {
        val query =  transaction {
            UserAuthorization
                .slice(UserAuthorization.informationalArea)
                .select { UserAuthorization.personId eq "704224962" }
                .toSet()
        }
        println(query)
    }
}