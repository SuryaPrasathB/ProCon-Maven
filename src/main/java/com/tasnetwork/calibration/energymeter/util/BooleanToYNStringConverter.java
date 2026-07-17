package com.tasnetwork.calibration.energymeter.util;

import javax.persistence.AttributeConverter;

public class BooleanToYNStringConverter implements AttributeConverter<Boolean, String> {

	@Override
    public String convertToDatabaseColumn(Boolean b) {
        if (b == null) {
            return null;
        }
        if (b.booleanValue()) {
            return "Y";
        }
        return "N";
    }

	@Override
    public Boolean convertToEntityAttribute(String s) {
        if (s == null) {
            return null;
        }
        /*if (s.equals("0")){
        	 return Boolean.FALSE;
        }else    */
        if (s.equals("1") || s.equalsIgnoreCase("Yes") || s.equalsIgnoreCase("Y") ) {
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

}

