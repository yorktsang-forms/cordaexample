package com.formssihk.cordaexample

import com.fasterxml.classmate.TypeResolver
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import io.swagger.annotations.ApiModel
import net.corda.core.contracts.ContractState
import net.corda.core.contracts.StateRef
import net.corda.core.contracts.TransactionState
import net.corda.core.crypto.SecureHash
import net.corda.core.identity.AbstractParty
import net.corda.core.identity.Party
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.schema.AlternateTypeRule
import springfox.documentation.service.ApiInfo
import springfox.documentation.service.Contact
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2
import java.time.Instant
import java.util.*

@SpringBootApplication
open class Application {
    companion object {
        @JvmStatic fun main(args: Array<String>) {
            runApplication<Application>(*args)
        }
    }

    /*
    @Bean
    @Primary
    open fun objectMapper(): ObjectMapper {
        val mapper = ObjectMapper()
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        return mapper
    }
    */


}

/**
 * Starts our Spring Boot application.
 */
fun main(args: Array<String>) {
    runApplication<Application>(*args)
}


/**
 * Swagger Configuration/
 *
 */
@Configuration
@EnableSwagger2
open class SwaggerConfig {

    @Autowired
    private lateinit var typeResolver: TypeResolver

    @Bean
    open fun apidoc(): Docket {
        val resolver = TypeResolver()
        val partyType = resolver.resolve(Party::class.java)

        return Docket(DocumentationType.SWAGGER_2)
            .select().apis(RequestHandlerSelectors.basePackage("com.formssihk.cordaexample"))
            .paths(PathSelectors.regex("(?!/v1).+")) //exclude swagger only
            .build()
            .directModelSubstitute(Party::class.java, String::class.java)
            .directModelSubstitute(StateRef::class.java, String::class.java)
            //.directModelSubstitute(StateAndRef::class.java, StateAndRefJsonModel::class.java)
            .directModelSubstitute(SecureHash::class.java, String::class.java)
            .directModelSubstitute(Instant::class.java, Long::class.java)
            .ignoredParameterTypes(AbstractParty::class.java, Pair::class.java, ContractState::class.java, TransactionState::class.java)
            .apiInfo(apiInfo())
    }

    private fun apiInfo(): ApiInfo {
        return ApiInfo(
            "Corda Example",
            "This is a rough sketch of APIs, things may change in the future. ",
            "0.0.1",
            "",
            Contact("York Tsang", "", "yorktsang@formssihk.com"),
            "", "", Collections.emptyList()
        )
    }
}