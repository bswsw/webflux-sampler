package bsw.webfluxsampler.web.book

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.router

@Configuration("bookRouterV1")
class BookRouter {

    @Bean
    fun route(handler: BookHandler) = router {
        POST("/v2/books", handler::create)
        PATCH("/v2/books/{book-id}", handler::update)
        GET("/v2/books", handler::getList)
        GET("v2/books/{book-id}", handler::getOne)
    }

}