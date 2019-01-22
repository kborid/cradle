package com.pioneer.cradle.PLocProvider.tools;

import java.util.List;

import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.text.TextUtils;
import android.widget.Toast;

public class NaviTools {
	
	public static List<ResolveInfo> getNaviApp(Context c) {
		Intent mainIntent = new Intent("com.pioneer.cradle.intent.action.NAVI");
		List<ResolveInfo> naviApps = c.getPackageManager().queryIntentActivities(mainIntent, 0);
		return naviApps;
	}
	
	public static void initNaviApp(Context c) {
		List<ResolveInfo> list = getNaviApp(c);
		if(list.size() == 1) {
			SharedPreferenceData.NAVI_APP_PACKAGE.setValue(list.get(0).activityInfo.packageName);
			ConntectedControl.setSharedPreferenceToService(c, SharedPreferenceData.NAVI_APP_PACKAGE, list.get(0).activityInfo.packageName);
		} else {
			String packageName = SharedPreferenceData.NAVI_APP_PACKAGE.getString();
			if(!TextUtils.isEmpty(packageName) && !checkPackageNameValid(c, packageName)) {
				SharedPreferenceData.NAVI_APP_PACKAGE.setValue("");
				ConntectedControl.setSharedPreferenceToService(c, SharedPreferenceData.NAVI_APP_PACKAGE, "");
			}
		}
		
	}
	private static boolean checkPackageNameValid(Context c, String packname) {
		if(TextUtils.isEmpty(packname)) {
			return false;
		}
		List<ResolveInfo> list = getNaviApp(c);
		for(ResolveInfo r : list) {
			if(r.activityInfo.packageName.equals(packname)){
				return true;
			}
		}
		return false;
	}
	
	public static void startNaviApp(Context c, String packname, Toast toast) {
		boolean vaild = checkPackageNameValid(c, packname);
		if(vaild) {
			startNaviAppFromVaildPackageName(c, packname);
		} else {
			List<ResolveInfo> list = getNaviApp(c);
			if(list.size()==0) {
				toast.setText(R.string.no_navi_tip);
				toast.show();
			} else if(list.size()==1) {
				startNaviAppFromVaildPackageName(c, list.get(0).activityInfo.packageName);
			} else {
				toast.setText(R.string.more_navi_tip);
				toast.show();
			}
		}
	}
	
	private static void startNaviAppFromVaildPackageName(Context c, String packname) {
		
		try {
			Intent mainIntent = new Intent("com.pioneer.cradle.intent.action.NAVI");
			mainIntent.setPackage(packname);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(mainIntent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
		}
		
	}


}
