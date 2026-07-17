package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.DailyTestSummary;

import java.time.LocalDate;
import java.util.Optional;
import java.util.List;

/**
 * Spring Data JPA repository for DailyTestSummary entities.
 * Provides standard CRUD operations and custom query methods for daily aggregates.
 */
@Repository
public interface DailyTestSummaryRepository extends JpaRepository<DailyTestSummary, Long> {

    /**
     * Finds a daily test summary for a specific bay and date.
     * This is crucial for retrieving and updating the daily aggregate.
     * @param bayKey The key identifying the bay.
     * @param summaryDate The date for which the summary is recorded.
     * @return An Optional containing the DailyTestSummary if found, otherwise empty.
     */
    Optional<DailyTestSummary> findByBayKeyAndSummaryDate(String bayKey, LocalDate summaryDate);

    /**
     * Finds all daily test summaries for a specific bay, ordered by date descending.
     * Useful for displaying historical trends for a particular bay.
     * @param bayKey The key identifying the bay.
     * @return A list of DailyTestSummary objects.
     */
    List<DailyTestSummary> findByBayKeyOrderBySummaryDateDesc(String bayKey);

    /**
     * Finds all daily test summaries for a specific date, across all bays.
     * @param summaryDate The date for which the summaries are recorded.
     * @return A list of DailyTestSummary objects.
     */
    List<DailyTestSummary> findBySummaryDate(LocalDate summaryDate);
}
