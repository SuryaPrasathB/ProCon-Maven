package com.tasnetwork.calibration.conveyor.util;
import com.sun.javafx.scene.control.skin.TableViewSkin;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ConstantProTamp;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.calibration.energymeter.util.TMS_FloatConversion;
import com.tasnetwork.calibration.energymeter.util.TextAreaInputDialog;

import javafx.collections.ListChangeListener;
import javafx.scene.control.Skin;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

import org.apache.commons.lang3.StringUtils;


public class GUIUtils {

	private static String OS = null;
	
	private final static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	
/*    public static  ArrayList<String> extractDeviceIdAddressList(String deviceId,String parseDataAfter) {
        int omIndex = deviceId.indexOf(parseDataAfter);
        ArrayList<String> addressList = new  ArrayList<String>();
        if (omIndex == -1) {
        	ApplicationLauncher.logger.debug("extractDeviceIdAddressList : Invalid deviceId: " + deviceId);
            return addressList;
        }

        // Get data after "OM"
        String afterOM = deviceId.substring(omIndex + 2);

        // Split into chunks of 2
       // System.out.print("Device ID: " + deviceId + " -> Parts: ");
        for (int i = 0; i < afterOM.length(); i += 2) {
            if (i + 2 <= afterOM.length()) {
            	ApplicationLauncher.logger.debug(afterOM.substring(i, i + 2));
            	addressList.add(afterOM.substring(i, i + 2));
 
            }
        }
        ApplicationLauncher.logger.debug("extractDeviceIdAddressList : addressList: " + addressList);
        return addressList;
    }*/
    
	
	public static String asciiToHex(String asciiString) {
		//ApplicationLauncher.logger.debug("asciiToHex: Entry: " );
        StringBuilder hexString = new StringBuilder();
        String hex = "";
        for (int i = 0; i < asciiString.length(); i++) {
            char c = asciiString.charAt(i);
            //String hex = Integer.toHexString((int) c);
            hex = String.format("%02X", ((int)c));
            //ApplicationLauncher.logger.debug("asciiToHex: hex: " + hex);
            hexString.append(hex);
        }
        return hexString.toString();
    }
	
	public static String IntStringToHexStringOneByte(String arg) {
		//int inpValue  = Integer.parseInt(arg);
		//Integer.parseInt(arg,16);
        //hex_value = Integer.toHexString(checksum);
        //hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 4, '0');
		int inpValue=Integer.parseInt(arg);
		String HexString=String.format("%02X", inpValue);;
		return HexString.toUpperCase();
	}
	public static String bytesToAscii(byte[] bytes) {
    	String convertedHex = bytesToHex(bytes);        
    	String convertedAscii = hexToAsciiV2(convertedHex);
        return convertedAscii;
    }
	
	 public static String bytesToHex(byte[] bytes) {
	    	//char[] hexArray = "0123456789ABCDEF".toCharArray();
	        char[] hexChars = new char[bytes.length * 2];
	        for ( int j = 0; j < bytes.length; j++ ) {
	            int v = bytes[j] & 0xFF;
	            hexChars[j * 2] = hexArray[v >>> 4];
	            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	        }
	         
	        
	        return new String(hexChars);
	    }
	    
	
	public static String hexToAsciiV2(String inputHexStr) {
		// when using hexToAscii - beyond data 0x7F all converted as 0x3F   which is equivalent to "?"\
		// this is due to limitation of Ascii...hence need to use the Extended ascii...which is UTF-8 format
		String convertedUtf8Value = "";
		//String hex = "6174656ec3a7c3a36f";                                  // AAA
		ByteBuffer buff = ByteBuffer.allocate(inputHexStr.length()/2);
		for (int i = 0; i < inputHexStr.length(); i+=2) {
		    buff.put((byte)Integer.parseInt(inputHexStr.substring(i, i+2), 16));
		}
		buff.rewind();
		//Charset cs = Charset.forName("UTF-8"); //("UTF-8");                              // BBB
		Charset cs = Charset.forName("ISO-8859-1");
		//Charset cs = Charset.forName("UTF-16");
		CharBuffer cb = cs.decode(buff);                                    // BBB
		convertedUtf8Value = cb.toString();                                  // CCC
		//ApplicationLauncher.logger.debug("hexToString: convertedUtf8Value: "+ convertedUtf8Value);
		return convertedUtf8Value;
	}
	public static String hexToAscii(String hexStr) {
		StringBuilder output = new StringBuilder("");

		for (int i = 0; i < hexStr.length(); i += 2) {
			String str = hexStr.substring(i, i + 2);
			output.append((char) Integer.parseInt(str, 16));
		}

		return output.toString();
	}
	
	public static String FormatTimeForAvgPulses(int time_in_sec){
		int min = time_in_sec/60;
		int sec = time_in_sec%60;
		String minute = String.format("%02d", min);
		String second = String.format("%02d", sec);
		String time = minute + second;
		return time;
	}

	public static String FormatPulseRate(String InputData){
		/*int InputLength = InputData.length();
		int ExponentialValue = 0;
		int BeginningValue =0;*/
		long InputLength = InputData.length();
		long ExponentialValue = 0;
		long BeginningValue =0;
		String OutputData = null;

		ApplicationLauncher.logger.info("FormatPulseRate : InputData: "+ InputData);
		if(isNumber(InputData)){
			if(InputLength == 4){
				OutputData = InputData +"E00";
			} else if (InputLength > 4){
				try{
					ExponentialValue = InputLength -4;
					ApplicationLauncher.logger.debug("FormatPulseRate : ExponentialValue: "+ ExponentialValue);
					//BeginningValue = Integer.parseInt(InputData)/((int) Math.pow(10, ExponentialValue));
					BeginningValue = Long.parseLong(InputData)/((long) Math.pow(10, ExponentialValue));
					ApplicationLauncher.logger.debug("FormatPulseRate : BeginningValue: "+ BeginningValue);
					OutputData = BeginningValue +"E"+String.format("%02d", ExponentialValue);
					ApplicationLauncher.logger.debug("FormatPulseRate : OutputData: "+ OutputData);
				}catch (Exception e){
					ApplicationLauncher.logger.error("FormatPulseRate: Exception:" +e.getMessage());

				} 
			}
			else if(InputLength < 4){
				//int ValueTobeFormatted = Integer.parseInt(InputData);
				long ValueTobeFormatted = Long.parseLong(InputData);
				InputData = String.format("%04d", ValueTobeFormatted);
				OutputData = InputData +"E00";
			}
		}else{
			ApplicationLauncher.logger.info("FormatPulseRate : Not a valid data for Impulses/Unit: Current Data: <"+ InputData + ">:Expected data format: <3200>" );
		}
		ApplicationLauncher.logger.info("FormatPulseRate : Impulses/Unit: OutputData: "+ OutputData);
		return OutputData;
	}
	
	public static String StringToHex(String arg) {
		//ApplicationLauncher.logger.debug("StringToHex : arg: " + arg);
		return String.format("%02x", new BigInteger(1, arg.getBytes(/*YOUR_CHARSET?*/))).toUpperCase();
	}
	
	public static String apFormatPulseRate(String InputData){//applied Precision format pulse
		/*int InputLength = InputData.length();
		int ExponentialValue = 0;
		int BeginningValue =0;*/
		long InputLength = InputData.length();
		long ExponentialValue = 0;
		float BeginningValue =0;
		String OutputData = null;

		ApplicationLauncher.logger.info("apFormatPulseRate : InputData: "+ InputData);
		if(isNumber(InputData)){
			if(InputLength == 4){
				OutputData = InputData +"E00";
			} else if (InputLength > 4){
				try{
					ExponentialValue = InputLength -1;
					ApplicationLauncher.logger.debug("apFormatPulseRate : ExponentialValue: "+ ExponentialValue);
					//BeginningValue = Integer.parseInt(InputData)/((int) Math.pow(10, ExponentialValue));
					BeginningValue = Float.parseFloat(InputData)/((float) Math.pow(10, ExponentialValue));
					ApplicationLauncher.logger.debug("apFormatPulseRate : BeginningValue: "+ BeginningValue);
					OutputData = BeginningValue +"E+"+String.format("%02d", ExponentialValue);
					ApplicationLauncher.logger.debug("apFormatPulseRate : OutputData: "+ OutputData);
				}catch (Exception e){
					ApplicationLauncher.logger.error("apFormatPulseRate: Exception:" +e.getMessage());

				} 
			}
			else if(InputLength < 4){
				//int ValueTobeFormatted = Integer.parseInt(InputData);
				long ValueTobeFormatted = Long.parseLong(InputData);
				InputData = String.format("%04d", ValueTobeFormatted);
				OutputData = InputData +"E00";
			}
		}else{
			ApplicationLauncher.logger.info("apFormatPulseRate : Not a valid data for Impulses/Unit: Current Data: <"+ InputData + ">:Expected data format: <3200>" );
		}
		ApplicationLauncher.logger.info("apFormatPulseRate : Impulses/Unit: OutputData: "+ OutputData);
		return OutputData;
	}

	public static boolean isNumber(String value) {
		//ApplicationLauncher.logger.info("GUIUtils: isNumber: Entry:" );
		int size = 0;
		try{
			
			size = value.length();
			for (int i = 0; i < size; i++) {
				if (!Character.isDigit(value.charAt(i))) {

					return false;

				}
			}		
		}catch (Exception e){
			ApplicationLauncher.logger.error("GUIUtils: isNumber: Exception:" +e.getMessage());
			return false;
		}
		return size > 0;
	}



	public static void autoResizeColumns( TableView<?> table,Boolean IsLiveTable )
	{
		//Set the right policy
		table.setColumnResizePolicy( TableView.UNCONSTRAINED_RESIZE_POLICY);
		table.getColumns().stream().forEach( (column) ->
		{

			javafx.scene.text.Text t = new javafx.scene.text.Text( column.getText() );
			double max = t.getLayoutBounds().getWidth();
			for ( int i = 0; i < table.getItems().size(); i++ )
			{
				//cell must not be empty
				if ( column.getCellData( i ) != null )
				{
					t = new javafx.scene.text.Text( column.getCellData( i ).toString() );
					double calcwidth = t.getLayoutBounds().getWidth();
					//remember new max-width
					if ( calcwidth > max )
					{
						max = calcwidth;
					}
				}
			}
			//set the new max-widht with some extra space
			if (IsLiveTable){
				column.setPrefWidth( max + 30.0d );
			}else{
				column.setPrefWidth( max + 20.0d );
			}
		} );
	}




	public static String FormatAvgPulses(String Pulses){
		ApplicationLauncher.logger.debug("FormatAvgPulses:input Pulses: "+Pulses);
		int no_of_pulses  = Integer.parseInt(Pulses);
		String pulses_in_hex = String.format("%04x", no_of_pulses).toUpperCase();

		ApplicationLauncher.logger.info("FormatAvgPulses : "+pulses_in_hex.toUpperCase());
		ApplicationLauncher.logger.debug("FormatAvgPulses:pulses_in_hex "+pulses_in_hex);
		return pulses_in_hex;
	}

	public static String FormatErrorInput(String InputErrorValue){
		Boolean IsNegative = false;
		Boolean IsPositive = false;
		Boolean PopulatePlusSign = false;
		Boolean IsDotExist = false;
		Boolean IsDotExistInCorrectPosition = false;
		String OutputErrorValue = null;
		String regex = "\\d\\.?\\d*";
		//String regex = "^\\d*\\.\\d+|\\d+\\.\\d*$";
		//int InputLength = InputErrorValue.length();
		//ApplicationLauncher.logger.info("Test1:");

		boolean status = Validate_Error(InputErrorValue);
		if(!status){
			return null;
		}
		if(InputErrorValue.contains("-")){
			IsNegative = true;
			InputErrorValue = InputErrorValue.replaceFirst("-", "");
		}
		//ApplicationLauncher.logger.info("Test2:");
		if(InputErrorValue.contains("+")){
			//ApplicationLauncher.logger.info("Test2-1:");
			IsPositive = true;
			//ApplicationLauncher.logger.info("Test2-2:");
			InputErrorValue =InputErrorValue.replaceFirst("\\+", "");
			//ApplicationLauncher.logger.info("Test2-3:");
		}

		int InputLength = InputErrorValue.length();
		//ApplicationLauncher.logger.info("test24:"+InputErrorValue);
		//ApplicationLauncher.logger.info("Test3:");
		if(InputErrorValue.contains(".")){
			IsDotExist = true;
			if ((InputErrorValue.substring(1,2)).equals(".")){
				//ApplicationLauncher.logger.debug("FormatErrorInput :Dot exist in correct Position");
				IsDotExistInCorrectPosition = true;
			}
			if ((InputErrorValue.substring(0,1)).equals(".")){
				//ApplicationLauncher.logger.debug("FormatErrorInput :Dot exist in correct Position2");
				InputErrorValue = "0"+InputErrorValue;
				InputLength = InputErrorValue.length();
				IsDotExistInCorrectPosition = true;
			}
		}

		//ApplicationLauncher.logger.info("Test4:");
		if(!IsNegative && !IsPositive ){
			PopulatePlusSign = true;
		}


		//ApplicationLauncher.logger.info("Test5:"+OutputErrorValue);
		if (!IsDotExist){

			if (InputLength<2){
				if(InputErrorValue.matches(regex)){
					OutputErrorValue = InputErrorValue+".";
					OutputErrorValue  = OutputErrorValue+"00";
				}
			}
		}else{
			if(IsDotExistInCorrectPosition){
				//ApplicationLauncher.logger.info("Test51:"+OutputErrorValue);
				if (InputLength == 4){
					OutputErrorValue = InputErrorValue;
					//ApplicationLauncher.logger.info("Test52:"+OutputErrorValue);
				} else if (InputLength<4){
					//ApplicationLauncher.logger.info("Test53:"+OutputErrorValue);
					//ApplicationLauncher.logger.info("Test54:"+InputErrorValue);
					if(InputErrorValue.matches(regex)){
						OutputErrorValue  = InputErrorValue+"0";
						//ApplicationLauncher.logger.info("Test55:"+OutputErrorValue);
					}
				}

				if (InputLength > 4){
					OutputErrorValue = InputErrorValue.substring(0,4);
					//ApplicationLauncher.logger.info("Test56:"+OutputErrorValue);
				}


			}
		}

		//ApplicationLauncher.logger.info("Test6:"+OutputErrorValue);

		if (PopulatePlusSign || IsPositive){
			if (!(OutputErrorValue== null)){
				OutputErrorValue = "+"+OutputErrorValue;
			}
		} else{
			//OutputErrorValue = InputErrorValue;
		}

		//ApplicationLauncher.logger.info("Test7:"+OutputErrorValue);
		if (IsNegative){
			if (!(OutputErrorValue==null)){
				OutputErrorValue = "-"+OutputErrorValue;
			}
		}
		//ApplicationLauncher.logger.info("Test8:"+OutputErrorValue);
		return OutputErrorValue;

	}

/*	public static boolean Validate_voltage(String voltage){
		boolean valid_status = false;
		if(!voltage.isEmpty()){
			try{
				float volt = Float.parseFloat(voltage);
				String EM_CT_Type= ProjectController.getProjectEM_CT_Type();
				if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_LTCT)){
					if((volt >= ConstantConfig.LTCT_VOLT_MIN) && 
							(volt <= ConstantConfig.LTCT_VOLT_MAX)){
						valid_status = true;
					}else {
						ApplicationLauncher.logger.info("Validate_voltage: LTCT voltage is not with in acceptable limit. Kindly check the config file : input voltage:" +voltage + ": ConfigProperty.VOLT_MIN:" + ConstantConfig.LTCT_VOLT_MIN + " : ConfigProperty.VOLT_MAX:"+ConstantConfig.LTCT_VOLT_MAX);
	
					}
				}else if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_HTCT)){
					if((volt >= ConstantConfig.HTCT_VOLT_MIN) && 
							(volt <= ConstantConfig.HTCT_VOLT_MAX)){
						valid_status = true;
					}else {
						ApplicationLauncher.logger.info("Validate_voltage: LTCT voltage is not with in acceptable limit. Kindly check the config file : input voltage:" +voltage + ": ConfigProperty.VOLT_MIN:" + ConstantConfig.LTCT_VOLT_MIN + " : ConfigProperty.VOLT_MAX:"+ConstantConfig.LTCT_VOLT_MAX);
	
					}
				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_voltage: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_voltage: voltage is not a valid float value : input voltage:" +voltage);

			}
		}else{
			ApplicationLauncher.logger.info("Validate_voltage: voltage is empty");


		}
		return valid_status;

	}*/
	
	public static boolean Validate_voltage(String voltage){
		boolean valid_status = false;
		if(!voltage.isEmpty()){
			try{
				float volt = Float.parseFloat(voltage);
				if((volt >= ConstantConveyorConfig.VOLT_MIN) && 
						(volt <= ConstantConveyorConfig.VOLT_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_voltage: voltage is not with in acceptable limit. Kindly check the config file : input voltage:" +voltage + ": ConfigProperty.VOLT_MIN:" + ConstantConveyorConfig.VOLT_MIN + " : ConfigProperty.VOLT_MAX:"+ConstantConveyorConfig.VOLT_MAX);

				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_voltage: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_voltage: voltage is not a valid float value : input voltage:" +voltage);

			}
		}else{
			ApplicationLauncher.logger.info("Validate_voltage: voltage is empty");


		}
		return valid_status;

	}

/*	public static boolean Validate_current(String current){
		boolean valid_status = false;
		if(!current.isEmpty()){
			try{
				float i_current = Float.parseFloat(current);
				String EM_CT_Type= ProjectController.getProjectEM_CT_Type();
				if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_LTCT)){
					if((i_current >= ConstantConfig.LTCT_CURRENT_MIN) && 
							(i_current <= ConstantConfig.LTCT_CURRENT_MAX)){
						valid_status = true;
					}else {
						ApplicationLauncher.logger.info("Validate_current: LTCT current is not with in acceptable limit. Kindly check the config file : input current:" +current + ": ConfigProperty.CURRENT_MIN:" + ConstantConfig.LTCT_CURRENT_MIN + " : ConfigProperty.CURRENT_MAX:"+ConstantConfig.LTCT_CURRENT_MAX);
	
					}
				}else if(EM_CT_Type.equals(ConstantApp.METER_CT_TYPE_HTCT)){
					if((i_current >= ConstantConfig.HTCT_CURRENT_MIN) && 
							(i_current <= ConstantConfig.HTCT_CURRENT_MAX)){
						valid_status = true;
					}else {
						ApplicationLauncher.logger.info("Validate_current: HTCT current is not with in acceptable limit. Kindly check the config file : input current:" +current + ": ConfigProperty.CURRENT_MIN:" + ConstantConfig.LTCT_CURRENT_MIN + " : ConfigProperty.CURRENT_MAX:"+ConstantConfig.LTCT_CURRENT_MAX);
	
					}
				
				}
			
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_current: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_current: current is not a valid float value : input current:" +current);

			}
		}else {
			ApplicationLauncher.logger.info("Validate_current: current is empty");

		}
		return valid_status;

	}*/
	
	public static boolean Validate_current(String current){
		boolean valid_status = false;
		if(!current.isEmpty()){
			try{
				float i_current = Float.parseFloat(current);
				if((i_current >= ConstantConveyorConfig.CURRENT_MIN) && 
						(i_current <= ConstantConveyorConfig.CURRENT_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_current: current is not with in acceptable limit. Kindly check the config file : input current:" +current + ": ConfigProperty.CURRENT_MIN:" + ConstantConveyorConfig.CURRENT_MIN + " : ConfigProperty.CURRENT_MAX:"+ConstantConveyorConfig.CURRENT_MAX);

				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_current: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_current: current is not a valid float value : input current:" +current);

			}
		}else {
			ApplicationLauncher.logger.info("Validate_current: current is empty");

		}
		return valid_status;

	}

	public static boolean Validate_phasedegree(String degree){
		boolean valid_status = false;
		if(!degree.isEmpty()){
			try{
				int phasedegree = Integer.parseInt(degree);
/*				if((phasedegree >= ConstantConfig.DEGREE_MIN) && 
						(phasedegree <= ConstantConfig.DEGREE_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_phasedegree: phasedegree is not with in acceptable limit. Kindly check the config file : input phasedegree:" +degree + ": ConfigProperty.DEGREE_MIN:" + ConstantConfig.DEGREE_MIN + " : ConfigProperty.DEGREE_MAX:"+ConstantConfig.DEGREE_MAX);


				}*/
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_phasedegree: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_phasedegree: phasedegree is not a valid float value : input phasedegree:" +degree);


			}
		}else {
			ApplicationLauncher.logger.info("Validate_phasedegree: phasedegree is empty");

		}
		return valid_status;

	}
	public static boolean Validate_PhaseLagLead(String degree){
		ApplicationLauncher.logger.info("Validate_PhaseLagLead: PF:"+degree);
		boolean valid_status = false;
		if(!degree.isEmpty()){
			try{
				String lag_lead_value ="";
				if( degree.equals( "1")  ){
					lag_lead_value= degree;
				}else{

					lag_lead_value = degree.substring(0, degree.length() - 1);
				}
				float phasedegree = Float.parseFloat(lag_lead_value);
				if((phasedegree >= ConstantApp.PF_MIN) && 
						(phasedegree <= ConstantApp.PF_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_PhaseLagLead: pf is not with in acceptable limit.  input pf:" +degree + ": MyProperty.PF_MIN:" + ConstantApp.PF_MIN + " : MyProperty.PF_MAX:"+ConstantApp.PF_MAX);

				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_PhaseLagLead: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_PhaseLagLead: pf is not a valid float value : input pf:" +degree);

			}
		}else {
			ApplicationLauncher.logger.info("Validate_PhaseLagLead: pf is empty");

		}
		return valid_status;

	}


	public static boolean Validate_frequency(String freq){
		boolean valid_status = false;
		if(!freq.isEmpty()){
			try{
				float frequency = Float.parseFloat(freq);
				valid_status = true;
/*				if((frequency >= ConstantConfig.FREQUENCY_MIN) && 
						(frequency <= ConstantConfig.FREQUENCY_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_frequency: freq is not with in acceptable limit. Kindly check the config file : input freq:" +freq + ": ConfigProperty.FREQUENCY_MIN:" + ConstantConfig.FREQUENCY_MIN + " : ConfigProperty.FREQUENCY_MAX:"+ConstantConfig.FREQUENCY_MAX);

				}*/
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_frequency: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_frequency: pf is not a valid float value : input freq:" +freq);

			}
		}else {
			ApplicationLauncher.logger.info("Validate_frequency: freq is empty");

		}
		return valid_status;

	}

	public static boolean Validate_Error(String error){
		boolean valid_status = false;
		if(!error.isEmpty()){
			try{
				float error_value = Float.parseFloat(error);
				valid_status = true;
/*				if((error_value >= ConstantConfig.ERROR_MIN) && 
						(error_value <= ConstantConfig.ERROR_MAX)){
					valid_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_Error: freq is not with in acceptable limit. Kindly check the config file : input error:" +error + ": ConfigProperty.ERROR_MIN:" + ConstantConfig.ERROR_MIN + " : ConfigProperty.ERROR_MAX:"+ConstantConfig.ERROR_MAX);

				}*/
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_Error: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_Error: pf is not a valid float value : input error:" +error);

			}
		}else {
			ApplicationLauncher.logger.info("Validate_Error: error is empty");

		}
		return valid_status;

	}

	public static boolean is_number(String value){
		boolean valid_status = false;
		if(!value.isEmpty()){
			try{
				float parsed_value = Integer.parseInt(value);
				if(parsed_value >= 0){// By Prasanth
					valid_status = true; 
				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_number: Exception:" +e.getMessage());
			}
		}
		return valid_status;

	}

	public static boolean is_float(String value){
		boolean valid_status = false;
		if(!value.isEmpty()){
			try{
				float parsed_value = Float.parseFloat(value);
				if(parsed_value >= 0){// By Prasanth
					valid_status = true;
				}
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_float: Exception:" +e.getMessage());
			}
		}
		return valid_status;

	}

	public static boolean is_long(String value){
		boolean valid_status = false;
		if(!value.isEmpty()){
			try{
				long parsed_value = Long.parseLong(value);
				valid_status = true;
			}
			catch(Exception e){
				valid_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("is_long: Exception:" +e.getMessage());
			}
		}
		return valid_status;

	}

	public static String FormatUnForDisplay(String Un){
		//ApplicationLauncher.logger.info("FormatUnForDisplay: Un: " + Un);

		String un_display_name = Un +"U";

		return un_display_name;
	}
	public static boolean Validate_time_duration(String time_duration){
		boolean valid_time_duration_status = false;
		if(!time_duration.isEmpty()){
			try{
				int time=Integer.parseInt(time_duration);
/*				if((time >= ConstantConfig.TIME_MIN) && 
						(time <= ConstantConfig.TIME_MAX)){
					valid_time_duration_status = true;
				}else {
					ApplicationLauncher.logger.info("Validate_time_duration: time_duration is not with in acceptable limit. Kindly check the config file : input time:" +time_duration + ": ConfigProperty.TIME_MIN:" + ConstantConfig.TIME_MIN + " : ConfigProperty.TIME_MAX:"+ConstantConfig.TIME_MAX);

				}*/
			}
			catch(Exception e){
				valid_time_duration_status = false;
				e.printStackTrace();
				ApplicationLauncher.logger.error("Validate_time_duration: Exception:" +e.getMessage());
				ApplicationLauncher.logger.info("Validate_time_duration: time_duration is not a valid int value : input time:" +time_duration);


			}
		}else{
			ApplicationLauncher.logger.info("Validate_time_duration: time_duration is empty");


		}
		return valid_time_duration_status;

	}
	
	public static String generateCommandforRefStdBNC_Constant(String bncConstant, String bncMetricIndexCommand){
		double InputData = Float.valueOf(bncConstant);
		//double RSS_InputSetValue = (1/(InputData))*1000;
		double RSS_InputSetValue = (1/(InputData));
		//double RSS_InputSetValue = (1/(InputData));
		ApplicationLauncher.logger.info("InputData:"+InputData);
		//ApplicationLauncher.logger.info("RSS_InputSetValue:"+RSS_InputSetValue);
		//String OutputData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		//ApplicationLauncher.logger.info("OutputData:"+OutputData);
		String RSS_InputSetValueString = String.format ("%.09f", RSS_InputSetValue);
		//String RSS_InputSetValueString = String.format ("%.011f", RSS_InputSetValue);
		ApplicationLauncher.logger.info("RSS_InputSetValueString:"+RSS_InputSetValueString);
		InputData = Float.valueOf(RSS_InputSetValueString);
		String FloatHexData = TMS_FloatConversion.FloatToTMS320_Hex(InputData);
		ApplicationLauncher.logger.info("FloatHexData:"+FloatHexData);
		String RSS_SetCommandWithOutCheckSum = bncMetricIndexCommand+FloatHexData;
		String checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		ApplicationLauncher.logger.info("checkSum:"+checkSum);
		String OutputData = RSS_SetCommandWithOutCheckSum+checkSum;
		ApplicationLauncher.logger.info("OutputData:"+OutputData);
		
		/*RSS_SetCommandWithOutCheckSum = "A632000700000100000000";
		checkSum = generateCheckSum(RSS_SetCommandWithOutCheckSum);
		ApplicationLauncher.logger.info("checkSum:"+checkSum);
		OutputData = RSS_SetCommandWithOutCheckSum+checkSum;
		ApplicationLauncher.logger.info("OutputData:"+OutputData);*/
		return OutputData;
	}
	
	public static String generateCheckSum(String InputHexString){
		
        String hex_value = new String();
        // 'hex_value' will be used to store various hex values as a string
        int x, i, checksum = 0;
        // 'x' will be used for general purpose storage of integer values
        // 'i' is used for loops
        // 'checksum' will store the final checksum
        for (i = 0; i < InputHexString.length() - 2; i = i + 2)
        {
            //x = (int) (InputHexString.charAt(i));
            //hex_value = (InputHexString.charAt(i));//Integer.toHexString(x);
            //x = (int) (InputHexString.charAt(i + 1));
            //hex_value =  hex_value + Integer.toHexString(x);
            hex_value =Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i+1));
            // Extract two characters and get their hexadecimal ASCII values
            ApplicationLauncher.logger.info("generateCheckSum: InputHexString1:"+InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
                    + hex_value);
            x = Integer.parseInt(hex_value, 16);
            ApplicationLauncher.logger.info( "generateCheckSum: Int Value:" + x );
            // Convert the hex_value into int and store it
            checksum += x;
            // Add 'x' into 'checksum'
        }
        if (InputHexString.length() % 2 == 0)
        {
            // If number of characters is even, then repeat above loop's steps
            // one more time.
            //x = (int) (InputHexString.charAt(i));
            //hex_value = Integer.toHexString(x);
            //x = (int) (InputHexString.charAt(i + 1));
            //hex_value = hex_value + Integer.toHexString(x);
            hex_value =Character.toString(InputHexString.charAt(i)) + Character.toString(InputHexString.charAt(i+1));
            
            ApplicationLauncher.logger.info("generateCheckSum: InputHexString2:"+InputHexString.charAt(i) + "" + InputHexString.charAt(i + 1) + " : "
                    + hex_value);
            x = Integer.parseInt(hex_value, 16);
        }
        else
        {
            // If number of characters is odd, last 2 digits will be 00.
            x = (int) (InputHexString.charAt(i));
            hex_value = "00" + Character.toString(InputHexString.charAt(i));//Integer.toHexString(x);
            x = Integer.parseInt(hex_value, 16);
            ApplicationLauncher.logger.info("generateCheckSum: InputHexString3:"+InputHexString.charAt(i) + " : " + hex_value);
        }
        checksum += x;
        // Add the generated value of 'x' from the if-else case into 'checksum'
        hex_value = Integer.toHexString(checksum);
        hex_value = StringUtils.leftPad(hex_value.toUpperCase(), 4, '0');
        //hex_value= String.format("%04", hex_value);
        // Convert into hexadecimal string
/*        if (hex_value.length() > 4)
        {
            // If a carry is generated, then we wrap the carry
            int carry = Integer.parseInt(("" + hex_value.charAt(0)), 16);
            // Get the value of the carry bit
            hex_value = hex_value.substring(1, 5);
            // Remove it from the string
            checksum = Integer.parseInt(hex_value, 16);
            // Convert it into an int
            checksum += carry;
            // Add it to the checksum
        }*/
        //checksum = generateComplement(checksum);
        // Get the complement
        return hex_value;
		
	}
	
	   
	   public static String getOsName()
	   {
	      if(OS == null) { OS = System.getProperty("os.name"); }
	      return OS;
	   }
	   
	   public static void getCurrentOS_Name(){
		   
		   String osNameMatch = getOsName().toLowerCase();
		   ApplicationLauncher.logger.info("getCurrentOS_Name : OS_Name: "+osNameMatch);
		   if(osNameMatch.contains("linux")) {
			   ConstantProTamp.TARGET_DEVICE_IS_LINUX = true;
			}else if(osNameMatch.contains("windows")) {
				ConstantProTamp.TARGET_DEVICE_IS_WINDOWS = true;
			}else if(osNameMatch.contains("solaris") || osNameMatch.contains("sunos")) {
			   
			}else if(osNameMatch.contains("mac os") || osNameMatch.contains("macos") || osNameMatch.contains("darwin")) {
			    
			}
		   ApplicationLauncher.logger.info("getCurrentOS_Name : ConstantProTamp.TARGET_DEVICE_IS_LINUX: "+ConstantProTamp.TARGET_DEVICE_IS_LINUX);
		   ApplicationLauncher.logger.info("getCurrentOS_Name : ConstantProTamp.TARGET_DEVICE_IS_WINDOWS: "+ConstantProTamp.TARGET_DEVICE_IS_WINDOWS);
		   
		   
	   }
	   
	   
	   public static String textAreaInputDialogDisplay(String header, String title, String outputFolderPathWithFileName,String outputFolderPath){
			ApplicationLauncher.logger.debug("textAreaInputDialogDisplay: Entry ");
			//Platform.runLater(() -> {

			// TextAreaInputDialog dialog = new TextAreaInputDialog();
			String userInputData = "";
			TextAreaInputDialog dialog = new TextAreaInputDialog(outputFolderPathWithFileName);

			dialog.setHeaderText(header);//"Enter ParamProfile Name");
			dialog.setTitle(title);//"ParamProfile");
			dialog.setGraphic(null);
			dialog.getEditor().setEditable(false);
			dialog.getEditor().setWrapText(true);
			//dialog.setHeight(arg0);
			Stage stage = (Stage) dialog.getDialogPane().getScene().getWindow();		
			stage.getIcons().add(new Image("file:images/"+ConstantVersion.APP_ICON_FILENAME));
			// dialog.eee
			// Show the dialog and capture the result.
			Optional result = dialog.showAndWait();

			// If the "Okay" button was clicked, the result will contain our String in the get() method
			if (result.isPresent()) {
				//System.out.println(result.get());

				ApplicationLauncher.logger.debug("textFieldInputDialogDisplay: result.get(): " + result.get());
				userInputData = (String) result.get();
				//ref_cmbBxOperationParamProfileName.getItems().add(result.get());
				//ref_cmbBxOperationParamProfileName.getSelectionModel().select(result.get());
				//ref_tvOperationParamProfile.getItems().clear();
				//getSerialNo().set(1);
			}
			return userInputData;
			//	});
		}
	   
	   public static String HexToString(String hex){

			StringBuilder sb = new StringBuilder();
			StringBuilder temp = new StringBuilder();

			//49204c6f7665204a617661 split into two characters 49, 20, 4c...
			try {
				for( int i=0; i<hex.length()-1; i+=2 ){

					//grab the hex in pairs
					String output = hex.substring(i, (i + 2));
					//convert hex to decimal
					int decimal = Integer.parseInt(output, 16);
					//convert the decimal to character
					sb.append((char)decimal);

					temp.append(decimal);
				}
			}catch(Exception e) {
				e.printStackTrace();
				ApplicationLauncher.logger.error("HexToString: Exception: "+ e.getMessage());
				return "";
			}
			//System.out.println("Decimal : " + temp.toString());

			return sb.toString();
		}

/*	   public static boolean isWindows()
	   {
	      return getOsName().startsWith("Windows");
	   }

	   public static boolean isUnix() {
		   return getOsName().startsWith("Linux");
	   }*/

	   

}