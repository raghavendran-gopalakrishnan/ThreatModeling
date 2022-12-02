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

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ResponseBean 
{
	public static final String HTTP_CODE = "code:";
	public static final String EXCEPTION_STR = "exception:";
	
	private int status = HttpURLConnection.HTTP_OK; // default to 200 OK. will be overwritten if there is an error
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String	message;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String	url;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private String 	errorDetail;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Object data;
	
	@JsonInclude(JsonInclude.Include.NON_NULL)
	private Map<String,Long> filters = null;
		
	public int getStatus() {
		return status;
	}
	public void setStatus(int responseCode) {
		this.status = responseCode;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getErrorDetail() {
		return errorDetail;
	}
	public void setErrorDetail(String errorDetail) {
		this.errorDetail = errorDetail;
	}
	
	public void setErrorDetail(Exception e) {
		if(e != null)
		{
			setErrorDetail(e.toString());
		}
	}
	
	public Object getData() {
		return data;
	}
	public void setData(Object bean) {
		this.data = bean;
	}
	
	public boolean parseResponse(String responseStr, String tenantId, String configKey)
	{
		boolean isSuccess = false;
		if(responseStr == null)
		{
			this.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			this.setMessage("Failed to read the configuration");
		}
		else if(responseStr.startsWith(HTTP_CODE))
		{
			responseStr = responseStr.replace(HTTP_CODE, "");
			this.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			try
			{
				int code = Integer.parseInt(responseStr);
				this.setStatus(code);
			}
			catch(Exception e) {}
			
			if(HttpURLConnection.HTTP_UNAVAILABLE == this.getStatus())
			{
				this.setMessage("Service is unavailable. The data service or the underlying database may not be functioning.");
			}
			else if(HttpURLConnection.HTTP_NOT_FOUND == this.getStatus())
			{
				this.setMessage("Requested API is not reachable.");
			}
			else if(HttpURLConnection.HTTP_BAD_REQUEST == this.getStatus())
			{
				this.setMessage("Failed to find the configuration '" + configKey + "' for tenant " + tenantId);
			}
			else
			{
				this.setMessage("Server error");
			}
		}
		else if(responseStr.startsWith(EXCEPTION_STR))
		{
			this.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			this.setMessage("Unexcepted server error: ");
			this.setErrorDetail(responseStr.replace(EXCEPTION_STR, ""));
		}
		else if(responseStr.contains("not found"))
		{
			this.setStatus(HttpURLConnection.HTTP_BAD_REQUEST);
			this.setMessage("Failed to find the configuration '" + configKey + "' for tenant " + tenantId);
		}
		else if(responseStr.contains("save failed"))
		{
			this.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
			this.setMessage("Failed to save configuration '" + configKey + "' for " + tenantId);
		}
		else if(responseStr.contains("status"))
		{
			// json error message
			try
			{
				ObjectMapper mapper = new ObjectMapper();
				ResponseBean tempBean = mapper.readValue(responseStr, ResponseBean.class);
				this.setStatus(tempBean.getStatus());
				this.setMessage(tempBean.getMessage());
				this.setData(tempBean.getData());
				this.setFilters(tempBean.getFilters());
			}
			catch(Exception ex)
			{
				this.setStatus(HttpURLConnection.HTTP_INTERNAL_ERROR);
				this.setMessage("Server error");
			}
			isSuccess = isSuccess();
		}
		else
		{
			isSuccess = true;
			this.setStatus(HttpURLConnection.HTTP_OK);
			this.setMessage("Success");
		}
		
		return isSuccess;
	}
	
	@JsonIgnore
	public boolean isSuccess() {
		return (this.getStatus() == HttpURLConnection.HTTP_OK);
	}
	
	
	public String toJson()
	{
		String jsonStr;
		try
		{
			ObjectMapper om = new ObjectMapper();
			StringWriter sw = new StringWriter();
			om.writeValue(sw, this);
	
			jsonStr = sw.getBuffer().toString();
		}
		catch(Exception e)
		{
			jsonStr ="{\"message\":\"Unexpected error - " + e.getMessage() + "\"}";
		}
		
		return jsonStr;
	}
	
	public Map<String, Long> getFilters() {
		return filters;
	}
	public void setFilters(Map<String, Long> filters) {
		this.filters = filters;
	}
		
	
}
