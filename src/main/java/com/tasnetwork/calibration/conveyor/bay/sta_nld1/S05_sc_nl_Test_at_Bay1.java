package com.tasnetwork.calibration.conveyor.bay.sta_nld1;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_sc_nl_Test_at_Bay1 implements STA_NoLoadTestBay1State {
	
	
    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay1.logger.info("S05_sc_nl_Test_at_Bay1 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);

		Map<String,Object> responseReturn =  scNlTestAtBay1();	 
		boolean scNlTestAtBay1 = (boolean)responseReturn.get("status");
	    
        boolean status = scNlTestAtBay1;                                // Call the function to perform SC-NL test at Bay1

        if (status) {
            StaNld_Bay1.logger.info("S05_sc_nl_Test_at_Bay1 : SC-NL Test at Bay1 Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
        } else {
            StaNld_Bay1.logger.info("S05_sc_nl_Test_at_Bay1 : SC-NL Test at Bay1 Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY1_010);  // Failure error code
        }

        StaNld_Bay1.logger.info("S05_sc_nl_Test_at_Bay1 : Exit");
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> scNlTestAtBay1() {
        StaNld_Bay1.logger.debug("S05_sc_nl_Test_at_Bay1 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        

        // Add logic for the SC-NL test at Bay1 here
        // Set 'status' to true if the test is successful, otherwise false
        
        // 1. Start Source 
        // 2. Run Test (Supply V&I) 
        // 3. Stop Source
		
		if(StateExecutorController.simulateSCTNLTBay1HappyPath){
			status = true; 
		}
		 
        StaNld_Bay1.logger.debug("S05_sc_nl_Test_at_Bay1 : status : " + status); 

		responseReturn.put("status", status);
		

        StaNld_Bay1.logger.debug("S05_sc_nl_Test_at_Bay1 : Exit");
        return responseReturn;
    }
    
    //===============================================================


}
