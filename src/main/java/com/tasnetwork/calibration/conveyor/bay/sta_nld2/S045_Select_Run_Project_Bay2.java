package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.xml.ws.Endpoint;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.bay.sta_nld2.S046_Stop_Execution_Bay2;
import com.tasnetwork.calibration.conveyor.bay.verific.Verification;
//import com.tasnetwork.calibration.conveyor.bay_calibration.CalibrationBay;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteResponse;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.YesNoDialogFX;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S045_Select_Run_Project_Bay2 implements STA_NoLoadTestBay2State  {

	BayUtils bayUtils = new BayUtils();
	
	String paramKey = "";
	String paramValue = "";


	String sequencePathId = "p1";
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		StaNld_Bay2.logger.info("S045_Select_Run_Project : Entry");
		
		S046_Stop_Execution_Bay2.setStopExecutionRequested(false);
		
		BayUtils.delay(2000);
		
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );
		
		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		paramKey = ConstantConveyorConfig.PARAM_PROJECT_NAME; //"projectName";
		paramValue = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_PROJECT_NAME; //"DevSysSta2";
		
		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_IP_PORT;
		String endpoint = ConstantConveyorConfig.CONVEYOR_PROCAL_STA2_END_PATH;
		String commandMessage = "updateDutSerialNo";
		ClusterServer stdNldt2ClusterServer = new ClusterServer(ip_address, ip_port);

		// Send meter serial numbers to PROCAL STA 2
		//Map<Integer, String> meterSerialNumberMap = getMeterSerialNumberMap();
		
		Map<Integer, Map<String, String>> meterSerialNumberWithPalletMap = getMeterSerialNumberWithPalletMap();

		for (Map.Entry<Integer, Map<String, String>> entry : meterSerialNumberWithPalletMap.entrySet()) {
		    Integer position = entry.getKey();
		    Map<String, String> valueMap = entry.getValue();

		    String meterSerialNo = valueMap.getOrDefault(ConstantConveyor.PALLET_METER_SERIAL_NO_KEY, "NA2");
		    String palletDistinctId = valueMap.getOrDefault(ConstantConveyor.PALLET_DISTINCT_ID_KEY, "NA2");

		    StaNld_Bay2.logger.info("S045_Select_Run_Project: Verific : Position: " + position +
		                             ", Meter Serial No: " + meterSerialNo +
		                             ", Pallet Distinct ID: " + palletDistinctId);
		}

		//String procalServerResponse = procalRemoteSender.sendDutMeterSerialNo(stdNldt2ClusterServer, endpoint, commandMessage,  meterSerialNumberMap);
		String procalServerResponse = procalRemoteSender.sendDutMeterSerialNoV2(stdNldt2ClusterServer, endpoint, commandMessage,  meterSerialNumberWithPalletMap);

		if (procalServerResponse.equals("updateDutSerialNoDone")) {
			BayUtils.delay(5000);
			StaNld_Bay2.logger.info("S045_Select_Run_Project : updateDutSerialNo Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			StaNld_Bay2.logger.info("S045_Select_Run_Project : updateDutSerialNo Failed");
			bayResponse.setStatus(false);
			//BayUtils.delay(1000);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_028);
		}

		BayUtils.delay(2000);
		int retry =3;
		while( (retry>0) && (!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) ) {
			StaNld_Bay2.logger.info("S045_Select_Run_Project : sendSelectRunCommandToProcal retry : " +retry);
			retry--;
			procalServerResponse = procalRemoteSender.sendSelectRunCommandToProcal(stdNldt2ClusterServer, endpoint, paramKey, paramValue);
	
			if (procalServerResponse.equals("ProjectSelected")) {
				BayUtils.delay(1000);
				StaNld_Bay2.logger.info("S045_Select_Run_Project : Close Run Project Success");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}else if (procalServerResponse.equals("ProjectRunScreenNotDisplayed")) {
				BayUtils.delay(5000);
				StaNld_Bay2.logger.info("S045_Select_Run_Project : ProjectRunScreenNotDisplayed");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			} else {
				StaNld_Bay2.logger.info("S045_Select_Run_Project : Close Run Project Failed");
				bayResponse.setStatus(false);
				BayUtils.delay(1000);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
			}
		}
		
		if (!StaNld_Bay2.isStopProcessRequestedStaNldBay2()) {
			if (!procalServerResponse.equals("ProjectSelected")) {
				StaNld_Bay2.logger.debug("S045_Select_Run_Project : Unexpected response from Procal STA2 : " +procalServerResponse);
				Platform.runLater(()->{
					String header = "STA2 Bay : Problem in ProCAL-Sta2- Test Run Screen. Kindly Check!!";
					String title  = "STA2 Bay";
					YesNoDialogFX dialog = new YesNoDialogFX(title, header,YesNoDialogFX.MessageType.WARNING);
					dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
						if (Boolean.TRUE.equals(newVal)) {
							StaNld_Bay2.logger.debug("S045_Select_Run_Project: Select Project Run: prompt user hit: YES");
						} else {
							StaNld_Bay2.logger.debug("S045_Select_Run_Project: Select Project Run: prompt user hit: NO");
						}


					});
					dialog.show(); // This will NOT block the JavaFX thread 
					StaNld_Bay2.logger.debug("S045_Select_Run_Project: Prompt shown, returning immediately");

				});


			}
		}
		
		StaNld_Bay2.logger.info("S045_Select_Run_Project : Exit");
		return bayResponse;
	}
	
	public Map<Integer, Map<String, String>> getMeterSerialNumberWithPalletMap() {
		StaNld_Bay2.logger.info("S045_Select_Run_Project : getMeterSerialNumberWithPalletMap : Entry");

	    Map<Integer, Map<String, String>> positionToMeterMap = new HashMap<>();
	    List<PalletManage> palletManageList = new ArrayList<PalletManage> ();
	    String presentBayKey = ConstantConveyor.STA_NLD2_BAY_KEY;

		
	    while (true && !BayUtils.isUserAborted() && !StaNld_Bay2.isStopProcessRequestedStaNldBay2()) {
	        palletManageList = MySqlServiceManager.getPalletManageService()
	                .findByPresentBayKeyAndPalletActive(presentBayKey);

	        if (palletManageList.size() < 4) {
	        	StaNld_Bay2.logger.error("getMeterSerialNumberWithPalletMap: Not enough pallets (" + palletManageList.size() + ") in STA 2 Bay. Displaying alert.");
	            //displayNotEnoughPalletsAlert();
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                StaNld_Bay2.logger.warn("getMeterSerialNumberWithPalletMap: Thread interrupted while waiting to re-check pallets.", e);
	            }
	        } else {
	        	StaNld_Bay2.logger.info("Enough pallets (" + palletManageList.size() + ") found. Proceeding with meter mapping.");
	            break;
	        }
	    }

	    int[][] customOrder = ConstantConveyor.LDU_PLACEMENT_ORDER;
	    	
/*	    	{
	            {3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6},
	            {2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6},
	            {1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6},
	            {0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}
	    };*/

	    for (int globalPos = 0; globalPos < customOrder.length; globalPos++) {
	        int palletIndex = customOrder[globalPos][0];
	        int localPalletRackPos = customOrder[globalPos][1];

	        if (palletIndex >= palletManageList.size()) {
	        	StaNld_Bay2.logger.error("getMeterSerialNumberWithPalletMap: " + String.format(
	                    "Error: palletIndex %d is out of bounds for palletManageList (size: %d). Skipping this global position. - pallet positionId - %d",
	                    palletIndex, palletManageList.size(), localPalletRackPos));
	            Map<String, String> errorMap = new HashMap<>();
	            errorMap.put(ConstantConveyor.PALLET_METER_SERIAL_NO_KEY, "ERROR");
	            errorMap.put(ConstantConveyor.PALLET_DISTINCT_ID_KEY, "INVALID");
	            errorMap.put(ConstantConveyor.PALLET_RACK_POSITION_NO_KEY, String.valueOf(localPalletRackPos));
	            positionToMeterMap.put(globalPos + 1, errorMap);
	            continue;
	        }

	        PalletManage palletManage = palletManageList.get(palletIndex);
	        String palletDistinctId = palletManage.getPalletDistinctId();

	        Optional<PalletMeter> meterOpt = MySqlServiceManager.getPalletMeterService()
	                .findByRackPositionNoAndPalletDistinctId(localPalletRackPos, palletDistinctId);
	        if(meterOpt.isPresent()) {
		        PalletMeter meter = meterOpt.get();
		        String serialNumber = (meter != null) ? meter.getMeterSerialNo() : "NA_V1";
		        Map<String, String> innerMap = new HashMap<>();
		        innerMap.put(ConstantConveyor.PALLET_METER_SERIAL_NO_KEY, serialNumber);
		        innerMap.put(ConstantConveyor.PALLET_DISTINCT_ID_KEY, palletDistinctId);
		        innerMap.put(ConstantConveyor.PALLET_RACK_POSITION_NO_KEY, String.valueOf(meter.getRackPositionNo()));
		        positionToMeterMap.put(globalPos + 1, innerMap);
	
		        StaNld_Bay2.logger.info("getMeterSerialNumberWithPalletMap: " + String.format(
		                "Mapped globalPos=%d => palletIndex=%d, configPalletRackPos=%d => Serial=%s, PalletID=%s, PalletRackPosition=%d",
		                (globalPos + 1), palletIndex, localPalletRackPos, serialNumber, palletDistinctId,meter.getRackPositionNo()
		        ));
	        }else {
	        	StaNld_Bay2.logger.info("S045_Select_Run_Project : palletDistinctId-1: " + palletDistinctId+" ,localRackPos not found: " + localPalletRackPos );
			}
	        
	    }

	    StaNld_Bay2.logger.info("S045_Select_Run_Project : getMeterSerialNumberWithPalletMap: Final positionToMeterMap = " + positionToMeterMap);
	    return positionToMeterMap;
	}
	
	//============================================================================================================================================  
	
	public Map<Integer, String> getMeterSerialNumberMap() {
		StaNld_Bay2.logger.info("S045_Select_Run_Project : getMeterSerialNumberMap : Entry");

		Map<Integer, String> meterSerialNumberMap = new HashMap<>();
		String presentBayKey = ConstantConveyor.STA_NLD2_BAY_KEY;
		List<PalletManage> myPalletManageList = MySqlServiceManager.getPalletManageService()
				.findByPresentBayKeyAndPalletActive(presentBayKey);

		if (myPalletManageList.size() < 4) {
			StaNld_Bay2.logger.error("Not enough pallets to proceed");
			return meterSerialNumberMap;
		}

		// Order of mapping: [palletIndex, rackPosition]
		// New custom order as requested:
		// Pallet 0 positions 1-6
		// Pallet 1 positions 1-6
		// Pallet 2 positions 1-6
		// Pallet 3 positions 1-6
		int[][] customOrder = ConstantConveyor.LDU_PLACEMENT_ORDER;
			
/*			{
				{3, 1}, {3, 2}, {3, 3}, {3, 4}, {3, 5}, {3, 6}, // Pallet 3
				{2, 1}, {2, 2}, {2, 3}, {2, 4}, {2, 5}, {2, 6}, // Pallet 2
				{1, 1}, {1, 2}, {1, 3}, {1, 4}, {1, 5}, {1, 6}, // Pallet 1
				{0, 1}, {0, 2}, {0, 3}, {0, 4}, {0, 5}, {0, 6}  // Pallet 0
		};*/

		for (int globalPos = 0; globalPos < customOrder.length; globalPos++) {
			int palletIndex = customOrder[globalPos][0];
			int localRackPos = customOrder[globalPos][1];

			// Ensure palletIndex is within bounds before accessing
			if (palletIndex >= myPalletManageList.size()) {
				StaNld_Bay2.logger.error(String.format(
						"Error: palletIndex %d is out of bounds for myPalletManageList (size: %d). Skipping this global position.",
						palletIndex, myPalletManageList.size()));
				meterSerialNumberMap.put(globalPos + 1, "ERROR: Invalid Pallet Index");
				continue; // Skip to the next iteration
			}


			PalletManage palletManage = myPalletManageList.get(palletIndex);
			String palletDistinctId = palletManage.getPalletDistinctId();

			Optional<PalletMeter> meterOpt = MySqlServiceManager.getPalletMeterService()
					.findByRackPositionNoAndPalletDistinctId(localRackPos, palletDistinctId);
			if(meterOpt.isPresent()) {
		        PalletMeter meter = meterOpt.get();
				String serialNumber = (meter != null) ? meter.getMeterSerialNo() : "NA";
				meterSerialNumberMap.put(globalPos + 1, serialNumber);
	
				StaNld_Bay2.logger.info(String.format(
						"Mapped globalPos=%d => palletIndex=%d, rackPos=%d => Serial=%s",
						globalPos + 1, palletIndex, localRackPos, serialNumber
				));
			}else {
	        	StaNld_Bay2.logger.info("S045_Select_Run_Project : palletDistinctId-2: " + palletDistinctId+" ,localRackPos not found: " + localRackPos );
			}
		}

		StaNld_Bay2.logger.info("S045_Select_Run_Project : Final meterSerialNumberMap = " + meterSerialNumberMap);
		return meterSerialNumberMap;
	}
	
	//============================================================================================================================================  

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
