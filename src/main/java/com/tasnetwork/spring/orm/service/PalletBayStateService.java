package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.PalletBayState;
import com.tasnetwork.spring.orm.repository.PalletBayStateRepo;

@Component
public class PalletBayStateService {
	
	
	@Autowired
	private PalletBayStateRepo palletBayStateRepo; 
	
	@Transactional
	public PalletBayState saveToDb(PalletBayState data) {
		
		PalletBayState newRecord = palletBayStateRepo.save(data);
		return newRecord;
	}

}
