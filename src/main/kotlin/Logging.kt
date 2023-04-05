import java.util.*
import java.util.logging.*
import java.util.logging.Formatter

object Log {
    private val DEFAULT_LEVEL = Level.OFF
    private val DEBUG_LEVEL = Level.ALL
    private val VERBOSE_LEVEL = Level.INFO

    private val consoleHandler: ConsoleHandler = ConsoleHandler().apply {
        formatter = MessageFormatter()
        level = DEFAULT_LEVEL
    }

    private val logger = Logger.getLogger("Log").apply {
        level = DEFAULT_LEVEL
        useParentHandlers = false

        addHandler(consoleHandler)
    }

    fun useDebugLevel() {
        logger.level = DEBUG_LEVEL
        consoleHandler.level = DEBUG_LEVEL
    }

    fun useVerboseLevel() {
        logger.level = VERBOSE_LEVEL
        consoleHandler.level = VERBOSE_LEVEL
    }

    fun debug(msg: String) {
        logger.fine(msg)
    }

    fun info(msg: String) {
        logger.info(msg)
    }

    fun error(msg: String) {
        logger.severe(msg)
    }

    private class MessageFormatter: Formatter() {
        override fun format(record: LogRecord?): String {
            if (record == null) return ""

            // TODO: figure out a better way to turn the current date/time into a string
            val date = Date(record.millis)

            // String formatters (using the first argument)
            val year = "%1\$tY"
            val month = "%1\$tm"
            val day = "%1\$td"
            val hour = "%1\$tH"
            val minute = "%1\$tM"
            val second = "%1\$tS"

            return String.format(
                "[$year-$month-$day $hour:$minute:$second] [%2\$s] %3\$s %n",
                date,
                record.level,
                formatMessage(record)
            )
        }

    }
}