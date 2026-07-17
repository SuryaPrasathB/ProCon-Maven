package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.PalletMeterArchivedResults;
import com.tasnetwork.spring.orm.model.PalletMeterResults;

@Repository
public interface PalletMeterArchivedResultsRepo extends JpaRepository<PalletMeterArchivedResults, Long>{


/*	PalletMeter findById(int id);
	
	PalletMeter findByMeterSerialNoAndPalletDistinctId(String MeterSerialNo, String palletDistinctId);
	
	PalletMeter findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId);*/
}
