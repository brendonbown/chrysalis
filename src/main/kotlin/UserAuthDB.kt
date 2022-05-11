import org.jetbrains.exposed.sql.*

class UserAuthDB(username: String, password: String) {
    init {
        Database.connect(
            "jdbc:oracle:thin:ora7gdev.byu.edu:1521/cescpy1.byu.edu",
            driver = "oracle.jdbc.OracleDriver",
            username,
            password
        )
    }
}