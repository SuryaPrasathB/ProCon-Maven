package com.tasnetwork.calibration.energymeter.util;


import java.util.Date;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.util.converter.DateStringConverter;

public class MyDateStringConverter extends DateStringConverter {

	public MyDateStringConverter(final String pattern) {
		super(pattern);
	}

	@Override
	public Date fromString(String value) {
		// catches the RuntimeException thrown by
		// DateStringConverter.fromString()
		try {
			return super.fromString(value);
		} catch (RuntimeException ex) {
			ex.printStackTrace();
			ApplicationLauncher.logger.error("MyDateStringConverter: fromString: RuntimeException:" +ex.getMessage());
			return null;
		}
	}
}
