package com.har01d.toolkit.random

import com.har01d.toolkit.generator.DataGenerator
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("random")
class RandomController(private val service: RandomService, private val generator: DataGenerator) {
    @PostMapping("generator")
    fun generateJson(@RequestBody template: String, size: Int = 10) = generator.generate(template, size)

    @GetMapping("id")
    fun snowflake(@RequestParam(name = "datacenterId", required = false, defaultValue = "0") datacenterId: Int,
                  @RequestParam(name = "workerId", required = false, defaultValue = "0") workerId: Int): Long {
        return service.snowflake(datacenterId, workerId)
    }

    @GetMapping("password")
    fun password(@RequestParam(name = "length", required = false, defaultValue = "12") length: Int) = service.password(length)

    @GetMapping("word")
    fun word() = service.word()

    @GetMapping("name")
    fun name(full: Boolean = false) = service.name(full)

    @GetMapping("boolean")
    fun boolean() = service.boolean()

    @GetMapping("number")
    fun number(lower: Int?, upper: Int?) = service.number(lower, upper)

    @GetMapping("int")
    fun int(lower: Int?, upper: Int?) = service.number(lower, upper)

    @GetMapping("long")
    fun long(lower: Long?, upper: Long?) = service.long(lower, upper)

    @GetMapping("double")
    fun double(lower: Double?, upper: Double?) = service.double(lower, upper)

    @GetMapping("gaussian")
    fun gaussian() = service.gaussian()

    @GetMapping("string")
    fun string(@RequestParam(name = "length", required = false, defaultValue = "12") length: Int) = service.string(length)

    @GetMapping("uuid")
    fun uuid() = service.uuid()
}

data class GeneratorDto(val template: String, val size: Int = 10)
