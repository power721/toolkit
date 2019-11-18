package com.har01d.toolkit.generator

import com.har01d.toolkit.random.RandomService
import org.springframework.stereotype.Service
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Service
class DataGenerator(private val randomService: RandomService) {
    companion object {
        private val DELIMITER = Regex("\\s*,\\s*");
        private val TOKEN = Pattern.compile("(\\$\\{?(" +
                "id\\[(\\d+)\\]|id|" +
                "boolean|" +
                "number\\[(\\d+),\\s*(\\d+)\\]|number\\[(\\d+)\\]|number|" +
                "double\\[(\\d+),\\s*(\\d+)\\]|double\\[(\\d+)\\]|double|" +
                "word|fullname|name|username|country|company|" +
                "password\\[(\\d+)\\]|password|" +
                "hex\\[(\\d+)\\]|hex|" +
                "datetime|date|timezone|timestamp|time|month|weekday|" +
                "uuid|email|color|ip|mac|version|domain|mime|file|" +
                "url\\[(\\d+)\\]|url|" +
                "size\\[(.+)\\]|size|" +
                "hostname\\[(.+)\\]|hostname|" +
                "string\\[(\\d+)\\]|string|" +
                "sentence\\[(\\d+),\\s*(\\d+)\\]|sentence\\[(\\d+)\\]|sentence|" +
                "paragraph\\[(\\d+),\\s*(\\d+)\\]|paragraph\\[(\\d+)\\]|paragraph|" +
                "enum\\[(.+)\\])\\}?)")
    }

    fun generate(template: String, size: Int = 10): String {
        val sb = StringBuffer()
        sb.append("[\n")

        val id = IdHolder()
        for (i in 1..size) {
            if (i > 1) {
                sb.append(",\n")
            }
            val matcher = TOKEN.matcher(template)
            while (matcher.find()) {
                val token = matcher.group(2)
                val type = extractType(token)
                val params = extractParams(token)
                matcher.appendReplacement(sb, generateValue(type, params, id))
            }
            matcher.appendTail(sb);
        }
        sb.append("\n]")
        return sb.toString()
    }

    fun extractType(token: String): String {
        val index = token.indexOf('[')
        if (index > -1) {
            return token.substring(0, index)
        }
        return token
    }

    fun extractParams(token: String): String? {
        val index = token.indexOf('[')
        if (index > -1) {
            return token.substring(index + 1, token.length - 1).trim()
        }
        return null
    }

    // id, id[1000]  -->  1000,1001, 1002
    // boolean  -->  true,false
    // hex, hex[3]  --> "0f75c1", "1a257d"
    // number, number[100], number[100,200]  -->  125,101,149
    // double, double[100.0], double[100.0,200.0]  -->  107.5,154.3,120.0
    // word  -->  "supervision", "graphs", "cattle"
    // name  -->  "Abolish", "Peter", "Scot"
    // password, password[12]  -->  "Hs*=UCow9%ry9", "%gOFjNpBKW&8x"
    // date  -->  "2019-09-25","2016-11-07", "2012-12-31"
    // time  -->  "12:05:00", "20:35:19", "09:15:36"
    // datetime  -->  "2019-04-25 10:23:12", "2017-12-10 15:35:12"
    // month  -->  "November", "April"
    // weekday  -->  "Friday", "Monday"
    // timestamp  -->  1573523117, 1490141289
    // timezone  -->  "Asia/Taipei", "America/Godthab"
    // uuid  -->  "aaad1003-4ecc-4c32-8372-549cee76a7dd"
    // string  -->  "GRvLv95h1vsC", "XsjZ1FgiRLVo"
    // sentence, sentence[7], sentence[7,12]
    // paragraph, paragraph[3], paragraph[3,7]
    // email  -->  "Harold.Li@gmail.com", "123456789@qq.com"
    // username  --> "admin", "user"
    // color  -->  "Red", "Green", "Pink"
    // country  -->  "China"
    // city
    // phone
    // company  --> "Apple", "Alphabet"
    // language
    // domain  -->  "google.com"
    // hostname, hostname[google.com]  -->  test.google.com
    // url, url[1]  -->  "https://www.google.com/test"
    // ip  -->  "10.121.235.200", "216.58.196.174"
    // mac  -->  "00:50:56:af:e5:bf"
    // version  -->  "6.1.10", "1.8.201", "10.16.3"
    // file  -->  "index.html", "app.css"
    // mime  --> "application/json", "text/html"
    // enum[E1,E2,E3]  -->  "E3", "E1", "E2"
    // size, size[MB,GB]  --> "5 MB", "60 GB", "128 MB"
    fun generateValue(type: String, params: String?, id: IdHolder): String {
        when (type) {
            "id" -> {
                var initial = 1000
                if (params != null) {
                    initial = Integer.parseInt(params)
                }
                return id.next(initial).toString()
            }
            "boolean" -> return randomService.boolean().toString()
            "number" -> {
                var lower = 0
                var upper = Int.MAX_VALUE
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    lower = parts[0].toInt()
                    if (parts.size == 2) {
                        upper = parts[1].toInt()
                    }
                }
                return randomService.number(lower, upper).toString()
            }
            "double" -> {
                var lower = 0.0
                var upper = Int.MAX_VALUE.toDouble()
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    lower = parts[0].toDouble()
                    if (parts.size == 2) {
                        upper = parts[1].toDouble()
                    }
                }
                return randomService.double(lower, upper).toString()
            }
            "word" -> return randomService.word()
            "name" -> return randomService.name()
            "fullname" -> return randomService.name(true)
            "password" -> {
                var length = 12
                if (params != null) {
                    length = Integer.parseInt(params)
                }
                return PasswordGenerator.generate(length).replace("$", "\\$")
            }
            "hex" -> {
                var count = 3
                if (params != null) {
                    count = Integer.parseInt(params)
                }
                return randomService.hex(count)
            }
            "date" -> {
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                return format.format(randomDateTime())
            }
            "time" -> {
                val format = DateTimeFormatter.ofPattern("HH:mm:ss")
                return format.format(randomDateTime())
            }
            "datetime" -> {
                val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                return format.format(randomDateTime())
            }
            "timestamp" -> return randomDateTime().toEpochSecond().toString()
            "month" -> return randomService.month()
            "weekday" -> return randomService.weekday()
            "timezone" -> return randomService.timezone()
            "country" -> return randomService.country()
            "company" -> return randomService.company()
            "uuid" -> return randomService.uuid()
            "username" -> return randomService.username()
            "email" -> return randomService.email()
            "domain" -> return randomService.domain()
            "mime" -> return randomService.mime()
            "file" -> return randomService.file()
            "url" -> {
                var length = 1
                if (params != null) {
                    length = Integer.parseInt(params)
                }
                return randomService.url(length)
            }
            "color" -> return randomService.color()
            "ip" -> return randomService.ip()
            "mac" -> return randomService.mac()
            "version" -> return randomService.version()
            "string" -> {
                var length = 12
                if (params != null) {
                    length = Integer.parseInt(params)
                }
                return randomService.string(length)
            }
            "sentence" -> {
                var min = 7
                var max = 12
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    min = parts[0].toInt()
                    if (parts.size == 2) {
                        max = parts[1].toInt()
                    }
                }
                return randomService.sentence(min, max)
            }
            "paragraph" -> {
                var min = 3
                var max = 7
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    min = parts[0].toInt()
                    if (parts.size == 2) {
                        max = parts[1].toInt()
                    }
                }
                return randomService.paragraph(min, max)
            }
            "enum" -> {
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    return parts[ThreadLocalRandom.current().nextInt(parts.size)].trim()
                }
                return ""
            }
            "size" -> {
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    return randomService.size(parts)
                }
                return randomService.size()
            }
            "hostname" -> {
                if (params != null) {
                    val parts = params.split(DELIMITER)
                    return randomService.hostname(parts)
                }
                return randomService.hostname()
            }
            else -> return type
        }
    }

    fun randomDateTime(): ZonedDateTime {
        val now = ZonedDateTime.now()
        return now.minusSeconds(randomService.long(0, TimeUnit.DAYS.toSeconds(7300)))
    }
}

class IdHolder {
    fun next(initial: Int): Int {
        if (id < initial) {
            id = initial
        }
        return id++
    }

    var id: Int = Int.MIN_VALUE
}
