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
@Table(name = "threats")
public class Threat implements Serializable{

	private static final long serialVersionUID = 7236398086163708757L;
	@Id
	@Column(name = "threat_id")
	private int threatID;
	@Column(name = "threat")
	private String threat;
	public int getThreatID() {
		return threatID;
	}
	public void setThreatID(int threatID) {
		this.threatID = threatID;
	}
	public String getThreat() {
		return threat;
	}
	public void setThreat(String threat) {
		this.threat = threat;
	}
}
