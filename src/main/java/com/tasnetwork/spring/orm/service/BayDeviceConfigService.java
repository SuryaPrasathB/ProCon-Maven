package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.repository.BayDeviceConfigRepo;
import com.tasnetwork.spring.orm.repository.TerminalProfileSettingRepo;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;


@Component
public class BayDeviceConfigService {

	@Autowired
	private BayDeviceConfigRepo bayDeviceConfigRepo;
	

	@Transactional
	public int saveToDb(BayDeviceConfig data) {
		BayDeviceConfig bayDeviceSetting = bayDeviceConfigRepo.save(data);
		return bayDeviceSetting.getId();
	}
	
	@Transactional
	public List<BayDeviceConfig> findAll() {
		return bayDeviceConfigRepo.findAll();
	}
}
