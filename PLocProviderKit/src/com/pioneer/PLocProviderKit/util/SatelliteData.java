package com.pioneer.PLocProviderKit.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


import android.location.GpsSatellite;


public class SatelliteData implements Serializable, Comparable<SatelliteData>{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8519912742031539695L;
	
	private boolean	bUseSatellite;		// [GSV] 測位使用衛星フラグ(0:未使用,1:使用)
	private int		bySatelliteID;		// [GSV] 衛星番号
	private float	fElevation;			// [GSV] 衛星仰角(deg)
	private float	fAzimuth;			// [GSV] 衛星方位(deg)
	private float	fSNR;				// [GSV] 信号強度
	
	public SatelliteData(int id){
		bySatelliteID = id;
		
	}
	
	/**
	 * call from c++, don't remove it.
	 * if you want change it, must change C++ source both.
	 * 
	 * @see jniNmeaDecoderIF.getSatelliteInfo()
	 * */
	public void setInfo(boolean use, float elevation, float azimuth, float snr) {
		bUseSatellite = use;
		fElevation = elevation;
		fAzimuth = azimuth;
		fSNR = snr;
	}
	
    /**
     * Returns the Id for the satellite.
     *
     * @return PRN number
     */
    public int getSatelliteId() {
        return bySatelliteID;
    }

    /**
     * Returns the signal to noise ratio for the satellite.
     *
     * @return the signal to noise ratio
     */
    public float getSnr() {
        return fSNR;
    }

    /**
     * Returns the elevation of the satellite in degrees.
     * The elevation can vary between 0 and 90.
     *
     * @return the elevation in degrees
     */
    public float getElevation() {
        return fElevation;
    }

    /**
     * Returns the azimuth of the satellite in degrees.
     * The azimuth can vary between 0 and 360.
     *
     * @return the azimuth in degrees
     */
    public float getAzimuth() {
        return fAzimuth;
    }

    /**
     * Returns true if the satellite was used by the GPS engine when
     * calculating the most recent GPS fix.
     *
     * @return true if the satellite was used to compute the most recent fix.
     */
    public boolean used() {
        return bUseSatellite;
    }

    public static SatelliteData[] getObject(String serStr) {
		
		ByteArrayInputStream byteArrayInputStream = null;
		ObjectInputStream objectInputStream = null;
		try {
			String redStr = java.net.URLDecoder.decode(serStr, "UTF-8");
			byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));  
			objectInputStream = new ObjectInputStream(byteArrayInputStream);   
			SatelliteData[] newList = (SatelliteData[])objectInputStream.readObject();   
			return newList;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} finally {
			try {
				objectInputStream.close();
				byteArrayInputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
		} 
		return null;
	}

	public static String getInputStream(SatelliteData[] my) {
		ByteArrayOutputStream byteArrayOutputStream = null;
		ObjectOutputStream objectOutputStream = null;
		try{
			byteArrayOutputStream = new ByteArrayOutputStream();  
			objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);  
			objectOutputStream.writeObject(my);    
			String serStr = byteArrayOutputStream.toString("ISO-8859-1");  
			serStr = java.net.URLEncoder.encode(serStr, "UTF-8");  
			
			return serStr;
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				objectOutputStream.close();
				byteArrayOutputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}  
		}
		return null;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (bUseSatellite ? 1231 : 1237);
		result = prime * result + bySatelliteID;
		result = prime * result + Float.floatToIntBits(fAzimuth);
		result = prime * result + Float.floatToIntBits(fElevation);
		result = prime * result + Float.floatToIntBits(fSNR);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SatelliteData other = (SatelliteData) obj;
		if (bUseSatellite != other.bUseSatellite)
			return false;
		if (bySatelliteID != other.bySatelliteID)
			return false;
		if (Float.floatToIntBits(fAzimuth) != Float
				.floatToIntBits(other.fAzimuth))
			return false;
		if (Float.floatToIntBits(fElevation) != Float
				.floatToIntBits(other.fElevation))
			return false;
		if (Float.floatToIntBits(fSNR) != Float.floatToIntBits(other.fSNR))
			return false;
		return true;
	}

	@SuppressWarnings("unused")
	private GpsSatellite obtain() {
		GpsSatellite satellite = getGpsSatelliteObject();
		if(satellite != null) {
			
		}
		setField(satellite, "mUsedInFix", bUseSatellite);
		setField(satellite, "mValid", bUseSatellite);
		setField(satellite, "mSnr", fSNR);
		setField(satellite, "mElevation", fElevation);
		setField(satellite, "mAzimuth", fAzimuth);
		return satellite;
	}
	
	@Override
	public String toString() {
		return "[SatelliteID="+bySatelliteID +", " +
				"Used="+bUseSatellite+", " +
				"Snr="+fSNR+", " +
				"Elevation="+fElevation+", " +
				"Azimuth="+fAzimuth+"]";
	}

	private GpsSatellite getGpsSatelliteObject() {
		GpsSatellite satellite = null;
		try {
			 Constructor<GpsSatellite> c = GpsSatellite.class.getConstructor(Integer.TYPE);
			 satellite =  c.newInstance(bySatelliteID);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return satellite;
	}
	
	private void setField(Object object, String fieldname, Object value){
		try {
			Field field = GpsSatellite.class.getDeclaredField(fieldname);
			field.setAccessible(true);
			if(value instanceof Boolean) {
				field.setBoolean(object, (Boolean) value);
			} else if(value instanceof Integer){
				field.setInt(object, (Integer) value);
			} else if(value instanceof Float) {
				field.setFloat(object, (Float) value);
			} else {
				field.set(object, value);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int compareTo(SatelliteData another) {
		if(this.bySatelliteID == another.bySatelliteID) {
			return 0;
		}
		return this.bySatelliteID > another.bySatelliteID ? 1:-1;
	}

}
