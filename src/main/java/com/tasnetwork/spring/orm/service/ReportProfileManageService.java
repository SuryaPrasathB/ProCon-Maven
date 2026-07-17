package com.tasnetwork.spring.orm.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.device.DeviceDataManagerController;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;
import com.tasnetwork.spring.orm.repository.ReportProfileManageRepo;

@Component
public class ReportProfileManageService {

	@Autowired
	private ReportProfileManageRepo reportProfileManageRepo;
	
	//static private ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService = DeviceDataManagerController.getReportProfileMeterMetaDataFilterService();

	@Transactional
	public int saveToDb(ReportProfileManage data) {
		ReportProfileManage reportProfileManage = reportProfileManageRepo.save(data);
		return reportProfileManage.getId();
	}
	
	@Transactional
	public List<ReportProfileManage> findAll() {
		return reportProfileManageRepo.findAll();
	}
	
	@Transactional
	public List<ReportProfileManage> findByActiveProfile() {
		return reportProfileManageRepo.findByActiveProfile(true);
	}
	
	
	
	@Transactional
	public List<ReportProfileManage> findByReportGroupIdList(List<String> reportGroupId) {
		return reportProfileManageRepo.findByReportGroupIdIn(reportGroupId);
	}
	
	@Transactional
	public List<ReportProfileManage> findByReportGroupNameList(List<String> reportGroupName) {
		return reportProfileManageRepo.findByReportGroupNameIn(reportGroupName);
	}
	
	
	@Transactional
	public List<ReportProfileManage> findActiveProfileByReportGroupIdList(List<String> reportGroupId) {
		return reportProfileManageRepo.findByActiveProfileAndReportGroupIdIn(true,reportGroupId);
	}
	
	@Transactional
	public List<ReportProfileManage> findActiveProfileByReportGroupNameList(List<String> reportGroupName) {
		return reportProfileManageRepo.findByActiveProfileAndReportGroupNameIn(true,reportGroupName);
	}
	
	
	@Transactional
	public List<ReportProfileManage> findActiveProfileAndCustomerId(String customerId) {
		return reportProfileManageRepo.findByActiveProfileAndCustomerId(true,customerId);
	}
	
	@Transactional
	public List<ReportProfileManage> findActiveCustomerByReportGroupIdList(String customerId, List<String> reportGroupId) {
		return reportProfileManageRepo.findByActiveProfileAndCustomerIdAndReportGroupIdIn(true,customerId,reportGroupId);
	}
	
	@Transactional
	public List<ReportProfileManage> findActiveCustomerByReportGroupNameList(String customerId, List<String> reportGroupName) {
		return reportProfileManageRepo.findByActiveProfileAndCustomerIdAndReportGroupNameIn(true,customerId,reportGroupName);
	}
	
	@Transactional
	public List<ReportProfileManage> findByActiveProfileAndCustomerIdAndReportGroupNameAndReportProfileNameAndBaseTemplateName (boolean activeProfile,String customerId,String reportGroupName, String reportProfileName, String baseTemplateName){
		return reportProfileManageRepo.findByActiveProfileAndCustomerIdAndReportGroupNameAndReportProfileNameAndBaseTemplateName(activeProfile,customerId,reportGroupName, reportProfileName, baseTemplateName);
	}
	
/*	@Transactional
	public ReportProfileManage findById(int id) {
		
		ReportProfileManage reportProfileManage  = reportProfileManageRepo.findById(id);
		List<ReportProfileMeterMetaDataFilter> metaDataList =  getReportProfileMeterMetaDataFilterService().findByReportProfileManageId(reportProfileManage.getId());// getReportProfileManageModel().getDutMetaDataList();
		reportProfileManage.setDutMetaDataList(metaDataList);
		return reportProfileManage;
		//return  reportProfileManageRepo.findById(id);
	}*/
	
	@Transactional
	public ReportProfileManage findById(int id) {
		
		return  reportProfileManageRepo.findById(id);
	}

/*	public static ReportProfileMeterMetaDataFilterService getReportProfileMeterMetaDataFilterService() {
		return reportProfileMeterMetaDataFilterService;
	}

	public static void setReportProfileMeterMetaDataFilterService(
			ReportProfileMeterMetaDataFilterService reportProfileMeterMetaDataFilterService) {
		ReportProfileManageService.reportProfileMeterMetaDataFilterService = reportProfileMeterMetaDataFilterService;
	}*/
}
