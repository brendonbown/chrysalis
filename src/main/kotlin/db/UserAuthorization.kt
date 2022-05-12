package db

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object UserAuthorization : Table("USER_AUTHORIZATION") {
    val creditInstitution: Column<String> = varchar("CREDIT_INSTITUTION", 15)
    val personId: Column<String> = varchar("PERSON_ID", 9)
    val informationalArea: Column<String> = varchar("INFORMATIONAL_AREA", 20)
    val effectiveDate: Column<LocalDate> = date("EFFECTIVE_DATE")
    val limitationType: Column<String> = varchar("LIMITATION_TYPE", 15)
    val limitationValue: Column<String> = varchar("LIMITATION_VALUE", 30)
    val dateTimeGranted: Column<LocalDate> = date("DATE_TIME_GRANTED")
    val grantedById: Column<String> = varchar("GRANTED_BY_ID", 9)
    val expiredDate: Column<LocalDate> = date("EXPIRED_DATE")
    val dateTimeRevoked: Column<LocalDate> = date("DATE_TIME_REVOKED")
    val revokedById: Column<String> = varchar("REVOKED_BY_ID", 9)
    val roleType: Column<String> = varchar("ROLE_TYPE", 8)
    val updateType: Column<String> = varchar("UPDATE_TYPE", 1)
    val clockStartTime: Column<String> = varchar("CLOCK_START_TIME", 5)
    val clockEndTime: Column<String> = varchar("CLOCK_END_TIME", 5)
    val allowableDomain: Column<String> = varchar("ALLOWABLE_DOMAIN", 50)
}