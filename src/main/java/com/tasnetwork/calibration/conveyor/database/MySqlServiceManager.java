package com.tasnetwork.calibration.conveyor.database;


import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantVersion;
import com.tasnetwork.spring.orm.service.BayDeviceConfigService;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsService;
import com.tasnetwork.spring.orm.service.ConveyorOutputMetricsSummaryService;
import com.tasnetwork.spring.orm.service.DeviceSettingService;
import com.tasnetwork.spring.orm.service.DutCommandService;
import com.tasnetwork.spring.orm.service.OperationParamService;
import com.tasnetwork.spring.orm.service.OperationProcessService;
import com.tasnetwork.spring.orm.service.PalletBayStateService;
import com.tasnetwork.spring.orm.service.PalletManageService;
import com.tasnetwork.spring.orm.service.PalletMeterArchivedResultsService;
import com.tasnetwork.spring.orm.service.PalletMeterResultsService;
import com.tasnetwork.spring.orm.service.PalletMeterService;
import com.tasnetwork.spring.orm.service.ReportProfileManageService;
import com.tasnetwork.spring.orm.service.ReportProfileMeterMetaDataFilterService;
import com.tasnetwork.spring.orm.service.ReportProfileTestDataFilterService;
import com.tasnetwork.spring.orm.service.ResultSummaryService;
import com.tasnetwork.spring.orm.service.RpPrintPositionService;
import com.tasnetwork.spring.orm.service.StateFlowService;
import com.tasnetwork.spring.orm.service.TerminalProfileSettingService;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class MySqlServiceManager {

	public static OperationParamService rpOperationParamService = null ;
	
	public static ResultSummaryService resultSummaryService = null ;
	
	public static ConveyorOutputMetricsService conveyorOutputMetricsService = null;
	
	public static ConveyorOutputMetricsSummaryService conveyorOutputMetricsSummaryService = null;
	
	public static DeviceSettingService deviceSettingService = null ;
	
	public static StateFlowService stateFlowService = null ;
	
	public static TerminalProfileSettingService terminalProfileSettingService = null;
	
	public static BayDeviceConfigService bayDeviceConfigService = null;
	
	public static PalletManageService palletManageService = null;
	
	public static PalletMeterService palletMeterService = null;
	
	public static PalletMeterArchivedResultsService palletMeterArchivedResultsService = null;
	
	public static PalletMeterResultsService palletMeterResultsService = null;
	
	public static PalletBayStateService palletBayStateService = null;
	
	public static DutCommandService dutCommandService = null ;
	
	
/*	public static ClassPathXmlApplicationContext springAppCtx = ConstantVersion.springAppContext;
	
	public static ClassPathXmlApplicationContext getSpringAppCtx() {
		return springAppCtx;
	}


	public static void setSpringAppCtx(ClassPathXmlApplicationContext springAppCtx) {
		MySqlServiceManager.springAppCtx = springAppCtx;
	}*/
	
	static public void springDataInit(){
		ApplicationLauncher.logger.info("springDataInit : Entry" );

		rpOperationParamService = ApplicationLauncher.springContext.getBean(OperationParamService.class);		
		resultSummaryService = ApplicationLauncher.springContext.getBean(ResultSummaryService.class);
		conveyorOutputMetricsService = ApplicationLauncher.springContext.getBean(ConveyorOutputMetricsService.class);
		conveyorOutputMetricsSummaryService = ApplicationLauncher.springContext.getBean(ConveyorOutputMetricsSummaryService.class);
		deviceSettingService = ApplicationLauncher.springContext.getBean(DeviceSettingService.class);
		stateFlowService = ApplicationLauncher.springContext.getBean(StateFlowService.class);
		terminalProfileSettingService = ApplicationLauncher.springContext.getBean(TerminalProfileSettingService.class);
		bayDeviceConfigService = ApplicationLauncher.springContext.getBean(BayDeviceConfigService.class);
		palletManageService = ApplicationLauncher.springContext.getBean(PalletManageService.class);
		palletMeterService = ApplicationLauncher.springContext.getBean(PalletMeterService.class);
		palletMeterResultsService = ApplicationLauncher.springContext.getBean(PalletMeterResultsService.class);
		palletMeterArchivedResultsService = ApplicationLauncher.springContext.getBean(PalletMeterArchivedResultsService.class);
		
		palletBayStateService = ApplicationLauncher.springContext.getBean(PalletBayStateService.class);
		dutCommandService = ApplicationLauncher.springContext.getBean(DutCommandService.class);
	}
	
	
	

	// G E T T E R S  A N D  S E T T E R S ========================================================
	
	public static OperationParamService getRpOperationParamService() {
		return rpOperationParamService;
	}

	public static void setRpOperationParamService(OperationParamService rpOperationParamService) {
		MySqlServiceManager.rpOperationParamService = rpOperationParamService;
	}


	public static ResultSummaryService getResultSummaryService() {
		return resultSummaryService;
	}


	public static void setResultSummaryService(ResultSummaryService resultSummaryService) {
		MySqlServiceManager.resultSummaryService = resultSummaryService;
	}


	public static DeviceSettingService getDeviceSettingService() {
		return deviceSettingService;
	}


	public static void setDeviceSettingService(DeviceSettingService deviceSettingService) {
		MySqlServiceManager.deviceSettingService = deviceSettingService;
	}


	public static StateFlowService getStateFlowService() {
		return stateFlowService;
	}


	public static void setStateFlowService(StateFlowService stateFlowService) {
		MySqlServiceManager.stateFlowService = stateFlowService;
	}


	public static TerminalProfileSettingService getTerminalProfileSettingService() {
		return terminalProfileSettingService;
	}


	public static void setTerminalProfileSettingService(TerminalProfileSettingService terminalProfileSettingService) {
		MySqlServiceManager.terminalProfileSettingService = terminalProfileSettingService;
	}


	public static BayDeviceConfigService getBayDeviceConfigService() {
		return bayDeviceConfigService;
	}


	public static void setBayDeviceConfigService(BayDeviceConfigService bayDeviceConfigService) {
		MySqlServiceManager.bayDeviceConfigService = bayDeviceConfigService;
	}
	
	public static PalletManageService getPalletManageService() {
		return palletManageService;
	}


	public static void setPalletManageService(PalletManageService palletManageService) {
		MySqlServiceManager.palletManageService = palletManageService;
	}


	public static PalletMeterService getPalletMeterService() {
		return palletMeterService;
	}


	public static void setPalletMeterService(PalletMeterService palletMeterService) {
		MySqlServiceManager.palletMeterService = palletMeterService;
	}


	public static PalletBayStateService getPalletBayStateService() {
		return palletBayStateService;
	}


	public static void setPalletBayStateService(PalletBayStateService palletBayStateService) {
		MySqlServiceManager.palletBayStateService = palletBayStateService;
	}


	public static DutCommandService getDutCommandService() {
		return dutCommandService;
	}


	public static void setDutCommandService(DutCommandService dutCommandService) {
		MySqlServiceManager.dutCommandService = dutCommandService;
	}


	public static ConveyorOutputMetricsService getConveyorOutputMetricsService() {
		return conveyorOutputMetricsService;
	}


	public static void setConveyorOutputMetricsService(ConveyorOutputMetricsService conveyorOutputMetricsService) {
		MySqlServiceManager.conveyorOutputMetricsService = conveyorOutputMetricsService;
	}


	public static PalletMeterResultsService getPalletMeterResultsService() {
		return palletMeterResultsService;
	}


	public static void setPalletMeterResultsService(PalletMeterResultsService palletMeterResultsService) {
		MySqlServiceManager.palletMeterResultsService = palletMeterResultsService;
	}
	
	/**
	 * @return the conveyorOutputMetricsSummaryService
	 */
	public static ConveyorOutputMetricsSummaryService getConveyorOutputMetricsSummaryService() {
		return conveyorOutputMetricsSummaryService;
	}


	/**
	 * @param conveyorOutputMetricsSummaryService the conveyorOutputMetricsSummaryService to set
	 */
	public static void setConveyorOutputMetricsSummaryService(
			ConveyorOutputMetricsSummaryService conveyorOutputMetricsSummaryService) {
		MySqlServiceManager.conveyorOutputMetricsSummaryService = conveyorOutputMetricsSummaryService;
	}


	public static PalletMeterArchivedResultsService getPalletMeterArchivedResultsService() {
		return palletMeterArchivedResultsService;
	}


	public static void setPalletMeterArchivedResultsService(
			PalletMeterArchivedResultsService palletMeterArchivedResultsService) {
		MySqlServiceManager.palletMeterArchivedResultsService = palletMeterArchivedResultsService;
	}
}
