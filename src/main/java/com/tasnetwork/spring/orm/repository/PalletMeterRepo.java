package com.tasnetwork.spring.orm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;

@Repository
public interface PalletMeterRepo extends JpaRepository<PalletMeter, Long>{

	PalletMeter findById(int id);
	
	PalletMeter findByMeterSerialNoAndPalletDistinctId(String MeterSerialNo, String palletDistinctId);
	
	Optional<PalletMeter> findByRackPositionNoAndPalletDistinctId(int rackPositionNo, String palletDistinctId);
}
