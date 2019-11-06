package com.har01d.toolkit.random

import com.har01d.toolkit.utils.IdGenerator
import org.springframework.context.ApplicationContext
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.security.SecureRandom
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.annotation.PostConstruct

@RestController
@RequestMapping("random")
class RandomController(private val context: ApplicationContext) {
    private val random = SecureRandom()
    private val map = ConcurrentHashMap<Int, Snowflake>()
    private lateinit var words: List<String>
    private lateinit var names: List<String>

    @PostConstruct
    fun init() {
        words = context.getResource("classpath:data/words.txt").file.readLines()
        names = context.getResource("classpath:data/names.txt").file.readLines().map { it.capitalize() }
    }

    @GetMapping("id")
    fun snowflake(@RequestParam(name = "datacenterId", required = false, defaultValue = "0") datacenterId: Int,
                  @RequestParam(name = "workerId", required = false, defaultValue = "0") workerId: Int): Long {
        val snowflake = map.computeIfAbsent(datacenterId * 10000 + workerId) {
            Snowflake(datacenterId, workerId)
        }
        return snowflake.nextId()
    }

    @GetMapping("password")
    fun password(@RequestParam(name = "length", required = false, defaultValue = "12") length: Int) = PasswordGenerator.generate(length)

    @GetMapping("word")
    fun word() = words[random.nextInt(words.size)]

    @GetMapping("name")
    fun name(full: Boolean = false): String {
        return if (full) {
            names[random.nextInt(2000)] + ' ' + names[random.nextInt(1000) + 2000]
        } else {
            names[random.nextInt(2000)]
        }
    }

    @GetMapping("number")
    fun number(lower: Int?, upper: Int?): Int {
        if (lower != null && upper != null) {
            return random.nextInt(upper - lower) + lower
        } else if (lower != null) {
            return random.nextInt(Int.MAX_VALUE - lower) + lower
        } else if (upper != null) {
            return random.nextInt(upper)
        }
        return random.nextInt(Int.MAX_VALUE)
    }

    @GetMapping("int")
    fun int() = random.nextInt()

    @GetMapping("long")
    fun long() = random.nextLong()

    @GetMapping("boolean")
    fun boolean() = random.nextBoolean()

    @GetMapping("double")
    fun double() = random.nextDouble()

    @GetMapping("gaussian")
    fun gaussian() = random.nextGaussian()

    @GetMapping("string")
    fun string(@RequestParam(name = "length", required = false, defaultValue = "12") length: Int) = IdGenerator.generate(length)

    @GetMapping("uuid")
    fun uuid() = UUID.randomUUID().toString()
}
