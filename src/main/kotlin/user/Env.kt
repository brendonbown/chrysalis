package user

object Env {
    private val isDebug = System.getenv("DEBUG") != null

    fun getDebugEnv(name: String) =
        if (isDebug)
            System.getenv(name)
        else
            null
}