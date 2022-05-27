package args

// A sum type representing the different kinds of identifiers
sealed class Identifier

class PersonId(val personId: String): Identifier() {
    override fun toString() = "Person ID<$personId>"
}
class ByuId(val byuId: String): Identifier() {
    override fun toString() = "BYU ID<$byuId>"
}
class NetId(val netId: String): Identifier() {
    override fun toString() = "Net ID<$netId>"
}