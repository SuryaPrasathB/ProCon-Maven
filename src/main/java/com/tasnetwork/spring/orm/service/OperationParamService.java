package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.repository.OperationParamRepo;
import com.tasnetwork.spring.orm.repository.OperationProcessRepo;

@Component
public class OperationParamService {
	@Autowired
	private OperationParamRepo operationParamRepo;
	
	
	@Transactional
	public int saveToDb(OperationParam data) {
		OperationParam reportOperationParamData = operationParamRepo.save(data);
		return reportOperationParamData.getId();
	}
	
	@Transactional
	public List<OperationParam> findByCustomerId(String id) {
		
		return  operationParamRepo.findByCustomerId(id);
	}
	
	@Transactional
	public List<OperationParam> findByCustomerIdAndOperationParamProfileName(String id, String operationParamProfileName) {
		
		return  operationParamRepo.findByCustomerIdAndOperationParamProfileName(id, operationParamProfileName);
	}
	
	
	@Transactional
	public void removeById(int id) {
		
		operationParamRepo.removeById(id);
	}
}
