package com.tasnetwork.calibration.conveyor.bay.sta_nld2;

import java.util.HashMap;
import java.util.Map;

import com.tasnetwork.calibration.conveyor.StateExecutorController;
import com.tasnetwork.calibration.conveyor.bay.BayResponse;
import com.tasnetwork.calibration.conveyor.util.ConvErrorCodeMapping;

public class S05_sc_nl_Test_at_Bay2 implements STA_NoLoadTestBay2State {

    //===========================================================================================
    @Override
    public BayResponse handleRequest() {
        StaNld_Bay2.logger.info("S05_sc_nl_Test_at_Bay2 : Entry");
        BayResponse bayResponse = new BayResponse();
        bayResponse.setStatus(true);
        bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);


		Map<String,Object> responseReturn =  scNlTestAtBay2();	 
		boolean scNlTestAtBay2 = (boolean)responseReturn.get("status");
	    
        boolean status = scNlTestAtBay2;  // Call the function to perform SC-NL test at Bay2

        if (status) {
            StaNld_Bay2.logger.info("S05_sc_nl_Test_at_Bay2 : SC-NL Test at Bay2 Successful");
            bayResponse.setStatus(true);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_601);  // Success error code
        } else {
            StaNld_Bay2.logger.info("S05_sc_nl_Test_at_Bay2 : SC-NL Test at Bay2 Failed");
            bayResponse.setStatus(false);
            bayResponse.setErrorCode(ConvErrorCodeMapping.ERROR_CODE_SCT_NLT_BAY2_010);  // Failure error code
        }

        StaNld_Bay2.logger.info("S05_sc_nl_Test_at_Bay2 : Exit");
        return bayResponse;
    }

    //============================================================================================================================================  

    private Map<String,Object> scNlTestAtBay2() {
        StaNld_Bay2.logger.debug("S05_sc_nl_Test_at_Bay2 : Entry");

        boolean status = false;
        Map<String,Object> responseReturn = new HashMap<String,Object>();
		responseReturn.put("status", false);
        

        // Add logic for the SC-NL test at Bay2 here
        // Set 'status' to true if the test is successful, otherwise false
        
        // 1. Start Source 
        // 2. Run Test (Supply V&I) 
        // 3. Stop Source
		
		if(StateExecutorController.simulateSCTNLTBay2HappyPath){
			status = true; 
		}
		 
        StaNld_Bay2.logger.debug("S05_sc_nl_Test_at_Bay2 : status : " + status); 

		responseReturn.put("status", status);
		

        StaNld_Bay2.logger.debug("S05_sc_nl_Test_at_Bay2 : Exit");
        return responseReturn;
    }
}
