package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.ResultSummary;
import com.tasnetwork.spring.orm.repository.ResultSummaryRepo;


@Component
public class ResultSummaryService {

	
	@Autowired
	private ResultSummaryRepo resultSummaryRepo;
	
	
	@Transactional
	public int saveToDb(ResultSummary data) {
		
		//resultSummaryRepo.findByCustomerId(id)
		ResultSummary resultSummaryData = resultSummaryRepo.save(data);
		return resultSummaryData.getId();
	}
	
	
	@Transactional
	public List<ResultSummary> findAll() {
		
		return resultSummaryRepo.findAll();
		
	}
	
	@Transactional
	public List<ResultSummary> findByCustomerId(String id) {
		return resultSummaryRepo.findByCustomerId(id);
	}
}
