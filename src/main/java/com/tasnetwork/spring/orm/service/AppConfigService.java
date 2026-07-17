package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.model.AppConfig;
import com.tasnetwork.spring.orm.model.DutCommand;
import com.tasnetwork.spring.orm.repository.AppConfigRepo;
import com.tasnetwork.spring.orm.repository.DutCommandRepo;


@Component
public class AppConfigService {

	@Autowired
	private AppConfigRepo appConfigRepo;
	
	
	@Transactional
	public Optional<AppConfig> findFirstByCustomerIdAndPropertyName(String customerId, String propertyName) {
        return appConfigRepo.findFirstByCustomerIdAndPropertyName(customerId, propertyName);
    }
	
/*	@Transactional
	public Long saveToDb (DutCommand data) {
		DutCommand dutCommand = dutCommandRepo.save(data);
		return dutCommand.getId();
		
		
	        // Try to find existing record
	        Optional<DutCommand> existingCommand = dutCommandRepo.findFirstByProjectNameAndTestAliasId(
	            data.getProjectName(), 
	            data.getTestAliasId()
	        );
	        
	        if (existingCommand.isPresent()) {
	            // Update existing record
	           // DutCommand existing = existingCommand.get();
	            
	            // Add any other fields that need to be updated
	            data.setId(existingCommand.get().getId());
	            DutCommand updated = dutCommandRepo.save(data);
	            return updated.getId();
	        } else {
	            // Save as new record
	            DutCommand newCommand = dutCommandRepo.save(data);
	            return newCommand.getId();
	        }
	    
	}
	
	
	@Transactional
	public List<DutCommand> findAll () {
		List<DutCommand> dutCommandList = dutCommandRepo.findAll();
		return dutCommandList;
	}
	
	@Transactional
	public Optional<DutCommand> findFirstByProjectNameAndTestAliasId(String projectName, String testAliasId) {
        return dutCommandRepo.findFirstByProjectNameAndTestAliasId(projectName, testAliasId);
    }
	
	@Transactional
	public Optional<DutCommand> findByProjectNameAndTestCaseNameAndTestAliasId(String projectName,String testCaseName, String testAliasId) {
        return dutCommandRepo.findByProjectNameAndTestCaseNameAndTestAliasId(projectName, testCaseName, testAliasId);
    }
	
	@Transactional
	public Optional<DutCommand> findFirstByProjectNameAndTestCaseNameStartingWith(String projectName, String testCaseName) {
        return dutCommandRepo.findFirstByProjectNameAndTestCaseNameStartingWith(projectName, testCaseName);
    }*/
}
