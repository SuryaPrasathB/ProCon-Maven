package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.StateFlow;
import com.tasnetwork.spring.orm.model.TerminalProfileSetting;
import com.tasnetwork.spring.orm.repository.TerminalProfileSettingRepo;


@Component
public class TerminalProfileSettingService {

	@Autowired
	private TerminalProfileSettingRepo terminalProfileSettingRepo;
	

	@Transactional
	public int saveToDb(TerminalProfileSetting data) {
		TerminalProfileSetting terminalProfileSetting = terminalProfileSettingRepo.save(data);
		return terminalProfileSetting.getId();
	}
	
	@Transactional
	public List<TerminalProfileSetting> findAll() {
		return terminalProfileSettingRepo.findAll();
	}
	
	@Transactional
	public TerminalProfileSetting findByBayKey(String bayKey) {
		return terminalProfileSettingRepo.findFirstByBayKey(bayKey);
	}
}
