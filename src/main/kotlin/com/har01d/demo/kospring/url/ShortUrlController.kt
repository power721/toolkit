package com.har01d.demo.kospring.url

import com.har01d.demo.kospring.core.NotFoundException
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("url")
class ShortUrlController(private val service: ShortUrlService, private val repository: ShortUrlRepository) {
    @PostMapping
    fun create(@RequestBody dto: ShortUrlDto): String {
        return service.generate(dto.url).shortUrl
    }

    @GetMapping("/{url}")
    fun route(@PathVariable url: String, response: HttpServletResponse) {
        val shortUrl = repository.findByShortUrl(url)
        if (shortUrl == null) {
            throw NotFoundException()
        } else {
            response.sendRedirect(shortUrl.url)
        }
    }
}

data class ShortUrlDto(val url: String)
