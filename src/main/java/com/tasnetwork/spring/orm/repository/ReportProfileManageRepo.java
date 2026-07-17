package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ReportProfileManage;



@Repository
public interface ReportProfileManageRepo extends JpaRepository<ReportProfileManage, Long>{

	public ReportProfileManage findById(int id);
	
	public List<ReportProfileManage> findByActiveProfile(boolean activeProfile);
	
	public List<ReportProfileManage> findByReportGroupIdIn(List<String> reportGroupId);
	
	public List<ReportProfileManage> findByReportGroupNameIn(List<String> reportGroupName);
	
	public List<ReportProfileManage> findByActiveProfileAndReportGroupIdIn(boolean activeProfile,List<String> reportGroupId);
	
	public List<ReportProfileManage> findByActiveProfileAndReportGroupNameIn(boolean activeProfile,List<String> reportGroupName);
	
	public List<ReportProfileManage> findByActiveProfileAndCustomerIdAndReportGroupIdIn(boolean activeProfile,String customerId,List<String> reportGroupId);
	
	
	public List<ReportProfileManage> findByActiveProfileAndCustomerId(boolean activeProfile,String customerId);
	
	public List<ReportProfileManage> findByActiveProfileAndCustomerIdAndReportGroupNameIn(boolean activeProfile,String customerId,List<String> reportGroupName);
	
	
	public List<ReportProfileManage> findByActiveProfileAndCustomerIdAndReportGroupNameAndReportProfileNameAndBaseTemplateName(boolean activeProfile,String customerId,String reportGroupName, String reportProfileName, String baseTemplateName);
	
}
