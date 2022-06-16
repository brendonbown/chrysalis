fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.concatToString() ?: readLine()
    else
        readLine()
}

val isDebug = System.getenv("DEBUG") != null

fun getDebugEnv(name: String) =
    if (isDebug)
        System.getenv(name)
    else
        null