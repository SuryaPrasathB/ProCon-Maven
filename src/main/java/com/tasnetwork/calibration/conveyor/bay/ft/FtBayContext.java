package com.tasnetwork.calibration.conveyor.bay.ft;

import com.tasnetwork.calibration.conveyor.bay.BayResponse;

public class FtBayContext  {
	 
	private FtBayState ftBayState;
	private FtBayState lastProcessedBayState;
	
	public FtBayContext(){
		ftBayState = new S01_check_for_pallet_at_FT_Bay();
	}
	
	public void setState(FtBayState state) {
        this.ftBayState = state;
    }
	
	public FtBayState getState() {
        return this.ftBayState;
    }
	
	 public BayResponse processPresentState() {
		 
		 Ft.logger.debug("FtBayContext: processPresentState: Entry ");
			
		 BayResponse bayResponse = ftBayState.handleRequest();
		 setLastProcessedBayState(getState());
		 return bayResponse;
	     //System.out.println("Fan turned on low.");
	     //fan.setState(new LowState());
		 
	 }

	public FtBayState getLastProcessedBayState() {
		return lastProcessedBayState;
	}

	public void setLastProcessedBayState(FtBayState lastProcessedBayState) {
		this.lastProcessedBayState = lastProcessedBayState;
	}
	 
	}