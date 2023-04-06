package db

import args.ByuId
import args.Identifier
import args.NetId
import args.PersonId
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.transactions.transaction

class CesDb(username: String, password: String) {
    init {
        Log.info("Connecting to database")
        Database.connect(
            "jdbc:oracle:thin:@ora7gdev.byu.edu:1521/cescpy1.byu.edu",
            driver = "oracle.jdbc.OracleDriver",
            username,
            password
        )
        Log.info("Connected to database")
    }

    fun getPersonId(ident: Identifier): String? {
        val (column, identValue) = when (ident) {
            is PersonId -> Pair(Person.personId, ident.personId)
            is ByuId -> Pair(Person.byuId, ident.byuId)
            is NetId -> Pair(Person.netId, ident.netId)
        }

        Log.info("Retrieving Person ID for $ident")
        val personIdList = transaction {
            Person
                .slice(Person.personId)
                .select { column eq identValue }
                .toList()
        }
        Log.info("Person ID retrieved for $ident")
        Log.debug("Received ${
            personIdList.joinToString(
                prefix = "[",
                postfix = "]"
            ) { it[Person.personId] }
        } for $ident")

        return personIdList.firstOrNull()?.get(Person.personId)
    }

    fun getAuthorizedAreas(personId: String): Set<String> {
        Log.info("Retrieving authorized areas for '$personId'")
        val query =  transaction {
            UserAuthorization
                .slice(UserAuthorization.informationalArea)
                .select { UserAuthorization.personId eq personId }
                .map { it[UserAuthorization.informationalArea] }
                .toSet()
        }
        Log.info("Retrieved authorized areas for '$personId'")
        Log.debug("Received ${query.joinToString(prefix = "[", postfix = "]")}")

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
        Log.info("Adding area '$area' for '$personId'")
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
                        UserAuthorization.effectiveDate.less(CurrentDateTime) and // TODO `CurrentDateTime.date()` doesn't generate the right SQL, try something else to resolve warning
                        (UserAuthorization.expiredDate.isNull() or
                            UserAuthorization.expiredDate.greater(CurrentDateTime)) and // TODO `CurrentDateTime.date()` doesn't generate the right SQL, try something else to resolve warning
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
        Log.info("Added area '$area' for '$personId'")
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
     *   person_id = $FROM_PERSON_ID and
     *   effective_date < sysdate and
     *   (expired_date is null or expired_date > sysdate) and
     *   update_type = 'U'
     * ```
     */
    fun copyAuthorizedArea(personId: String, fromPersonId: String) {
        Log.info("Copying areas from '$fromPersonId' for '$personId'")
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
                        UserAuthorization.personId.eq(fromPersonId) and
                                UserAuthorization.effectiveDate.less(CurrentDateTime) and // TODO `CurrentDateTime.date()` doesn't generate the right SQL, try something else to resolve warning
                                (UserAuthorization.expiredDate.isNull() or
                                        UserAuthorization.expiredDate.greater(CurrentDateTime)) and // TODO `CurrentDateTime.date()` doesn't generate the right SQL, try something else to resolve warning
                                UserAuthorization.updateType.eq("U")
                    },
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
        Log.info("Copied areas from '$fromPersonId' for '$personId'")
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
        Log.info("Removing area '$area' for '$personId'")
        transaction {
            UserAuthorization.deleteWhere {
                UserAuthorization.personId.eq(personId) and
                UserAuthorization.informationalArea.eq(area)
            }
        }
        Log.info("Removed area '$area' for '$personId'")
    }
}