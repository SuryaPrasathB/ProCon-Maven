package com.tasnetwork.calibration.conveyor.bay.ir;

import java.util.Timer;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;
//import com.tasnetwork.calibration.conveyor.bay_highvoltagetest.HighVoltageTestBay;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;


public class S10_error_Handling  implements IrtBayState {
	
	Timer insResStopTaskTimer;
	
	BayResponse bayResponse = new BayResponse();

	@Override
	public BayResponse handleRequest() {
		Ir.logger.info("S10_error_Handling : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode("NO_ERROR_001");

		return bayResponse;
	}

	public S10_error_Handling() {

	}

	public S10_error_Handling(String errorCode) {

		switch (errorCode) {
		case ConvErrorCodeMapping.ERROR_CODE_IRT_001:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_002:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_003:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_004:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_005:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_006:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_007:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_008:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_009:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_010:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_011:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_012:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		case ConvErrorCodeMapping.ERROR_CODE_IRT_013:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		default:
			insResStopTaskTimer = new Timer();
			insResStopTaskTimer.schedule(new InsulationResistanceTestBayStop(),100);
			Sleep(500);
			insResStopTaskTimer.cancel(); 			
			break;
		}
	}
	
	public void Sleep(int timeInMsec) {

		try {
			Thread.sleep(timeInMsec);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Ir.logger.error("Sleep :InterruptedException:"+ e.getMessage());
		}

	}
}