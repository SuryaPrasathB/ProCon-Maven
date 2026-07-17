package com.tasnetwork.calibration.energymeter.reportprofile;

import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tasnetwork.calibration.energymeter.constant.ConstantReportV2;

@Generated("jsonschema2pojo")
public class PrintStyle {


    @SerializedName("ReportPrintStyleId")
    @Expose
    private Integer reportPrintStyleId;
    @SerializedName("ReportPrintStyleName")
    @Expose
    private String reportPrintStyleName;
    @SerializedName("FontSize")
    @Expose
    private int fontSize;
    @SerializedName("FontName")
    @Expose
    private String fontName;
    @SerializedName("WrapText")
    @Expose
    private Boolean wrapText = false;
    @SerializedName("Bold")
    @Expose
    private Boolean bold = false;
    @SerializedName("HorizontalAlignmentCentre")
    @Expose
    private Boolean horizontalAlignmentCentre;
    
    @SerializedName("VerticalAlignmentCentre")
    @Expose
    private Boolean verticalAlignmentCentre;
    
    @SerializedName("Border")
    @Expose
    private Boolean border = false;
    @SerializedName("ResultValuePassColorHighLightEnabled")
    @Expose
    private Boolean resultValuePassColorHighLightEnabled = false;
    @SerializedName("ResultValueFailColorHighLightEnabled")
    @Expose
    private Boolean resultValueFailColorHighLightEnabled = false;
    @SerializedName("ResultValuePassTextFailedColor")
    @Expose
    private String resultValuePassTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultValuePassBackGroundFailedColor")
    @Expose
    private String resultValuePassBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultValueFailTextFailedColor")
    @Expose
    private String resultValueFailTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultValueFailBackGroundFailedColor")
    @Expose
    private String resultValueFailBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultValueFailBackGroundFillPattern")
    @Expose
    private String resultValueFailBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    
    @SerializedName("ResultValueErrorColorHighLightEnabled")
    @Expose
    private boolean resultValueErrorColorHighLightEnabled = false;
    
    @SerializedName("ResultValueErrorTextFailedColor")
    @Expose
    private String resultValueErrorTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultValueErrorBackGroundFailedColor")
    @Expose
    private String resultValueErrorBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultValueErrorBackGroundFillPattern")
    @Expose
    private String resultValueErrorBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    
    
    
    @SerializedName("ResultStatusPassColorHighLightEnabled")
    @Expose
    private Boolean resultStatusPassColorHighLightEnabled = false;
    @SerializedName("ResultStatusFailColorHighLightEnabled")
    @Expose
    private Boolean resultStatusFailColorHighLightEnabled = false;
    @SerializedName("ResultStatusPassTextFailedColor")
    @Expose
    private String resultStatusPassTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultStatusPassBackGroundFailedColor")
    @Expose
    private String resultStatusPassBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultStatusFailTextFailedColor")
    @Expose
    private String resultStatusFailTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultStatusFailBackGroundFailedColor")
    @Expose
    private String resultStatusFailBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultStatusFailBackGroundFillPattern")
    @Expose
    private String resultStatusFailBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    
    
    @SerializedName("ResultStatusErrorColorHighLightEnabled")
    @Expose
    private boolean resultStatusErrorColorHighLightEnabled = false;
    
    @SerializedName("ResultStatusErrorTextFailedColor")
    @Expose
    private String resultStatusErrorTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultStatusErrorBackGroundFailedColor")
    @Expose
    private String resultStatusErrorBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultStatusErrorBackGroundFillPattern")
    @Expose
    private String resultStatusErrorBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    
    
    
    @SerializedName("ResultPageStatusPassColorHighLightEnabled")
    @Expose
    private Boolean resultPageStatusPassColorHighLightEnabled = false;
    @SerializedName("ResultPageStatusFailColorHighLightEnabled")
    @Expose
    private Boolean resultPageStatusFailColorHighLightEnabled = false;
    @SerializedName("ResultPageStatusPassTextFailedColor")
    @Expose
    private String resultPageStatusPassTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultPageStatusPassBackGroundFailedColor")
    @Expose
    private String resultPageStatusPassBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultPageStatusFailTextFailedColor")
    @Expose
    private String resultPageStatusFailTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultPageStatusFailBackGroundFailedColor")
    @Expose
    private String resultPageStatusFailBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultOverAllStatusPassColorHighLightEnabled")
    @Expose
    private Boolean resultOverAllStatusPassColorHighLightEnabled = false;
    @SerializedName("ResultOverAllStatusFailColorHighLightEnabled")
    @Expose
    private Boolean resultOverAllStatusFailColorHighLightEnabled = false;
    @SerializedName("ResultOverAllStatusPassTextFailedColor")
    @Expose
    private String resultOverAllStatusPassTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultOverAllStatusPassBackGroundFailedColor")
    @Expose
    private String resultOverAllStatusPassBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultOverAllStatusFailTextFailedColor")
    @Expose
    private String resultOverAllStatusFailTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    @SerializedName("ResultOverAllStatusFailBackGroundFailedColor")
    @Expose
    private String resultOverAllStatusFailBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    
    
        
    
    @SerializedName("ResultPageStatusFailBackGroundFillPattern")
    @Expose
    private String resultPageStatusFailBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultPageStatusErrorTextFailedColor")
    @Expose
    private String resultPageStatusErrorTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultPageStatusErrorBackGroundFailedColor")
    @Expose
    private String resultPageStatusErrorBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultPageStatusErrorBackGroundFillPattern")
    @Expose
    private String resultPageStatusErrorBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultOverAllStatusFailBackGroundFillPattern")
    @Expose
    private String resultOverAllStatusFailBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultOverAllStatusErrorTextFailedColor")
    @Expose
    private String resultOverAllStatusErrorTextFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultOverAllStatusErrorBackGroundFailedColor")
    @Expose
    private String resultOverAllStatusErrorBackGroundFailedColor = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultOverAllStatusErrorBackGroundFillPattern")
    @Expose
    private String resultOverAllStatusErrorBackGroundFillPattern = ConstantReportV2.NONE_DISPLAYED;
    
    @SerializedName("ResultPageStatusErrorColorHighLightEnabled")
    @Expose
    private boolean resultPageStatusErrorColorHighLightEnabled = false;
    
    @SerializedName("ResultOverAllStatusErrorColorHighLightEnabled")
    @Expose
    private boolean resultOverAllStatusErrorColorHighLightEnabled = false;
    
    
  

    

    public Integer getReportPrintStyleId() {
        return reportPrintStyleId;
    }

    public void setReportPrintStyleId(Integer reportPrintStyleId) {
        this.reportPrintStyleId = reportPrintStyleId;
    }

    public PrintStyle withReportPrintStyleId(Integer reportPrintStyleId) {
        this.reportPrintStyleId = reportPrintStyleId;
        return this;
    }

    public String getReportPrintStyleName() {
        return reportPrintStyleName;
    }

    public void setReportPrintStyleName(String reportPrintStyleName) {
        this.reportPrintStyleName = reportPrintStyleName;
    }

    public PrintStyle withReportPrintStyleName(String reportPrintStyleName) {
        this.reportPrintStyleName = reportPrintStyleName;
        return this;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public PrintStyle withFontSize(Integer fontSize) {
        this.fontSize = fontSize;
        return this;
    }

    public String getFontName() {
        return fontName;
    }

    public void setFontName(String fontName) {
        this.fontName = fontName;
    }

    public PrintStyle withFontName(String fontName) {
        this.fontName = fontName;
        return this;
    }

    public Boolean isWrapText() {
        return wrapText;
    }

    public void setWrapText(Boolean wrap) {
        this.wrapText = wrap;
    }

    public PrintStyle withWrapText(Boolean wrap) {
        this.wrapText = wrap;
        return this;
    }

    public Boolean isBold() {
        return bold;
    }

    public void setBold(Boolean bold) {
        this.bold = bold;
    }

    public PrintStyle withBold(Boolean bold) {
        this.bold = bold;
        return this;
    }

    public Boolean isHorizontalAlignmentCentre() {
        return horizontalAlignmentCentre;
    }

    public void setHorizontalAlignmentCentre(Boolean alignment) {
        this.horizontalAlignmentCentre = alignment;
    }

    public PrintStyle withHorizontalAlignmentCentre(Boolean alignment) {
        this.horizontalAlignmentCentre = alignment;
        return this;
    }
    
    
    public Boolean isVerticalAlignmentCentre() {
        return verticalAlignmentCentre;
    }

    public void setVerticalAlignmentCentre(Boolean alignment) {
        this.verticalAlignmentCentre = alignment;
    }

    public PrintStyle withVerticalAlignmentCentre(Boolean alignment) {
        this.verticalAlignmentCentre = alignment;
        return this;
    }

    public Boolean getResultValuePassColorHighLightEnabled() {
        return resultValuePassColorHighLightEnabled;
    }

    public void setResultValuePassColorHighLightEnabled(Boolean resultValuePassColorHighLightEnabled) {
        this.resultValuePassColorHighLightEnabled = resultValuePassColorHighLightEnabled;
    }

    public PrintStyle withResultValuePassColorHighLightEnabled(Boolean resultValuePassColorHighLightEnabled) {
        this.resultValuePassColorHighLightEnabled = resultValuePassColorHighLightEnabled;
        return this;
    }

    public Boolean getResultValueFailColorHighLightEnabled() {
        return resultValueFailColorHighLightEnabled;
    }

    public void setResultValueFailColorHighLightEnabled(Boolean resultValueFailColorHighLightEnabled) {
        this.resultValueFailColorHighLightEnabled = resultValueFailColorHighLightEnabled;
    }

    public PrintStyle withResultValueFailColorHighLightEnabled(Boolean resultValueFailColorHighLightEnabled) {
        this.resultValueFailColorHighLightEnabled = resultValueFailColorHighLightEnabled;
        return this;
    }

    public String getResultValuePassTextFailedColor() {
        return resultValuePassTextFailedColor;
    }

    public void setResultValuePassTextFailedColor(String resultValuePassTextFailedColor) {
        this.resultValuePassTextFailedColor = resultValuePassTextFailedColor;
    }

    public PrintStyle withResultValuePassTextFailedColor(String resultValuePassTextFailedColor) {
        this.resultValuePassTextFailedColor = resultValuePassTextFailedColor;
        return this;
    }

    public String getResultValuePassBackGroundFailedColor() {
        return resultValuePassBackGroundFailedColor;
    }

    public void setResultValuePassBackGroundFailedColor(String resultValuePassBackGroundFailedColor) {
        this.resultValuePassBackGroundFailedColor = resultValuePassBackGroundFailedColor;
    }

    public PrintStyle withResultValuePassBackGroundFailedColor(String resultValuePassBackGroundFailedColor) {
        this.resultValuePassBackGroundFailedColor = resultValuePassBackGroundFailedColor;
        return this;
    }

    public String getResultValueFailTextFailedColor() {
        return resultValueFailTextFailedColor;
    }

    public void setResultValueFailTextFailedColor(String resultValueFailTextFailedColor) {
        this.resultValueFailTextFailedColor = resultValueFailTextFailedColor;
    }

    public PrintStyle withResultValueFailTextFailedColor(String resultValueFailTextFailedColor) {
        this.resultValueFailTextFailedColor = resultValueFailTextFailedColor;
        return this;
    }

    public String getResultValueFailBackGroundFailedColor() {
        return resultValueFailBackGroundFailedColor;
    }

    public void setResultValueFailBackGroundFailedColor(String resultValueFailBackGroundFailedColor) {
        this.resultValueFailBackGroundFailedColor = resultValueFailBackGroundFailedColor;
    }

    public PrintStyle withResultValueFailBackGroundFailedColor(String resultValueFailBackGroundFailedColor) {
        this.resultValueFailBackGroundFailedColor = resultValueFailBackGroundFailedColor;
        return this;
    }

    public Boolean getResultStatusPassColorHighLightEnabled() {
        return resultStatusPassColorHighLightEnabled;
    }

    public void setResultStatusPassColorHighLightEnabled(Boolean resultStatusPassColorHighLightEnabled) {
        this.resultStatusPassColorHighLightEnabled = resultStatusPassColorHighLightEnabled;
    }

    public PrintStyle withResultStatusPassColorHighLightEnabled(Boolean resultStatusPassColorHighLightEnabled) {
        this.resultStatusPassColorHighLightEnabled = resultStatusPassColorHighLightEnabled;
        return this;
    }

    public Boolean getResultStatusFailColorHighLightEnabled() {
        return resultStatusFailColorHighLightEnabled;
    }

    public void setResultStatusFailColorHighLightEnabled(Boolean resultStatusFailColorHighLightEnabled) {
        this.resultStatusFailColorHighLightEnabled = resultStatusFailColorHighLightEnabled;
    }

    public PrintStyle withResultStatusFailColorHighLightEnabled(Boolean resultStatusFailColorHighLightEnabled) {
        this.resultStatusFailColorHighLightEnabled = resultStatusFailColorHighLightEnabled;
        return this;
    }

    public String getResultStatusPassTextFailedColor() {
        return resultStatusPassTextFailedColor;
    }

    public void setResultStatusPassTextFailedColor(String resultStatusPassTextFailedColor) {
        this.resultStatusPassTextFailedColor = resultStatusPassTextFailedColor;
    }

    public PrintStyle withResultStatusPassTextFailedColor(String resultStatusPassTextFailedColor) {
        this.resultStatusPassTextFailedColor = resultStatusPassTextFailedColor;
        return this;
    }

    public String getResultStatusPassBackGroundFailedColor() {
        return resultStatusPassBackGroundFailedColor;
    }

    public void setResultStatusPassBackGroundFailedColor(String resultStatusPassBackGroundFailedColor) {
        this.resultStatusPassBackGroundFailedColor = resultStatusPassBackGroundFailedColor;
    }

    public PrintStyle withResultStatusPassBackGroundFailedColor(String resultStatusPassBackGroundFailedColor) {
        this.resultStatusPassBackGroundFailedColor = resultStatusPassBackGroundFailedColor;
        return this;
    }

    public String getResultStatusFailTextFailedColor() {
        return resultStatusFailTextFailedColor;
    }

    public void setResultStatusFailTextFailedColor(String resultStatusFailTextFailedColor) {
        this.resultStatusFailTextFailedColor = resultStatusFailTextFailedColor;
    }

    public PrintStyle withResultStatusFailTextFailedColor(String resultStatusFailTextFailedColor) {
        this.resultStatusFailTextFailedColor = resultStatusFailTextFailedColor;
        return this;
    }

    public String getResultStatusFailBackGroundFailedColor() {
        return resultStatusFailBackGroundFailedColor;
    }

    public void setResultStatusFailBackGroundFailedColor(String resultStatusFailBackGroundFailedColor) {
        this.resultStatusFailBackGroundFailedColor = resultStatusFailBackGroundFailedColor;
    }

    public PrintStyle withResultStatusFailBackGroundFailedColor(String resultStatusFailBackGroundFailedColor) {
        this.resultStatusFailBackGroundFailedColor = resultStatusFailBackGroundFailedColor;
        return this;
    }

    public Boolean getResultPageStatusPassColorHighLightEnabled() {
        return resultPageStatusPassColorHighLightEnabled;
    }

    public void setResultPageStatusPassColorHighLightEnabled(Boolean resultPageStatusPassColorHighLightEnabled) {
        this.resultPageStatusPassColorHighLightEnabled = resultPageStatusPassColorHighLightEnabled;
    }

    public PrintStyle withResultPageStatusPassColorHighLightEnabled(Boolean resultPageStatusPassColorHighLightEnabled) {
        this.resultPageStatusPassColorHighLightEnabled = resultPageStatusPassColorHighLightEnabled;
        return this;
    }

    public Boolean getResultPageStatusFailColorHighLightEnabled() {
        return resultPageStatusFailColorHighLightEnabled;
    }

    public void setResultPageStatusFailColorHighLightEnabled(Boolean resultPageStatusFailColorHighLightEnabled) {
        this.resultPageStatusFailColorHighLightEnabled = resultPageStatusFailColorHighLightEnabled;
    }

    public PrintStyle withResultPageStatusFailColorHighLightEnabled(Boolean resultPageStatusFailColorHighLightEnabled) {
        this.resultPageStatusFailColorHighLightEnabled = resultPageStatusFailColorHighLightEnabled;
        return this;
    }

    public String getResultPageStatusPassTextFailedColor() {
        return resultPageStatusPassTextFailedColor;
    }

    public void setResultPageStatusPassTextFailedColor(String resultPageStatusPassTextFailedColor) {
        this.resultPageStatusPassTextFailedColor = resultPageStatusPassTextFailedColor;
    }

    public PrintStyle withResultPageStatusPassTextFailedColor(String resultPageStatusPassTextFailedColor) {
        this.resultPageStatusPassTextFailedColor = resultPageStatusPassTextFailedColor;
        return this;
    }

    public String getResultPageStatusPassBackGroundFailedColor() {
        return resultPageStatusPassBackGroundFailedColor;
    }

    public void setResultPageStatusPassBackGroundFailedColor(String resultPageStatusPassBackGroundFailedColor) {
        this.resultPageStatusPassBackGroundFailedColor = resultPageStatusPassBackGroundFailedColor;
    }

    public PrintStyle withResultPageStatusPassBackGroundFailedColor(String resultPageStatusPassBackGroundFailedColor) {
        this.resultPageStatusPassBackGroundFailedColor = resultPageStatusPassBackGroundFailedColor;
        return this;
    }

    public String getResultPageStatusFailTextFailedColor() {
        return resultPageStatusFailTextFailedColor;
    }

    public void setResultPageStatusFailTextFailedColor(String resultPageStatusFailTextFailedColor) {
        this.resultPageStatusFailTextFailedColor = resultPageStatusFailTextFailedColor;
    }

    public PrintStyle withResultPageStatusFailTextFailedColor(String resultPageStatusFailTextFailedColor) {
        this.resultPageStatusFailTextFailedColor = resultPageStatusFailTextFailedColor;
        return this;
    }

    public String getResultPageStatusFailBackGroundFailedColor() {
        return resultPageStatusFailBackGroundFailedColor;
    }

    public void setResultPageStatusFailBackGroundFailedColor(String resultPageStatusFailBackGroundFailedColor) {
        this.resultPageStatusFailBackGroundFailedColor = resultPageStatusFailBackGroundFailedColor;
    }

    public PrintStyle withResultPageStatusFailBackGroundFailedColor(String resultPageStatusFailBackGroundFailedColor) {
        this.resultPageStatusFailBackGroundFailedColor = resultPageStatusFailBackGroundFailedColor;
        return this;
    }

    public Boolean getResultOverAllStatusPassColorHighLightEnabled() {
        return resultOverAllStatusPassColorHighLightEnabled;
    }

    public void setResultOverAllStatusPassColorHighLightEnabled(Boolean resultOverAllStatusPassColorHighLightEnabled) {
        this.resultOverAllStatusPassColorHighLightEnabled = resultOverAllStatusPassColorHighLightEnabled;
    }

    public PrintStyle withResultOverAllStatusPassColorHighLightEnabled(Boolean resultOverAllStatusPassColorHighLightEnabled) {
        this.resultOverAllStatusPassColorHighLightEnabled = resultOverAllStatusPassColorHighLightEnabled;
        return this;
    }

    public Boolean getResultOverAllStatusFailColorHighLightEnabled() {
        return resultOverAllStatusFailColorHighLightEnabled;
    }

    public void setResultOverAllStatusFailColorHighLightEnabled(Boolean resultOverAllStatusFailColorHighLightEnabled) {
        this.resultOverAllStatusFailColorHighLightEnabled = resultOverAllStatusFailColorHighLightEnabled;
    }

    public PrintStyle withResultOverAllStatusFailColorHighLightEnabled(Boolean resultOverAllStatusFailColorHighLightEnabled) {
        this.resultOverAllStatusFailColorHighLightEnabled = resultOverAllStatusFailColorHighLightEnabled;
        return this;
    }

    public String getResultOverAllStatusPassTextFailedColor() {
        return resultOverAllStatusPassTextFailedColor;
    }

    public void setResultOverAllStatusPassTextFailedColor(String resultOverAllStatusPassTextFailedColor) {
        this.resultOverAllStatusPassTextFailedColor = resultOverAllStatusPassTextFailedColor;
    }

    public PrintStyle withResultOverAllStatusPassTextFailedColor(String resultOverAllStatusPassTextFailedColor) {
        this.resultOverAllStatusPassTextFailedColor = resultOverAllStatusPassTextFailedColor;
        return this;
    }

    public String getResultOverAllStatusPassBackGroundFailedColor() {
        return resultOverAllStatusPassBackGroundFailedColor;
    }

    public void setResultOverAllStatusPassBackGroundFailedColor(String resultOverAllStatusPassBackGroundFailedColor) {
        this.resultOverAllStatusPassBackGroundFailedColor = resultOverAllStatusPassBackGroundFailedColor;
    }

    public PrintStyle withResultOverAllStatusPassBackGroundFailedColor(String resultOverAllStatusPassBackGroundFailedColor) {
        this.resultOverAllStatusPassBackGroundFailedColor = resultOverAllStatusPassBackGroundFailedColor;
        return this;
    }

    public String getResultOverAllStatusFailTextFailedColor() {
        return resultOverAllStatusFailTextFailedColor;
    }

    public void setResultOverAllStatusFailTextFailedColor(String resultOverAllStatusFailTextFailedColor) {
        this.resultOverAllStatusFailTextFailedColor = resultOverAllStatusFailTextFailedColor;
    }

    public PrintStyle withResultOverAllStatusFailTextFailedColor(String resultOverAllStatusFailTextFailedColor) {
        this.resultOverAllStatusFailTextFailedColor = resultOverAllStatusFailTextFailedColor;
        return this;
    }

    public String getResultOverAllStatusFailBackGroundFailedColor() {
        return resultOverAllStatusFailBackGroundFailedColor;
    }

    public void setResultOverAllStatusFailBackGroundFailedColor(String resultOverAllStatusFailBackGroundFailedColor) {
        this.resultOverAllStatusFailBackGroundFailedColor = resultOverAllStatusFailBackGroundFailedColor;
    }

    public PrintStyle withResultOverAllStatusFailBackGroundFailedColor(String resultOverAllStatusFailBackGroundFailedColor) {
        this.resultOverAllStatusFailBackGroundFailedColor = resultOverAllStatusFailBackGroundFailedColor;
        return this;
    }

	public Boolean isBorder() {
        return border;
	}

    public void setBorder(Boolean border) {
        this.border = border;
    }

    public PrintStyle withBorder(Boolean border) {
        this.border = border;
        return this;
    }

	public String getResultPageStatusFailBackGroundFillPattern() {
		return resultPageStatusFailBackGroundFillPattern;
	}

	public String getResultPageStatusErrorTextFailedColor() {
		return resultPageStatusErrorTextFailedColor;
	}

	public String getResultPageStatusErrorBackGroundFailedColor() {
		return resultPageStatusErrorBackGroundFailedColor;
	}

	public String getResultPageStatusErrorBackGroundFillPattern() {
		return resultPageStatusErrorBackGroundFillPattern;
	}

	public String getResultOverAllStatusFailBackGroundFillPattern() {
		return resultOverAllStatusFailBackGroundFillPattern;
	}

	public String getResultOverAllStatusErrorTextFailedColor() {
		return resultOverAllStatusErrorTextFailedColor;
	}

	public String getResultOverAllStatusErrorBackGroundFailedColor() {
		return resultOverAllStatusErrorBackGroundFailedColor;
	}

	public String getResultOverAllStatusErrorBackGroundFillPattern() {
		return resultOverAllStatusErrorBackGroundFillPattern;
	}

	public boolean isResultPageStatusErrorColorHighLightEnabled() {
		return resultPageStatusErrorColorHighLightEnabled;
	}

	public boolean isResultOverAllStatusErrorColorHighLightEnabled() {
		return resultOverAllStatusErrorColorHighLightEnabled;
	}

	public void setResultPageStatusFailBackGroundFillPattern(String resultPageStatusFailBackGroundFillPattern) {
		this.resultPageStatusFailBackGroundFillPattern = resultPageStatusFailBackGroundFillPattern;
	}

	public void setResultPageStatusErrorTextFailedColor(String resultPageStatusErrorTextFailedColor) {
		this.resultPageStatusErrorTextFailedColor = resultPageStatusErrorTextFailedColor;
	}

	public void setResultPageStatusErrorBackGroundFailedColor(String resultPageStatusErrorBackGroundFailedColor) {
		this.resultPageStatusErrorBackGroundFailedColor = resultPageStatusErrorBackGroundFailedColor;
	}

	public void setResultPageStatusErrorBackGroundFillPattern(String resultPageStatusErrorBackGroundFillPattern) {
		this.resultPageStatusErrorBackGroundFillPattern = resultPageStatusErrorBackGroundFillPattern;
	}

	public void setResultOverAllStatusFailBackGroundFillPattern(String resultOverAllStatusFailBackGroundFillPattern) {
		this.resultOverAllStatusFailBackGroundFillPattern = resultOverAllStatusFailBackGroundFillPattern;
	}

	public void setResultOverAllStatusErrorTextFailedColor(String resultOverAllStatusErrorTextFailedColor) {
		this.resultOverAllStatusErrorTextFailedColor = resultOverAllStatusErrorTextFailedColor;
	}

	public void setResultOverAllStatusErrorBackGroundFailedColor(String resultOverAllStatusErrorBackGroundFailedColor) {
		this.resultOverAllStatusErrorBackGroundFailedColor = resultOverAllStatusErrorBackGroundFailedColor;
	}

	public void setResultOverAllStatusErrorBackGroundFillPattern(String resultOverAllStatusErrorBackGroundFillPattern) {
		this.resultOverAllStatusErrorBackGroundFillPattern = resultOverAllStatusErrorBackGroundFillPattern;
	}

	public void setResultPageStatusErrorColorHighLightEnabled(boolean resultPageStatusErrorColorHighLightEnabled) {
		this.resultPageStatusErrorColorHighLightEnabled = resultPageStatusErrorColorHighLightEnabled;
	}

	public void setResultOverAllStatusErrorColorHighLightEnabled(boolean resultOverAllStatusErrorColorHighLightEnabled) {
		this.resultOverAllStatusErrorColorHighLightEnabled = resultOverAllStatusErrorColorHighLightEnabled;
	}

	public String getResultValueFailBackGroundFillPattern() {
		return resultValueFailBackGroundFillPattern;
	}

	public void setResultValueFailBackGroundFillPattern(String resultValueFailBackGroundFillPattern) {
		this.resultValueFailBackGroundFillPattern = resultValueFailBackGroundFillPattern;
	}

	public String getResultStatusFailBackGroundFillPattern() {
		return resultStatusFailBackGroundFillPattern;
	}

	public void setResultStatusFailBackGroundFillPattern(String resultStatusFailBackGroundFillPattern) {
		this.resultStatusFailBackGroundFillPattern = resultStatusFailBackGroundFillPattern;
	}

	public boolean isResultValueErrorColorHighLightEnabled() {
		return resultValueErrorColorHighLightEnabled;
	}

	public String getResultValueErrorTextFailedColor() {
		return resultValueErrorTextFailedColor;
	}

	public String getResultValueErrorBackGroundFailedColor() {
		return resultValueErrorBackGroundFailedColor;
	}

	public String getResultValueErrorBackGroundFillPattern() {
		return resultValueErrorBackGroundFillPattern;
	}

	public boolean isResultStatusErrorColorHighLightEnabled() {
		return resultStatusErrorColorHighLightEnabled;
	}

	public String getResultStatusErrorTextFailedColor() {
		return resultStatusErrorTextFailedColor;
	}

	public String getResultStatusErrorBackGroundFailedColor() {
		return resultStatusErrorBackGroundFailedColor;
	}

	public String getResultStatusErrorBackGroundFillPattern() {
		return resultStatusErrorBackGroundFillPattern;
	}

	public void setResultValueErrorColorHighLightEnabled(boolean resultValueErrorColorHighLightEnabled) {
		this.resultValueErrorColorHighLightEnabled = resultValueErrorColorHighLightEnabled;
	}

	public void setResultValueErrorTextFailedColor(String resultValueErrorTextFailedColor) {
		this.resultValueErrorTextFailedColor = resultValueErrorTextFailedColor;
	}

	public void setResultValueErrorBackGroundFailedColor(String resultValueErrorBackGroundFailedColor) {
		this.resultValueErrorBackGroundFailedColor = resultValueErrorBackGroundFailedColor;
	}

	public void setResultValueErrorBackGroundFillPattern(String resultValueErrorBackGroundFillPattern) {
		this.resultValueErrorBackGroundFillPattern = resultValueErrorBackGroundFillPattern;
	}

	public void setResultStatusErrorColorHighLightEnabled(boolean resultStatusErrorColorHighLightEnabled) {
		this.resultStatusErrorColorHighLightEnabled = resultStatusErrorColorHighLightEnabled;
	}

	public void setResultStatusErrorTextFailedColor(String resultStatusErrorTextFailedColor) {
		this.resultStatusErrorTextFailedColor = resultStatusErrorTextFailedColor;
	}

	public void setResultStatusErrorBackGroundFailedColor(String resultStatusErrorBackGroundFailedColor) {
		this.resultStatusErrorBackGroundFailedColor = resultStatusErrorBackGroundFailedColor;
	}

	public void setResultStatusErrorBackGroundFillPattern(String resultStatusErrorBackGroundFillPattern) {
		this.resultStatusErrorBackGroundFillPattern = resultStatusErrorBackGroundFillPattern;
	}


}
