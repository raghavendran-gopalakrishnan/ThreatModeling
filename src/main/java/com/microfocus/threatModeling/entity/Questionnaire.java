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
@Table(name = "questionnaire")
public class Questionnaire implements Serializable {
		
	private static final long serialVersionUID = -3127727830234537968L;
	@Id
	@Column(name = "question_id")
	private int questionID;
	@Column(name = "component_type_id")
	private int componentTypeId;
	@Column(name = "question")
	private String question;
	@Column(name = "yes_option")
	private String yesOption;
	@Column(name = "no_option")
	private String noOption;
	@Column(name = "yes_nextsteps")
	private int yesNextSteps;
	@Column(name = "no_nextsteps")
	private int noNextSteps;
	@Column(name = "threatid", nullable = true)
	private int threatID;
	@Column(name = "helptext")
	private String helpText;
	@Column(name = "helpurl")
	private String helpURL;
	@Column(name = "mitigationtext")
	private String mitigationText;
	
	public int getQuestionID() {
		return questionID;
	}
	public void setQuestionID(int questionID) {
		this.questionID = questionID;
	}

	public int getComponentTypeId() {
		return componentTypeId;
	}
	public void setComponentTypeId(int componentTypeId) {
		this.componentTypeId = componentTypeId;
	}
	public String getQuestion() {
		return question;
	}
	public void setQuestion(String question) {
		this.question = question;
	}
	public String getYesOption() {
		return yesOption;
	}
	public void setYesOption(String yesOption) {
		this.yesOption = yesOption;
	}
	public String getNoOption() {
		return noOption;
	}
	public void setNoOption(String noOption) {
		this.noOption = noOption;
	}
	public int getYesNextSteps() {
		return yesNextSteps;
	}
	public void setYesNextSteps(int yesNextSteps) {
		this.yesNextSteps = yesNextSteps;
	}
	public int getNoNextSteps() {
		return noNextSteps;
	}
	public void setNoNextSteps(int noNextSteps) {
		this.noNextSteps = noNextSteps;
	}
	public int getThreatID() {
		return threatID;
	}
	public void setThreatID(int threatID) {
		this.threatID = threatID;
	}
	public String getHelpText() {
		return helpText;
	}
	public void setHelpText(String helpText) {
		this.helpText = helpText;
	}
	public String getHelpURL() {
		return helpURL;
	}
	public void setHelpURL(String helpURL) {
		this.helpURL = helpURL;
	}
	public String getMitigationText() {
		return mitigationText;
	}
	public void setMitigationText(String mitigationText) {
		this.mitigationText = mitigationText;
	}
}
