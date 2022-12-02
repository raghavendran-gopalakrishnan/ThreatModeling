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
package com.microfocus.threatModeling.rest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.microfocus.smartcipher.common.rest.model.ResponseBean;
import com.microfocus.smartcipher.common.util.GenericUtil;
import com.microfocus.threatModeling.dao.DAOFactory;
import com.microfocus.threatModeling.entity.Questionnaire;
import com.microfocus.threatModeling.entity.Responses;
import com.microfocus.threatModeling.repository.QuestionnaireRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/threatModel")
@Api(value = "Questions", tags = "Questions")
public class ThreatModelingAPI {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThreatModelingAPI.class);
    
    @Autowired
	QuestionnaireRepository questionnaireRepository;


    @RequestMapping(value = "/getQuestions", method = RequestMethod.GET, produces = {"application/JSON"})
    @ApiOperation(value = "Get questionnaire for requested Component Type")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> getQuestions(
    		@ApiParam(value = "componentTypeID") @RequestParam int componentTypeID,
            @ApiParam(value = "Authorization") @RequestHeader("Authorization") String accessToken) {
        ResponseBean responseBean = new ResponseBean();
        List<Questionnaire> questions = DAOFactory.getInstance().getThreatModelingDAO().getQuestionnaire(componentTypeID);
        responseBean.setData(questions);
        responseBean.setStatus(HttpStatus.OK.value());
        return new ResponseEntity(responseBean.toJson(), GenericUtil.getStatus(responseBean));
    }
    
    @RequestMapping(value = "/saveResponses", method = RequestMethod.POST, produces = {"application/JSON"})
    @ApiOperation(value = "Save responses")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> saveResponses(
    		@ApiParam(value = "Responses") @RequestBody Responses responses,
            @ApiParam(value = "Authorization") @RequestHeader("Authorization") String accessToken
            ){
    	 ResponseBean responseBean = new ResponseBean();
    	 DAOFactory.getInstance().getThreatModelingDAO().saveResponse(responses);
    	 responseBean.setStatus(HttpStatus.OK.value());
         return new ResponseEntity(responseBean.toJson(), GenericUtil.getStatus(responseBean));
    }

    @RequestMapping(value = "/getResponses", method = RequestMethod.GET, produces = {"application/JSON"})
    @ApiOperation(value = "Get responses for the questionnaire")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Success")})
    public ResponseEntity<?> getResponses(
    		@ApiParam(value = "analysisID") @RequestParam int analysisID,
    		@ApiParam(value = "componentID") @RequestParam String componentID,
            @ApiParam(value = "Authorization") @RequestHeader("Authorization") String accessToken) {
        ResponseBean responseBean = new ResponseBean();
        List<Responses> responses = DAOFactory.getInstance().getThreatModelingDAO().getResponses(analysisID,componentID);
        responseBean.setData(responses);
        responseBean.setStatus(HttpStatus.OK.value());
        return new ResponseEntity(responseBean.toJson(), GenericUtil.getStatus(responseBean));
    }
  }
