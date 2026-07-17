package com.tasnetwork.spring.orm.model;

import java.time.LocalDate;
import javax.persistence.*;

/**
 * Represents a daily aggregated summary of test results for a specific bay.
 * This entity will be mapped to the 'daily_test_summaries' table in the database.
 * It focuses on overall pass/fail counts and error code occurrences per day.
 */
@Entity
@Table(name = "daily_test_summaries")
public class DailyTestSummary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Unique identifier for each daily summary record

    @Column(nullable = false)
    private String bayKey; // Key identifying the bay (e.g., "FT_BAY_KEY", "CAL_BAY_KEY")

    @Column(nullable = false)
    private LocalDate summaryDate; // The date for which this summary is recorded (for daily reset)

    @Column(nullable = false)
    private int metersPassedCount; // Total number of meters that passed tests on this day in this bay

    @Column(nullable = false)
    private int metersFailedCount; // Total number of meters that failed tests on this day in this bay

    // Stores a JSON string representation of a Map<String, Integer>
    // where key is errorCode (e.g., "ERR_COMM") and value is its count.
    @Column(columnDefinition = "TEXT") // Use TEXT for potentially larger JSON strings
    private String errorCodeCountsJson;

    // Default constructor
    public DailyTestSummary() {
        this.metersPassedCount = 0;
        this.metersFailedCount = 0;
        this.errorCodeCountsJson = "{}"; // Initialize as empty JSON object
    }

    // Constructor for initial creation
    public DailyTestSummary(String bayKey, LocalDate summaryDate) {
        this.bayKey = bayKey;
        this.summaryDate = summaryDate;
        this.metersPassedCount = 0;
        this.metersFailedCount = 0;
        this.errorCodeCountsJson = "{}";
    }

    // Getters and Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBayKey() {
        return bayKey;
    }

    public void setBayKey(String bayKey) {
        this.bayKey = bayKey;
    }

    public LocalDate getSummaryDate() {
        return summaryDate;
    }

    public void setSummaryDate(LocalDate summaryDate) {
        this.summaryDate = summaryDate;
    }

    public int getMetersPassedCount() {
        return metersPassedCount;
    }

    public void setMetersPassedCount(int metersPassedCount) {
        this.metersPassedCount = metersPassedCount;
    }

    public int getMetersFailedCount() {
        return metersFailedCount;
    }

    public void setMetersFailedCount(int metersFailedCount) {
        this.metersFailedCount = metersFailedCount;
    }

    public String getErrorCodeCountsJson() {
        return errorCodeCountsJson;
    }

    public void setErrorCodeCountsJson(String errorCodeCountsJson) {
        this.errorCodeCountsJson = errorCodeCountsJson;
    }

    @Override
    public String toString() {
        return "DailyTestSummary{" +
               "id=" + id +
               ", bayKey='" + bayKey + '\'' +
               ", summaryDate=" + summaryDate +
               ", metersPassedCount=" + metersPassedCount +
               ", metersFailedCount=" + metersFailedCount +
               ", errorCodeCountsJson='" + errorCodeCountsJson + '\'' +
               '}';
    }
}
