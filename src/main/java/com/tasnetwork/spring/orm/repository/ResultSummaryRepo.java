package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ResultSummary;

@Repository
public interface ResultSummaryRepo extends JpaRepository<ResultSummary, Long>{
	
	public List<ResultSummary> findByCustomerId(String id);
	
	//public List<ResultSummary> findByCustomerId(String id);
	
/*	public List<OperationParam> findByCustomerIdAndOperationParamProfileName(String id, String operationParamProfileName);
	
	public void removeById(int id);*/
	
}