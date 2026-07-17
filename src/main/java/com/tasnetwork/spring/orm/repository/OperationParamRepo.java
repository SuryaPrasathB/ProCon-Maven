package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.ReportProfileManage;

@Repository
public interface OperationParamRepo extends JpaRepository<OperationParam, Long>{
	
	public List<OperationParam> findByCustomerId(String id);
	
	public List<OperationParam> findByCustomerIdAndOperationParamProfileName(String id, String operationParamProfileName);
	
	public void removeById(int id);
}
