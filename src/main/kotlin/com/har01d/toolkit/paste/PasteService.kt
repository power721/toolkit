package com.har01d.toolkit.paste

import com.har01d.toolkit.generator.IdGenerator
import org.springframework.stereotype.Service

@Service
class PasteService(private val repository: PasteRepository) {
    fun create(content: String): Paste {
        var sUrl: String
        do {
            sUrl = IdGenerator.generate(6)
        } while (repository.existsByShortUrl(sUrl))
        return repository.save(Paste(sUrl, content))
    }
}
