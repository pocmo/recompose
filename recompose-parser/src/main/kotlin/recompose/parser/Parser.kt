package recompose.parser

interface Parser<T> {
    fun parse(input: String): T
}
