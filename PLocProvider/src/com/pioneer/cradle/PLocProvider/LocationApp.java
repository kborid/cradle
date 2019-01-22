package com.pioneer.cradle.PLocProvider;

import java.util.List;

import com.pioneer.PLocProviderKit.ConnectedListener;
import com.pioneer.PLocProviderKit.RemoteServiceControl;

import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.util.Log;

public class LocationApp extends Application {
	
	private static LocationApp instance;
	
	public static LocationApp getInstance() {
		return instance;
	}

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        instance = this;
    }

    @Override
	public void onCreate() {
		super.onCreate();
		Log.i("duan", "onCreate()============!");
		Log.i("duan", "Application:isRunningService " + isRunningService());
		if (isRunningService()) {
			RemoteServiceControl.getInstance().startLocProvider(this,
					new ConnectedListener() {

						@Override
						public void onServiceConnected(ComponentName name) {
							Log.i("duan", "binded service");
						}
					});
		}
	}

	private boolean isRunningService() {
		ActivityManager mActivityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> mServiceList = mActivityManager.getRunningServices(100);

		if (mServiceList.size() == 0) {
			return false;
		}
		
		final String serviceName = PLocProviderService.class.getName();
		Log.i("duan", "serviceName = " + serviceName);
		for (int i = 0; i < mServiceList.size(); i++) {
			if (serviceName.equals(mServiceList.get(i).service.getClassName())) {
				return true;
			}
		}
		return false;
	}
}
