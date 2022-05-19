package db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
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

    fun addAuthorizedArea(personId: String, area: String) {
        transaction {
            UserAuthorization.insert(
                UserAuthorization
                    .slice(
                        UserAuthorization.creditInstitution,
                        UserAuthorization.varchar(personId, 9),
                        UserAuthorization.informationalArea,
                        CurrentDateTime,
                        CurrentDateTime,
                        UserAuthorization.grantedById,
                        CurrentDateTime,
                        UserAuthorization.varchar("U", 1),
                        UserAuthorization.clockStartTime,
                        UserAuthorization.clockEndTime,
                        UserAuthorization.allowableDomain,
                        UserAuthorization.limitationType,
                        UserAuthorization.limitationValue
                    )
                    .select {
                        (UserAuthorization.informationalArea eq area) and
                        (CurrentDateTime.between(
                            UserAuthorization.effectiveDate,
                            UserAuthorization.expiredDate
                        )) and
                        (UserAuthorization.updateType eq "U")
                    }
                    .limit(1),
                columns = listOf(
                    UserAuthorization.creditInstitution,
                    UserAuthorization.personId,
                    UserAuthorization.informationalArea,
                    UserAuthorization.effectiveDate,
                    UserAuthorization.dateTimeGranted,
                    UserAuthorization.grantedById,
                    UserAuthorization.dateTimeRevoked,
                    UserAuthorization.updateType,
                    UserAuthorization.clockStartTime,
                    UserAuthorization.clockEndTime,
                    UserAuthorization.allowableDomain,
                    UserAuthorization.limitationType,
                    UserAuthorization.limitationValue
                )
            )
        }
    }

}