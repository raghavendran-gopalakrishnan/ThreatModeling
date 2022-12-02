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

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;



public class LoginFilter extends GenericFilterBean {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoginFilter.class);
	// @Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {
		try {
			HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
			
			// Exclude docker container health check from authentication flow - treat it as devMode
			// For now, assume that we wont have many API versions.
			if(httpRequest.getRequestURI().contains("/threatModel/") || 
				httpRequest.getRequestURI().contains("/auth/") )
			{
				// Proceed with authentication
				if(LoginValidator.isValidUser(httpRequest, false))
			    {
					String principal = "admin";
			        String roles = "ROLE_ADMIN";
			        
			        // Read the roles sent in the token
			        Collection<? extends GrantedAuthority> authorities =
			            Arrays.asList(roles.split(",")).stream()
				                .map(authority -> new SimpleGrantedAuthority(authority))
				                .collect(Collectors.toList());

			        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, "", authorities);
					SecurityContextHolder.getContext().setAuthentication(authentication);
			    }
				else
				{
					((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);					
					return;
				}
			}
			
			// always need to do this
			filterChain.doFilter(servletRequest, servletResponse);
			
		} catch (Exception e) {
			// log.info("Security exception for user {}", eje.getMessage());
			((HttpServletResponse) servletResponse).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		}
	}

}
