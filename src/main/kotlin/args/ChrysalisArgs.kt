package args

import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.default

enum class Action {
    LIST, // List all permissions for the user
    ADD, // Add a permission/set of permissions for the user
    REMOVE, // Add a permission/set of permissions for the user
    VERSION; // Get version information

    override fun toString(): String {
        return when (this) {
            LIST -> "list"
            ADD -> "add"
            REMOVE -> "remove"
            VERSION -> "version"
        }
    }

    companion object {
        fun fromString(value: String) = when (value) {
            "list" -> LIST
            "add" -> ADD
            "remove" -> REMOVE
            "version", "--version" -> VERSION // accept "--version" because that's how most CLIs do it
            else -> throw InvalidArgumentException(
                "args.Action must be one of 'list', 'add', 'remove', or 'version'"
            )
        }
    }
}

class ChrysalisArgs(parser: ArgParser) {
    val action by parser.positional(
        "ACTION",
        help = "action to perform (possible actions: 'list', 'add', 'remove', 'prod-perm', 'version')",
        transform = Action.Companion::fromString
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
        help = "perform the action for the person with the given Person ID"
    ) { PersonId(this) }.default<PersonId?>(null)

    val byuId by parser.storing(
        "-b", "--byuId",
        help = "perform the action for the person with the given BYU ID"
    ) { ByuId(this) }.default<ByuId?>(null)

    val netId by parser.storing(
        "-n", "--netId",
        help = "perform the action for the person with the given NetID"
    ) { NetId(this) }.default<NetId?>(null)

    val verbose by parser.flagging(
        "-v", "--verbose",
        help = "enable verbose logging"
    )

    val debug by parser.flagging(
        "-vv", "--debug",
        help = "enable debug logging (overrides '--verbose' option)"
    )
}