package com.formssihk.cordaexample;

import com.fasterxml.classmate.TypeResolver;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.StateRef;
import net.corda.core.contracts.TransactionState;
import net.corda.core.crypto.SecureHash;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import java.lang.reflect.Type;
import java.time.Instant;
import java.util.Collections;
import springfox.documentation.service.Contact;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Configuration
    @EnableSwagger2
    public class SwaggerConfig {

        @Autowired
        TypeResolver typeResolver;

        @Bean
        public Docket apiDoc() {

            TypeResolver resolver = new TypeResolver();

            return new Docket(DocumentationType.SWAGGER_2)
                    .select().apis(RequestHandlerSelectors.basePackage("com.formssihk.cordaexample"))
                    .paths(PathSelectors.regex("(?!/v1).+")) //exclude swagger only
                    .build()
                    .directModelSubstitute(Party.class, String.class)
                    .directModelSubstitute(StateRef.class, String.class)
                    //.directModelSubstitute(StateAndRef::class.java, StateAndRefJsonModel::class.java)
                    .directModelSubstitute(SecureHash.class, String.class)
                    .directModelSubstitute(Instant.class, Long.class)
                    .ignoredParameterTypes(AbstractParty.class, Pair.class, ContractState.class, TransactionState.class)
                    .apiInfo(apiInfo());
        }

        private ApiInfo apiInfo() {
            return new ApiInfo(
                    "Corda Example",
                    "This is a rough sketch of APIs, things may change in the future. ",
                    "0.0.1",
                    "",
                    new Contact("York Tsang", "", "yorktsang@formssihk.com"),
                    "", "", Collections.emptyList()
            );

        }
    }
}
