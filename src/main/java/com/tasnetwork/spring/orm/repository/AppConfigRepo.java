package com.tasnetwork.spring.orm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.AppConfig;

@Repository
public interface AppConfigRepo extends JpaRepository<AppConfig, Long>{

	Optional<AppConfig> findFirstByCustomerIdAndPropertyName(String customerId, String propertyName);
}
