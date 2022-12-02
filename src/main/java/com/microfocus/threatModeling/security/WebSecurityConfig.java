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
package com.microfocus.threatModeling.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;


import java.util.ArrayList;

/**
 * Entry point for Spring Security configuration to protect the AMDashboard web application.
 * It configures the access rights for various URLs and authentication mechanism.
 * 
 * @author Harippriya S
 *
 */
@Configuration 
@EnableWebSecurity 
public class WebSecurityConfig extends WebSecurityConfigurerAdapter
{
	@Autowired
    private Environment environment;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		http
			.authorizeRequests()
				.antMatchers("/threatModel/**").authenticated()  // Require authentication for APIs
				.anyRequest().permitAll()
				.and()
			.addFilterBefore(new LoginFilter(), UsernamePasswordAuthenticationFilter.class)
		    .csrf().disable();
	}

	@Bean
    @Override
    public UserDetailsService userDetailsService() {
        UserDetails user =
             User.withDefaultPasswordEncoder()
                .username("admin")
                .password("voltage")
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user);
    }

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				
				// Check if CORS allowed URL is specified
				String uiBaseUrl = environment.getProperty("SMARTCIPHER_UI_BASE_URL");
				if(uiBaseUrl != null) {
					registry.addMapping("/**").allowedMethods("*").allowedOrigins(uiBaseUrl).allowCredentials(true);
				} 
			}
		}; 
	}
	
}
