package hr.hsnopek.springjwtrtr.configuration;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.HttpAuthenticationScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfiguration {
	@Bean
	public Docket api() {

		return new Docket(DocumentationType.OAS_30)
				.apiInfo(apiInfo())
				.securityContexts(Arrays.asList(securityContext()))
				.securitySchemes(Collections.singletonList(apiKey())).select()
				.apis(RequestHandlerSelectors.any())
				.paths(PathSelectors.any())
				.build();
	}

	private SecurityContext securityContext() {
		return SecurityContext.builder().securityReferences(defaultAuth()).build();
	}

	private List<SecurityReference> defaultAuth() {
		AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
		AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
		authorizationScopes[0] = authorizationScope;
		return Arrays.asList(new SecurityReference("BearerToken", authorizationScopes));
	}

	private ApiInfo apiInfo() {
		return new ApiInfo(
				"Spring JWT authentication with rotating refresh tokens", 
				"Some custom description of API.", "1.0", 
				"Terms of service",
				new Contact("Hrvoje Snopek", "", "zsnopek@gmail.com"), 
				"License of API",
				"API license URL", 
				Collections.emptyList());
	}
	private SecurityScheme apiKey() {
		return HttpAuthenticationScheme.JWT_BEARER_BUILDER.name("BearerToken").build();
	}
	
	
}