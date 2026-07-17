package com.tasnetwork.calibration.conveyor.bay.calib;

import java.util.HashMap;
import java.util.Map;

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

public class S059_06_Turn_On_Neutral_CT implements CalibrationBayState {
	
    BayUtils bayUtils = new BayUtils();
    String sequencePathId = "p1";
    private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Calib.logger.info("S043_Turn_On_Neutral_CT : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        BayUtils.delay(500);
        Map<String, Object> responseReturn = turn_on_neutral_ct_make_pin();
        boolean turn_on_neutral_ct_make_pin = (boolean) responseReturn.get("status");

        if (turn_on_neutral_ct_make_pin) {
            Calib.logger.info("S043_Turn_On_Neutral_CT : turn_on_neutral_ct_make_pin : Success");
            BayUtils.delay(500);
            BayUtils.delay(1500);
            responseReturn = turn_off_neutral_ct_make_pin();
            boolean turn_off_neutral_ct_make_pin = (boolean) responseReturn.get("status");
            BayUtils.delay(2000);
            if (turn_off_neutral_ct_make_pin) {
                Calib.logger.info("S043_Turn_On_Neutral_CT : turn_off_neutral_ct_make_pin : Success");
                bayResponse.setStatus(true);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
            } else {
                Calib.logger.info("S043_Turn_On_Neutral_CT : Failed to turn_off_neutral_ct_make_pin Calib Bay");
                bayResponse.setStatus(false);
                bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
            }
        } else {
            Calib.logger.info("S043_Turn_On_Neutral_CT : Failed to turn_on_neutral_ct_make_pin Calib Bay");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_CALIB_026);
        }

        Calib.logger.info("S043_Turn_On_Neutral_CT : Exit");
        return bayResponse;
    }

	//============================================================================================================================================  

    private Map<String,Object> turn_on_neutral_ct_make_pin() {
		Calib.logger.debug("S042_Turn_On_Main_CT : turn_on_neutral_ct_make_pin : Entry");

		boolean status = false; 
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE );

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());

			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.CALIBRATION_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_08,
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			StateExecutorController.addToTestStatusGui(testIntefaceStatus);

		} else {
			Calib.logger.debug("S41_turn_on_neutral_ct_make_pin : turn_on_neutral_ct_make_pin : Output port not found");

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

		if(StateExecutorController.simulateCalibBayHappyPath){
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

		Calib.logger.debug("S042_Turn_On_Main_CT : turn_on_neutral_ct_make_pin : status : " + status); 
		Calib.logger.debug("S042_Turn_On_Main_CT : turn_on_neutral_ct_make_pin : Exit");
		return responseReturn;
	}

	//============================================================================================================================================  

    private Map<String,Object> turn_off_neutral_ct_make_pin() {
		Calib.logger.debug("S042_Turn_On_Main_CT : turn_off_neutral_ct_make_pin : Entry");

		boolean status = false; 
		Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE );

		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		if (portInfo != null) {
			Calib.logger.debug("PortId    : " + portInfo.getPortId());
			Calib.logger.debug("ClusterId : " + portInfo.getClusterId());
			Calib.logger.debug("BayId     : " + portInfo.getBayId());

			testIntefaceStatus = new TestInterfaceStatus(
					ConstantConveyor.CALIBRATION_BAY_KEY,
					ConstantBayStateManage.BAY_HP_SEQ_08,
					ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
					getSequencePathId(),
					"-",
					portInfo.getPortId(),
					ConstantBayPortNameMapping.CALIB_PORT_NAME_NEUTRAL_CT_MAKE,
					ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
					"Waiting",
					ConstantConveyor.COMM_EXECUTION_STATUS_INP);

			StateExecutorController.addToTestStatusGui(testIntefaceStatus);

		} else {
			Calib.logger.debug("S042_Turn_On_Main_CT : turn_off_neutral_ct_make_pin : Output port not found");

			testIntefaceStatus.setDeviceResponseStatus("Failed");
			testIntefaceStatus.setDeviceResponseData("O/P port not found");

			return responseReturn ;
		}

		BayUtils bayUtils = new BayUtils();

		String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(),
				portInfo.getBayId(),
				portInfo.getPortId(),
				Constant_IO_ActionMapping.OFF);

		status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;

		if(StateExecutorController.simulateCalibBayHappyPath){
			status = true; 
		}

		if(state.equals(Constant_IO_ActionMapping.CLOSE)){
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

		Calib.logger.debug("S042_Turn_On_Main_CT : turn_off_neutral_ct_make_pin : status : " + status); 
		Calib.logger.debug("S042_Turn_On_Main_CT : turn_off_neutral_ct_make_pin : Exit");
		return responseReturn;
	}

	//============================================================================================================================================

    public String getSequencePathId() {
        return sequencePathId;
    }

    public void setSequencePathId(String sequencePathId) {
        this.sequencePathId = sequencePathId;
    }
}