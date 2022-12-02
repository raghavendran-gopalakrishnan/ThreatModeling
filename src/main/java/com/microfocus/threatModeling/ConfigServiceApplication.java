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

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

import com.github.cafapi.correlation.spring.CorrelationIdInterceptor;
import com.microfocus.smartcipher.common.interceptor.HealthcheckInterceptor;
import com.microfocus.threatModeling.dao.DAOFactory;
import com.microfocus.threatModeling.dao.ThreatModelingDAO;

@SpringBootApplication(
    scanBasePackages = {"io.swagger", "com.microfocus.threatModeling"})
@EntityScan("com.microfocus.threatModeling")
public class ConfigServiceApplication extends SpringBootServletInitializer implements WebMvcConfigurer {
   

    public static void main(String[] args) {
    	SpringApplication.run(ConfigServiceApplication.class, args);
    }

    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        final UrlPathHelper urlPathHelper = new UrlPathHelper();
        urlPathHelper.setUrlDecode(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }

    @Override
    public void configureContentNegotiation(final ContentNegotiationConfigurer configurer) {
        configurer.favorPathExtension(true)
            .ignoreAcceptHeader(true)
            .useRegisteredExtensionsOnly(false)
            .defaultContentType(MediaType.APPLICATION_JSON, MediaType.ALL);
    }

    @Override
    public void addInterceptors(final InterceptorRegistry registry){
    	registry.addInterceptor(new LoggingInterceptor());
        registry.addInterceptor(new CorrelationIdInterceptor());

    }
    
    @Autowired
    private ThreatModelingDAO threatModelingDAO;
    
    
    @PostConstruct
    public void init() {
 	   DAOFactory.getInstance().setThreatModelingDAO(threatModelingDAO);
    }
    
    @PreDestroy
    public void onShutdown() 
    {

    }

}
