package com.har01d.demo.kospring.utils

import java.util.concurrent.ThreadLocalRandom

object IdGenerator {
    private val TOKENS = "Ok4jShBpcvKY5gTQMVRsEHfGe3nDdb81IJwrqLFP0UC6xilazo2ZWut9yNmA7X".toCharArray()
    private const val LOWER_BOUND = 62 * 62 * 62 * 62 * 62L
    private const val UPPER_BOUND = LOWER_BOUND * 62L

    fun generate(): String {
        val random = ThreadLocalRandom.current()
        val number = random.nextLong(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND
        return encode(number)
    }

    fun generate(length: Int): String {
        val sb = StringBuilder()
        val random = ThreadLocalRandom.current()
        for (i in 1..length) {
            sb.append(TOKENS[random.nextInt(TOKENS.size)])
        }
        return sb.toString()
    }

    fun encode(digits: Long): String {
        var number = digits
        if (number == 0L) {
            return String(TOKENS, 0, 1)
        }

        val n = TOKENS.size.toLong()
        val sb = StringBuilder()
        while (number > 0) {
            sb.append(TOKENS[(number % n).toInt()])
            number /= n
        }
        return sb.toString()
    }
}
