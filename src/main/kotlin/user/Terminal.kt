package user

object Terminal {
    fun prompt(promptText: String, hideInput: Boolean = false): String? {
        print(promptText)
        return if (hideInput)
            System.console()?.readPassword()?.concatToString() ?: readlnOrNull()
        else
            readlnOrNull()
    }
}