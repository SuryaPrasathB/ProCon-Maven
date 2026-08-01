package com.tasnetwork.spring.orm.service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;
import com.tasnetwork.spring.orm.repository.ConveyorOutputMetricsSummaryRepo;

@Service
public class ConveyorOutputMetricsSummaryService {

    @Autowired
    private ConveyorOutputMetricsSummaryRepo conveyorOutputMetricsSummaryRepo;

    /**
     * Finds the summary record for a specific customer and bay for the current date.
     *
     * @param customerName Customer name.
     * @param bayType      Bay type.
     * @return Optional of ConveyorOutputMetricsSummary.
     */
    public Optional<ConveyorOutputMetricsSummary> findByCustomerNameBayTypeAndCurrentDate(String customerName, String bayType) {
        LocalDate today = LocalDate.now();
        ZoneId zone = ZoneId.systemDefault();

        Date startOfDay = Date.from(today.atStartOfDay(zone).toInstant());
        Date endOfDay = Date.from(today.plusDays(1).atStartOfDay(zone).toInstant());

        return conveyorOutputMetricsSummaryRepo.findByCustomerNameAndBayTypeAndCreatedAtBetween(
            customerName, bayType, startOfDay, endOfDay
        );
    }


    /**
     * Saves or updates a ConveyorOutputMetricsSummary record.
     *
     * @param metrics Summary to save.
     * @return The saved summary.
     */
    public ConveyorOutputMetricsSummary saveToDb(ConveyorOutputMetricsSummary metrics) {
        return conveyorOutputMetricsSummaryRepo.save(metrics);
    }

    /**
     * Retrieves all summaries by bay type.
     *
     * @param bayType Bay type.
     * @return List of summaries.
     */
    public List<ConveyorOutputMetricsSummary> findByBayType(String bayType) {
        return conveyorOutputMetricsSummaryRepo.findByBayType(bayType);
    }
    
    public Optional<ConveyorOutputMetricsSummary> findByBayTypeAndCreatedAtBetween(String bayType, Date start, Date end){
    	return conveyorOutputMetricsSummaryRepo.findByBayTypeAndCreatedAtBetween(bayType,  start,  end);
    }
    
    public Optional<ConveyorOutputMetricsSummary> findByBayTypeAndDateH(String bayType, Date targetDate){
    	return conveyorOutputMetricsSummaryRepo.findByBayTypeAndDateH(bayType,  targetDate);
    }
}
