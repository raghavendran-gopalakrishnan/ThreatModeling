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


import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.json.*;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.microfocus.smartcipher.common.util.UserRequestContext;
import com.microfocus.smartcipher.common.util.KeyCloakUtility;



@Component
public class LoginValidator {

	public final static String AUTHORIZATION_HEADER = "Authorization";
	
	public final static String DEFAULT_RESPONSE_STR = "{\"cn\":\"admin\"}";
	
	private static final HashMap<String, AccessToken> sessionMap = new HashMap<>();
	
	public static boolean isValidUser(HttpServletRequest httpRequest, boolean getGroups) 
    {
    	boolean isValid = false;
    	AccessToken tokenObj = null;
    	String requestURI = httpRequest.getRequestURI();
    	
    	boolean isLogoutRequest = requestURI.contains("/logout");
    	if(isLogoutRequest)
    	{
    		// Just return true so we can invalidate the token even if it has expired.
        	return true;
    	}
    	
    	UserRequestContext.setLocale(httpRequest);
    	
    	final String authorization = httpRequest.getHeader("Authorization");
    	if (authorization != null && authorization.toLowerCase().startsWith("basic")) {
		    // User has logged in with Basic authentication. Credentials passed in the header as:
			// Authorization: Basic base64credentials
		    String base64Credentials = authorization.substring("Basic".length()).trim();
		    byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
		    String credentials = new String(credDecoded, StandardCharsets.UTF_8);
		    final String[] values = credentials.split(":", 2);
		    String username = values[0];
		    String password = values[1];
		    
	    	isValid = isValidCredential(username, password);
		}
    	else if (authorization != null)
    	{
    		//Logged in with bearer OAuth token. extracting username and tenant id from token
    		String jwt = resolveToken(httpRequest);
    		if(jwt != null)
    		{
    			
    			try
    			{
    				final String[] jwtValues = jwt.split("\\.");
    				
    				byte[] payloadDecoded = Base64.getDecoder().decode(jwtValues[1]);
    			    String payloadJSON = new String(payloadDecoded);
    			    
    			    JSONObject jsnnObj = new JSONObject(payloadJSON);
    			    
    			    String tokenUserName = new String(jsnnObj.getString("preferred_username"));
    			    String tokenIssuer = new String(jsnnObj.getString("iss"));
    			    
    			    String[] issuerArray = tokenIssuer.split("/");
    			    		
    			    String tokenTenantId = issuerArray[issuerArray.length-1];
    			    String userLocale=httpRequest.getLocale().toString();
    			    
    				UserRequestContext.setUserId(tokenUserName);
    				UserRequestContext.setTenantId(tokenTenantId);
    				
    				if(jsnnObj.has("email"))
    				{
    					String userEmail = new String(jsnnObj.getString("email"));
    					UserRequestContext.setUserEmail(userEmail);
    				}
    				if(jsnnObj.has("preferred_username"))
    				{
    					String userName = new String(jsnnObj.getString("preferred_username"));
    					UserRequestContext.setUserName(userName);
    				}
    				// TODO: Get the groups from the token if available
    				JSONArray arrJson = null;
    			    
    			    if(jsnnObj.has("groups"))
    			    {
    			    	
    			    	arrJson = jsnnObj.getJSONArray("groups");
        			    String[] groupArr = new String[arrJson.length()];
        			    if(arrJson.length() != 0)
        			    {
        			    	for(int i = 0; i < arrJson.length(); i++)
        			    	{
        			    		String group = arrJson.getString(i);
        			    		if(group.startsWith("/"))
        			    			groupArr[i] = group.substring(1);
        			    		else
        			    			groupArr[i] = group;
        			    	}
        			    	String userGroups = String.join(",", groupArr);
            			    UserRequestContext.setUserGroups(userGroups);
        			    }
        			    
    			    }
    			    else if(jsnnObj.has("sub")){
    			    	String userGuid = new String(jsnnObj.getString("sub"));
    			    	
    			    	String groups = KeyCloakUtility.getGroupsbyUid(UserRequestContext.getTenantId(), userGuid);
    					UserRequestContext.setUserGroups(groups);
    			    }
    			    
    				// parsed successfully without exceptions. Consider token as valid.
    				isValid = true;
    			}
    			catch (Exception e) {
    				e.printStackTrace();
    				//log.info("Invalid JWT token: " + e.getMessage());
    			}
    		}
    		else
    		{
    			// Logged in with access token. Check if valid.
    			tokenObj = getTokenObj(authorization.trim());
    			if(tokenObj != null && tokenObj.isSessionActive())
    			{
    				UserRequestContext.setUserId(tokenObj.getUserName());
    				UserRequestContext.setTenantId(tokenObj.getTenantId());
    				isValid = true;
    			}
    			else if(requestURI.contains("keyserver")) {
    				// TODO: Just allow the request until sensor supports token based auth
    				System.out.println("Assuming the value in Authorization header is the tenant ID");
    				UserRequestContext.setUserName("admin");
    				UserRequestContext.setUserEmail("admin@microfocus.com");
    				UserRequestContext.setTenantId(authorization.trim());
    				UserRequestContext.setUserGroups("mfgroup");
    				isValid = true;
    			}
    		}
    	}
		else
		{
			// check if logged in through a form.
			try
			{
				String credentialsJson = httpRequest.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
				if(credentialsJson!= null && credentialsJson.contains("{"))
				{
					ObjectMapper mapper = new ObjectMapper();
					AccessToken tmpTokenObj = mapper.readValue(credentialsJson, AccessToken.class);	
					isValid = isValidCredential(tmpTokenObj.getUserName(), tmpTokenObj.getPassword());
					if(tmpTokenObj.getTenantId() != null) {
						UserRequestContext.setTenantId(tmpTokenObj.getTenantId());
					}
				}
				
			}
			catch(Exception e)
			{
				//e.printStackTrace();
			}
		}
		
    	// TODO: Get roles from the token
    	if(isValid)
    	{
    		 String roles = "ROLE_ADMIN";
    		 UserRequestContext.setUserRoles(roles);
    	     
    	     if(tokenObj == null)
    	     {
	    	     tokenObj = new AccessToken();
	    	     tokenObj.setUserName(UserRequestContext.getUserId()); 
	    	     tokenObj.setTenantId(UserRequestContext.getTenantId());
	    	     tokenObj.generateToken();
    	     }
    	     tokenObj.updateLastAccess();
    	     
    	     if(getGroups && UserRequestContext.getUserGroups() == null) {
    	    	 // TODO: User groups was not part of the token. Get the groups for this user from KeyCloak.
    	    	 String tenant = UserRequestContext.getTenantId();
    	    	 String group = "";
    	    	 UserRequestContext.setUserGroups(group);
    	     }
    	     UserRequestContext.setAccessToken(tokenObj.getToken());
    	     addToSession(tokenObj);
    		
    	    
    		// TODO: Need a scheduler to purge expired sessions
    	}    	
    	
    	return isValid;
        
    }

	public static boolean isValidCredential(String username, String password)
	{
		boolean isValid = false;
		if(username.equals("admin@microfocus.com") && password.equals("voltage"))
    	{
    		UserRequestContext.setUserId(username);
            UserRequestContext.setTenantId("tenant1");
            UserRequestContext.setUserGroups("mfgroup");
            isValid = true;
        }
    	else if(username.equals("bob@acme.com") && password.equals("voltage"))
    	{
    		UserRequestContext.setUserId(username);
            UserRequestContext.setTenantId("tenant2");
            UserRequestContext.setUserGroups("acmegroup");
            isValid = true;
    	}
		return isValid;
	}
	
    

    public static String resolveToken(HttpServletRequest request){
    	String jwt = null;
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
        {
        	jwt = bearerToken.substring(7, bearerToken.length());
        }
        else 
        {
        	// maybe it was sent in as a URL parameter. Try to read that.
        	jwt = request.getParameter("token");
        }
        
        return jwt;
    }
    
    public static void addToSession(AccessToken tokenObj)
	{
		sessionMap.put(tokenObj.getToken(), tokenObj);
	}
	
	public static void endSession(String accessToken)
	{
		if(accessToken != null && sessionMap.containsKey(accessToken))
			sessionMap.remove(accessToken);
	}
	
	public static AccessToken getTokenObj(String accessToken)
	{
		return sessionMap.get(accessToken);
	}
}
