package com.pioneer.cradle.nmea;

import android.location.Location;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.util.SatelliteData;

class DataInfo {
	public final Location locationInfo;
	public final SatelliteData[] satellite;
	private String cradleMessage;
	
	private boolean isValid;
	
	private DataInfo() {
		locationInfo = new Location(PLocProviderKit.CRADLE_ASSIS_PROVIDER_NAME);
		satellite = new SatelliteData[32];
	}
	
	public static final DataInfo instance = new DataInfo();
	
	public boolean isValid() {
		return isValid;
	}

	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	public String getCradleMessage() {
		return cradleMessage;
	}

	public void setCradleMessage(String cradleMessage) {
		this.cradleMessage = cradleMessage;
	}
	
	
	
}
