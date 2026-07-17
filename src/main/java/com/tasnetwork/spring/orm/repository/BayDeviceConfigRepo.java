package com.tasnetwork.spring.orm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.OperationParam;
import com.tasnetwork.spring.orm.model.ReportProfileManage;
import com.tasnetwork.spring.orm.model.BayDeviceConfig;

@Repository
public interface BayDeviceConfigRepo extends JpaRepository<BayDeviceConfig, Long>{
	
/*	public List<DeviceSetting> findByCName(String cName);
	
	public DeviceSetting findFirstByCName(String cName);
	
	public List<DeviceSetting> findByDeviceTypeKey(String deviceTypeKey);
	
	public DeviceSetting findFirstByDeviceTypeKey(String deviceTypeKey);
	
	public int countByDeviceTypeKey(String deviceTypeKey);
	
	//public List<DeviceSetting> findByCustomerIdAndOperationParamProfileName(String id, String operationParamProfileName);
	*/
	public void removeById(int id);
}
