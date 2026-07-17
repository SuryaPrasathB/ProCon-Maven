package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.repository.StateFlowRepo;

@Component
public class StateFlowService {

	@Autowired
	private StateFlowRepo stateFlowRepo;
	
	@Transactional
	public int saveToDb(StateFlow data) {
		StateFlow stateFlowData = stateFlowRepo.save(data);
		return stateFlowData.getId();
	}
	
	@Transactional
	public void deleteById(int id) {
	    stateFlowRepo.deleteById(id);
	}
	
	@Transactional
	public List<StateFlow> findAll() {
		return stateFlowRepo.findAll();
	}
	
	@Transactional
	public List<StateFlow> findByBayKey(String bayKey) {
		//return stateFlowRepo.findByBayKeyOrderByPathAsc(bayKey);
		return stateFlowRepo.findByBayKeyOrderBySerialNoAsc(bayKey);
	}
	
	@Transactional
	public List<StateFlow> findByBayKeyAndExecutionMode(String bayKey, String executionMode) {
		//return stateFlowRepo.findByBayKeyOrderByPathAsc(bayKey);
		return stateFlowRepo.findByBayKeyAndExecutionModeOrderBySerialNoAsc(bayKey, executionMode);
	}
}
