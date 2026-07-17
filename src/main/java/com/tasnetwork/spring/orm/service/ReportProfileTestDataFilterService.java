package com.tasnetwork.spring.orm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.tasnetwork.spring.orm.repository.ReportProfileTestDataFilterRepo;


@Component
public class ReportProfileTestDataFilterService {
	@Autowired
	private ReportProfileTestDataFilterRepo testDataFilterRepository;
	
	
}
