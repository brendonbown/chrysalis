import com.xenomachina.argparser.ArgParser
import com.xenomachina.argparser.InvalidArgumentException
import com.xenomachina.argparser.default
import model.Action
import model.Identifier

class ChrysalisArgs(parser: ArgParser) {
    val action by parser.positional(
        "ACTION",
        help = "action to perform (possible actions: 'list', 'add', 'copy', 'remove', 'version')",
        transform = ::actionFromString
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
    ) { Identifier.PersonId(this) }.default(null)

    val byuId by parser.storing(
        "-b", "--byuId",
        help = "perform the action for the person with the given BYU ID"
    ) { Identifier.ByuId(this) }.default(null)

    val netId by parser.storing(
        "-n", "--netId",
        help = "perform the action for the person with the given NetID"
    ) { Identifier.NetId(this) }.default(null)

    val verbose by parser.counting(
        "-v", "--verbose",
        help = "enable verbose logging (-vv enables debug logging)"
    )

    val debug by parser.flagging(
        "--debug",
        help = "enable debug logging (overrides '--verbose' option)"
    )

    private fun actionFromString(value: String) = when (value) {
        "list" -> Action.LIST
        "add" -> Action.ADD
        "copy" -> Action.COPY
        "remove" -> Action.REMOVE
        "version", "--version" -> Action.VERSION // accept "--version" because that's how most CLIs do it
        else -> throw InvalidArgumentException(
            "args.Action must be one of 'list', 'add', 'copy', 'remove', or 'version'"
        )
    }
}