package com.tasnetwork.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

@RestController
@RequestMapping("/api")
public class RestAppController {

	// @Autowired
	// CommentRepository commentRepository;

	@GetMapping(value = "/")
	public String home() {

		System.out.println("RestAppController: home : Entry");
		ApplicationLauncher.logger.info("RestAppController: home path hit");
		return "Hello TAS Network!";
	}

}
