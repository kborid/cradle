package com.pioneer.cradle.PLocProvider.receiver;

import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;
import com.pioneer.cradle.nmea.NMEA0183Kit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

public class ShutdownBroadcastReceiver extends BroadcastReceiver {

	private static final String ACTION_SHUTDOWN = "android.intent.action.ACTION_SHUTDOWN"; 
	@Override
	public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION_SHUTDOWN)) {
        	
			String cradleMsg = NMEA0183Kit.getCradleMessage();
        	if(!TextUtils.isEmpty(cradleMsg)) {
        		SharedPreferenceData.CRADLE_BACKUP_MESSAGE.setValue(cradleMsg);
        	}
        	
        }  
	}

}
