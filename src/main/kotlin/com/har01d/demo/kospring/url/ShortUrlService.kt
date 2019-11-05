package com.har01d.demo.kospring.url

import com.har01d.demo.kospring.utils.IdGenerator
import org.springframework.stereotype.Service

@Service
class ShortUrlService(private val repository: ShortUrlRepository) {
    fun generate(url: String): ShortUrl {
        val shortUrl = repository.findByUrl(url)
        if (shortUrl != null) {
            return shortUrl
        }

        var sUrl: String
        do {
            sUrl = IdGenerator.generate()
        } while (repository.existsByShortUrl(sUrl))
        return repository.save(ShortUrl(sUrl, url))
    }
}
