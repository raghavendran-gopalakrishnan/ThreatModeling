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
package com.microfocus.threatModeling.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "responses")
public class Responses implements Serializable{

	private static final long serialVersionUID = 2093335438300334438L;

	@Id
    @Column(name = "analysis_id")
	private int analysisID;
    @Column(name = "component_id")
	private String componentID;
    @Column(name = "question_id")
	private int questionID;
    @Column(name = "response_option")
	private String responseOption;
    @Column(name = "is_threat")
	private boolean isThreat;
    @Column(name = "threat_id")
    private int threatId;
    @Column(name = "dread")
	private String dread;
    @Column(name = "mitigation")
	private String mitigation;
	public int getAnalysisID() {
		return analysisID;
	}
	public void setAnalysisID(int analysisID) {
		this.analysisID = analysisID;
	}
	public String getComponentID() {
		return componentID;
	}
	public void setComponentID(String componentID) {
		this.componentID = componentID;
	}
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}
	public String getResponseOption() {
		return responseOption;
	}
	public void setResponseOption(String responseOption) {
		this.responseOption = responseOption;
	}
	public boolean isThreat() {
		return isThreat;
	}
	public void setThreat(boolean isThreat) {
		this.isThreat = isThreat;
	}

	public int getThreatId() {
		return threatId;
	}
	public void setThreatId(int threatId) {
		this.threatId = threatId;
	}
	public String getDread() {
		return dread;
	}
	public void setDread(String dread) {
		this.dread = dread;
	}
	public String getMitigation() {
		return mitigation;
	}
	public void setMitigation(String mitigation) {
		this.mitigation = mitigation;
	}

}
