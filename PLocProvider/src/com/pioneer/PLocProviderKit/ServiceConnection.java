package com.pioneer.PLocProviderKit;

import android.content.ComponentName;

public class ServiceConnection {
	void setServiceListener() {
		PLocProvider.getInstance().listener = new ServiceConnectListener(){

			@Override
			public void onServiceConnected(ComponentName name) {
				
			}};
	}
}
