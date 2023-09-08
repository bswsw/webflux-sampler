package bsw.webfluxsampler.config

import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SpringdocConfig {

    @Bean
    fun bookDoc(): GroupedOpenApi =
        GroupedOpenApi
            .builder()
            .group("books")
            .pathsToMatch("/books/**")
            .build()
}
