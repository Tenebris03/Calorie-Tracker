package com.tenebris.health_tracker.data.service

/** Small WordPiece tokenizer matching the vocabulary exported with DistilBERT. */
class DistilBertTokenizer(vocabulary: List<String>) {
    private val ids = vocabulary.mapIndexed { index, token -> token to index }.toMap()
    private val unknownId = ids["[UNK]"] ?: 100
    private val clsId = ids["[CLS]"] ?: 101
    private val sepId = ids["[SEP]"] ?: 102
    private val padId = ids["[PAD]"] ?: 0

    fun attentionMask(tokens: IntArray): IntArray =
        IntArray(tokens.size) { index -> if (tokens[index] == padId) 0 else 1 }

    fun encode(text: String, length: Int): IntArray {
        val tokens = mutableListOf(clsId)
        text.lowercase()
            .replace(Regex("([.!?,:;()])"), " $1 ")
            .split(Regex("\\s+"))
            .filter(String::isNotBlank)
            .forEach { token ->
                tokens += wordPiece(token)
            }
        tokens += sepId

        return IntArray(length) { index -> tokens.getOrElse(index) { padId } }
    }

    private fun wordPiece(word: String): List<Int> {
        if (word.length > 100) return listOf(unknownId)
        val result = mutableListOf<Int>()
        var start = 0
        while (start < word.length) {
            var end = word.length
            var match: String? = null
            while (start < end) {
                val candidate = word.substring(start, end).let { if (start == 0) it else "##$it" }
                if (ids.containsKey(candidate)) {
                    match = candidate
                    break
                }
                end--
            }
            if (match == null) return listOf(unknownId)
            result += ids.getValue(match)
            start = end
        }
        return result
    }
}
