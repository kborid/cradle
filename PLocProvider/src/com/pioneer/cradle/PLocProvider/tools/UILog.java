package com.pioneer.cradle.PLocProvider.tools;

import android.util.Log;

public class UILog {
	
	private static long time = System.currentTimeMillis();
	
	
	private UILog() {
		
	}
	
	public static void d(String msg) {
		printStack(msg);
	}
	
	private static final String TAG = "UILog";
//	private static final String PREFIX = "com.billionav.navi";
	
	private static void printStack(String message) {
		StackTraceElement[] elements = new Throwable().fillInStackTrace().getStackTrace();
		Log.d(TAG, message);
		for (StackTraceElement item : elements) {
//			if (!item.getClassName().startsWith(PREFIX)) { continue;
//            }
			Log.d(TAG, item.toString());
		}
	}
	
	public static void showTime(String msg) {
		long cuttenttime = System.currentTimeMillis();
		Log.d(TAG, (cuttenttime - time)+msg);
	};
}
