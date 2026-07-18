package com.tasnetwork.calibration.conveyor.bay.hv;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S420_ByPass_delay1 implements HvtBayState {

	String sequencePathId = "p1";
	private TestInterfaceStatus testInterfaceStatus = new TestInterfaceStatus();

	// ===========================================================================================
	/**
	 * Bypass delay state for the HVT Bay.
	 * Provides a workaround delay.
	 *
	 * @return BayResponse indicating success or failure.
	 */
	@Override
	public BayResponse handleRequest() {
		Hv.logger.info("S420_ByPass_delay1 : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		// BayUtils.delay(1000);

		setSequencePathId("p1");
		setTestInterfaceStatus(null);

		// workaround added delay for the S03_close_the_fingerTip_Latch - HV finger -
		// #Gopi-09-06-2025
		int delayTimeInSec = 20;
		Hv.logger.info("S420_ByPass_delay1 : Delay Time Entry in Sec: " + delayTimeInSec);
		while (delayTimeInSec > 0 && !ConstantConveyor.ALL_LOOP_BREAK_FLAG && !Hv.isStopProcessRequestedHvtBay()) {
			delayTimeInSec--;
			BayUtils.delay(1000);
			Hv.logger.info("S420_ByPass_delay1 : Delay Time waiting in Sec: " + delayTimeInSec);
		}
		Hv.logger.info("S420_ByPass_delay1 : Delay Time Exit in Sec: " + delayTimeInSec);

		Hv.logger.info("S420_ByPass_delay1 : Exit");
		return bayResponse;
	}
	// ============================================================================================================================================

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
