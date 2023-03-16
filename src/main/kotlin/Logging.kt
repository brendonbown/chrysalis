import java.util.logging.ConsoleHandler
import java.util.logging.Level
import java.util.logging.Logger

object Log {
    private val DEFAULT_LEVEL = Level.ALL

    private val logger = Logger.getLogger("Log").apply {
        level = DEFAULT_LEVEL
        useParentHandlers = false

        val consoleHandler = ConsoleHandler()
        addHandler(consoleHandler)
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
}