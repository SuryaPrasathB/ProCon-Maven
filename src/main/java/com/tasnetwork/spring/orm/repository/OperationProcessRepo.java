package com.tasnetwork.spring.orm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.tasnetwork.spring.orm.model.OperationProcess;


@Repository
public interface OperationProcessRepo extends JpaRepository<OperationProcess, Long>{

}
