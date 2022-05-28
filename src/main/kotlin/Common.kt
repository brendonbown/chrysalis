fun printError(error: String) {
    System.err.println("ERROR: $error")
}

fun prompt(promptText: String, hideInput: Boolean = false): String? {
    print(promptText)
    return if (hideInput)
        System.console()?.readPassword()?.toString() ?: readLine()
    else
        readLine()
}