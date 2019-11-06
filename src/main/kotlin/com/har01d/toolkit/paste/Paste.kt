package com.har01d.toolkit.paste

import org.springframework.data.jpa.repository.JpaRepository
import java.time.Instant
import javax.persistence.*

@Entity
data class Paste(val shortUrl: String,
                 @Column(columnDefinition = "TEXT") val content: String,
                 val createdAt: Instant = Instant.now(),
                 @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null)

interface PasteRepository : JpaRepository<Paste, Int> {
    fun existsByShortUrl(url: String): Boolean
    fun findByShortUrl(url: String): Paste?
}
