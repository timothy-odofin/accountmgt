package iobank.org.accountmgt.config;


/**
 *
 * @author JIDEX
 */

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


@Configuration
@EnableSwagger2
public class SwaggerConfig {


    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("iobank.org.accountmgt.route"))
                .paths(PathSelectors.any())
                .build();
    }


    private ApiInfo apiInfo() {
        return new ApiInfoBuilder().title("Account management application that supports the creation of a\n" +
                        "new account, deposit, transaction log, and withdrawal.").description("")
                .termsOfServiceUrl("https://iobank.org")
                .contact(new Contact("Account Manager", "https://iobank.org", "admin@iobank.org"))
                .license("Open Source").licenseUrl("https://iobank.org").version("1.0.0").build();
    }

}
