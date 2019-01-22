package com.pioneer.cradle.PLocProvider.jni;


import android.util.Log;

import com.pioneer.PLocProviderKit.util.SatelliteData;
import com.pioneer.cradle.DataFormat.GPS_DATA;

public class jniNmeaDecoderIF {
	private static final jniNmeaDecoderIF instance = new jniNmeaDecoderIF();
	private jniNmeaDecoderIF() {};
	private GPS_DATA m_latestData;

	
	public static jniNmeaDecoderIF getInstance() {
		return instance;
	}

	public native boolean SendCradleData(byte[] data, int length);

	/**
	 * Get cradle information
	 * 
	 * @param byte[] buffer [IN]
	 * @return int result size
	 */
	@Deprecated
	public native int GetCradleInfo(byte[] buffer);
	
	/**
	 * you create a empty object, this method will fill it.
	 * */
	public native void getLocationInfo(GPS_DATA data);
	
	/**
	 * get satellite info, you can get null if not connected.
	 * */
	public native SatelliteData[] getSatelliteInfo();
	

	public GPS_DATA GetGPSDATA() {
		// Buffer
//		byte[] buffer = new byte[64];
//		// Call native interface
//		final int iSize = GetCradleInfo(buffer);
//		String strOutput = "";
//		for (int iCnt = 0; iCnt < iSize; ++iCnt) {
//			if (buffer[iCnt] != '\r'
//				&& buffer[iCnt] != '\n') {
//				// ASCII to string
//				strOutput += String.format("%02X", buffer[iCnt]);
//			}
//		}
//		m_latestData = new GPS_DATA(buffer, iSize);
		
		GPS_DATA gpsData = new GPS_DATA();
		getLocationInfo(gpsData);
		
		if(gpsData.isValid()) {
			m_latestData = gpsData;
		}
		
		return gpsData;
	}
	
	public GPS_DATA getLatestData() {
		return m_latestData;
	}


	static{
		try{
			System.loadLibrary("jniNmeaDecoder");
		} catch (Exception e){
			Log.d("test", "Load Library jniNmeaDecoder error");
		}
	}



}
