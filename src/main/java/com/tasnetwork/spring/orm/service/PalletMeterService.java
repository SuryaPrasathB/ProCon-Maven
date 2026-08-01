package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.repository.PalletMeterRepo;

@Component
public class PalletMeterService {
	

	@Autowired
	private PalletMeterRepo palletMeterRepo;
	
	@Transactional
	public List<PalletMeter> findAll() {
		return palletMeterRepo.findAll();
	}

	@Transactional
	public PalletMeter findById(int id) {
		return palletMeterRepo.findById(id);
	}
	
	@Transactional
	public PalletMeter findByMeterSerialNoAndPalletDistinctId(String MeterSerialNo, String palletDistinctId) {
		return palletMeterRepo.findByMeterSerialNoAndPalletDistinctId( MeterSerialNo,  palletDistinctId);
	}
	
	@Transactional
	public Optional<PalletMeter> findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId) {
		return palletMeterRepo.findByRackPositionNoAndPalletDistinctId(rackPositionNo,palletDistinctId);
	}
	
	@Transactional
	public int save(PalletMeter palletMeter) {
		//if there is issue in saving the PalletMeterResults 
		// when adding to list use the function addPalletMeterResults
		
/*		for (PalletMeterResults palletMeterResult : palletMeter.getPalletMeterResultsList()) {
			palletMeterResult.setPalletMeter(palletMeter); 
		}*/
		
		
		//for (PalletMeter meter : data.getPalletMeterList()) {
			ApplicationLauncher.logger.debug("PalletMeterService: getMeterSerialNo: " + palletMeter.getMeterSerialNo());
		//}
		String meterSerialNo = palletMeter.getMeterSerialNo();
	    
	    if (meterSerialNo == null) {
	        ApplicationLauncher.logger.error("PalletMeter save failed: meterSerialNo is null. PalletMeter ID: " 
	                                         + palletMeter.getId()+ " position ID: " + palletMeter.getRackPositionNo() + " getPalletDistinctId: " + palletMeter.getPalletDistinctId());
	        
	    }else if (meterSerialNo.trim().isEmpty()) {
	    	
	    	ApplicationLauncher.logger.error("PalletMeter save failed: meterSerialNo is empty. PalletMeter ID: " 
                    + palletMeter.getId() + " position ID: " + palletMeter.getRackPositionNo() + " getPalletDistinctId: " + palletMeter.getPalletDistinctId());
	    }
		palletMeterRepo.save(palletMeter);
		int generatedId = 0;
		generatedId = palletMeter.getId(); 
		return generatedId;
	}
}
