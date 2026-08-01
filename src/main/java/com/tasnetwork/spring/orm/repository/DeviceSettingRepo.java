package com.tasnetwork.spring.orm.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DeviceSetting;

@Repository
public interface DeviceSettingRepo extends JpaRepository<DeviceSetting, Long>{
	
	public List<DeviceSetting> findByCanName(String cName);
	
	public DeviceSetting findFirstByCanName(String cName);
	
	public List<DeviceSetting> findByDeviceTypeKey(String deviceTypeKey);
	
	public List<DeviceSetting> findByDeviceType(String deviceType);
	
	public Optional<DeviceSetting> findByDeviceIdAndPositionNo( String deviceId,String positionNo);
	
	public DeviceSetting findFirstByDeviceTypeKey(String deviceTypeKey);
	
	public int countByDeviceTypeKey(String deviceTypeKey);
	
	//public List<DeviceSetting> findByCustomerIdAndOperationParamProfileName(String id, String operationParamProfileName);
	
	public void removeById(int id);
}
