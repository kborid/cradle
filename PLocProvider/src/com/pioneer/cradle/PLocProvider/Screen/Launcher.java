package com.pioneer.cradle.PLocProvider.Screen;

import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningTaskInfo;
import android.bluetooth.BluetoothDevice;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Process;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pioneer.PLocProviderKit.ConnectedListener;
import com.pioneer.PLocProviderKit.RemoteServiceControl;
import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.btConnection.DeviceTools;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;
import com.pioneer.cradle.PLocProvider.tools.UIHandler;

public class Launcher extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.launcher);
		
		boolean gotoNaviApp = getIntent().getBooleanExtra("gotoNaviApp", false);
		if(gotoNaviApp) {
			gotoNaviApp();
		} else {
			startService();
		}
		
	}
	
	private void gotoNaviApp() {
		new Handler() {
			public void handleMessage(android.os.Message msg) {
				if (msg.what == 1) {
					try {
	    	        	Intent activityIntent = new Intent();
	    	        	activityIntent.setAction("com.pioneer.cradle.intent.action.NAVI");
	    	        	activityIntent.setPackage(SharedPreferenceData.NAVI_APP_PACKAGE.getString());
	    	        	activityIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	        	startActivity(activityIntent);
	    	        	sendEmptyMessage(2);
					} catch (ActivityNotFoundException e) {
						Toast.makeText(Launcher.this, R.string.no_navi_tip, Toast.LENGTH_LONG).show();
						startService();
					}
					
				} else if(msg.what == 2) {
					finish();
					if(isSingleOpen()) {
						System.exit(0);
					}
				}
				
			};
		}.sendEmptyMessageDelayed(1, 1000);
	}
	
	
	private boolean isSingleOpen() {
	    ActivityManager am = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
	    List<RunningTaskInfo> list = am.getRunningTasks(5);
	    if(list.size() == 0) return false;
	    for(RunningTaskInfo task : list) {
	    	if(task.id == getTaskId() && task.baseActivity.equals(task.topActivity)) {
	    		return true;
	    	}
	    }
	    return false;
	}
	
	private void showDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setMessage(R.string.dead_line_tip);
		build.setPositiveButton(R.string.quit, null);
		AlertDialog dialog = build.create();
		dialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				finish();
				System.exit(0);
			}
		});
		dialog.show();
	}

	private void startService() {
		
		BluetoothDevice dev = getIntent().getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		
		if(!DeviceTools.checkDeviceType(dev)) {
			ConntectedControl.startServices(this);
		}else {
			ConntectedControl.startCradleAccept(this,dev);
		}
		
		RemoteServiceControl.getInstance().stopLocProvider(this);
		UIHandler.postDelayed(new Runnable() {
			@Override
			public void run() {
				bindService();
			}
		}, 500);
	}

	private void bindService() {
		RemoteServiceControl.getInstance().startLocProvider(this, new ConnectedListener() {
			
			@Override
			public void onServiceConnected(ComponentName name) {
				endStart();
			}
		});
	}

	private void endStart() {

		Intent i = new Intent();
		
		if (SharedPreferenceData.AGREE_CONTRACT.getBoolean()) {
			i.setClass(this, MainActivity.class);
		} else {
			i.setClass(this, Contract.class);
			i.putExtra("FromLauncher", true);
		}
		startActivity(i);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

}
