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

public class S14_open_the_HV_guard implements HvtBayState {
	
	String sequencePathId = "p1";
	private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();

    //===========================================================================================
	
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S14_open_the_HV_guard : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        
        BayUtils.delay(1000);      
        
        setSequencePathId("p1");
		setTestInterfaceStatus(null);

		Map<String,Object> responseReturn =  close_hv_guard_HvtBay();	 
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		boolean close_hv_guard_HvtBay = (boolean)responseReturn.get("status");
	    
        if (close_hv_guard_HvtBay) {
            Hv.logger.info("S14_open_the_HV_guard : close_hv_guard_HvtBay : Finger Tip Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Hv.logger.info("S14_open_the_HV_guard : close_hv_guard_HvtBay : Failed to Close Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_003);
        }

        Hv.logger.info("S14_open_the_HV_guard : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> close_hv_guard_HvtBay() {
        Hv.logger.debug("S14_open_the_HV_guard : close_hv_guard_HvtBay : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        
        //============================================================================================
        IoPortInfo portInfo = BayUtils.getOutputPortDetails(ConstantBayPortNameMapping.HV_PORT_NAME_PSTN_HV_GUARD);
        
		TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus();

		testIntefaceStatus = new TestInterfaceStatus(
				ConstantConveyor.HV_BAY_KEY,
				ConstantBayStateManage.BAY_HP_SEQ_03,
				ConstantConveyor.DEVICE_TYPE_CLUSTER_OUTPUT,
				getSequencePathId(),
				"-",
				portInfo.getPortId(),
				ConstantBayPortNameMapping.HV_PORT_NAME_PSTN_HV_GUARD,
				ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
				"Waiting",
				ConstantConveyor.COMM_EXECUTION_STATUS_INP);

		StateExecutorController.addToTestStatusGui(testIntefaceStatus);

        if (portInfo != null) {
            Hv.logger.debug("PortId    : " + portInfo.getPortId());
            Hv.logger.debug("ClusterId : " + portInfo.getClusterId());
            Hv.logger.debug("BayId     : " + portInfo.getBayId());
        } else {
            Hv.logger.debug("S14_open_the_HV_guard : Output port not found");
            return responseReturn ;
        }

        BayUtils bayUtils = new BayUtils();
        
        String state = bayUtils.setOutputDataToBay(portInfo.getClusterId(), 
                                              portInfo.getBayId(), 
                                              portInfo.getPortId(),
                                              Constant_IO_ActionMapping.CLOSE);
        
        status = state.equals(Constant_IO_ActionMapping.OFF) ? true : false;       
        
        testIntefaceStatus.setPortName(portInfo.getPortId());
		
		if(status){
			testIntefaceStatus.setDeviceResponseStatus("Success");
		}else{
			testIntefaceStatus.setDeviceResponseStatus("Failed");
		}
		if(portInfo.getPortId().equals(state)){
			testIntefaceStatus.setDeviceResponseData("TimeOut");
		}else{
			testIntefaceStatus.setDeviceResponseData(state);
		}
        
        if(StateExecutorController.simulateHvBayHappyPath){
        	status = true; 
        }
        
		StateExecutorController.updateTestStatusGui(testIntefaceStatus);
        
		responseReturn.put("status", status);
		responseReturn.put("responseData", state);
		responseReturn.put("testInterfaceStatus", testIntefaceStatus);

		
        //============================================================================================  
        Hv.logger.debug("S14_open_the_HV_guard : close_hv_guard_HvtBay : status : " + status); 
		Hv.logger.debug("S14_open_the_HV_guard : close_hv_guard_HvtBay : Exit");
        return responseReturn;
    }

	public String getSequencePathId() {
		return sequencePathId;
	}

	public TestInterfaceStatus getTestInterfaceStatus() {
		return testInterfaceStatus;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public void setTestInterfaceStatus(TestInterfaceStatus testInterfaceStatus) {
		this.testInterfaceStatus = testInterfaceStatus;
	}
}
