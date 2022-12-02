package com.microfocus.threatModeling.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.microfocus.threatModeling.entity.Responses;

public interface ResponsesRepository extends CrudRepository<Responses, Integer>{
	List<Responses> findByAnalysisIDAndComponentID(int analysisID, String componentID);
}
