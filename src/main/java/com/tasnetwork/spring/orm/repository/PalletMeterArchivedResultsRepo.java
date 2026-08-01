package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.PalletMeterArchivedResults;

@Repository
public interface PalletMeterArchivedResultsRepo extends JpaRepository<PalletMeterArchivedResults, Long>{


/*	PalletMeter findById(int id);
	
	PalletMeter findByMeterSerialNoAndPalletDistinctId(String MeterSerialNo, String palletDistinctId);
	
	PalletMeter findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId);*/
}
