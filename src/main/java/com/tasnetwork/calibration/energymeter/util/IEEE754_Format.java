package com.tasnetwork.calibration.energymeter.util;

import java.io.IOException;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

public class IEEE754_Format {
	
	public static String hex(int n) {
	    // call toUpperCase() if that's required
	    return String.format("%8s", Integer.toHexString(n)).replace(' ', '0').toUpperCase();
	    //return String.format("%08X", Integer.toHexString(n)).replace(' ', '0');
	}

	public static String EncodeFloatToHexString(float f) {
	    // change the float to raw integer bits(according to the OP's requirement)
			    return hex(Float.floatToRawIntBits(f));
	}
	
	public static String convertFloatToIEEE_754_FourBytes(float value){// used for Arduino to transfer float data in CPRI- 
		//ProRes projects (Residual current panel)
		String IEEE_754 = Integer.toHexString(Float.floatToIntBits(value)).toUpperCase(); 
		return IEEE_754;
	}
	
	
	
	public static String hexToFloat(String hexString) {
		//ApplicationLauncher.logger.debug("hexToFloat : input Hex String : "+ hexString);
		Long i = 0L;
		Float f = 0.0f;
		String Data ="";
		try{
			i = Long.parseLong(hexString, 16);
	        f = Float.intBitsToFloat(i.intValue());
	       // ApplicationLauncher.logger.debug("hexToFloat : converted float value : "+ f);
	       
			try{
				Data =String.format("%.4f",f);
				if(Data.equals("-0.0000")){
					Data = "0.0000";
				}
			} catch (Exception e){

				e.printStackTrace();
				ApplicationLauncher.logger.error("hextofloat: Exception2:" +e.getMessage());
				
			}
		} catch (Exception e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("hexToFloat: Exception: "+e.getMessage());


		}
		
		return Data;
	}

}
