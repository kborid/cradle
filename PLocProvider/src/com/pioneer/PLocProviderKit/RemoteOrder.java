package com.pioneer.PLocProviderKit;

import com.pioneer.cradle.PLocProvider.btConnection.ConnectLog;
import com.pioneer.cradle.PLocProvider.btConnection.CradleCommand;
import com.pioneer.cradle.PLocProvider.btConnection.GpsLog;
import com.pioneer.cradle.PLocProvider.btConnection.NmeaLog;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

import android.os.Bundle;

public class RemoteOrder {
	public final static int SHAREDPREFERENCE_SET = 1001;
	public final static int SHAREDPREFERENCE_GET = 1002;
	public final static int GPS_LOG_ENABLE = 1003;
	public final static int GPS_NMEA_ENABLE = 1004;
	public final static int GPS_SNS_ENABLE = 1005;
	public final static int LEARN_CLEAN = 1006;
	public final static int LEARN_DMYSET = 1007;
	
	private RemoteOrder() {
	}
	
	public static Bundle order(Bundle arg0){
		Message msg = Message.getMessage(arg0);
		if(msg == null) {
			return null;
		}
		
		if(msg.orderkey == SHAREDPREFERENCE_SET) {
			SharedPreferenceData spd = SharedPreferenceData.valueOf(msg.str);
    		Object value = msg.obj;
			if(value != null){
				if(value instanceof Boolean){
					spd.setValue((Boolean)value);
				} else if(value instanceof Integer){
					spd.setValue((Integer)value);
				} else if(value instanceof Float){
					spd.setValue((Float)value);
				} else if(value instanceof String){
					spd.setValue((String)value);
				}
			}

		} else if(msg.orderkey == SHAREDPREFERENCE_GET) {
			SharedPreferenceData spd = SharedPreferenceData.valueOf(msg.str);
    		Object value = msg.obj;
			if(value != null){
				if(value.equals(Boolean.class)){
					msg.obj = spd.getBoolean();
					return msg.obtainBundle();
				} else if(value.equals(Integer.class)){
					msg.obj = spd.getInt();
					return msg.obtainBundle();
				} else if(value.equals(Float.class)){
					msg.obj = spd.getFloat();
					return msg.obtainBundle();
				} else if(value.equals(String.class)){
					msg.obj = spd.getString();
					return msg.obtainBundle();
				}
			}
		} else if(msg.orderkey == GPS_LOG_ENABLE) {
			NmeaLog.getInstance().syncLogEnable();
			GpsLog.getInstance().syncLogEnable();
			ConnectLog.getInstance().syncLogEnable();
		} else if(msg.orderkey == GPS_SNS_ENABLE) {
			CradleCommand.getInstance().setSNSLog(SharedPreferenceData.GPS_SNS_LOG_RECORD.getBoolean());
		} else if(msg.orderkey == GPS_NMEA_ENABLE) {
			CradleCommand.getInstance().setNMEALog(SharedPreferenceData.GPS_NMEA_LOG_RECORD.getBoolean());
		} else if(msg.orderkey == LEARN_CLEAN) {
			CradleCommand.getInstance().clearLearn();
		} else if(msg.orderkey == LEARN_DMYSET) {
			CradleCommand.getInstance().setDmyset();
		}
		return null;
	}
	
}
