package com.tasnetwork.calibration.conveyor.dashboard;

import org.omg.CORBA.portable.IDLEntity;

import javafx.scene.paint.Color;

public enum MeterStatus {
    TESTING(Color.LIGHTYELLOW),
    PASSED(Color.LIMEGREEN),
    FAILED(Color.SALMON),
	IDLE(Color.LIGHTGREY);

    private final Color color;

    MeterStatus(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}