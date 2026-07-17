package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterResults;
import com.tasnetwork.spring.orm.repository.PalletMeterRepo;
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
		// TODO Auto-generated method stub
		//palletMeterResultsRepo.deleteAll(recordsToDelete);
/*		List<Long> idsToDelete = recordsToDelete.stream()
			    .map(PalletMeterResults::getId)
			    .collect(Collectors.toList());*/

		//	palletMeterResultsRepo.deleteAllById(idsToDelete);
		//palletMeterResultsRepo.deleteAllInBatch(recordsToDelete);
		palletMeterResultsRepo.deleteInBatch(recordsToDelete);
	}
	
	
	@Transactional
	public String findMaxOfMeterHardwareId() {
		
/*		try {
			Optional<PalletMeterResults> maxPalletMeterResults = palletMeterResultsRepo.findFirstByOrderByMeterHardwareIdDesc();
			if(maxPalletMeterResults.isPresent()){
				return maxPalletMeterResults.get().getMeterHardwareId();
			}else {
				ApplicationLauncher.logger.debug("findMaxOfMeterHardwareId: MeterResult not found: ");
				return "";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			ApplicationLauncher.logger.debug("findMaxOfMeterHardwareId: Exception: " + e.getMessage());
			return "";
		}*/
		
		try {
	        Optional<PalletMeterResults> maxPalletMeterResults = 
	            palletMeterResultsRepo.findFirstByOrderByMeterHardwareIdNumericDesc();
	        if(maxPalletMeterResults.isPresent()){
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
	
/*	@Transactional
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
	public PalletMeter findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId) {
		return palletMeterRepo.findByRackPositionNoAndPalletDistinctId(rackPositionNo,palletDistinctId);
	}
	
	@Transactional
	public int save(PalletMeter palletMeter) {
		//if there is issue in saving the PalletMeterResults 
		// when adding to list use the function addPalletMeterResults
		
		for (PalletMeterResults palletMeterResult : palletMeter.getPalletMeterResultsList()) {
			palletMeterResult.setPalletMeter(palletMeter); 
		}
		
		
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
	}*/
}
