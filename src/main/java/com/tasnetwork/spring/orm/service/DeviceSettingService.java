package com.tasnetwork.spring.orm.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.repository.DeviceSettingRepo;




@Component
public class DeviceSettingService {

	
	@Autowired
	private DeviceSettingRepo deviceSettingRepo;
	
	
	@Transactional
	public void removeById(int id) {
		deviceSettingRepo.removeById(id);
	}
	
	@Transactional
	public boolean saveToDb(DeviceSetting inputRecord) {
		
		//resultSummaryRepo.findByCustomerId(id)
		/*DeviceSetting deviceSettingData = deviceSettingRepo.save(data);
		return deviceSettingData.getDeviceTypeKey();*/
		
		List<DeviceSetting> deviceSettingDataList  = deviceSettingRepo.findByDeviceTypeKey(inputRecord.getDeviceTypeKey());
		if(deviceSettingDataList.size()>0) {
			int existingId = deviceSettingDataList.get(0).getId();
			inputRecord.setId(existingId);
			
		}
		deviceSettingRepo.save(inputRecord);
		return true;
	}
	
	
	@Transactional
	public List<DeviceSetting> findAll() {
		
		return deviceSettingRepo.findAll();
		
	}
	
	@Transactional
	public List<DeviceSetting> findByCname(String cName) {
		
		return deviceSettingRepo.findByCanName(cName);
		
	}
	
	@Transactional
	public DeviceSetting findFirstByCname(String cName) {
		
		return deviceSettingRepo.findFirstByCanName(cName);
		
	}
	
	@Transactional
	public DeviceSetting findFirstByDeviceTypeKey(String deviceTypeKey) {
		
		return deviceSettingRepo.findFirstByDeviceTypeKey(deviceTypeKey);
		
	}
	
	@Transactional
	public List<DeviceSetting> findByDeviceType(String deviceType) {
		
		return deviceSettingRepo.findByDeviceType(deviceType);
		
	}
	
	@Transactional
	public Optional<DeviceSetting> findByDeviceIdAndPositionNo(String deviceId,String positionNo) {
		
		return deviceSettingRepo.findByDeviceIdAndPositionNo(deviceId,positionNo);
		
	}
	
	@Transactional
	public int countByDeviceTypeKey(String deviceTypeKey) {
		
		return deviceSettingRepo.countByDeviceTypeKey(deviceTypeKey);
		
	}
	
/*	@Transactional
	public List<ResultSummary> findByCustomerId(String id) {
		return resultSummaryRepo.findByCustomerId(id);
	}*/
}
