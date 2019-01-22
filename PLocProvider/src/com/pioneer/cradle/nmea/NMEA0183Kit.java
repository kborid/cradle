package com.pioneer.cradle.nmea;


import java.util.Arrays;

import com.pioneer.PLocProviderKit.util.SatelliteData;

import android.location.Location;

public class NMEA0183Kit {

	public static boolean receiveNmeaInfo(String nmeaData) {
		return Distribute.instance.receiveNmeaInfo(nmeaData);
	}
	
	public static Location getLocation() {
		return new Location(DataInfo.instance.locationInfo);
	}
	
	public static SatelliteData[] getSatalliteData(){
		int length = DataInfo.instance.satellite.length;
		for(int i=0; i<length; i++) {
			if(DataInfo.instance.satellite[i] == null){
				length = i;
				break;
			}
		}
		
		SatelliteData[] data = new SatelliteData[length];
		System.arraycopy(DataInfo.instance.satellite, 0, data, 0, length);
		Arrays.sort(data);
		return data;
	}
	
	public static String getCradleMessage() {
		return DataInfo.instance.getCradleMessage();
	}
}
