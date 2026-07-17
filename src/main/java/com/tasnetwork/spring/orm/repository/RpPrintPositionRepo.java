package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.OperationProcess;
import com.tasnetwork.spring.orm.model.RpPrintPosition;

@Repository
public interface RpPrintPositionRepo extends JpaRepository<RpPrintPosition, Long>{

}
