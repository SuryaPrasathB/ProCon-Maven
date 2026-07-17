package com.tasnetwork.spring.orm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DutCommand;

@Repository
public interface DutCommandRepo extends JpaRepository<DutCommand, Long>{

	Optional<DutCommand> findFirstByProjectNameAndTestCaseNameStartingWith(String projectName, String testCaseName);
	Optional<DutCommand> findFirstByProjectNameAndTestAliasId(String projectName, String testAliasId);
	Optional<DutCommand> findByProjectNameAndTestCaseNameAndTestAliasId(String projectName, String testCaseName, String testAliasId);
	List<DutCommand> findByProjectNameOrderById(String projectName);
	List<DutCommand> findByProjectNameIgnoreCaseStartingWithOrderById(String projectNamePrefix);
	
}
