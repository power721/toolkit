package com.har01d.demo.kospring.paste

import com.har01d.demo.kospring.core.NotFoundException
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("paste")
class PasteController(private val service: PasteService, private val repository: PasteRepository) {
    @PostMapping
    fun create(@RequestBody dto: PasteDto): String {
        return service.create(dto.content).shortUrl
    }

    @GetMapping("/{url}")
    fun get(@PathVariable url: String): Paste {
        return repository.findByShortUrl(url) ?: throw NotFoundException()
    }

    @GetMapping("/{url}/plain")
    fun getPlain(@PathVariable url: String): String {
        val paste = repository.findByShortUrl(url)
        if (paste == null) {
            throw NotFoundException()
        } else {
            return paste.content
        }
    }

    @DeleteMapping("/{url}")
    fun delete(@PathVariable url: String) {
        val paste = repository.findByShortUrl(url)
        if (paste != null) {
            repository.delete(paste)
        }
    }
}

data class PasteDto(val content: String)
