package db.table

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate

object UserAuthorization : Table("USER_AUTHORIZATION") {
    val creditInstitution: Column<String> = varchar("CREDIT_INSTITUTION", 15)
    val personId: Column<String> = varchar("PERSON_ID", 9)
    val informationalArea: Column<String> = varchar("INFORMATIONAL_AREA", 20)
    val effectiveDate: Column<LocalDate> = date("EFFECTIVE_DATE")
    val limitationType: Column<String?> = varchar("LIMITATION_TYPE", 15).nullable()
    val limitationValue: Column<String?> = varchar("LIMITATION_VALUE", 30).nullable()
    val dateTimeGranted: Column<LocalDate?> = date("DATE_TIME_GRANTED").nullable()
    val grantedById: Column<String?> = varchar("GRANTED_BY_ID", 9).nullable()
    val expiredDate: Column<LocalDate?> = date("EXPIRED_DATE").nullable()
    val dateTimeRevoked: Column<LocalDate?> = date("DATE_TIME_REVOKED").nullable()
    val updateType: Column<String?> = varchar("UPDATE_TYPE", 1).nullable()
    val clockStartTime: Column<String?> = varchar("CLOCK_START_TIME", 5).nullable()
    val clockEndTime: Column<String?> = varchar("CLOCK_END_TIME", 5).nullable()
    val allowableDomain: Column<String?> = varchar("ALLOWABLE_DOMAIN", 50).nullable()
}