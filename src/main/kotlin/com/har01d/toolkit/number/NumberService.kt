package com.har01d.toolkit.number

import org.springframework.stereotype.Service

@Service
class NumberService {
    companion object {
        private val english = mapOf(0 to "zero", 1 to "one", 2 to "two", 3 to "three", 4 to "four", 5 to "five", 6 to "six", 7 to "seven", 8 to "eight", 9 to "nine",
                10 to "ten", 11 to "eleven", 12 to "twelve", 13 to "thirteen", 14 to "fourteen", 15 to "fifteen", 16 to "sixteen", 17 to "seventeen", 18 to "eughteen", 19 to "nineteen",
                20 to "twenty", 30 to "thirty", 40 to "forty", 50 to "fifty", 60 to "sixty", 70 to "seventy", 80 to "eighty", 90 to "ninety")
        private val chinese = mapOf(0 to "零", 1 to "一", 2 to "二", 3 to "三", 4 to "四", 5 to "五", 6 to "六", 7 to "七", 8 to "八", 9 to "九", 10 to "十")
        private val traditional = mapOf(0 to "零", 1 to "壹", 2 to "贰", 3 to "叁", 4 to "肆", 5 to "伍", 6 to "陆", 7 to "柒", 8 to "捌", 9 to "玖", 10 to "拾")
    }

    fun toEnglishWords(value: Int): String {
        when {
            value <= 20 -> return english[value]!!
            value < 100 -> return if (value % 10 == 0) {
                english[value]!!
            } else {
                english[value / 10 * 10] + "-" + english[value % 10]
            }
            value < 1000 -> return if (value % 100 == 0) {
                english[value / 100] + " hundred"
            } else {
                english[value / 100] + " hundred and " + toEnglishWords(value % 100)
            }
            value < 1000000 -> return if (value % 1000 == 0) {
                toEnglishWords(value / 1000) + " thousand"
            } else {
                toEnglishWords(value / 1000) + " thousand " + toEnglishWords(value % 1000)
            }
            value < 1000000000 -> return if (value % 1000000 == 0) {
                toEnglishWords(value / 1000000) + " million"
            } else {
                toEnglishWords(value / 1000000) + " million " + toEnglishWords(value % 1000000)
            }
            else -> return if (value % 1000000000 == 0) {
                toEnglishWords(value / 1000000000) + " billion"
            } else {
                toEnglishWords(value / 1000000000) + " billion " + toEnglishWords(value % 1000000000)
            }
        }
    }

    fun toChineseWords(value: Int): String {
        when {
            value < 10 -> return chinese[value]!!
            value < 100 -> return if (value % 10 == 0) {
                chinese[value / 10] + "十"
            } else if (value < 20) {
                "十" + chinese[value % 10]
            } else {
                chinese[value / 10] + "十" + chinese[value % 10]
            }
            value < 1000 -> return if (value % 100 == 0) {
                chinese[value / 100] + "百"
            } else {
                chinese[value / 100] + "百" + (if (value % 100 < 10) "零" else "") + toChineseWords(value % 100)
            }
            value < 10000 -> return if (value % 1000 == 0) {
                chinese[value / 1000] + "千"
            } else {
                chinese[value / 1000] + "千" + (if (value % 1000 < 100) "零" else "") + toChineseWords(value % 1000)
            }
            value < 100000000 -> return if (value % 10000 == 0) {
                toChineseWords(value / 10000) + "万"
            } else {
                toChineseWords(value / 10000) + "万" + (if (value % 10000 < 1000) "零" else "") + toChineseWords(value % 10000)
            }
            else -> return if (value % 100000000 == 0) {
                toChineseWords(value / 100000000) + "亿"
            } else {
                toChineseWords(value / 100000000) + "亿" + (if (value % 100000000 < 10000) "零" else "") + toChineseWords(value % 100000000)
            }
        }
    }

    fun toTraditionalWords(value: Int): String {
        when {
            value < 10 -> return traditional[value]!!
            value < 100 -> return if (value % 10 == 0) {
                traditional[value / 10] + "拾"
            } else if (value < 20) {
                "十" + traditional[value % 10]
            } else {
                traditional[value / 10] + "拾" + traditional[value % 10]
            }
            value < 1000 -> return if (value % 100 == 0) {
                traditional[value / 100] + "伯"
            } else {
                traditional[value / 100] + "伯" + (if (value % 100 < 10) "零" else "") + toTraditionalWords(value % 100)
            }
            value < 10000 -> return if (value % 1000 == 0) {
                traditional[value / 1000] + "仟"
            } else {
                traditional[value / 1000] + "仟" + (if (value % 1000 < 100) "零" else "") + toTraditionalWords(value % 1000)
            }
            value < 100000000 -> return if (value % 10000 == 0) {
                toTraditionalWords(value / 10000) + "万"
            } else {
                toTraditionalWords(value / 10000) + "万" + (if (value % 10000 < 1000) "零" else "") + toTraditionalWords(value % 10000)
            }
            else -> return if (value % 100000000 == 0) {
                toTraditionalWords(value / 100000000) + "亿"
            } else {
                toTraditionalWords(value / 100000000) + "亿" + (if (value % 100000000 < 10000) "零" else "") + toTraditionalWords(value % 100000000)
            }
        }
    }
}
