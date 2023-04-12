package model

enum class Action {
    LIST, // List all permissions for the user
    ADD, // Add a permission/set of permissions for the user
    COPY, // Copy permissions from a given person
    REMOVE, // Add a permission/set of permissions for the user
    VERSION; // Get version information

    override fun toString(): String {
        return when (this) {
            LIST -> "list"
            ADD -> "add"
            COPY -> "copy"
            REMOVE -> "remove"
            VERSION -> "version"
        }
    }
}