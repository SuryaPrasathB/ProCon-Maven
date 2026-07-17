package com.tasnetwork.calibration.conveyor.bay.ft;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayPortNameMapping;
import com.tasnetwork.calibration.conveyor.constant.ConstantBayStateManage;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

public class S06_scanning_of_Meters_Presence implements FtBayState {
	
	private String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();
    
    // Adjusted to return the constant directly as per your request
	public String getMyBayKey() {
		return myBayKey;
	}

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        // Structured log entry for sequence entry
        Ft.logger.info(String.format("[%s] : [METER_PRESENCE_SCAN] : [SEQUENCE_ENTRY] - Starting meter presence scan sequence.", getMyBayKey()));

        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

        // Placeholder for actual meter presence check logic
        boolean status = false; // Initialize to false by default
        Map<String,Object> meterScanResponse = new HashMap<>();

        try {
            meterScanResponse = are_All_Meters_Present_inPallet(); // Call the function to check meters presence
            status = (boolean) meterScanResponse.getOrDefault("status", false); // Get status from the response
            Ft.logger.debug(String.format("[%s] : [METER_PRESENCE_SCAN] : [SENSOR_READ] - Meter presence sensor read result: %s", getMyBayKey(), status));

        } catch (Exception e) {
            Ft.logger.error(String.format("[%s] : [METER_PRESENCE_SCAN] : [ERROR] - Exception during meter presence scan: %s", getMyBayKey(), e.getMessage()), e);
            status = false; // Ensure status is false on exception
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_008); // Use a specific error code
        }

        // Initialize TestInterfaceStatus using the constructor for consistency
        TestInterfaceStatus testIntefaceStatus = new TestInterfaceStatus(
            getMyBayKey(),
            ConstantBayStateManage.BAY_HP_SEQ_06,
            ConstantConveyor.DEVICE_TYPE_DUT, // Assuming DUT (Device Under Test) is appropriate here
            getSequencePathId(),
            ConstantBayPortNameMapping.QR_SCNR_FT_BAY_PALLET_POS_ID + "", // Convert int to String explicitly for port ID
            "", // portName is empty here, if it applies to DUT it should be set from meterScanResponse
            "", // cName is empty here, if it applies to DUT it should be set from meterScanResponse
            ConstantConveyor.COMM_STATUS_NOT_APPLICABLE,
            "Waiting", // Initial status before actual update from response
            ConstantConveyor.COMM_EXECUTION_STATUS_INP
        );
        
        // Update TestInterfaceStatus based on the result of meter presence check
        if (status) {
            Ft.logger.info(String.format("[%s] : [METER_PRESENCE_SCAN] : [ALL_PRESENT] - All meters confirmed present in pallet.", getMyBayKey()));
            testIntefaceStatus.setDeviceResponseStatus("Success");
            testIntefaceStatus.setDeviceResponseData("All Meters Present");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
        } else {
            Ft.logger.error(String.format("[%s] : [METER_PRESENCE_SCAN] : [NOT_ALL_PRESENT] - Meters not present or scan failed. Error: %s", getMyBayKey(), ConvErrorCodeMapping.ERROR_CODE_FT_008));
            testIntefaceStatus.setDeviceResponseStatus("Failed");
            testIntefaceStatus.setDeviceResponseData("Meters Not Present");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_FT_008);  // Failure error code
        }

        // Add or update GUI status (assuming StateExecutorController handles Platform.runLater() internally)
        StateExecutorController.addToTestStatusGui(testIntefaceStatus);
        StateExecutorController.updateTestStatusGui(testIntefaceStatus); // Update with final status

        // Structured log for sequence exit
        Ft.logger.info(String.format("[%s] : [METER_PRESENCE_SCAN] : [SEQUENCE_EXIT] - Meter presence scan sequence completed.", getMyBayKey()));
        return bayResponse;
    }
    //============================================================================================================================================  

    private Map<String,Object> are_All_Meters_Present_inPallet() {
        // Structured debug log for method entry
        Ft.logger.debug(String.format("[%s] : [METER_PRESENCE_INTERNAL] : [ENTRY] - Checking all meters present in pallet.", getMyBayKey()));

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false); // Default status to false

        // --- ADD YOUR ACTUAL LOGIC HERE TO CHECK METER PRESENCE ---
        // This method currently does not have the actual implementation.
        // You would typically read sensor data, perhaps iterate through expected meter positions,
        // and set the 'status' variable based on that.
        // Example:
        // IoPortInfo meterSensor1 = BayUtils.getInputPortDetails(ConstantBayPortNameMapping.FT_METER_1_SENSOR);
        // String meter1State = BayUtils.getInputDataFromBayV2(meterSensor1);
        // if (meter1State.equals(Constant_IO_ActionMapping.OLD_OFF_NEW_ON)) { ... }
        // For now, it will return false unless `StateExecutorController.simulateFtBayHappyPath` is true.

        // Simulation for happy path
        if (StateExecutorController.simulateFtBayHappyPath) {
            status = true; // Simulate all meters present
            Ft.logger.debug(String.format("[%s] : [SIMULATION] : All meters present status overridden to TRUE.", getMyBayKey()));
        }

        // Log the final resolved status of the internal check
        Ft.logger.debug(String.format("[%s] : [METER_PRESENCE_INTERNAL] : [FINAL_STATUS] : %s", getMyBayKey(), status));
		responseReturn.put("status", status);		

        // Structured debug log for method exit
        Ft.logger.debug(String.format("[%s] : [METER_PRESENCE_INTERNAL] : [EXIT] - Meter presence check completed.", getMyBayKey()));
        return responseReturn;
    }

	public String getSequencePathId() {
		return sequencePathId;
	}

	public void setSequencePathId(String sequencePathId) {
		this.sequencePathId = sequencePathId;
	}

	public TestInterfaceStatus getPalletAvailableTest_I_F_Status() {
		return palletAvailableTest_I_F_Status;
	}

	public void setPalletAvailableTest_I_F_Status(TestInterfaceStatus palletAvailableTest_I_F_Status) {
		this.palletAvailableTest_I_F_Status = palletAvailableTest_I_F_Status;
	}
}
