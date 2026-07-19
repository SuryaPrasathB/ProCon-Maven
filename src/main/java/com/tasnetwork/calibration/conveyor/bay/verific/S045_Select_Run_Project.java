package com.tasnetwork.calibration.conveyor.bay.verific;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.tasnetwork.calibration.conveyor.ClusterServer;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.bay.BayUtils;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyor;
import com.tasnetwork.calibration.conveyor.constant.ConstantConveyorConfig;
import com.tasnetwork.calibration.conveyor.constant.ProconFeatureEnable;
import com.tasnetwork.calibration.conveyor.database.MySqlServiceManager;
import com.tasnetwork.calibration.conveyor.remote.ProcalRemoteSender;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;
import com.tasnetwork.calibration.energymeter.util.YesNoDialogFX;
import com.tasnetwork.spring.orm.model.PalletManage;
import com.tasnetwork.spring.orm.model.PalletMeter;
import com.tasnetwork.spring.orm.model.TestInterfaceStatus;

import javafx.application.Platform;

public class S045_Select_Run_Project implements VerificTestBayState  {

	BayUtils bayUtils = new BayUtils();

	List<PalletManage> myPalletManageList;

	String paramKey = "";
	String paramValue = "";

	String sequencePathId = "p1";
	
	ArrayList<String> groupedPalletDistinctIdList = new ArrayList<String>();
	private TestInterfaceStatus palletAvailableTest_I_F_Status = new TestInterfaceStatus();

	//===========================================================================================
	@Override
	public BayResponse handleRequest() {
		Verification.logger.info("S045_Select_Run_Project : Entry");
		BayResponse bayResponse = new BayResponse();
		bayResponse.setStatus(true);
		bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601 );

		ProcalRemoteSender procalRemoteSender = new ProcalRemoteSender();
		
		

		paramKey = ConstantConveyorConfig.PARAM_PROJECT_NAME; //"projectName";
		paramValue = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_PROJECT_NAME;//"DevSysVerific01";

		String ip_address = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_ADDRESS;
		String ip_port = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_IP_PORT;
		String endPoint = ConstantConveyorConfig.CONVEYOR_PROCAL_VERIFIC_END_PATH;
		String commandMessage = "updateDutSerialNo";
		ClusterServer verificClusterServer = new ClusterServer(ip_address, ip_port);



		//String procalServerResponse = procalRemoteSender.sendSelectRunCommandToProcal(verificClusterServer, endpoint, paramKey, paramValue);

		// Send meter serial numbers to PROCAL VERIFIC
		Map<Integer, String> meterSerialNumberMap = getMeterSerialNumberMap();
		for (Map.Entry<Integer, String> entry : meterSerialNumberMap.entrySet()) {
			Verification.logger.info("Key: " + entry.getKey() + ", Value: " + entry.getValue());
		}
		Map<Integer, Map<String, String>> meterSerialNumberWithPalletMap = getMeterSerialNumberWithPalletMap();

		for (Map.Entry<Integer, Map<String, String>> entry : meterSerialNumberWithPalletMap.entrySet()) {
		    Integer position = entry.getKey();
		    Map<String, String> valueMap = entry.getValue();

		    String meterSerialNo = valueMap.getOrDefault(ConstantConveyor.PALLET_METER_SERIAL_NO_KEY, "NA2");
		    String palletDistinctId = valueMap.getOrDefault(ConstantConveyor.PALLET_DISTINCT_ID_KEY, "NA2");

		    Verification.logger.info("S045_Select_Run_Project: Verific : Position: " + position +
		                             ", Meter Serial No: " + meterSerialNo +
		                             ", Pallet Distinct ID: " + palletDistinctId);
		}


		//String procalServerResponse = procalRemoteSender.sendDutMeterSerialNo(verificClusterServer, endPoint, commandMessage,  meterSerialNumberMap);
		String procalServerResponse = procalRemoteSender.sendDutMeterSerialNoV2(verificClusterServer, endPoint, commandMessage,  meterSerialNumberWithPalletMap);

		if (procalServerResponse.equals("updateDutSerialNoDone")) {
			BayUtils.delay(5000);
			for (String eachPalletDistinctId : groupedPalletDistinctIdList) {
				Verification.logger.info("S045_Select_Run_Project : clearing db results for eachPalletDistinctId: " +eachPalletDistinctId);
				//BayUtils bayUtils = new BayUtils();
				bayUtils.archiveExistingResultInDb(getMyBayKey(),eachPalletDistinctId);
			}
			Verification.logger.info("S045_Select_Run_Project : updateDutSerialNo Success");
			bayResponse.setStatus(true);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
		} else {
			Verification.logger.info("S045_Select_Run_Project : updateDutSerialNo Failed");
			bayResponse.setStatus(false);
			bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_028);
		}

		BayUtils.delay(2000);
		int retry =3;
		while( (retry>0) && (!Verification.isStopProcessRequestedVerificBay()) ) {
			Verification.logger.info("S045_Select_Run_Project : sendSelectRunCommandToProcal retry : " +retry);
			procalServerResponse = procalRemoteSender.sendSelectRunCommandToProcal(verificClusterServer, endPoint, paramKey, paramValue);
			retry--;
			// NEED FIXING : EXCEPTION OCCURS ?
			if (procalServerResponse.equals("ProjectSelected")) {
				BayUtils.delay(5000);
				Verification.logger.info("S045_Select_Run_Project : Select Run Project Success");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}else if (procalServerResponse.equals("ProjectRunScreenNotDisplayed")) {
				BayUtils.delay(5000);
				Verification.logger.info("S045_Select_Run_Project : ProjectRunScreenNotDisplayed");
				bayResponse.setStatus(true);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);
			}else {
				Verification.logger.info("S045_Select_Run_Project : Select Run Project Failed");
				bayResponse.setStatus(false);
				BayUtils.delay(1000);
				bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_VERIFIC_027);
			}
		}
		if (!Verification.isStopProcessRequestedVerificBay()) {
			if (!procalServerResponse.equals("ProjectSelected")) {
				Verification.logger.debug("S045_Select_Run_Project : Unexpected response from Procal verific : " +procalServerResponse);
				Platform.runLater(()->{
					String header = "Verification Bay : Problem in ProCAL-Verific- Test Run Screen. Kindly Check!!";
					String title  = "Verification Bay";
					YesNoDialogFX dialog = new YesNoDialogFX(title, header,YesNoDialogFX.MessageType.WARNING);
					dialog.resultProperty().addListener((obs, oldVal, newVal) -> {
						if (Boolean.TRUE.equals(newVal)) {
							Verification.logger.debug("S045_Select_Run_Project: Select Project Run: prompt user hit: YES");
						} else {
							Verification.logger.debug("S045_Select_Run_Project: Select Project Run: prompt user hit: NO");
						}


					});
					dialog.show(); // This will NOT block the JavaFX thread 
					Verification .logger.debug("S045_Select_Run_Project: Prompt shown, returning immediately");

				});


			}
		}

		if (ProconFeatureEnable.WAIT_FOR_USER_INPUT) {

			StateExecutorController.getRef_btn_VerificDone().setDisable(false);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_VERIFIC_prompt.setText("Start Verification Test");
			});

			// Start a timer to update the text after 2 minutes (120 seconds)
			Executors.newSingleThreadScheduledExecutor().schedule(() -> {
				Platform.runLater(() -> {
					// Update the text after 2 minutes
					StateExecutorController.ref_tf_VERIFIC_prompt.setText("Is Verification Test Completed?");
				});
			}, 2, TimeUnit.MINUTES);

			// Wait for the verification test to complete
			while (!ConstantConveyor.isVERIFIC_TESTING_DONE()) {
				Verification.logger.debug("S045_Select_Run_Project : Waiting to complete Verification Test ");
			}

			BayUtils.delay(2000);
			ConstantConveyor.setVERIFIC_TESTING_DONE(false);
			StateExecutorController.getRef_btn_VerificDone().setDisable(true);

			Platform.runLater(() -> {
				StateExecutorController.ref_tf_VERIFIC_prompt.clear();
			});
		}	

		Verification.logger.info("S045_Select_Run_Project : Exit");
		return bayResponse;
	}

	//============================================================================================================================================  

	public Map<Integer, String> getMeterSerialNumberMap() {
		Verification.logger.info("S045_Select_Run_Project : getMeterSerialNumberMap : Entry");

		Map<Integer, String> meterSerialNumberMap = new HashMap<>();
		String presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;

		// Loop indefinitely until enough pallets are found
		while (true && !BayUtils.isUserAborted() && !Verification.isStopProcessRequestedVerificBay()) {
			// Fetch the latest list of pallets
			myPalletManageList = MySqlServiceManager.getPalletManageService()
					.findByPresentBayKeyAndPalletActive(presentBayKey);

			if (myPalletManageList.size() < 4) {
				Verification.logger.error("Not enough pallets (" + myPalletManageList.size() + ") in Verification Bay. Displaying alert.");
				displayNotEnoughPalletsAlert();
				try {
					Thread.sleep(1000); // Wait 1 second before re-checking
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
					Verification.logger.warn("Thread interrupted while waiting to re-check pallets.", e);
					// If interrupted, we might want to exit or handle differently depending on context.
					// For now, it will just re-loop immediately.
				}
			} else {
				Verification.logger.info("Enough pallets (" + myPalletManageList.size() + ") found. Proceeding with meter mapping.");
				break; // Exit the loop as we have enough pallets
			}
		}

		int[][] customOrder = ConstantConveyor.LDU_PLACEMENT_ORDER;


		for (int globalPos = 0; globalPos < customOrder.length; globalPos++) {
			int palletIndex = customOrder[globalPos][0];
			int localRackPos = customOrder[globalPos][1];

			// Ensure palletIndex is within bounds before accessing
			if (palletIndex >= myPalletManageList.size()) {
				Verification.logger.error(String.format(
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
	
				Verification.logger.info(String.format(
						"Mapped globalPos=%d => palletIndex=%d, rackPos=%d => Serial=%s",
						globalPos + 1, palletIndex, localRackPos, serialNumber
						));
			}else {
				Verification.logger.info("S045_Select_Run_Project : palletDistinctId-2: " + palletDistinctId+" ,localRackPos not found: " + localRackPos );
			}
		}

		Verification.logger.info("S045_Select_Run_Project : Final meterSerialNumberMap = " + meterSerialNumberMap);
		return meterSerialNumberMap;
	}
	
	public Map<Integer, Map<String, String>> getMeterSerialNumberWithPalletMap() {
	    Verification.logger.info("S045_Select_Run_Project : getMeterSerialNumberWithPalletMap : Entry");

	    Map<Integer, Map<String, String>> positionToMeterMap = new HashMap<>();
	    List<PalletManage> palletManageList = new ArrayList<PalletManage> ();
	    String presentBayKey = ConstantConveyor.VERIFICATION_BAY_KEY;

		
	    while (true && !BayUtils.isUserAborted()) {
	        palletManageList = MySqlServiceManager.getPalletManageService()
	                .findByPresentBayKeyAndPalletActive(presentBayKey);

	        if (palletManageList.size() < 4) {
	            Verification.logger.error("getMeterSerialNumberWithPalletMap: Not enough pallets (" + palletManageList.size() + ") in Verification Bay. Displaying alert.");
	            displayNotEnoughPalletsAlert();
	            try {
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                Verification.logger.warn("getMeterSerialNumberWithPalletMap: Thread interrupted while waiting to re-check pallets.", e);
	            }
	        } else {
	            Verification.logger.info("Enough pallets (" + palletManageList.size() + ") found. Proceeding with meter mapping.");
	            groupedPalletDistinctIdList.clear();
	            for(PalletManage eachPalletManage : palletManageList ) {
	            	groupedPalletDistinctIdList.add(eachPalletManage.getPalletDistinctId());
	            }
	            break;
	        }
	    }

	    int[][] customOrder = ConstantConveyor.LDU_PLACEMENT_ORDER;

	    for (int globalPos = 0; globalPos < customOrder.length; globalPos++) {
	        int palletIndex = customOrder[globalPos][0];
	        int localPalletRackPos = customOrder[globalPos][1];

	        if (palletIndex >= palletManageList.size()) {
	            Verification.logger.error("getMeterSerialNumberWithPalletMap: " + String.format(
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
		        
		        Verification.logger.info("getMeterSerialNumberWithPalletMap: " + String.format(
		                "Mapped globalPos=%d => palletIndex=%d, configPalletRackPos=%d => Serial=%s, PalletID=%s, PalletRackPosition=%d",
		                (globalPos + 1), palletIndex, localPalletRackPos, serialNumber, palletDistinctId,meter.getRackPositionNo()
		        ));
	        }else {
				Verification.logger.info("S045_Select_Run_Project : palletDistinctId-1: " + palletDistinctId+" ,localRackPos not found: " + localPalletRackPos );
			}
	    }

	    Verification.logger.info("S045_Select_Run_Project : getMeterSerialNumberWithPalletMap: Final positionToMeterMap = " + positionToMeterMap);
	    return positionToMeterMap;
	}


	/**
	 * Displays a non-blocking UI alert indicating that there are not enough pallets.
	 * The calling thread will block until the user dismisses the dialog.
	 */
	private void displayNotEnoughPalletsAlert() {
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
