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

import org.slf4j.MDC;
import org.springframework.web.servlet.HandlerMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;

import com.microfocus.smartcipher.common.util.UserRequestContext;

/** Request interceptor that is used by the CAF-Logging and CAF-CorrelationId libraries
 *  to format and log the statements.
 *  
 *  Prints log messages in the format:
 *  [(UTC Time) #(Process Id).(Thread Id) (Log Level) (Tenant Id) (Correlation Id)] (Logger): (Log Message)
 *  
 * @author Harippriya
 *
 */
public final class LoggingInterceptor implements HandlerInterceptor {

	@Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

        HashMap<String, String> pathParameters = (HashMap<String, String>) request
                .getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);

        if(pathParameters != null){
            final String tenantId = pathParameters.get("tenantId");
            if(tenantId != null) {
                UserRequestContext.setTenantId(tenantId);
            }
        }
        return true;
    }

    @Override
    public void postHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler,
                           final ModelAndView modelAndView) {
    	
    	// Remove all entries for this request
    	UserRequestContext.clearData();        
    }
}