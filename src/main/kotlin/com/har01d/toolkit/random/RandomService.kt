package com.har01d.toolkit.random

import com.har01d.toolkit.generator.IdGenerator
import com.har01d.toolkit.generator.PasswordGenerator
import org.apache.commons.io.IOUtils
import org.springframework.context.ApplicationContext
import org.springframework.core.io.Resource
import org.springframework.stereotype.Service
import java.time.ZoneId
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ThreadLocalRandom
import javax.annotation.PostConstruct

@Service
class RandomService(private val context: ApplicationContext) {
    companion object {
        private val DOMAINS = arrayOf(".com", ".net", ".org", ".edu")
        private val COLORS = arrayOf("white", "silver", "gray", "black", "red", "maroon", "yellow", "olive", "lime", "green", "aqua", "teal", "blue", "navy", "fuchsia", "purple",
                "pink", "salmon", "crimson", "firebrick", "tomato", "coral", "orange", "moccasin", "gold", "brown", "cyan", "violet", "indigo")
    }

    private val map = ConcurrentHashMap<Int, Snowflake>()
    private lateinit var words: List<String>
    private lateinit var names: List<String>

    @PostConstruct
    fun init() {
        words = context.getResource("classpath:data/words.txt").readLines()
        names = context.getResource("classpath:data/names.txt").readLines().map { it.capitalize() }
    }

    fun snowflake(datacenterId: Int,
                  workerId: Int): Long {
        val snowflake = map.computeIfAbsent(datacenterId * 10000 + workerId) {
            Snowflake(datacenterId, workerId)
        }
        return snowflake.nextId()
    }

    fun password(length: Int) = PasswordGenerator.generate(length)

    fun word() = words[ThreadLocalRandom.current().nextInt(words.size)]

    fun color() = COLORS[ThreadLocalRandom.current().nextInt(COLORS.size)]

    fun name(full: Boolean = false): String {
        val random = ThreadLocalRandom.current()
        return if (full) {
            names[random.nextInt(2000)] + ' ' + names[random.nextInt(1000, 2000)]
        } else {
            names[random.nextInt(2000)]
        }
    }

    fun boolean() = ThreadLocalRandom.current().nextBoolean()

    fun hex(count: Int = 3): String {
        val random = ThreadLocalRandom.current()
        val sb = StringBuilder()
        for (i in 1..count) {
            val hex = random.nextInt(256).toString(16)
            if (hex.length < 2) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    fun mac(): String {
        val random = ThreadLocalRandom.current()
        val sb = StringBuilder()
        for (i in 1..6) {
            if (i > 1) {
                sb.append(':')
            }
            val hex = random.nextInt(256).toString(16)
            if (hex.length < 2) {
                sb.append('0')
            }
            sb.append(hex)
        }
        return sb.toString()
    }

    fun number(lower: Int?, upper: Int?): Int {
        val random = ThreadLocalRandom.current()
        return random.nextInt(lower ?: 0, upper ?: Int.MAX_VALUE)
    }

    fun int() = ThreadLocalRandom.current().nextInt()

    fun long(lower: Long?, upper: Long?): Long {
        val random = ThreadLocalRandom.current()
        return random.nextLong(lower ?: 0, upper ?: Long.MAX_VALUE)
    }

    fun double(lower: Double?, upper: Double?): Double {
        val random = ThreadLocalRandom.current()
        return random.nextDouble(lower ?: 0.0, upper ?: Double.MAX_VALUE)
    }

    fun gaussian() = ThreadLocalRandom.current().nextGaussian()

    fun string(length: Int) = IdGenerator.generate(length)

    fun uuid() = UUID.randomUUID().toString()

    fun ip(): String {
        val random = ThreadLocalRandom.current()
        return random.nextInt(1, 254).toString() + "." + random.nextInt(255) + "." + random.nextInt(255) + "." + random.nextInt(1, 254)
    }

    fun version(): String {
        val random = ThreadLocalRandom.current()
        return random.nextInt(1, 20).toString() + "." + random.nextInt(10) + "." + random.nextInt(201)
    }

    fun timezone(): String {
        val random = ThreadLocalRandom.current()
        val zones = ZoneId.getAvailableZoneIds().toList().filter { !it.startsWith("Etc") }
        return zones[random.nextInt(zones.size)]
    }

    fun email(): String {
        val random = ThreadLocalRandom.current()
        return name(true).replace(' ', '.') + '@' + word() + DOMAINS[random.nextInt(DOMAINS.size)]
    }
}

fun Resource.readLines(): MutableList<String> = IOUtils.readLines(this.inputStream, "UTF-8")
