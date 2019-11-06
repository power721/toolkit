package com.har01d.demo.kospring.number

import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("number")
class NumberController {
    @PostMapping("convert")
    fun convert(@RequestParam(name = "value", required = false, defaultValue = "100") value: String,
                @RequestParam(name = "radix", required = false, defaultValue = "10") radix: Int): Number {
        val decimal = value.toLong(radix)
        return Number(decimal.toString(2),
                decimal.toString(3),
                decimal.toString(4),
                decimal.toString(8),
                decimal.toString(10),
                decimal.toString(12).toUpperCase(),
                decimal.toString(16).toUpperCase(),
                decimal.toString(20).toUpperCase(),
                decimal.toString(36).toUpperCase())
    }

    @PostMapping("convert/{fromRadix}/{toRadix}")
    fun convertNumber(@PathVariable fromRadix: Int,
                      @PathVariable toRadix: Int,
                      value: String) = value.toLong(fromRadix).toString(toRadix).toUpperCase()
}

data class Number(val binary: String,
                  val ternary: String,
                  val quaternary: String,
                  val octal: String,
                  val decimal: String,
                  val duodecimal: String,
                  val hex: String,
                  val vigesimal: String,
                  val base36: String)
