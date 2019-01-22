package com.pioneer.cradle.PLocProvider.btConnection;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import android.widget.Toast;

import com.pioneer.cradle.PLocProvider.LocationApp;
import com.pioneer.cradle.PLocProvider.fileAccess.FileWriterAssistant;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class GpsLog {
//	private static final String TAG = GpsLog.class.getName();
	
	public static final String FilePath =  FileWriterAssistant.getRootPath()+ "/user/RW/GpsLog/";
	private static boolean mFileNameFlag = false;
	private String mflieName = "";

	private static final GpsLog instance = new GpsLog();
	
	private GpsLog() {
		syncLogEnable();
	}
	
	public static final GpsLog getInstance() {
		return instance;
	}
	
	// for m_bWriteLogsToFileEnable flag
	private boolean m_bWriteLogsToFileEnable = false;
	public final void setWriteLogsToFileEnable(boolean bEnable) {
		m_bWriteLogsToFileEnable = bEnable;
	}
	public final boolean getWriteLogsFile() {
		return m_bWriteLogsToFileEnable;
	}
	
	public final void syncLogEnable() {
		setWriteLogsToFileEnable(SharedPreferenceData.GPS_LOG_RECORD.getBoolean());
	}
	
	// create file path.
	public void createFolder(String folderPath) {

		try {
			java.io.File myFilePath = new java.io.File(folderPath);
			// create folder if file path isn't exist.
			if (!myFilePath.exists()) {
				if (myFilePath.mkdirs() == false) {
					ShowMessage(" LocationListener Create Folder Failed");
					return;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void ShowMessage(String message) {
//		if (mJniLocInfor.GetGpsViewFlag()) {
			System.out.println(message);
			Toast.makeText(LocationApp.getInstance().getApplicationContext(), message, Toast.LENGTH_SHORT).show();
//		}
	}
	// write log file
	public void WriteFile(String filename, String data) {

		synchronized (this) {

			FileWriter file = null;
			try {
				// open file;
				file = new FileWriter(filename, true);
				// append data to the end of the file;
				file.append(data);
				file.flush();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					// close file;
					file.close();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
	}

	public void writeGpslogfile(String logdata) {
		// Write logs
//		Log.d(TAG, logdata);
		if (m_bWriteLogsToFileEnable) {
			// print logs to console

			if (mFileNameFlag == false) {
				// change time format
				Calendar caldr = Calendar.getInstance();
				Date time1;
				time1 = caldr.getTime();
				// String time1 = String.valueOf(getTime());
				String date = new java.text.SimpleDateFormat("yyyyMMddHHmmss")
						.format(time1);
				mflieName = FilePath + "CradleGps" + date+".txt";

				mFileNameFlag = true;

				// Create file folder //"/sdcard/GpsLog/";
				createFolder(FilePath);
			}
		
			// get time
			Calendar caldr = Calendar.getInstance();
			Date curTime;
			curTime = caldr.getTime();
			String date = new java.text.SimpleDateFormat("MM-dd HH:mm:ss.SSS  ").format(curTime);
			logdata = date + logdata;
			
			// write logs to file
			WriteFile(mflieName, logdata + "\r\n");
		}
	}
}
