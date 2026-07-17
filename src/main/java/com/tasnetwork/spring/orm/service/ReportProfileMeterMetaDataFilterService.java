package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;
import com.tasnetwork.spring.orm.repository.ReportProfileMeterMetaDataFilterRepo;

@Component
public class ReportProfileMeterMetaDataFilterService {

	@Autowired
	private ReportProfileMeterMetaDataFilterRepo meterMetaDataFilterRepository;

	@Transactional
	public void saveToDb(ReportProfileMeterMetaDataFilter data) {
		meterMetaDataFilterRepository.save(data);
	}
	
	@Transactional
	public List<ReportProfileMeterMetaDataFilter> findAll() {
		return meterMetaDataFilterRepository.findAll();
	}
	
/*	@Transactional
	public List<ReportProfileMeterMetaDataFilter> findByReportProfileManageId(int reportProfileManageId) {
		return meterMetaDataFilterRepository.findByReportProfileManageId(reportProfileManageId);
	}*/
	
	@Transactional
	public List<ReportProfileMeterMetaDataFilter> findAllByOrderByTableSerialNoAsc() {
		return meterMetaDataFilterRepository.findAllByOrderByTableSerialNoAsc();
	}
	
	
	@Transactional
	public List<ReportProfileMeterMetaDataFilter> findAllByOrderByPageNumberAsc() {
		return meterMetaDataFilterRepository.findAllByOrderByPageNumberAsc();
	}
}
