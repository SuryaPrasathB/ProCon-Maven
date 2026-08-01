package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.PalletMeterResults;
import com.tasnetwork.spring.orm.repository.PalletMeterResultsRepo;

@Component
public class PalletMeterResultsService {

	@Autowired
	private PalletMeterResultsRepo palletMeterResultsRepo;

	@Transactional
	public long save(PalletMeterResults palletMeterResults) {

		PalletMeterResults savedPalletMeterResults = palletMeterResultsRepo.save(palletMeterResults);
		return savedPalletMeterResults.getId();
	}

	@Transactional
	public void deleteAll(List<PalletMeterResults> recordsToDelete) {

		palletMeterResultsRepo.deleteAllInBatch(recordsToDelete);
	}

	@Transactional
	public String findMaxOfMeterHardwareId() {

		try {
			Optional<PalletMeterResults> maxPalletMeterResults = palletMeterResultsRepo
					.findFirstByOrderByMeterHardwareIdNumericDesc();
			if (maxPalletMeterResults.isPresent()) {
				return maxPalletMeterResults.get().getMeterHardwareId();
			} else {
				ApplicationLauncher.logger.debug("findMaxOfMeterHardwareId: MeterResult not found: ");
				return "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			ApplicationLauncher.logger.debug("findMaxOfMeterHardwareId: Exception: " + e.getMessage());
			return "";
		}

	}
}
