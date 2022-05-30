import args.ByuId
import args.NetId
import args.PersonId
import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.SystemExitException
import com.xenomachina.argparser.default

enum class Action {
    LIST, // List all permissions for the user
    ADD, // Add a permission/set of permissions for the user
    REMOVE, // Add a permission/set of permissions for the user
    PRODUCT_PERM, // Find the permissions for a given product (ex. ADV010)
    CONFIG, // Configure Chrysalis
    VERSION; // Get version information

    override fun toString(): String {
        return when (this) {
            LIST -> "list"
            ADD -> "add"
            REMOVE -> "remove"
            PRODUCT_PERM -> "prod-perm"
            CONFIG -> "config"
            VERSION -> "version"
        }
    }

    companion object {
        fun fromString(value: String) = when (value) {
            "list" -> LIST
            "add" -> ADD
            "remove" -> REMOVE
            "prod-perm" -> PRODUCT_PERM
            "config" -> CONFIG
            "version", "--version" -> VERSION // accept "--version" because that's how most CLIs do it
            else -> throw SystemExitException(
                "Action must be one of 'list', 'add', 'remove', 'prod-perm', or 'config'", 1
            )
        }
    }
}

class ChrysalisArgs(parser: ArgParser) {
    val action by parser.positional(
        "ACTION",
        help = "action to perform",
        transform = Action::fromString
    )

    val areas by parser.positionalList(
        "AREAS",
        help = "areas to add/remove",

        // it is possible for there to be zero areas for commands like "list" or "version",
        // so checking for 1 or more must be done in commands that require this, such as "add" and "remove"
        sizeRange = 0..Int.MAX_VALUE
    )

    val personId by parser.storing(
        "-p", "--personId",
        help = "perform the list/add/remove action for the person with the given Person ID"
    ) { PersonId(this) }.default<PersonId?>(null)

    val byuId by parser.storing(
        "-b", "--byuId",
        help = "perform the list/add/remove action for the person with the given BYU ID"
    ) { ByuId(this) }.default<ByuId?>(null)

    val netId by parser.storing(
        "-n", "--netId",
        help = "perform the list/add/remove action for the person with the given NetID"
    ) { NetId(this) }.default<NetId?>(null)

}