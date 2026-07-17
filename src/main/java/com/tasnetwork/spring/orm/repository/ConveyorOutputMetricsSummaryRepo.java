package com.tasnetwork.spring.orm.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetricsSummary;

@Repository
public interface ConveyorOutputMetricsSummaryRepo extends JpaRepository<ConveyorOutputMetricsSummary, Integer> {

    /**
     * Finds a record by customer name, bay type, and creation date (typically start of day).
     *
     * @param customerName Customer name.
     * @param bayType      Bay type (e.g., "FT_Bay").
     * @param createdAt    Start of day timestamp.
     * @return Optional of ConveyorOutputMetricsSummary if found.
     */
    //Optional<ConveyorOutputMetricsSummary> findByCustomerNameAndBayTypeAndCreatedAt(String customerName, String bayType, Date createdAt);
    
    Optional<ConveyorOutputMetricsSummary> findByCustomerNameAndBayTypeAndCreatedAtBetween(
    	    String customerName,
    	    String bayType,
    	    Date start,
    	    Date end
    	);


    /**
     * Finds all records for a given bay type.
     *
     * @param bayType The bay type to filter by.
     * @return List of ConveyorOutputMetricsSummary.
     */
    List<ConveyorOutputMetricsSummary> findByBayType(String bayType);
    
    Optional<ConveyorOutputMetricsSummary> findByBayTypeAndCreatedAtBetween(String bayType, Date start, Date end);

    Optional<ConveyorOutputMetricsSummary> findByBayTypeAndDateH(String bayType, Date targetDate);


}
