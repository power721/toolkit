package com.har01d.demo.kospring.url

import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("u")
class ShortUrlController(private val service: ShortUrlService, private val repository: ShortUrlRepository) {
    @PostMapping
    fun create(@RequestBody dto: ShortUrlDto): String {
        return service.generate(dto.url).shortUrl
    }

    @GetMapping("/{url}")
    fun route(@PathVariable url: String, response: HttpServletResponse) {
        val shortUrl = repository.findByShortUrl(url)
        if (shortUrl == null) {
            response.sendError(404)
        } else {
            response.sendRedirect(shortUrl.url)
        }
    }
}

data class ShortUrlDto(val url: String)
