import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException

enum class Action {
    LIST, // List all permissions for the user
    ADD, // Add a permission/set of permissions for the user
    REMOVE, // Add a permission/set of permissions for the user
    PRODUCT_PERM, // Find the permissions for a given product (ex. ADV010)
    CONFIG, // Configure Chrysalis
    VERSION; // Get version information

    companion object {
        fun fromString(value: String) = when (value) {
            "list" -> LIST
            "add" -> ADD
            "remove" -> REMOVE
            "prod-perm" -> PRODUCT_PERM
            "config" -> CONFIG
            "version", "--version" -> VERSION // accept "--version" because that's how most CLIs do it
            else -> throw InvalidArgumentException(
                "Action must be one of 'list', 'add', 'remove', 'prod-perm', or 'config'"
            )
        }
    }
}

class ChrysalisArgs(parser: ArgParser) {
    val action: Action by parser.positional(
        "ACTION",
        help = "action to perform",
        transform = Action::fromString
    )


}