----------------------------------------------------------
-- Initialize DB with questions
----------------------------------------------------------

INSERT INTO components_type (component_type_id, component_type_name) VALUES (1, 'Server Endpoint');


INSERT INTO app_metadata(analysis_id, product) VALUES (1, 'FAS 3.7');
INSERT INTO app_metadata(analysis_id, product) VALUES (2, 'FAS 3.8 MIP Feature');
INSERT INTO app_metadata(analysis_id, product) VALUES (3, 'FAS 3.9 Fusion Dashboard');

INSERT INTO components(component, component_name, component_type_id, analysis_id)
VALUES ('E1', 'FAS On-prem Agent', 1, 2);
INSERT INTO components(component, component_name, component_type_id, analysis_id)
VALUES ('E2', 'MIP On-prem service', 1, 2);
INSERT INTO components(component, component_name, component_type_id, analysis_id)
VALUES ('E3', 'AJP SaaS Service', 2, 2);
INSERT INTO components(component, component_name, component_type_id, analysis_id)
VALUES ('C1', 'HTTPS Connection', 3, 2);

INSERT INTO threats(threat_id, threat) VALUES (1, 'Spoofing');
INSERT INTO threats(threat_id, threat) VALUES (2, 'Information Disclosure');
INSERT INTO threats(threat_id, threat) VALUES (3, 'Elevation of privilege');
INSERT INTO threats(threat_id, threat) VALUES (4, 'Tampering');
INSERT INTO threats(threat_id, threat) VALUES (5, 'Repudiation');
INSERT INTO threats(threat_id, threat) VALUES (6, 'Information leakage');
INSERT INTO threats(threat_id, threat) VALUES (7, 'Denial of service');

INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) 
VALUES (1, 1, 'Does the endpoint support authentication?', 'Yes', 'No', 2, 0, '', '', '', 1);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(2, 1, 'What type of authentication is supported?', 'Username/Password + session cookie', 'OAuth token', 4, -1, '', '', '', 0);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(3, 1, 'How is the password stored?', 'Salted Hash/Encrypted', 'Plain Text', -1, 0, '', '', '', 2);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(4, 1, 'What is the expiration of session cookie / oauth token?', '', '', -1, -1, '', '', '', 0);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(5, 1, 'Can the session cookie or oauth token be replayed?', 'Yes', 'No', 0, -1, '', '', '', 1);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(6, 1, 'Does the endpoint support authorization?', 'Yes', 'No', 7, -1, '', '', '', 0);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(7, 1, 'If elevation of privilege is possible?', 'Yes', 'No', 0, -1, '', '', '', 3);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(8, 1, 'Is the old session invalidated on logout or re-login?', 'Yes', 'No', -1, 0, '', '', '', 1);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(9, 1, 'Is CSRF protection enabled?', 'Yes', 'No', -1, 0, '', '', '', 1);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(10, 1, 'Is HTTPS enabled?', 'Yes', 'No', -1, 0, '', '', '', 4);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(11, 1, 'Are the requests audited?', 'Yes', 'No', -1, 0, '', '', '', 5);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(12, 1, 'Is sensitive information included in the debug logs?', 'Yes', 'No', -1, 0, '', '', '', 6);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(13, 1, 'Is the request rate limited ?', 'Yes', 'No', -1, 0, '', '', '', 7);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(14, 1, 'Are inputs validated on the server side?', 'Yes', 'No', -1, 0, '', '', '', 4);
INSERT INTO questionnaire (question_id, component_type_id, question, yes_option, no_option, yes_nextSteps, no_nextSteps, helpText,	helpURL, mitigationText, threatID) VALUES
(15, 1, 'Are inputs/outputs sanitized on the server side?', 'Yes', 'No', -1, 0, '', '', '', 4);

