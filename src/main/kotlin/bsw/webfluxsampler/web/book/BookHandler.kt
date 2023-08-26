package bsw.webfluxsampler.web.book

import mu.KotlinLogging
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyToMono
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.switchIfEmpty
import reactor.kotlin.core.util.function.component1
import reactor.kotlin.core.util.function.component2
import java.net.URI

@Component("bookHandlerV1")
class BookHandler(private val mapper: BookMapper) {

    fun create(request: ServerRequest) =
        request
            .bodyToMono<BookBody.Post>()
            .flatMap { saveBook(it.bookName) }
            .flatMap {
                ServerResponse
                    .created(URI.create("/v1/books/${it.id}"))
                    .build()
            }

    fun update(request: ServerRequest) =
        Mono
            .just(request.pathVariable("book-id").toLong())
            .zipWith(request.bodyToMono<BookBody.Patch>())
            .flatMap { (bookId, body) -> updateBook(bookId, body.bookName) }
            .flatMap {
                ServerResponse
                    .ok()
                    .bodyValue(mapper.bookToResponse(it))
            }
            .switchIfEmpty { ServerResponse.notFound().build() }

    fun getList(request: ServerRequest) =
        findBooks()
            .collectList()
            .flatMap {
                ServerResponse
                    .ok()
                    .bodyValue(it.map(mapper::bookToResponse))
            }

    fun getOne(request: ServerRequest) =
        Mono
            .just(request.pathVariable("book-id").toLong())
            .flatMap(::findBook)
            .flatMap {
                ServerResponse
                    .ok()
                    .bodyValue(mapper.bookToResponse(it))
            }
            .switchIfEmpty { ServerResponse.notFound().build() }
            .onErrorResume { ServerResponse.badRequest().bodyValue("error") }

    private val books: List<Book> = mutableListOf(
        Book(1L, "java"),
        Book(2L, "kotlin"),
        Book(3L, "scala"),
    )

    private fun saveBook(name: String) =
        Mono.just(Book(books.last().id + 1, name))

    private fun updateBook(id: Long, name: String) =
        findBook(id).map { Book(id, name) }

    private fun findBooks(): Flux<Book> =
        Flux.fromIterable(books)

    private fun findBook(id: Long): Mono<Book> =
        Mono.justOrEmpty(books.find { it.id == id })

    private val logger = KotlinLogging.logger { }
}