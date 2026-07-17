package com.tasnetwork.spring.orm.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.annotation.CreatedDate;

import com.tasnetwork.calibration.energymeter.ApplicationLauncher;

@Entity
@Table(name = "RpManage")
public class ReportProfileManage {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;
    

    
    
    @OneToMany(mappedBy = "rpManage", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    //Set<ReportProfileMeterMetaDataFilter> reportProfileMeterMetaDataFilterList = new HashSet<>();
    List<ReportProfileMeterMetaDataFilter> reportProfileMeterMetaDataFilterList = new ArrayList<ReportProfileMeterMetaDataFilter>();

    
    @OneToMany(mappedBy = "rpManage", cascade = CascadeType.ALL, orphanRemoval = true,fetch=FetchType.EAGER)
    @Fetch(value = FetchMode.SUBSELECT)
    List<ReportProfileTestDataFilter> reportProfileTestDataFilterList = new ArrayList<ReportProfileTestDataFilter>();

    
   // @CreatedBy
   // private User user;

    //@Column(name = "active", columnDefinition = "VARCHAR(3)", nullable = false)
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    //@Convert(converter=BooleanToYNStringConverter.class)
    @Type(type = "yes_no")
    private boolean activeProfile;
   

    
    @Column(/*name = "report_group_id",*/columnDefinition = "VARCHAR(45)")
    private String reportGroupId;
    
    @Column(/*name = "report_group_name",*/columnDefinition = "VARCHAR(45)")
    private String reportGroupName;
    
    @Column(/*name = "report_profile_id",*/columnDefinition = "VARCHAR(45)")
    private String reportProfileId;
    
    @Column(/*name = "report_profile_name",*/columnDefinition = "VARCHAR(45)")
    private String reportProfileName;
    
    @Column(/*name = "base_template_name",*/columnDefinition = "VARCHAR(45)")
    private String baseTemplateName;
    
    @Column(/*name = "base_template_meta_data_populate_type",*/columnDefinition = "VARCHAR(45)")
    private String baseTemplateMetaDataPopulateType;

    @Column(/*name = "base_template_meta_data_populate_type",*/columnDefinition = "VARCHAR(45)")
    private String baseTemplateTestFilterDataPopulateType;

    @Column(columnDefinition = "VARCHAR(500)")
    private String templateFileNameWithPath;
    
    @Column(columnDefinition = "VARCHAR(500)")
    private String outputFolderPath;
    
    @Column(/*name = "base_template_meta_data_populate_type",*/columnDefinition = "VARCHAR(45)")
    private String customerId;
    
    
    
    
    private Integer maxDutDisplayPerPage;
    
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean splitDutDisplayInToMultiplePage;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean dutMainCtSelected;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean dutNeutralCtSelected;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean importModeSelected;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean exportModeSelected;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean appendDutSerialNoAndRackPosition;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean dutMetaDataApplyForAllPages;
    
    @Column( columnDefinition = "VARCHAR(3)", nullable = false)
    @Type(type = "yes_no")
    private boolean dutMetaDataClubPageNoAndMaxNoOfPages;
    
    
    @Column(columnDefinition = "VARCHAR(100)")
    private String parameterProfileName;
    
    
    @Column(columnDefinition = "VARCHAR(100)")
    private String printStyleResult;
    
    @Column(columnDefinition = "VARCHAR(100)")
    private String printStyleGenericHeader;
    
    @Column(columnDefinition = "VARCHAR(100)")
    private String printStyleTableHeader;
    
    public void addDutMetaData(ReportProfileMeterMetaDataFilter inpReportProfileMeterMetaDataFilter){
    	reportProfileMeterMetaDataFilterList.add(inpReportProfileMeterMetaDataFilter);
    	inpReportProfileMeterMetaDataFilter.setReportProfileManage(this);
    }
    
/*    public void upsertMetaData(ReportProfileMeterMetaDataFilter inpReportProfileMeterMetaDataFilter){
    	reportProfileMeterMetaDataFilterList.add(inpReportProfileMeterMetaDataFilter);
    	inpReportProfileMeterMetaDataFilter.setReportProfileManage(this);
    }*/
    public void removeDutMetaData(ReportProfileMeterMetaDataFilter inpReportProfileMeterMetaDataFilter){
    	reportProfileMeterMetaDataFilterList.remove(inpReportProfileMeterMetaDataFilter);
    	inpReportProfileMeterMetaDataFilter.setReportProfileManage(null);
    }
    
    public void updateDutMetaData(int indexValue,ReportProfileMeterMetaDataFilter inpReportProfileMeterMetaDataFilter){
    	reportProfileMeterMetaDataFilterList.set(indexValue, inpReportProfileMeterMetaDataFilter);
    	inpReportProfileMeterMetaDataFilter.setReportProfileManage(this);
   }
    
    public void clearDutMetaDataList(){
    	reportProfileMeterMetaDataFilterList.clear();
    	//inpReportProfileMeterMetaDataFilter.setReportProfileManage(null);
    }
    
    public List<ReportProfileMeterMetaDataFilter> getDutMetaDataList(){
    	return reportProfileMeterMetaDataFilterList;
    	//inpReportProfileMeterMetaDataFilter.setReportProfileManage(null);
    }
    
    public void  setDutMetaDataList(List<ReportProfileMeterMetaDataFilter> inpReportProfileMeterMetaDataFilterList){
    	this.reportProfileMeterMetaDataFilterList = inpReportProfileMeterMetaDataFilterList;
    	//inpReportProfileMeterMetaDataFilter.setReportProfileManage(null);
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

/*	public List<ReportProfileMeterMetaDataFilter> getReportProfileMeterMetaDataFilter() {
		return reportProfileMeterMetaDataFilterList;
	}

	public void setReportProfileMeterMetaDataFilter(
			ArrayList<ReportProfileMeterMetaDataFilter> reportProfileMeterMetaDataFilter) {
		this.reportProfileMeterMetaDataFilterList = reportProfileMeterMetaDataFilter;
	}*/

	public boolean isProfileActive() {
		return activeProfile;
	}

	public void setProfileActive(boolean profileActive) {
		this.activeProfile = profileActive;
	}

	public String getReportGroupId() {
		return reportGroupId;
	}

	public void setReportGroupId(String reportGroupId) {
		this.reportGroupId = reportGroupId;
	}

	public String getReportGroupName() {
		return reportGroupName;
	}

	public void setReportGroupName(String reportGroupName) {
		this.reportGroupName = reportGroupName;
	}

	public String getReportProfileId() {
		return reportProfileId;
	}

	public void setReportProfileId(String reportProfileId) {
		this.reportProfileId = reportProfileId;
	}

	public String getReportProfileName() {
		return reportProfileName;
	}

	public void setReportProfileName(String reportProfileName) {
		this.reportProfileName = reportProfileName;
	}

	public String getBaseTemplateName() {
		return baseTemplateName;
	}

	public void setBaseTemplateName(String baseTemplateName) {
		this.baseTemplateName = baseTemplateName;
	}

	public String getBaseTemplateMetaDataPopulateType() {
		return baseTemplateMetaDataPopulateType;
	}

	public void setBaseTemplateMetaDataPopulateType(String baseTemplateMetaDataPopulateType) {
		this.baseTemplateMetaDataPopulateType = baseTemplateMetaDataPopulateType;
	}

	public String getBaseTemplateTestFilterDataPopulateType() {
		return baseTemplateTestFilterDataPopulateType;
	}

	public void setBaseTemplateTestFilterDataPopulateType(String baseTemplateTestFilterDataPopulateType) {
		this.baseTemplateTestFilterDataPopulateType = baseTemplateTestFilterDataPopulateType;
	}

	public String getTemplateFileNameWithPath() {
		return templateFileNameWithPath;
	}

	public void setTemplateFileNameWithPath(String templateFileNameWithPath) {
		this.templateFileNameWithPath = templateFileNameWithPath;
	}

	public String getOutputFolderPath() {
		return outputFolderPath;
	}

	public void setOutputFolderPath(String outputFolderPath) {
		this.outputFolderPath = outputFolderPath;
	}

	public Integer getMaxDutDisplayPerPage() {
		return maxDutDisplayPerPage;
	}

	public void setMaxDutDisplayPerPage(Integer maxDutDisplayPerPage) {
		this.maxDutDisplayPerPage = maxDutDisplayPerPage;
	}

	public boolean isSplitDutDisplayInToMultiplePage() {
		return splitDutDisplayInToMultiplePage;
	}

	public void setSplitDutDisplayInToMultiplePage(boolean splitDutDisplayInToMultiplePage) {
		this.splitDutDisplayInToMultiplePage = splitDutDisplayInToMultiplePage;
	}

	public boolean isDutMainCtSelected() {
		return dutMainCtSelected;
	}

	public void setDutMainCtSelected(boolean dutMainCtSelected) {
		this.dutMainCtSelected = dutMainCtSelected;
	}

	public boolean isDutNeutralCtSelected() {
		return dutNeutralCtSelected;
	}

	public void setDutNeutralCtSelected(boolean dutNeutralCtSelected) {
		this.dutNeutralCtSelected = dutNeutralCtSelected;
	}

	public boolean isImportModeSelected() {
		return importModeSelected;
	}

	public void setImportModeSelected(boolean importModeSelected) {
		this.importModeSelected = importModeSelected;
	}

	public boolean isExportModeSelected() {
		return exportModeSelected;
	}

	public void setExportModeSelected(boolean expotModeSelected) {
		this.exportModeSelected = expotModeSelected;
	}

	public boolean isAppendDutSerialNoAndRackPosition() {
		return appendDutSerialNoAndRackPosition;
	}

	public void setAppendDutSerialNoAndRackPosition(boolean appendDutSerialNoAndRackPosition) {
		this.appendDutSerialNoAndRackPosition = appendDutSerialNoAndRackPosition;
	}

	public boolean isDutMetaDataApplyForAllPages() {
		return dutMetaDataApplyForAllPages;
	}

	public void setDutMetaDataApplyForAllPages(boolean dutMetaDataApplyForAllPages) {
		this.dutMetaDataApplyForAllPages = dutMetaDataApplyForAllPages;
	}

	public boolean isDutMetaDataClubPageNoAndMaxNoOfPages() {
		return dutMetaDataClubPageNoAndMaxNoOfPages;
	}

	public void setDutMetaDataClubPageNoAndMaxNoOfPages(boolean dutMetaDataClubPageNoAndMaxNoOfPages) {
		this.dutMetaDataClubPageNoAndMaxNoOfPages = dutMetaDataClubPageNoAndMaxNoOfPages;
	}

	public String getCustomerId() {
		return customerId;
	}

	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}

	public List<ReportProfileTestDataFilter> getReportProfileTestDataFilterList() {
		return reportProfileTestDataFilterList;
	}

	public void setReportProfileTestDataFilterList(List<ReportProfileTestDataFilter> reportProfileTestDataFilterList) {
		this.reportProfileTestDataFilterList = reportProfileTestDataFilterList;
	}
	
	
	public void clearTestDataFilterList(){
		reportProfileTestDataFilterList.clear();
    }
	
	public void addTestDataFilter(ReportProfileTestDataFilter inpTestDataFilter){
		reportProfileTestDataFilterList.add(inpTestDataFilter);
		inpTestDataFilter.setReportProfileManage(this);
    }

	public String getParameterProfileName() {
		return parameterProfileName;
	}

	public void setParameterProfileName(String parameterProfileName) {
		this.parameterProfileName = parameterProfileName;
	}

	public String getPrintStyleResult() {
		return printStyleResult;
	}

	public void setPrintStyleResult(String printStyleResult) {
		this.printStyleResult = printStyleResult;
	}

	public String getPrintStyleGenericHeader() {
		return printStyleGenericHeader;
	}

	public void setPrintStyleGenericHeader(String printStyleGenericHeader) {
		this.printStyleGenericHeader = printStyleGenericHeader;
	}

	public String getPrintStyleTableHeader() {
		return printStyleTableHeader;
	}

	public void setPrintStyleTableHeader(String printStyleTableHeader) {
		this.printStyleTableHeader = printStyleTableHeader;
	}


}
