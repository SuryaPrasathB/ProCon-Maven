package com.tasnetwork.calibration.energymeter.util;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

import javafx.util.converter.FloatStringConverter;

public class MyFloatStringConverter extends FloatStringConverter{
	@Override
	public Float fromString(final String value) {
		try{

		return value.isEmpty() || !isFloat(value) ? null
				: super.fromString(value);
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("MyFloatStringConverter: Exception:" +e.getMessage());
			return null;
		}
	}
	
	public boolean isFloat(String value) {
		int size = value.length();
		try{
			Float.parseFloat(value);
	
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.info("MyFloatStringConverter: isFloat Exception input String:" +value);
			ApplicationLauncher.logger.error("MyFloatStringConverter: isFloat Exception message:" +e.getMessage());
			return false;
		}
		return size > 0;
	}

	public boolean isNumber(String value) {
		ApplicationLauncher.logger.info("MyFloatStringConverter: isNumber: Entry:" );
		int size = value.length();
		try{
		for (int i = 0; i < size; i++) {
			if (!Character.isDigit(value.charAt(i))) {
				
				if((!(value.charAt(i) == '.')) && (!(value.charAt(i) == '+'))  ){
					ApplicationLauncher.logger.info("MyFloatStringConverter: isNumber 3: value.charAt(i):" + value.charAt(i));
				
					return false;
				}
			}
		}		
		}catch (Exception e){
			e.printStackTrace();
			ApplicationLauncher.logger.error("MyFloatStringConverter:isNumber: Exception:" +e.getMessage());
			return false;
		}
		return size > 0;
	}
}
