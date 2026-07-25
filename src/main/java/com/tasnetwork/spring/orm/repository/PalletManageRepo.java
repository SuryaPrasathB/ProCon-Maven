package com.tasnetwork.spring.orm.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DeviceSetting;
import com.tasnetwork.spring.orm.model.OperationProcess;
import com.tasnetwork.spring.orm.model.PalletManage;

@Repository
public interface PalletManageRepo extends JpaRepository<PalletManage, Long>{
	
	public PalletManage findFirstByPalletDistinctId(String inpDistinctId);
	Optional<PalletManage> findByPalletDistinctId(String palletDistinctId);
	public PalletManage findLastByPalletDistinctId(String inpDistinctId);
	
	//public List<PalletManage> findByPresentBayKeyAndPalletActive(String presentBayKey, boolean palletActive);
	//public List<PalletManage> findByPresentBayKeyAndPalletActiveOrderByPalletBatchNoAsc(String presentBayKey, boolean palletActive);
	public List<PalletManage> findByPresentBayKeyAndPalletActiveOrderByPalletConvEntryTimeEpochAscPalletBatchNoAsc(String presentBayKey, boolean palletActive);
	
	
	public List<PalletManage> findByPalletQrIdAndExitAppeared(String palletQrCode,boolean exitAppeared);
	public Optional<PalletManage> findTopByPresentBayKeyAndPalletQrIdAndExitAppearedOrderByIdDesc(
		    String presentBayKey,
		    String palletQrId,
		    Boolean exitAppeared
		);
	public PalletManage findLastByPalletQrIdAndExitAppeared(String palletQrCode,boolean exitAppeared);
	
	//public Optional<PalletManage> findLastByPalletQrIdAndPresentBayKey(String palletQrCode,String presentBayKey);
	
	public Optional<PalletManage> findTopByPalletQrIdAndPresentBayKeyOrderByIdDesc(String palletQrCode,String presentBayKey);
	public List<PalletManage> findByPalletActive(boolean palletActive);
	
	public List<PalletManage> findByCreatedAtAfterOrderByCreatedAtDesc(Date cutoff);
	public Page<PalletManage> findByCreatedAtAfterOrderByCreatedAtDesc(Date cutoff, Pageable pageable);
	
	public List<PalletManage> findByPalletConvEntryDateH(String palletConvEntryDateH);
	
	public List<PalletManage> findAllByOrderByPalletDistinctIdAsc();
	public Page<PalletManage> findAllByOrderByCreatedAtDesc(Pageable pageable);
	    
	public List<PalletManage> findByPalletConvEntryTimeEpochBetweenAndPalletExecutionStatus(String startEpoch, String endEpoch, String palletExecutionStatus);
	
	public List<PalletManage> findByPresentBayKeyAndPalletActive(String bayKey, boolean isPalletActive);
	
	public Optional<PalletManage> findTopByPresentBayKeyAndPalletActive(String bayKey, boolean isPalletActive);
	
	    // For descending order:
	public List<PalletManage> findAllByOrderByPalletDistinctIdDesc();
	
	@Query("SELECT p FROM PalletManage p " +
		       "WHERE p.palletActive = true AND p.presentBayKey = :bayKey " +
		       "ORDER BY p.createdAt ASC")
	List<PalletManage> findByPresentBayKeyAndActiveOrderByCreatedAtAsc(
		    @Param("bayKey") String bayKey, Pageable pageable);
	
	
	@Query(
		    value = "SELECT * FROM pallet_manage p " +
		            "WHERE p.present_bay_key = :presentBayKey " +
		            "AND p.pallet_active = false " +
		            "AND p.exit_appeared = false " +
		            "AND DATE(p.created_at) = CURRENT_DATE " +
		            "ORDER BY p.id DESC LIMIT 1",
		    nativeQuery = true
		)
	List<PalletManage> findTopByPresentBayKeyAndPalletActiveFalseToday(
		    @Param("presentBayKey") String presentBayKey
		);
	
	
	@Query("SELECT p FROM PalletManage p " +
		       "WHERE p.presentBayKey = :bayKey " +
		       "AND CAST(p.palletConvEntryTimeEpoch AS int) BETWEEN :startEpoch AND :endEpoch")
		List<PalletManage> findByBayKeyAndEpochRange(
		    @Param("bayKey") String bayKey,
		    @Param("startEpoch") int startEpoch,
		    @Param("endEpoch") int endEpoch
		);
	
	

	@Query(
		    value = "SELECT * FROM pallet_manage p " +
		            "WHERE p.present_bay_key = :presentBayKey " +
		            "AND p.pallet_qr_id = :palletQrId " +
		            "AND p.exit_appeared = :exitAppeared " +
		            "AND DATE(p.created_at) = CURRENT_DATE " +
		            "ORDER BY p.id DESC LIMIT 1",
		    nativeQuery = true
		)
	Optional<PalletManage> findTopByPresentBayKeyAndPalletQrIdAndExitAppearedToday(
		        @Param("presentBayKey") String presentBayKey,
		        @Param("palletQrId") String palletQrId,
		        @Param("exitAppeared") boolean exitAppeared);
	
	List<PalletManage> findByPresentBayKeyAndPalletActiveAndCreatedAtAfterOrderByCreatedAtAsc(
		    String presentBayKey,
		    Boolean palletActive,
		    Date createdAt,
		    Pageable pageable
		);
	
	
	

	
/*	@Query("SELECT p FROM PalletManage p LEFT JOIN FETCH p.palletMeterList LEFT JOIN FETCH p.palleteBayStateList WHERE p.id = :id")
	PalletManage findWithDetails(@Param("id") Integer id);*/

}
