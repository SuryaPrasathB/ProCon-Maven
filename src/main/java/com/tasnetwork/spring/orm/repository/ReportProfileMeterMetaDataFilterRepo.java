package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ReportProfileMeterMetaDataFilter;

@Repository
public interface ReportProfileMeterMetaDataFilterRepo extends JpaRepository<ReportProfileMeterMetaDataFilter, Long> {

	List<ReportProfileMeterMetaDataFilter> findAllByOrderByTableSerialNoAsc();
	
	List<ReportProfileMeterMetaDataFilter> findAllByOrderByPageNumberAsc();
	
	//List<ReportProfileMeterMetaDataFilter> findByReportProfileManageId(int reportProfileManageId);

}
