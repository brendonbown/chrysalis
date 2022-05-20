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

    /**
     * Adds the given area to the person with the given Person ID
     *
     * Performs the following SQL call:
     *
     * ```sql
     * insert into user_authorization
     *   (credit_institution,
     *   person_id,
     *   informational_area,
     *   effective_date,
     *   date_time_granted,
     *   granted_by_id,
     *   date_time_revoked,
     *   update_type,
     *   clock_start_time,
     *   clock_end_time,
     *   allowable_domain,
     *   limitation_type,
     *   limitation_value)
     * select
     *   credit_institution,
     *   $PERSON_ID,
     *   informational_area,
     *   sysdate,
     *   sysdate,
     *   granted_by_id,
     *   sysdate,
     *   'U',
     *   clock_start_time,
     *   clock_end_time,
     *   allowable_domain,
     *   limitation_type,
     *   limitation_value
     * from user_authorization where
     *   informational_area = $AREA and
     *   effective_date < sysdate and
     *   (expired_date is null or expired_date > sysdate) and
     *   update_type = 'U' and
     *
     *   -- limit to only one row
     *   rownum = 1
     * ```
     */
    fun addAuthorizedArea(personId: String, area: String) {
        transaction {
            UserAuthorization.insert(
                UserAuthorization
                    .slice(
                        UserAuthorization.creditInstitution,
                        stringLiteral(personId), // UserAuthorization.personId
                        UserAuthorization.informationalArea,
                        CurrentDateTime.alias("EFFECTIVE_DATE"), // UserAuthorization.effectiveDate
                        CurrentDateTime.alias("DATE_TIME_GRANTED"), // UserAuthorization.dateTimeGranted
                        UserAuthorization.grantedById,
                        CurrentDateTime.alias("DATE_TIME_REVOKED"), // UserAuthorization.dateTimeRevoked
                        stringLiteral("U"), // UserAuthorization.updateType
                        UserAuthorization.clockStartTime,
                        UserAuthorization.clockEndTime,
                        UserAuthorization.allowableDomain,
                        UserAuthorization.limitationType,
                        UserAuthorization.limitationValue
                    )
                    .select {
                        UserAuthorization.informationalArea.eq(area) and
                        UserAuthorization.effectiveDate.less(CurrentDateTime) and
                        (UserAuthorization.expiredDate.isNull() or
                            UserAuthorization.expiredDate.greater(CurrentDateTime)) and
                        UserAuthorization.updateType.eq("U")
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

    /**
     * Removes the given area from the person with the given Person ID
     *
     * Performs the following SQL call:
     *
     * ```
     * delete from user_authorization where
     *   person_id = $PERSON_ID and
     *   informational_area = $AREA
     * ```
     */
    fun removeAuthorizedArea(personId: String, area: String) {
        transaction {
            UserAuthorization.deleteWhere {
                UserAuthorization.personId.eq(personId) and
                UserAuthorization.informationalArea.eq(area)
            }
        }
    }
}