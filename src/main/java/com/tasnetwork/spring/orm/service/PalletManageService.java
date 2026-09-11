package com.tasnetwork.spring.orm.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.repository.PalletManageRepo;

@Component
public class PalletManageService {

	@Autowired
	private PalletManageRepo palletManageRepo;
	
	@Transactional
	public PalletManage saveToDb(PalletManage data) {
		
		for (PalletMeter meter : data.getPalletMeterList()) {
			ApplicationLauncher.logger.debug("PalletManageService: PalletMeter.getId(): " + meter.getId() + ", Position : "+meter.getRackPositionNo()+
					", getMeterSerialNo: " + meter.getMeterSerialNo());
		}
		
		PalletManage newRecord = palletManageRepo.save(data);
		return newRecord;
	}
	
	@Transactional
	public List<PalletManage> saveAll(Iterable<PalletManage> entities) {
		return palletManageRepo.saveAll(entities);
	}
	
	@Transactional
	public List<PalletManage> findAll() {
		return palletManageRepo.findAll();
	}
	
	@Transactional
	public Page<PalletManage> findAllPaginated(int page, int size) {
		return palletManageRepo.findAllByOrderByCreatedAtDesc(PageRequest.of(page, size));
	}
	
	@Transactional
	public List<PalletManage> findAllByOrderByPalletDistinctIdAsc() {
		return palletManageRepo.findAllByOrderByPalletDistinctIdAsc();
	}
	
	
	@Transactional
	public List<PalletManage> findByPalletConvEntryTimeEpochBetweenAndPalletExecutionStatus(
			String startEpoch, String endEpoch, String palletExecutionStatus){
		return palletManageRepo.findByPalletConvEntryTimeEpochBetweenAndPalletExecutionStatus( startEpoch, endEpoch, palletExecutionStatus);
	}
	
	@Transactional
	public List<PalletManage> findByCreatedAtAfter(Date  cutoff) {
		return palletManageRepo.findByCreatedAtAfterOrderByCreatedAtDesc(cutoff);
	}
	
	@Transactional
	public Page<PalletManage> findByCreatedAtAfterPaginated(Date cutoff, int page, int size) {
		return palletManageRepo.findByCreatedAtAfterOrderByCreatedAtDesc(cutoff, PageRequest.of(page, size));
	}
	
	@Transactional
	public List<PalletManage> findByPalletActive() {
		return palletManageRepo.findByPalletActive(true);
	}

	@Transactional
	public List<PalletManage> findByPalletActiveOrderByIdDesc() {
		return palletManageRepo.findByPalletActiveOrderByIdDesc(true);
	}

	@Transactional
	public List<PalletManage> findByPalletQrIdAndPalletActive(String palletQrCode, boolean isPalletActive) {
		return palletManageRepo.findByPalletQrIdAndPalletActive(palletQrCode, isPalletActive);
	}
	
	@Transactional
	public List<PalletManage> findByBayKeyAndEpochRange(String bayKey,int startEpoch,int endEpoch) {
		return palletManageRepo.findByBayKeyAndEpochRange( bayKey,startEpoch, endEpoch);
		
	}
	
	@Transactional
	public List<PalletManage> findByBayKeyAndPalletActive(String bayKey) {
		return palletManageRepo.findByPresentBayKeyAndPalletActive( bayKey,true);
		
	}
	@Transactional
	public Optional<PalletManage> findTopByBayKeyAndPalletActive(String bayKey) {
		return palletManageRepo.findTopByPresentBayKeyAndPalletActive( bayKey,true);
		
	}

	@Transactional
	public Optional<PalletManage> findTopByBayKeyAndPalletActiveOrderByIdDesc(String bayKey) {
		return palletManageRepo.findTopByPresentBayKeyAndPalletActiveOrderByIdDesc(bayKey, true);
	}
	
	@Transactional
	public List<PalletManage> findByPresentBayKeyAndPalletActive(String presentBayKey) {
		//return palletManageRepo.findByPresentBayKeyAndPalletActive(presentBayKey, true);
		//return palletManageRepo.findByPresentBayKeyAndPalletActiveOrderByPalletBatchNoAsc(presentBayKey, true);
		return palletManageRepo.findByPresentBayKeyAndPalletActiveOrderByPalletConvEntryTimeEpochAscPalletBatchNoAsc(presentBayKey, true);
	}
	
	@Transactional
	public Optional<PalletManage> findByPalletDistinctId(String inpDistinctId) {
	    return palletManageRepo.findByPalletDistinctId(inpDistinctId);
	}
	
	@Transactional
	public PalletManage findFirstByPalletDistinctId(String inpDistinctId) {
		return palletManageRepo.findFirstByPalletDistinctId(inpDistinctId);
	}
	
	@Transactional
	public List<PalletManage> findByPalletQrIdAndExitNotAppeared(String palletQrCode) {
		return palletManageRepo.findByPalletQrIdAndExitAppeared(palletQrCode,false);
	}
	
	@Transactional
	public Optional<PalletManage> findTopByPresentBayKeyAndPalletQrIdAndExitNotAppeared(String presentBayKey,   String palletQrId) {
		return palletManageRepo.findTopByPresentBayKeyAndPalletQrIdAndExitAppearedOrderByIdDesc(presentBayKey, palletQrId,    false);
	}
	

	
	@Transactional
	public Optional<PalletManage> findTopByTodayDateAndPresentBayKeyAndPalletQrIdAndExitAppeared(String presentBayKey, String palletQrId,boolean exitAppeared) {
	    return palletManageRepo.findTopByPresentBayKeyAndPalletQrIdAndExitAppearedToday(presentBayKey, palletQrId, exitAppeared);
	}
	
	@Transactional
	public Optional<PalletManage> findTopByPalletQrId(String presentBayKey, String palletQrId,boolean exitAppeared) {
	    return palletManageRepo.findTopByPresentBayKeyAndPalletQrIdAndExitAppearedToday(presentBayKey, palletQrId, exitAppeared);
	}
	
	
/*	@Transactional
	public Optional<PalletManage> findTopFromLastXDays(String lastXdays, String presentBayKey, String palletQrId,boolean exitAppeared) {
		
		LocalDateTime xDaysAgo = LocalDateTime.now().minusDays(Long.parseLong(lastXdays));
		
		Pageable topItem = PageRequest.of(0, 1);
	    return palletManageRepo.findTopByPresentBayKeyAndPalletQrIdAndExitAppearedToday(presentBayKey, palletQrId, exitAppeared);
	}*/
	
	@Transactional
	public List<PalletManage> findTopByPresentBayKeyAndPalletActiveFalseToday(String presentBayKey) {
	    return palletManageRepo.findTopByPresentBayKeyAndPalletActiveFalseToday(presentBayKey);
	}
	
	@Transactional
	public PalletManage findLastByPalletQrIdAndExitNotAppeared(String palletQrCode) {
		return palletManageRepo.findLastByPalletQrIdAndExitAppeared(palletQrCode,false);
	}
	
	@Transactional
	public Optional<PalletManage> findLastByPalletQrIdAndPresentBayKey(String palletQrCode,String presentBayKey) {
		return palletManageRepo.findTopByPalletQrIdAndPresentBayKeyOrderByIdDesc(palletQrCode,presentBayKey);
	}
	
	@Transactional
	public PalletManage findLastByPalletDistinctId(String inpDistinctId) {
		return palletManageRepo.findLastByPalletDistinctId(inpDistinctId);
	}
	
	@Transactional
	public List<PalletManage> findByPalletConvEntryDateH(String PalletConvEntryDateH) {
		return palletManageRepo.findByPalletConvEntryDateH(PalletConvEntryDateH);
	}
	
	public List<PalletManage> findTopXActiveByPresentBayKey(String bayKey, int x) {
	    //Pageable pageable = new PageRequest(0, x);
	    Pageable pageable = PageRequest.of(0, x);
	    return palletManageRepo.findByPresentBayKeyAndActiveOrderByCreatedAtAsc(bayKey, pageable);
	    
	}
	
	public List<PalletManage> findLastWeekTopXActiveByPresentBayKey(String bayKey, int x) {
	    // Create Pageable for pagination
	    //Pageable pageable = new PageRequest(0, x); // use PageRequest.of() instead of deprecated constructor
		Pageable pageable = PageRequest.of(0, x);
	    // Calculate date for 1 week ago
	    Calendar calendar = Calendar.getInstance();
	    calendar.add(Calendar.DAY_OF_YEAR, -7);
	    Date oneWeekAgo = calendar.getTime();

	    // Call updated repository method
	    return palletManageRepo.findByPresentBayKeyAndPalletActiveAndCreatedAtAfterOrderByCreatedAtAsc(
	        bayKey, true, oneWeekAgo, pageable
	    );
	}
/*	@Transactional
	public PalletManage findWithDetails(int id) {
		return palletBayTestRepo.findWithDetails(id);
	}*/
	
	
}
