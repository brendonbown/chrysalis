import com.xenomachina.argparser.SystemExitException

@Throws(SystemExitException::class)
fun printArgError(error: String)  {
    throw SystemExitException("ERROR: $error", 1)
}

fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.concatToString() ?: readLine()
    else
        readLine()
}