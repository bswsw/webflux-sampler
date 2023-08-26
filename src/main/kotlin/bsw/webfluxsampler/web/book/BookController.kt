package bsw.webfluxsampler.web.book

import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.*
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/v1/books", produces = [MediaType.APPLICATION_JSON_VALUE])
class BookController(private val bookMapper: BookMapper) {

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/{book-id}")
    fun getBook(@PathVariable("book-id") bookId: Long): Mono<BookResponse?> {
        val book = books.find { it.id == bookId }

        logger.info { "id: ${book?.id}, name: ${book?.name}" }

        return Mono.justOrEmpty(book?.let { bookMapper.bookToResponse(it) })
    }

    @GetMapping
    fun getBooks(): Flux<BookResponse> {
        logger.info { "books: ${books.size}" }

        return Flux.fromIterable(books.map(bookMapper::bookToResponse))
    }

    private val logger = KotlinLogging.logger { }

    private val books = listOf(
        Book(1L, "java"),
        Book(2L, "kotlin"),
        Book(3L, "scala"),
    )

}
