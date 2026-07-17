package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterArchivedResults;
import com.tasnetwork.spring.orm.model.PalletMeterResults;
import com.tasnetwork.spring.orm.repository.PalletMeterArchivedResultsRepo;
import com.tasnetwork.spring.orm.repository.PalletMeterRepo;
import com.tasnetwork.spring.orm.repository.PalletMeterResultsRepo;

@Component
public class PalletMeterArchivedResultsService {
	

	@Autowired
	private PalletMeterArchivedResultsRepo palletMeterArchivedResultsRepo;
	
	@Transactional
	public void save(PalletMeterArchivedResults palletMeterArchivedResults) {

		palletMeterArchivedResultsRepo.save(palletMeterArchivedResults);

	}
	
}
