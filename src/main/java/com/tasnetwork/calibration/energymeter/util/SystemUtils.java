package com.tasnetwork.calibration.energymeter.util;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.tasnetwork.calibration.conveyor.database.MySQL_Controller;
import com.tasnetwork.calibration.energymeter.ApplicationLauncher;
import com.tasnetwork.calibration.energymeter.constant.ConstantApp;

public class SystemUtils {

	public static void deleteLogFilesOlderThanNdays(long daysBack, String dirWay) {
		File directory = new File(dirWay);
		if (directory.exists()) {

			File[] listFiles = directory.listFiles();
			long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);
			for (File listFile : listFiles) {
				if (listFile.lastModified() < purgeTime) {
					if (!listFile.delete()) {
						System.err.println("Unable to delete file: " + listFile);
					}
				}
			}
		}

	}

	public static void deleteLogFilesOlderThanNdays2(int daysBack, String dirWay) {
		File directory = new File(dirWay);
		ApplicationLauncher.logger.info("deleteLogFilesOlderThanNdays: daysBack: " + daysBack);
		if (directory.exists()) {

			File[] listFiles = directory.listFiles();
			long purgeTime = System.currentTimeMillis() - (daysBack * 24 * 60 * 60 * 1000);

			ApplicationLauncher.logger.info("deleteLogFilesOlderThanNdays: purgeTime: " + purgeTime);
			for (File listFile : listFiles) {
				ApplicationLauncher.logger.info("deleteLogFilesOlderThanNdays: listFile: " + listFile);
				ApplicationLauncher.logger
						.info("deleteLogFilesOlderThanNdays: listFile.lastModified(): " + listFile.lastModified());
				if (listFile.lastModified() < purgeTime) {
					if (!listFile.delete()) {
						ApplicationLauncher.logger
								.info("deleteLogFilesOlderThanNdays: Unable to delete file: " + listFile);
					}
				}
			}
		}

	}

	public static boolean LoadSystemTime() throws Exception, NumberFormatException {
		String current_timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
		// String current_timestamp = "2020-09-01 00:00:00";
		JSONObject system_config = MySQL_Controller.sp_getsystem_config();
		JSONArray json_arr = new JSONArray();
		String start_time_stamp = "";
		try {
			if (system_config.length() > 0) {
				json_arr = system_config.getJSONArray("Properties");
				JSONObject jobj = new JSONObject();
				for (int i = 0; i < json_arr.length(); i++) {
					jobj = json_arr.getJSONObject(i);
					String property = jobj.getString("property");
					if (property.equals(ConstantApp.SYSTEM_CONFIG_KEY)) {
						start_time_stamp = jobj.getString("value");
						break;
					}
				}
			} else {
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			ApplicationLauncher.logger.error("LoadSystemTime : JSONException: " + e.getMessage());
		}

		long epoch = 2 * 366 * 24 * 60 * 60;
		long current_value = calcEpoch(current_timestamp);
		long max_value = Long.parseLong(start_time_stamp) + epoch;

		if (current_value < max_value) {
			return true;
		} else {
			return false;
		}

	}

	public static long calcEpoch(String Date_time) {
		long epoch = 0;
		// String str = "2014-07-04 04:05:10"; // UTC
		String str = Date_time; // UTC

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date datenew = null;
		try {
			datenew = df.parse(str);
		} catch (ParseException e) {

			e.printStackTrace();
			ApplicationLauncher.logger.error("calcEpoch : ParseException: " + e.getMessage());
		}
		epoch = datenew.getTime() / 1000;

		ApplicationLauncher.logger.info("ce: " + epoch);
		return epoch;
	}

}
