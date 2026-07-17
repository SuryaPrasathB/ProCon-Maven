package com.tasnetwork.calibration.marked_for_delete;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.energymeter.reportprofile.PrintStyle;

public class PrintStyleModel {
    @SerializedName("PrintStyle")
    @Expose
    private List<PrintStyle> printStyle = new ArrayList<PrintStyle>();

    public List<PrintStyle> getPrintStyle() {
        return printStyle;
    }

    public void setPrintStyle(List<PrintStyle> printStyle) {
        this.printStyle = printStyle;
    }

    public PrintStyleModel withPrintStyle(List<PrintStyle> printStyle) {
        this.printStyle = printStyle;
        return this;
    }
}
