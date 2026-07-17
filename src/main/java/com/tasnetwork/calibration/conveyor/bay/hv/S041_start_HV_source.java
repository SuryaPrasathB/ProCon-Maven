package com.tasnetwork.calibration.conveyor.bay.hv;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.Constant_IO_ActionMapping;
import com.tasnetwork.calibration.conveyor.bay.IoPortInfo;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S041_start_HV_source implements HvtBayState {

 /*   String LOW   = "START";
    String HIGH  = "STOP";
    String CLOSE  = "Off";
    String OPEN   = "On";
    String ON  = "On";
 	String OFF  = "Off";*/
	BayUtils bayUtils = new BayUtils();

	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S041_start_HV_source : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  turn_on_start_pin_hv_bay();	 
		boolean turn_on_start_pin_hv_bay = (boolean)responseReturn.get("status");
	    
        if (turn_on_start_pin_hv_bay) {   	
            Hv.logger.info("S041_start_HV_source : turn_on_start_pin_hv_bay : Success");
            
            BayUtils.delay(500);
            
            responseReturn =  turn_off_start_pin_hv_bay();	           
            boolean turn_off_start_pin_hv_bay = (boolean)responseReturn.get("status");          

            if (turn_off_start_pin_hv_bay) {
                Hv.logger.info("S041_start_HV_source : turn_off_start_pin_hv_bay : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
                
			} 
            else {
            	 Hv.logger.info("S041_start_HV_source : Failed to turn_off_start_pin HV Source");
                 bayResponse.setStatus(false);
                 bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014 );
			}    
        } else {
            Hv.logger.info("S041_start_HV_source : Failed to turn_on_start_pin HV Source");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_014 );
        }
        
		//StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

        Hv.logger.info("S041_start_HV_source : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

 /*   private boolean startHvSource() {
		// TODO Auto-generated method stub
        HighVoltageTestBay.logger.debug("S041_start_HV_source : startHvSource : Entry");

		boolean status = false;
		
		status = turn_on_start_pin_hv_bay();
		if (status) {
	        HighVoltageTestBay.logger.debug("S041_start_HV_source : startHvSource : Turned on Source Start Pin : Success");
			BayUtils.delay(500);
		} else {
	        HighVoltageTestBay.logger.debug("S041_start_HV_source : startHvSource : Failed to turn on Source Start Pin");
	        status = false;
		}
		status = true;
		status = turn_off_start_pin_hv_bay();
		if (status) {
	        HighVoltageTestBay.logger.debug("S12_turn_on_start_pin_hv_bay : startHvSource : Turned Off Source Start Pin : Success");

		} else {
	        HighVoltageTestBay.logger.debug("S12_turn_on_start_pin_hv_bay : startHvSource : Failed to turn off Source Start Pin");
	        status = false;
		}
		
		HighVoltageTestBay.logger.debug("S041_start_HV_source : startHvSource : Exit");
		return responseReturn;
	}*/
    //============================================================================================================================================  

	private Map<String,Object> turn_on_start_pin_hv_bay() {
	    Hv.logger.debug("S041_start_HV_source : turn_on_start_pin_hv_bay : Entry");

	    boolean status = false; 
	    Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
	    IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SRC_START );

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

	    if (portInfo != null) {
	        Hv.logger.debug("PortId    : " + portInfo.getPortId());
	        Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
	        Hv.logger.debug("BayId     : " + portInfo.getBayId());
	        
	        testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.HV_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_08,
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.HV_PORT_NAME_SRC_START,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);  // In Progress
			
			StateExecutorController.addToTestStatusGui(testIntefaceStatus);
			
	    } else {
	        Hv.logger.debug("S41_turn_on_start_pin_hv_bay : turn_on_start_pin_hv_bay : Output port not found");
	        
	        testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");
			
	        return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
	                                          portInfo.getBayId(),
	                                          portInfo.getPortId(),
	                                          Constant_IO_ActionMapping.ON);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
        
	    if(StateExecutorController.simulateHvBayHappyPath){
	    	status = true; 
	    }
	    
	    if(state.equals(Constant_IO_ActionMapping.OPEN)){
			testIntefaceStatus.setDeviceResponseStatus("Success");
			//responseReturn.put("status", true); 
			status = true ;
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
			//responseReturn.put("status", false);
			status = false ;
		}
		if(portInfo.getPortId().equals(state)){
			state = "TimeOut";
			testIntefaceStatus.setDeviceResponseData("TimeOut");
		}else{
			testIntefaceStatus.setDeviceResponseData(state);
		}
		
		
	    responseReturn.put("status", status);
	    responseReturn.put("responseData", state);
	    responseReturn.put("testInterfaceStatus", testIntefaceStatus);
	    
	    Hv.logger.debug("S041_start_HV_source : turn_on_start_pin_hv_bay : status : " + status); 
	    Hv.logger.debug("S041_start_HV_source : turn_on_start_pin_hv_bay : Exit");
	    return responseReturn;
	}

    //============================================================================================================================================  
	
	private Map<String,Object> turn_off_start_pin_hv_bay() {
	    Hv.logger.debug("S041_start_HV_source : turn_off_start_pin_hv_bay : Entry");

	    boolean status = false; 
	    Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
	    IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SRC_START );

	    if (portInfo != null) {
	        Hv.logger.debug("PortId    : " + portInfo.getPortId());
	        Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
	        Hv.logger.debug("BayId     : " + portInfo.getBayId());
	    } else {
	        Hv.logger.debug("S41_turn_on_start_pin_hv_bay : turn_off_start_pin_hv_bay : Output port not found");
	        return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
	                                          portInfo.getBayId(),
	                                          portInfo.getPortId(),
	                                          Constant_IO_ActionMapping.OFF);
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;
        
	    if(StateExecutorController.simulateHvBayHappyPath){
	    	status = true; 
	    }
	    
	    Hv.logger.debug("S041_start_HV_source : turn_off_start_pin_hv_bay : status : " + status); 

		responseReturn.put("status", status);
		 
		
	    Hv.logger.debug("S041_start_HV_source : turn_off_start_pin_hv_bay : Exit");
	    return responseReturn;
	}

	public String getSequencePathId() {
		return sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}
	
	
/*	private Map<String,Object> turn_off_start_pin_hv_bay() {
	    HighVoltageTestBay.logger.debug("S041_start_HV_source : hvBay_StartPin_Status : Entry");

	    boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
	    IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_SRC_START);

	    if (portInfo != null) {
	        HighVoltageTestBay.logger.debug("PortId    : " + portInfo.getPortId());
	        HighVoltageTestBay.logger.debug("ClusterId : " + portInfo.getClusterId());
	        HighVoltageTestBay.logger.debug("BayId     : " + portInfo.getBayId());
	    } else {
	        HighVoltageTestBay.logger.debug("S041_start_HV_source : Output port not found");
	        return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
	                                          portInfo.getBayId(),
	                                          portInfo.getPortId(),
	                                          Constant_IO_ActionMapping.OPEN);
        status = state.equals(Constant_IO_ActionMapping.ON) ? true : false;
	    HighVoltageTestBay.logger.debug("S041_start_HV_source : hvBay_StartPin_Status : status : " + status);
	    HighVoltageTestBay.logger.debug("S041_start_HV_source : hvBay_StartPin_Status : Exit");
	    return responseReturn;
	}*/

    //============================================================================================================================================  

}

