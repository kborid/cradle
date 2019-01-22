package com.pioneer.cradle.PLocProvider;

import java.io.Serializable;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import com.pioneer.cradle.PLocProvider.Screen.Launcher;
import com.pioneer.cradle.PLocProvider.btConnection.ConnectLog;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class ConntectedControl {

	private static final String fromApp = "fromApp";
	private static final String key_Conntect_Device = "sendConnectDivece";

	private static final String arg1 = "arg1";
	private static final String arg2 = "arg2";

	private static final String commad = "commad";
	private static final int startService = 1;
	private static final int startConnect = 2;
	private static final int stopConnect = 3;
	private static final int startAccept = 4;
	private static final int startConnectFromBootReceiver = 7;
	private static final int sharedPreferce = 8;

	static boolean onStart(final Context c, Intent intent) {
		if (intent == null || intent.getBooleanExtra("fromSelf", false)) {
			ConnectLog.getInstance().writeGpslogfile("onstart fromSelf");
			return false;
		}

		int commad = intent.getIntExtra(ConntectedControl.commad, 0);

		if (commad == startService) {
			ConnectLog.getInstance().writeGpslogfile("onstart start service, auto connect.");
			ConntectHelper.Instance().beginAcceptConnect();
		} else if (commad == startConnect) {
			BluetoothDevice dev = (BluetoothDevice) intent.getParcelableExtra(key_Conntect_Device);
			if (dev != null) {
				ConnectLog.getInstance().writeGpslogfile("onstart connect dev=" + dev);
				ConntectHelper.Instance().sendStartCradleMsg(dev);
			}
		} else if (commad == startAccept) {
			BluetoothDevice dev = (BluetoothDevice) intent.getParcelableExtra(key_Conntect_Device);
			if (dev != null) {
				ConnectLog.getInstance().writeGpslogfile("onstart accept dev=" + dev);
				ConntectHelper.Instance().beginAcceptConnect(dev);

				try {
					if (!isRunActivity(c, "com.pioneer.cradle.PLocProvider")) {

						Intent activityIntent = c.getPackageManager().getLaunchIntentForPackage("com.pioneer.cradle.PLocProvider");
						activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						if (SharedPreferenceData.ENTER_NAVI_DRECTION.getBoolean()) {
							activityIntent.putExtra("gotoNaviApp", true);
						}
						c.startActivity(activityIntent);
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else if (commad == stopConnect) {
			ConnectLog.getInstance().writeGpslogfile("onstart stop connect");
			ConntectHelper.Instance().sendStopCradleMsg();
		} else if (commad == startConnectFromBootReceiver) {
			BluetoothDevice dev = (BluetoothDevice) intent.getParcelableExtra(key_Conntect_Device);

			if (ConntectHelper.Instance().isNoConnect()) {
				if (dev != null) {
					boolean alreadyTry = ConntectHelper.Instance().sendStartCradleTry5times(dev);
					if (alreadyTry) {
						return true;
					}
					try {
						if (SharedPreferenceData.ENTER_NAVI_DRECTION.getBoolean()) {
							Intent activityIntent = new Intent();
							activityIntent.putExtra("gotoNaviApp", true);
							activityIntent.setClass(c, Launcher.class);
							activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							c.startActivity(activityIntent);
						} else {
							if (!isRunActivity(c, "com.pioneer.cradle.PLocProvider")) {
								Intent activityIntent = c.getPackageManager().getLaunchIntentForPackage("com.pioneer.cradle.PLocProvider");
								activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
								c.startActivity(activityIntent);
							}

						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		} else if (commad == sharedPreferce) {
			SharedPreferenceData spd = (SharedPreferenceData) intent.getSerializableExtra(arg1);

			Object value = intent.getSerializableExtra(arg2);
			if (value != null) {
				if (value instanceof Boolean) {
					spd.setValue((Boolean) value);
				} else if (value instanceof Integer) {
					spd.setValue((Integer) value);
				} else if (value instanceof Float) {
					spd.setValue((Float) value);
				} else if (value instanceof String) {
					spd.setValue((String) value);
				}
			}
		}

		return true;
	}

	private static boolean isRunActivity(Context context, String packageName) {
		ActivityManager __am = (ActivityManager) context.getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> __list = __am.getRunningTasks(100);
		if (__list.size() == 0)
			return false;
		for (RunningTaskInfo task : __list) {
			if (task.topActivity.getPackageName().equals(packageName)) {

				Intent activityIntent = new Intent();
				activityIntent.setComponent(task.topActivity);
				activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				context.startActivity(activityIntent);
				return true;
			}
		}
		return false;
	}

	public static void startServices(Context c) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, startService);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

	public static void startCradleConnect(Context c, BluetoothDevice dev) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, startConnect);
		serviceIntent.putExtra(key_Conntect_Device, dev);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

	public static void startCradleAccept(Context c, BluetoothDevice dev) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, startAccept);
		serviceIntent.putExtra(key_Conntect_Device, dev);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

	public static void stopCradleConnect(Context c) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, stopConnect);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

	public static void startCradleConnectFromBootReceiver(Context c, BluetoothDevice dev) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, startConnectFromBootReceiver);
		serviceIntent.putExtra(key_Conntect_Device, dev);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

	public static void setSharedPreferenceToService(Context c, SharedPreferenceData d, boolean value) {
		setSharedPreferenceToService(c, d, (Serializable) value);
	}

	public static void setSharedPreferenceToService(Context c, SharedPreferenceData d, int value) {
		setSharedPreferenceToService(c, d, (Serializable) value);
	}

	public static void setSharedPreferenceToService(Context c, SharedPreferenceData d, float value) {
		setSharedPreferenceToService(c, d, (Serializable) value);
	}

	public static void setSharedPreferenceToService(Context c, SharedPreferenceData d, String value) {
		setSharedPreferenceToService(c, d, (Serializable) value);
	}

	private static void setSharedPreferenceToService(Context c, SharedPreferenceData d, Serializable value) {
		Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
		serviceIntent.setClass(c, PLocProviderService.class);
		serviceIntent.putExtra(fromApp, true);
		serviceIntent.putExtra(commad, sharedPreferce);
		serviceIntent.putExtra(arg1, d);
		serviceIntent.putExtra(arg2, value);
		serviceIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		c.startService(serviceIntent);
	}

}
