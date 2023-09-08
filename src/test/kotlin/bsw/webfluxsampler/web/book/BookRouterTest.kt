package bsw.webfluxsampler.web.book

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.http.MediaType
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.RestDocumentationExtension
import org.springframework.restdocs.request.RequestDocumentation.parameterWithName
import org.springframework.restdocs.request.RequestDocumentation.pathParameters
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.document
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation.documentationConfiguration
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.TestConstructor
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.ServerResponse

@ExtendWith(SpringExtension::class, MockKExtension::class, RestDocumentationExtension::class)
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureRestDocs
@ContextConfiguration(classes = [BookRouter::class])
class BookRouterTest(
    @MockkBean
    private val handler: BookHandler,
    bookRouterV2: RouterFunction<ServerResponse>,
    restDocumentation: RestDocumentationContextProvider
) {

    private val webTestClient = WebTestClient
        .bindToRouterFunction(bookRouterV2)
        .configureClient()
        .filter(documentationConfiguration(restDocumentation))
        .build()

    @Test
    fun `404로 Book 조회 실패`() {
        every { handler.getOne(any()) } returns ServerResponse.notFound().build()

        webTestClient
            .get()
            .uri("/v2/books/{book-id}", 1)
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isNotFound
            .expectBody()
            .consumeWith(
                document(
                    "books/getOne",
                    pathParameters(
                        parameterWithName("book-id").description("bookId")
                    )
                )
            )
    }
}
