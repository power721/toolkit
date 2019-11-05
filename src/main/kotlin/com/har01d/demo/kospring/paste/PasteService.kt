package com.har01d.demo.kospring.paste

import com.har01d.demo.kospring.utils.IdGenerator
import org.springframework.stereotype.Service

@Service
class PasteService(private val repository: PasteRepository) {
    fun create(content: String): Paste {
        var sUrl: String
        do {
            sUrl = IdGenerator.generate()
        } while (repository.existsByShortUrl(sUrl))
        return repository.save(Paste(sUrl, content))
    }
}
