package com.har01d.toolkit.time

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.*
import java.util.*

@RestController
@RequestMapping("time")
class TimeController {
    @GetMapping
    fun timeInfo() = TimeInfo()

    @GetMapping("now")
    fun now() = Date()

    @GetMapping("datetime")
    fun datetime(): LocalDateTime = LocalDateTime.now()

    @GetMapping("date")
    fun date(): LocalDate = LocalDate.now()

    @GetMapping("time")
    fun time(): LocalTime = LocalTime.now()

    @GetMapping("timestamp")
    fun timestamp() = Instant.now().epochSecond
}

data class TimeInfo(val now: Date = Date(),
                    val instant: Instant = Instant.now(),
                    val dateTime: LocalDateTime = LocalDateTime.now(),
                    val date: LocalDate = dateTime.toLocalDate(),
                    val time: LocalTime = dateTime.toLocalTime(),
                    val month: Month = date.month,
                    val dayOfYear: Int = date.dayOfYear,
                    val weekday: DayOfWeek = date.dayOfWeek,
                    val zoneId: ZoneId = ZoneId.systemDefault(),
                    val timestamp: Long = instant.epochSecond,
                    val millis: Long = System.currentTimeMillis())
