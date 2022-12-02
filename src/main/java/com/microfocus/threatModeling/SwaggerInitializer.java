/*
 * Copyright 2021-2022 Micro Focus or one of its affiliates.
 *
 * The only warranties for products and services of Micro Focus and its
 * affiliates and licensors ("Micro Focus") are set forth in the express
 * warranty statements accompanying such products and services. Nothing
 * herein should be construed as constituting an additional warranty.
 * Micro Focus shall not be liable for technical or editorial errors or
 * omissions contained herein. The information contained herein is subject
 * to change without notice.
 *
 * Contains Confidential Information. Except as specifically indicated
 * otherwise, a valid license is required for possession, use or copying.
 * Consistent with FAR 12.211 and 12.212, Commercial Computer Software,
 * Computer Software Documentation, and Technical Data for Commercial
 * Items are licensed to the U.S. Government under vendor's standard
 * commercial license.
 */
package com.microfocus.threatModeling;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.service.SecurityScheme;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.builders.PathSelectors;
import java.util.*;

@Configuration
@EnableSwagger2
@ComponentScan(basePackages = "com.microfocus.threatModeling")
public class SwaggerInitializer {
	
	public static final String SSL_ENABLE = "SSL_ENABLE";

    @Bean
    public Docket rulesApi(){
    	
    	Tag questionApi = new Tag("Questions", "Get questionnaire for requested component type");
    	
  
    	
    	String protocols[] = {"http"};
   
    	
    	// Create Tags or groups of the APIs
    	return new Docket(DocumentationType.SWAGGER_2)
                .select()
                	.apis(RequestHandlerSelectors.basePackage("com.microfocus.threatModeling.rest"))
                	//.paths(PathSelectors.regex(".*/v.*"))
                	.build()
                .apiInfo(apiInfo())
                .protocols(new HashSet<String>(Arrays.asList(protocols)))
                .tags(questionApi)
                .useDefaultResponseMessages(false);
               // .securityContexts(Arrays.asList(securityContext()))
               // .securitySchemes(Arrays.asList(basicAuthScheme()));
    }

    private ApiInfo apiInfo() {
    	return new ApiInfoBuilder()
    	        .title("SmartCipher Configuration APIs")
    	        .description("APIs to read and manage the configurations of SmartCipher. "
    	                + "These APIs are to be used by administrators or by other processes that need to manage the configuration. "
    	                + "<br /> <br />"
    	                + "<b>Common API response codes</b>: <br />"
    	                + "200: Success<br />"
    	                + "400: Invalid Input. The tenant identifier may be incorrect or the configuration data may be invalid.<br />"
    	                + "503: One or more backend services are not functioning as expected. <br />"
    	                + "500: Unexpected server error.<br />")
    	        .version("1.0.0")
    	        .build();
    }
    
    private SecurityContext securityContext() {
        return SecurityContext.builder()
                .securityReferences(Arrays.asList(basicAuthReference()))
                .forPaths(PathSelectors.ant("/threatModel/**"))
                .build();
    }

    private SecurityScheme basicAuthScheme() {
        return new BasicAuth("basicAuth");
    }

    private SecurityReference basicAuthReference() {
        return new SecurityReference("basicAuth", new AuthorizationScope[0]);
    }

}
