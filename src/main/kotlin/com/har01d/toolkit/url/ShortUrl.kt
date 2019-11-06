package com.har01d.toolkit.url

import org.springframework.data.jpa.repository.JpaRepository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
data class ShortUrl(val shortUrl: String,
                    val url: String,
                    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Int? = null)

interface ShortUrlRepository : JpaRepository<ShortUrl, Int> {
    fun existsByShortUrl(url: String): Boolean
    fun findByShortUrl(url: String): ShortUrl?
    fun findByUrl(url: String): ShortUrl?
}
