package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.TerminalProfileSetting;

@Repository
public interface TerminalProfileSettingRepo extends JpaRepository<TerminalProfileSetting, Long>{

	public TerminalProfileSetting findFirstByBayKey(String bayKey);
}
