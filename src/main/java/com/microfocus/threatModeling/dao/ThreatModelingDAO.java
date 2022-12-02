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
package com.microfocus.threatModeling.dao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.microfocus.threatModeling.repository.ComponentsRepository;
import com.microfocus.threatModeling.repository.ComponentsTypeRepository;
import com.microfocus.threatModeling.repository.QuestionnaireRepository;
import com.microfocus.threatModeling.repository.ResponsesRepository;
import com.microfocus.threatModeling.repository.ThreatsRepository;
import com.microfocus.threatModeling.entity.Questionnaire;
import com.microfocus.threatModeling.entity.Responses;
import com.microfocus.threatModeling.entity.Threat;

@Service
public class ThreatModelingDAO {


	
	@Autowired
	ComponentsRepository componentsRepository;
	
	@Autowired
	ComponentsTypeRepository componentsTypeRepository;
	
	@Autowired
	QuestionnaireRepository questionnaireRepository;
	
	@Autowired
	ThreatsRepository threatsRepository;
	
	@Autowired
	ResponsesRepository responsesRepository;
	
	public List<Questionnaire> getQuestionnaire (int componentTypeID) {
		List<Questionnaire> list= questionnaireRepository.findByComponentTypeId(componentTypeID);
		return list;
	}
	
	public void saveResponse (Responses responses) {
		responsesRepository.save(responses);
	}
	
	public List<Responses> getResponses(int analysisID, String componentID){
		return responsesRepository.findByAnalysisIDAndComponentID(analysisID, componentID);
	}
	
	public enum componentType{
		SERVER_ENDPOINT(1);

		private final int component_type_id;
		private componentType(int component_type_id) {
			this.component_type_id = component_type_id;
		}
		public int getDateFilter() {
			return component_type_id;
		}
	}
}
