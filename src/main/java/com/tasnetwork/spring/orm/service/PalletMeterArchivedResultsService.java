package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.PalletMeterArchivedResults;
import com.tasnetwork.spring.orm.repository.PalletMeterArchivedResultsRepo;

@Component
public class PalletMeterArchivedResultsService {
	

	@Autowired
	private PalletMeterArchivedResultsRepo palletMeterArchivedResultsRepo;
	
	@Transactional
	public void save(PalletMeterArchivedResults palletMeterArchivedResults) {

		palletMeterArchivedResultsRepo.save(palletMeterArchivedResults);

	}
	
}
