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

public class S420_ByPass_delay1 implements HvtBayState {
	
	String sequencePathId = "p1";
	private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Hv.logger.info("S420_ByPass_delay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        //BayUtils.delay(1000);      
        
        setSequencePathId("p1");
		setTestInterfaceStatus(null);

/*		Map<String,Object> responseReturn =  close_FingerTipLatch_HvtBay();	 
		StateExecutorController.updateTestInterfaceStatusOnGui(responseReturn,ConstantConveyor.COMM_EXECUTION_STATUS_COMPLETED);

		boolean close_FingerTipLatch_HvtBay = (boolean)responseReturn.get("status");
	    
        if (close_FingerTipLatch_HvtBay) {
            Hv.logger.info("S03_close_the_fingerTip_Latch : close_FingerTipLatch_HvtBay : Finger Tip Latch Closed");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
        } else {
            Hv.logger.info("S03_close_the_fingerTip_Latch : close_FingerTipLatch_HvtBay : Failed to Close Finger Tip Latch");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_HVT_003);
        }*/

        //workaround added delay for the S03_close_the_fingerTip_Latch - HV finger - #Gopi-09-06-2025 
		int delayTimeInSec = 20;
		Hv.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec >0 && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Hv.isStopProcessRequestedHvtBay()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			Hv.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		Hv.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);
		
        //BayUtils.delay(20000);
        /////////////////////
        Hv.logger.info("S420_ByPass_delay1 : Exit");
        return bayResponse;
    }
    //============================================================================================================================================  

    

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
