package com.pioneer.cradle.PLocProvider.Screen;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.KeyEvent;
import android.widget.Toast;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.tools.MusicTools;
import com.pioneer.cradle.PLocProvider.tools.NaviTools;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class MainActivity extends MainUILayer{
	
	private boolean pressOneTimeed = false;
	
	private Handler pressAgainTimeout = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if(msg.what == 1) {
				pressOneTimeed = false;
			}
		};
	};
	
	private PLocProviderKit kit = new PLocProviderKit();
	
	private Toast toast;
	private AlertDialog dialog;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		toast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
//		RemoteServiceControl.getInstance().startServiceConnection(this);// new ServiceConnectListener() 
		
		if(!ConntectHelper.Instance().isBTEnabled() 
				&& (savedInstanceState==null||savedInstanceState.getBoolean("bluetoothDisableDialogShowing"))) {
			showBluetoothDisableDialog();
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if(dialog != null && dialog.isShowing()) {
			outState.putBoolean("bluetoothDisableDialogShowing", true);
		}
	}
	
	private void showBluetoothDisableDialog() {
		AlertDialog.Builder build = new AlertDialog.Builder(this);
		build.setMessage(R.string.bluetooth_disable_tip);
		build.setPositiveButton(R.string.yes, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent buletoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
				buletoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(buletoothIntent);
			}
		});
		build.setNegativeButton(R.string.no, null);
		dialog = build.create();
		dialog.show();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		NaviTools.initNaviApp(this);
		MusicTools.initMusicApp(this);
		
		pressOneTimeed = false;
	}
	
	
	protected void clickNavi() {
		pressOneTimeed = false;
		NaviTools.startNaviApp(this, SharedPreferenceData.NAVI_APP_PACKAGE.getString(), toast);
	}
	
	protected void clickMusic() {
		pressOneTimeed = false;
		MusicTools.startMusicApp(this, SharedPreferenceData.MUSIC_APP_PACKAGE.getString(), toast);
	}
	
	protected void clickPhone() {
		pressOneTimeed = false;
		try {
			Intent i = new Intent();
			i.setAction(Intent.ACTION_DIAL);
			i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(i);
		} catch (ActivityNotFoundException e) {
			toast.setText(R.string.no_phone_tip);
			toast.show();
		}
		
	}
	
	protected void clickSetting() {
		pressOneTimeed = false;
		Intent i = new Intent();
		i.setClass(this, Setting.class);
		startActivity(i);
		
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK) {
			if(!pressOneTimeed) {
				pressOneTimeed = true;
				toast.setText(R.string.exit_tip);
				toast.show();
				pressAgainTimeout.sendEmptyMessageDelayed(1, 1500);
				return true;
			} else {
				pressAgainTimeout.removeMessages(1);
				finish();
				System.exit(0);
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	
	 @Override
	protected void onDestroy() {
		kit.stopLocProvider(this);
		super.onDestroy();
	}
}
