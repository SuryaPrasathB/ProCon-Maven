package com.tasnetwork.spring.orm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.PalletMeterResults;

@Repository
public interface PalletMeterResultsRepo extends JpaRepository<PalletMeterResults, Long>{

//	void deleteAllInBatch(List<PalletMeterResults> list);
//	public void  deleteAllInBatch(List<Long> idsToDelete);
	//public void  deleteAllById(List<Long> idsToDelete);
//	public void deleteAll(List<PalletMeterResults> recordsToDelete);
/*	PalletMeter findById(int id);
	
	PalletMeter findByMeterSerialNoAndPalletDistinctId(String MeterSerialNo, String palletDistinctId);
	
	PalletMeter findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId);*/
	
	Optional<PalletMeterResults> findFirstByOrderByMeterHardwareIdDesc();
	
	//@Query("SELECT p FROM PalletMeterResults p ORDER BY CAST(p.meterHardwareId AS int) DESC")
	@Query(value = "SELECT * FROM pallet_results ORDER BY CAST(meter_hardware_id AS UNSIGNED) DESC LIMIT 1", 
	           nativeQuery = true)
    Optional<PalletMeterResults> findFirstByOrderByMeterHardwareIdNumericDesc();
}
