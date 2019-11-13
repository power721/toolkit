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
        private val TOKEN = Pattern.compile("(\\$\\{?(" +
                "id\\[(\\d+)\\]|id|" +
                "boolean|" +
                "number\\[(\\d+),\\s*(\\d+)\\]|number\\[(\\d+)\\]|number|" +
                "double\\[(\\d+),\\s*(\\d+)\\]|double\\[(\\d+)\\]|double|" +
                "word|fullname|name|country|company|" +
                "password\\[(\\d+)\\]|password|" +
                "hex\\[(\\d+)\\]|hex|" +
                "datetime|date|timezone|timestamp|time|" +
                "uuid|email|color|ip|mac|version|" +
                "string\\[(\\d+)\\]|string|" +
                "sentence\\[(\\d+),\\s*(\\d+)\\]|sentence\\[(\\d+)\\]|sentence|" +
                "paragraph\\[(\\d+),\\s*(\\d+)\\]|paragraph\\[(\\d+)\\]|paragraph|" +
                "enum\\[(.+)\\])\\}?)")
    }

    fun generate(template: String, size: Int = 10): String {
        val sb = StringBuffer()
        sb.append("[")

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
        sb.append("]")
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
            return token.substring(index + 1, token.length - 1)
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
    // timestamp  -->  1573523117, 1490141289
    // timezone  -->  "Asia/Taipei", "America/Godthab"
    // uuid  -->  "aaad1003-4ecc-4c32-8372-549cee76a7dd"
    // string  -->  "GRvLv95h1vsC", "XsjZ1FgiRLVo"
    // sentence, sentence[7], sentence[7,12]
    // paragraph, paragraph[3], paragraph[3,7]
    // email  -->  "Harold.Li@emc.com", "123456789@qq.com"
    // username
    // color  -->  "Red", "Green", "Pink"
    // country  -->  "China"
    // city
    // phone
    // company  --> "Apple", "Alphabet"
    // language
    // domain
    // url
    // ip  -->  "10.121.235.200", "216.58.196.174"
    // mac  -->  "00:50:56:af:e5:bf"
    // version  -->  "6.1.10", "1.8.201", "10.16.3"
    // file
    // mime
    // enum[E1,E2,E3]  -->  "E3", "E1", "E2"
    fun generateValue(type: String, params: String?, id: IdHolder): String {
        if (type == "id") {
            var initial = 1000
            if (params != null) {
                initial = Integer.parseInt(params)
            }
            return id.next(initial).toString()
        } else if (type == "boolean") {
            return randomService.boolean().toString()
        } else if (type == "number") {
            var lower = 0
            var upper = Int.MAX_VALUE
            if (params != null) {
                val parts = params.split(",")
                lower = parts[0].toInt()
                if (parts.size == 2) {
                    upper = parts[1].toInt()
                }
            }
            return randomService.number(lower, upper).toString()
        } else if (type == "double") {
            var lower = 0.0
            var upper = Int.MAX_VALUE.toDouble()
            if (params != null) {
                val parts = params.split(",")
                lower = parts[0].toDouble()
                if (parts.size == 2) {
                    upper = parts[1].toDouble()
                }
            }
            return randomService.double(lower, upper).toString()
        } else if (type == "word") {
            return randomService.word()
        } else if (type == "name") {
            return randomService.name()
        } else if (type == "fullname") {
            return randomService.name(true)
        } else if (type == "password") {
            var length = 12
            if (params != null) {
                length = Integer.parseInt(params)
            }
            return PasswordGenerator.generate(length).replace("$", "\\$")
        } else if (type == "hex") {
            var count = 3
            if (params != null) {
                count = Integer.parseInt(params)
            }
            return randomService.hex(count)
        } else if (type == "date") {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            return format.format(randomDateTime())
        } else if (type == "time") {
            val format = DateTimeFormatter.ofPattern("HH:mm:ss")
            return format.format(randomDateTime())
        } else if (type == "datetime") {
            val format = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            return format.format(randomDateTime())
        } else if (type == "timestamp") {
            return randomDateTime().toEpochSecond().toString()
        } else if (type == "timezone") {
            return randomService.timezone()
        } else if (type == "country") {
            return randomService.country()
        } else if (type == "company") {
            return randomService.company()
        } else if (type == "uuid") {
            return randomService.uuid()
        } else if (type == "email") {
            return randomService.email()
        } else if (type == "color") {
            return randomService.color()
        } else if (type == "ip") {
            return randomService.ip()
        } else if (type == "mac") {
            return randomService.mac()
        } else if (type == "version") {
            return randomService.version()
        } else if (type == "string") {
            var length = 12
            if (params != null) {
                length = Integer.parseInt(params)
            }
            return randomService.string(length)
        } else if (type == "sentence") {
            var min = 7
            var max = 12
            if (params != null) {
                val parts = params.split(",")
                min = parts[0].toInt()
                if (parts.size == 2) {
                    max = parts[1].toInt()
                }
            }
            return randomService.sentence(min, max)
        } else if (type == "paragraph") {
            var min = 3
            var max = 7
            if (params != null) {
                val parts = params.split(",")
                min = parts[0].toInt()
                if (parts.size == 2) {
                    max = parts[1].toInt()
                }
            }
            return randomService.paragraph(min, max)
        } else if (type == "enum") {
            if (params != null) {
                val parts = params.split(",")
                return parts[ThreadLocalRandom.current().nextInt(parts.size)].trim()
            }
            return ""
        } else {
            return type
        }
    }

    fun randomDateTime(): ZonedDateTime {
        val now = ZonedDateTime.now()
        return now.minusSeconds(randomService.long(0, TimeUnit.DAYS.toSeconds(3650)))
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
