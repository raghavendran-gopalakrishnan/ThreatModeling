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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

public class AccessToken {
	private String token;
	private String tenantId;
	private long lastAccess;
	
	private String userName;
	
	// This field is only to deserialize the login JSON input. Cleared once user is validated.
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String password; 
	
	
	public void generateToken()
	{
		this.token = UUID.randomUUID().toString();
	}
	
	public void updateLastAccess()
	{
		this.lastAccess = Calendar.getInstance().getTimeInMillis();
	}
	
	public boolean isSessionActive()
	{
		// Inactivity timeout: 30 mins
		long minLastActivity = Calendar.getInstance().getTimeInMillis() - (30*60*1000);
		return (this.lastAccess >= minLastActivity);			
	}
		
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userId) {
		this.userName = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getTenantId() {
		return tenantId;
	}
	public void setTenantId(String tenantId) {
		this.tenantId = tenantId;
	}
	public long getLastAccess() {
		return lastAccess;
	}
	public void setLastAccess(long lastAccess) {
		this.lastAccess = lastAccess;
	}
	
	public String getLastAccessTimestamp() {
		return (new Timestamp(getLastAccess())).toString();
	}
	
}
