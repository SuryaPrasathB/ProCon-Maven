package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.repository.OperationProcessRepo;



@Component
public class OperationProcessService {
	@Autowired
	private OperationProcessRepo operationProcessRepo;
}
