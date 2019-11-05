package com.har01d.demo.kospring.url

import org.springframework.stereotype.Service
import java.util.concurrent.ThreadLocalRandom

@Service
class ShortUrlService(private val repository: ShortUrlRepository) {
    companion object {
        private val TOKENS = "Ok4jShBpcvKY5gTQMVRsEHfGe3nDdb81IJwrqLFP0UC6xilazo2ZWut9yNmA7X".toCharArray()
        private const val LOWER_BOUND = 62 * 62 * 62 * 62 * 62L
        private const val UPPER_BOUND = LOWER_BOUND * 62L
    }

    fun generate(url: String): ShortUrl {
        val shortUrl = repository.findByUrl(url)
        if (shortUrl != null) {
            return shortUrl
        }

        val random = ThreadLocalRandom.current()
        var sUrl: String
        do {
            val number = random.nextLong(UPPER_BOUND - LOWER_BOUND) + LOWER_BOUND
            sUrl = encode(number)
        } while (repository.existsByShortUrl(sUrl))
        return repository.save(ShortUrl(url, sUrl))
    }

    private fun encode(digits: Long): String {
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
