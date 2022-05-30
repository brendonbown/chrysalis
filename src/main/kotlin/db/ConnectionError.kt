package db

enum class ConnectionError {
    // The user's account has been locked and must be unlocked by a database administrator
    LOCKED_ACCOUNT,

    // We were unable to establish a connection (probably because the VPN is off)
    NO_CONNECTION,

    // The credentials provided by the user were invalid
    INVALID_LOGIN,

    // We have no idea why it didn't connect
    UNKNOWN;

    companion object {
        // I've found these error codes as I've actually run into them,
        // I have no idea where to find documentation for them
        fun fromErrorCode(errorCode: Int) = when (errorCode) {
            28000 -> LOCKED_ACCOUNT
            17002 -> NO_CONNECTION
            1017 -> INVALID_LOGIN

            // if we don't know what the error is, just return an unknown
            else -> UNKNOWN
        }
    }

    override fun toString(): String =
        when (this) {
            LOCKED_ACCOUNT ->
                "Your account has been locked. Please contact a database administrator to have it unlocked."
            NO_CONNECTION ->
                "Unable to establish connection with the database." + "\n" +
                "Please check that your VPN is on, then try again."
            INVALID_LOGIN ->
                "Your username and/or password is incorrect." + "\n" +
                "Please try again with the correct credentials."
            UNKNOWN ->
                "An unknown error has occurred."
        }
}