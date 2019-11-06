package com.har01d.demo.kospring.random

import com.har01d.demo.kospring.core.BadRequestException

class Snowflake(private val datacenterId: Int, private val workerId: Int) {
    private val epoch = 1573008493430L
    private val datacenterIdBits = 5
    private val workerIdBits = 5
    private val sequenceBits = 12

    private val workerIdShift = sequenceBits
    private val datacenterIdShift = sequenceBits + workerIdBits
    private val timestampShift = sequenceBits + workerIdBits + datacenterIdBits
    private val sequenceMask = -1L xor (-1L shl sequenceBits)

    private var sequence = 0L
    private var lastTimestamp = -1L

    fun nextId(): Long {
        var timestamp = System.currentTimeMillis()
        if (timestamp < lastTimestamp) {
            throw BadRequestException("Clock moved backwards")
        }

        if (timestamp == lastTimestamp) {
            sequence = (sequence + 1) and sequenceMask
            if (sequence == 0L) {
                timestamp = nextMillis(lastTimestamp)
            }
        } else {
            sequence = 0L
        }

        lastTimestamp = timestamp
        return ((timestamp - epoch) shl timestampShift) or (datacenterId shl datacenterIdShift).toLong() or (workerId shl workerIdShift).toLong() or sequence
    }

    private fun nextMillis(lastTimestamp: Long): Long {
        var timestamp = System.currentTimeMillis()
        while (timestamp <= lastTimestamp) {
            timestamp = System.currentTimeMillis()
        }
        return timestamp
    }
}
