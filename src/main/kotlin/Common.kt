fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.concatToString() ?: readlnOrNull()
    else
        readlnOrNull()
}

val isDebug = System.getenv("DEBUG") != null

fun getDebugEnv(name: String) =
    if (isDebug)
        System.getenv(name)
    else
        null