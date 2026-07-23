package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tasnetwork.spring.orm.model.StateFlow;

public interface StateFlowRepo extends JpaRepository<StateFlow, Long>{
	public List<StateFlow> findByBayKeyOrderByPathAsc(String bayKey);
	public List<StateFlow> findByBayKeyOrderBySerialNoAsc(String bayKey);
	public List<StateFlow> findByBayKeyAndExecutionModeOrderByPathAsc(String bayKey, String executionMode);
	public List<StateFlow> findByBayKeyAndExecutionModeOrderBySerialNoAsc(String bayKey, String executionMode);
	public List<StateFlow> deleteById(int id);
}
