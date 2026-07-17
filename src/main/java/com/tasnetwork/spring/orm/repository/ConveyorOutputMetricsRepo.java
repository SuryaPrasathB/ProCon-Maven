package com.tasnetwork.spring.orm.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.ConveyorOutputMetrics;

@Repository
public interface ConveyorOutputMetricsRepo extends JpaRepository<ConveyorOutputMetrics, Integer> {
    /**
     * Finds a ConveyorOutputMetrics record by customer name, bay type, and the creation date.
     * The date comparison is typically for the start of a day if you're tracking daily metrics.
     * Spring Data JPA will map 'CreatedAt' to the 'createdAt' field in the entity.
     *
     * @param customerName The name of the customer.
     * @param bayType The type of the bay.
     * @param createdAt The date (typically start of day) for which to find the metrics.
     * @return An Optional containing the ConveyorOutputMetrics if found, or empty if not.
     */
    Optional<ConveyorOutputMetrics> findByCustomerNameAndBayTypeAndCreatedAt(String customerName, String bayType, Date createdAt);

	List<ConveyorOutputMetrics> findByBayType(String bayType);
	
	Optional<ConveyorOutputMetrics> findByPalletDistinctId(String palletDistinctId);
	
	List<ConveyorOutputMetrics> findAllByBayTypeAndCreatedAtBetween(String bayType, Date start, Date end);

	List<ConveyorOutputMetrics> findAllByBayTypeAndDateH(String bayType, Date targetDate);


}