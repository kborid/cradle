package com.pioneer.cradle.PLocProvider.tools;

import java.util.ArrayList;
import java.util.List;

import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

public class MusicTools {
	
	public static List<ResolveInfo> getMusicApp(Context c) {
		List<ResolveInfo> list1 = getMusicAppFromMusicplayer(c);
		List<ResolveInfo> list2 = getMusicAppFromAudio(c);
		
		ArrayList<ResolveInfo> list = new ArrayList<ResolveInfo>();
		list.addAll(list1);
		
		for(ResolveInfo r : list2) {
			boolean isExists = false;
			for(ResolveInfo tempR : list1) {
				if(tempR.activityInfo.packageName.equals(r.activityInfo.packageName)){
					isExists = true;
					break;
				}
			}
			if(!isExists) {
				list.add(r);
			}
		}
		
		return list;
	}
	
	public static void initMusicApp(Context c) {
		List<ResolveInfo> list = getMusicApp(c);
		if(list.size() == 1) {
			SharedPreferenceData.MUSIC_APP_PACKAGE.setValue(list.get(0).activityInfo.packageName);
			ConntectedControl.setSharedPreferenceToService(c, SharedPreferenceData.MUSIC_APP_PACKAGE, list.get(0).activityInfo.packageName);
		} else {
			String musicPackageName = SharedPreferenceData.MUSIC_APP_PACKAGE.getString();
			if(!TextUtils.isEmpty(musicPackageName) && !checkPackageNameValid(c, musicPackageName)) {
				SharedPreferenceData.MUSIC_APP_PACKAGE.setValue("");
				ConntectedControl.setSharedPreferenceToService(c, SharedPreferenceData.MUSIC_APP_PACKAGE, "");
			}
		}
		
	}
	
	private static boolean checkPackageNameValid(Context c, String packname) {
		if(TextUtils.isEmpty(packname)) {
			return false;
		}
		List<ResolveInfo> list = getMusicApp(c);
		for(ResolveInfo r : list) {
			if(r.activityInfo.packageName.equals(packname)){
				return true;
			}
		}
		return false;
	}
	
	public static void startMusicApp(Context c, String packname, Toast toast) {
		boolean vaild = checkPackageNameValid(c, packname);
		if(vaild) {
			startMusicAppFromVaildPackageName(c, packname);
		} else {
			List<ResolveInfo> list = getMusicApp(c);
			if(list.size()==0) {
				toast.setText(R.string.no_music_tip);
				toast.show();
			} else if(list.size()==1) {
				startMusicAppFromVaildPackageName(c, list.get(0).activityInfo.packageName);
			} else {
				toast.setText(R.string.more_music_tip);
				toast.show();
			}
		}
	}
	
	private static void startMusicAppFromVaildPackageName(Context c, String packname) {
		
		try {
			Intent mainIntent = new Intent("android.intent.action.MUSIC_PLAYER");
			mainIntent.setPackage(packname);
			mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			c.startActivity(mainIntent);
		} catch (ActivityNotFoundException e) {
//			e.printStackTrace();
			try {
		        Intent mainIntent = new Intent("android.intent.action.VIEW");
		        mainIntent.setDataAndType(Uri.parse("file://"), "audio/*");
		        mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mainIntent.setPackage(packname);
				c.startActivity(mainIntent);
			} catch (ActivityNotFoundException e2) {
				e.printStackTrace();
			}
		}
		
	}
	
	private static List<ResolveInfo> getMusicAppFromMusicplayer(Context c) {
		Intent mainIntent = new Intent("android.intent.action.MUSIC_PLAYER", null);
		List<ResolveInfo> apps = c.getPackageManager().queryIntentActivities(mainIntent, 0);
		return apps;
	}
	
	private static List<ResolveInfo> getMusicAppFromAudio(Context c) {
        Intent mainIntent = new Intent("android.intent.action.VIEW");
        mainIntent.setDataAndType(Uri.parse("file://"), "audio/*");
        List<ResolveInfo> musicApps = c.getPackageManager().queryIntentActivities(mainIntent, 0);
        return musicApps;
	}

}
