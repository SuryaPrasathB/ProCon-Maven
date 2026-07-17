package com.tasnetwork.calibration.conveyor.bay.unloading;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.ConveyorDebugController;
import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_indicate_the_status_of_meters implements UnloadingBayState {
    
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        Unloading.logger.info("S05_indicate_the_status_of_meters : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		Map<String,Object> responseReturn =  indicate_the_status_of_meters();	 
		boolean indicate_the_status_of_meters = (boolean)responseReturn.get("status");
	    
        boolean status = indicate_the_status_of_meters;   

        if (status) {
            Unloading.logger.info("S05_indicate_the_status_of_meters :  ");
            // Logic for success case (status is true)
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
        } else {
            Unloading.logger.info("S05_indicate_the_status_of_meters :  ");
            // Logic for failure case (status is false)
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_UNLOADING_012);  // Failure error code
        }

        Unloading.logger.info("S05_indicate_the_status_of_meters : Exit");
        return bayResponse;
    }
    
    //============================================================================================================================================  

    private Map<String,Object> indicate_the_status_of_meters() {
        // TODO Auto-generated method stub
        
        Unloading.logger.debug("S05_indicate_the_status_of_meters : indicate_the_status_of_meters : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
		 

		if(StateExecutorController.simulateUnloadingBayHappyPath){
			status = true; 
		}


  
        Unloading.logger.debug("S05_indicate_the_status_of_meters : indicate_the_status_of_meters : status : " + status); 

		responseReturn.put("status", status);
		

        Unloading.logger.debug("S05_indicate_the_status_of_meters : indicate_the_status_of_meters : Exit");
        return responseReturn;
    }
}
