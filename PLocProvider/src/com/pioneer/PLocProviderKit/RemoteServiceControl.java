package com.pioneer.PLocProviderKit;


import java.io.Serializable;

import android.content.Context;
import android.os.Bundle;

import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public final class RemoteServiceControl extends PLocProviderKit{

	private static RemoteServiceControl instance;
	private RemoteServiceControl(){
	};
	
	public static RemoteServiceControl getInstance(){
		if(null == instance){
			instance = new RemoteServiceControl();
		}
		return instance;
	}
	
	public boolean startLocProvider(Context context, ConnectedListener listener)	{
		return PLocProvider.getInstance().startServiceConnection(context, listener);
	}	
	
	public int getConnectionStatus() {
		return getExtDeviceConnectionStatus();
	}
	
    public boolean getBoolean(SharedPreferenceData key){
    	return (Boolean)getSharedPreferenceData(key, Boolean.class);				
	}
    
    public void setBoolean(SharedPreferenceData key, boolean value){
    	setSharedPreferenceData(key, (Serializable)value);
    }
    
    public int getInt(SharedPreferenceData key){
    	return (Integer)getSharedPreferenceData(key, Integer.class);		
    }
    
    public void setInt(SharedPreferenceData key, int value){
    	setSharedPreferenceData(key, (Serializable)value);
    }
    
    public float getFloat(SharedPreferenceData key){
		return (Float)getSharedPreferenceData(key, Float.class);
		
    }
    
    public void setFloat(SharedPreferenceData key, float value){
    	setSharedPreferenceData(key, (Serializable)value);
    }
    
    public String getString(SharedPreferenceData key){
		return (String)getSharedPreferenceData(key, String.class);
    }
    
    public void setString(SharedPreferenceData key, String value){
    	setSharedPreferenceData(key, (Serializable)value);
    }	
	
	private void setSharedPreferenceData(SharedPreferenceData key, Serializable value){
		Message msg = Message.obtain(RemoteOrder.SHAREDPREFERENCE_SET, key.name());
		msg.obj = value;
		setOrder(RemoteOrder.SHAREDPREFERENCE_SET, msg);
	}
    
	private Object getSharedPreferenceData(SharedPreferenceData key, Class<?> value){
		Message msg = Message.obtain(RemoteOrder.SHAREDPREFERENCE_GET, key.name());
		msg.obj = value;
		Bundle b = PLocProvider.getInstance().order(msg.obtainBundle());
		return Message.getMessage(b).obj;
	}
    
    public void order(int orderKey){
    	Message msg = Message.obtain(orderKey);
    	setOrder(orderKey, msg);
    }
    
    public void order(int orderKey, String str){
    	Message msg = Message.obtain(orderKey, str);
    	setOrder(orderKey, msg);
    }
    
    public void order(int orderKey, Serializable obj){
    	Message msg = Message.obtain(orderKey, obj);
    	setOrder(orderKey, msg);
    }
    
    public void order(int orderKey, int arg0, int arg1){
    	Message msg = Message.obtain(orderKey, arg0, arg1);
    	setOrder(orderKey, msg);
    }
    
    private void setOrder(int orderkey, Message msg){
		PLocProvider.getInstance().order(msg.obtainBundle());
    }
    
    public void setGpsLogFileEnable(boolean enable){
    	SharedPreferenceData.GPS_LOG_RECORD.setValue(enable);
    	setSharedPreferenceData(SharedPreferenceData.GPS_LOG_RECORD, enable);
    	order(RemoteOrder.GPS_LOG_ENABLE);
    }
    
    public void setSNSGpsLogFileEnable(boolean enable){
    	SharedPreferenceData.GPS_SNS_LOG_RECORD.setValue(enable);
    	setSharedPreferenceData(SharedPreferenceData.GPS_SNS_LOG_RECORD, enable);
    	order(RemoteOrder.GPS_SNS_ENABLE);
    }
    
    public void setNMEALogFileEnable(boolean enable){
    	SharedPreferenceData.GPS_NMEA_LOG_RECORD.setValue(enable);
    	setSharedPreferenceData(SharedPreferenceData.GPS_NMEA_LOG_RECORD, enable);
    	order(RemoteOrder.GPS_NMEA_ENABLE);
    }
    
    public void clearLearn(){
    	order(RemoteOrder.LEARN_CLEAN);
    }
    
    public void setDmyset(){
    	order(RemoteOrder.LEARN_DMYSET);
    }
    
}
