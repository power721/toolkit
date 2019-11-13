package com.har01d.toolkit.generator

import com.har01d.toolkit.core.BadRequestException
import java.util.concurrent.ThreadLocalRandom

object PasswordGenerator {
    private val lowerChars = "abcdefghijklmnopqrstuvwxyz".toCharArray()
    private val upperChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()
    private val numbers = "0123456789".toCharArray()
    private val special = "!@#$%^&*_+-=|".toCharArray()
    private val all = lowerChars + upperChars + numbers + special

    fun generate(length: Int): String {
        if (length < 5) {
            throw BadRequestException("Password length requires at least 5")
        }

        val sb = StringBuilder()
        val random = ThreadLocalRandom.current()
        sb.append(lowerChars[random.nextInt(lowerChars.size)])
        sb.append(upperChars[random.nextInt(upperChars.size)])
        sb.append(numbers[random.nextInt(numbers.size)])
        sb.append(special[random.nextInt(special.size)])

        for (i in 4..length) {
            sb.append(all[random.nextInt(all.size)])
        }

        val list = mutableListOf<Char>()
        sb.toCollection(list).shuffle()
        return String(list.toCharArray())
    }
}
