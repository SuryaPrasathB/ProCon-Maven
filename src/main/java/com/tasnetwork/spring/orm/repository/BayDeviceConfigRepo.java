package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.BayDeviceConfig;

@Repository
public interface BayDeviceConfigRepo extends JpaRepository<BayDeviceConfig, Long>{

	public void removeById(int id);
}
