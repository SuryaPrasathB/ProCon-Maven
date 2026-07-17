package com.tasnetwork.calibration.conveyor.bay;

import com.tasnetwork.spring.orm.model.PalletManage;

public class MappedResult {
    private int mappedPosition=1;;
    private  PalletManage selectedPallet=new PalletManage();

    public MappedResult() {
    	
    }
    public MappedResult(int mappedPosition, PalletManage selectedPallet) {
        this.mappedPosition = mappedPosition;
        this.selectedPallet = selectedPallet;
    }

    public int getMappedPosition() {
        return mappedPosition;
    }

    public PalletManage getSelectedPallet() {
        return selectedPallet;
    }
}
