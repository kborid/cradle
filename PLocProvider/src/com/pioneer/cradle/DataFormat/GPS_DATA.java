package com.pioneer.cradle.DataFormat;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import com.pioneer.PLocProviderKit.PLocProviderKit;

import android.location.Location;

public class GPS_DATA {
	
	private long dLatitude = 0l;
	private long dLongitude = 0l;
	private float fAltitude = 0;
	private float fSpeed = 0.0f;
	private float fAngel = 0.0f;
	private int iDimension = 0;
	private long time = 0;

	public GPS_DATA() {
	}
	
	/**
	 * call from c++, don't remove it.
	 * if you want change it, must change C++ source both.
	 * 
	 * @see jniNmeaDecoderIF.getLocationInfo(GPS_DATA data)
	 * */
	public void setLocation(long latitude, long longitude, float altitude,float speed, float angel, int dimension, int[] time) {
		this.dLatitude = latitude;
		this.dLongitude = longitude;
		this.fAltitude = altitude;
		this.fSpeed = speed;
		this.fAngel = angel;
		this.iDimension = dimension;
		this.time = getTime(time);
	}
	
	private static long getTime(int[] time) {
		if (time.length != 6) {
			return -1;
		}
		
		String timestr = time[0] + "-" + time[1] + "-" + time[2] + " "
				+ time[3] + ":" + time[4] + ":" + time[5];
		SimpleDateFormat foo = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
		foo.setCalendar(c);
		try {
			Date date = foo.parse(timestr);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return -1;
		}

	}
	
	public void setLocation(long latitude, long longitude, float altitude,float speed, float angel, int dimension, long time) {
		this.dLatitude = latitude;
		this.dLongitude = longitude;
		this.fAltitude = altitude;
		this.fSpeed = speed;
		this.fAngel = angel;
		this.iDimension = dimension;
		this.time = time;
	}
	
	public void setLocation(Location info) {
		this.dLatitude = (long) (info.getLatitude()*256*3600);
		this.dLongitude = (long) (info.getLongitude()*256*3600);
		this.fAltitude = (float) info.getAltitude();
		this.fSpeed = info.getSpeed();
		this.fAngel = info.getBearing();
		this.iDimension = (int) info.getAccuracy();
		this.time = info.getTime();
	}
	
	public long getLatitude() {
		return dLatitude;
	}

	public long getLongitude() {
		return dLongitude;
	}

	public float getAltitude() {
		return fAltitude;
	}

	public float getSpeed() {
		return fSpeed;
	}

	public float getAngel() {
		return fAngel;
	}

	public int getDimension() {
		return iDimension;
	}

	public long getTime() {
		return time;
	}

	public Location obtain(){
		Location l = new Location(PLocProviderKit.CRADLE_ASSIS_PROVIDER_NAME);
		l.setLatitude(((double)dLatitude)/3600/256);
		l.setLongitude(((double)dLongitude)/3600/256);
		l.setAltitude(fAltitude);
		l.setSpeed(fSpeed);
		l.setAccuracy(iDimension);
		l.setBearing(fAngel);
		l.setTime(time);
		return l;
	}
	
	public boolean isValid() {
		return (dLatitude != 0  &&  dLongitude != 0);
	}

	@Override
	public String toString() {
        return "Location[mProvider=" + "PLocProvider" +
                ",mTime=" + time +
                ",mLatitude=" + dLatitude +
                ",mLongitude=" + dLongitude +
                ",mAltitude=" + fAltitude +
                ",mSpeed=" + fSpeed +
                ",mBearing=" + fAngel +
                ",mAccuracy=" + iDimension + "]";
	}
}
