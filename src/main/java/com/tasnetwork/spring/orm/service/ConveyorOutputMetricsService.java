package com.tasnetwork.spring.orm.service;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics;
import com.tasnetwork.spring.orm.repository.ConveyorOutputMetricsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class ConveyorOutputMetricsService {

    @Autowired
    private ConveyorOutputMetricsRepo conveyorOutputMetricsRepo;

    /**
     * Finds or creates a daily ConveyorOutputMetrics record for a given customer and bay type.
     * The search is based on the start of the current day.
     *
     * @param customerName The name of the customer.
     * @param bayType The type of the bay (e.g., "FT_Bay", "UnloadingBay").
     * @return An Optional containing the ConveyorOutputMetrics for today, if found.
     */
    public Optional<ConveyorOutputMetrics> findByCustomerNameBayTypeAndCurrentDate(String customerName, String bayType) {
        // Get today's date at the start of the day (midnight) in the system's default time zone.
        // This ensures the query looks for records created exactly at the beginning of the day,
        // which is standard for daily aggregation.
        LocalDate today = LocalDate.now();
        Date startOfToday = Date.from(today.atStartOfDay(ZoneId.systemDefault()).toInstant());

        // Call the repository method with the correct property name 'CreatedAt'
        return conveyorOutputMetricsRepo.findByCustomerNameAndBayTypeAndCreatedAt(customerName, bayType, startOfToday);
    }

    /**
     * Saves or updates a ConveyorOutputMetrics record to the database.
     * If it's a new record and `createdAt` is not set, it defaults to the start of the current day.
     *
     * @param metrics The ConveyorOutputMetrics object to save.
     * @return The saved ConveyorOutputMetrics object.
     */
    public ConveyorOutputMetrics saveToDb(ConveyorOutputMetrics metrics) {
        // Ensure createdAt is set if it's a new record, defaulting to start of current day.
/*        if (metrics.getCreatedAt() == null) {
            metrics.setCreatedAt(Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant()));
        }*/
        // updatedAt should ideally be set automatically by JPA (e.g., using @PrePersist/@PreUpdate or database triggers)
        // or explicitly here if not handled by JPA: metrics.setUpdatedAt(new Date());
        return conveyorOutputMetricsRepo.save(metrics);
    }
    
    public List<ConveyorOutputMetrics> findByBayType(String bayType) {
		return conveyorOutputMetricsRepo.findByBayType(bayType);
	}
    
    public Optional<ConveyorOutputMetrics> findByPalletDistinctId(String palletDistinctId) {
		return conveyorOutputMetricsRepo.findByPalletDistinctId(palletDistinctId);
	}
    
    public List<ConveyorOutputMetrics> findAllByBayTypeAndCreatedAtBetween(String bayType, Date start, Date end){
    	return conveyorOutputMetricsRepo.findAllByBayTypeAndCreatedAtBetween(bayType,  start,  end);
    }
    
    public List<ConveyorOutputMetrics> findAllByBayTypeAndDateH(String bayType, Date targetDate){
    	return conveyorOutputMetricsRepo.findAllByBayTypeAndDateH(bayType,  targetDate);
    }

    // You can add other standard CRUD or custom query methods here if needed
}