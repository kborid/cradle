package com.pioneer.cradle.PLocProvider.Screen;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.RemoteServiceControl;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.components.DebugView;
import com.pioneer.cradle.PLocProvider.components.DebugView.DebugOnListener;
import com.pioneer.cradle.PLocProvider.components.IconListPreference;
import com.pioneer.cradle.PLocProvider.components.IconListPreference.Item;
import com.pioneer.cradle.PLocProvider.tools.MusicTools;
import com.pioneer.cradle.PLocProvider.tools.NaviTools;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class Setting extends LongClickPreferenceActivity {

	PreferenceScreen rootPref;
	
	Preference bluetoothConnectedPref;
	Preference searchDevicePref;
	CheckBoxPreference useLastLocation;
	CheckBoxPreference enterNaviDirection;
	Preference cleanLearn;
	
	IconListPreference defaultNaviPref;
	IconListPreference defaultMusicPref;
	
	Preference version;
	Preference contract;
	
	PreferenceCategory debug;
	Preference locationInfo;
	Preference cradleSetting;
	Preference debugOff;
	
	PLocProviderKit kit = new PLocProviderKit();
	RequiredListener requiredListener;
	
	private long[] clickVersionCount = new long[10];
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		  addPreferencesFromResource(R.xml.setting_preferencess);
		  
		  rootPref = (PreferenceScreen) findPreference("root");
		  bluetoothConnectedPref = findPreference("bluetooth_connecting");
		  searchDevicePref = findPreference("search_device");
		  useLastLocation = (CheckBoxPreference) findPreference("use_last_location");
		  enterNaviDirection = (CheckBoxPreference) findPreference("enter_navi_direction");
		  
		  defaultNaviPref = (IconListPreference) findPreference("default_navi_app");
		  defaultMusicPref = (IconListPreference) findPreference("default_music_app");
		  cleanLearn = findPreference("clear_learn");
		  version = findPreference("app_version");
		  contract = findPreference("contract");
		  
		  debug = (PreferenceCategory) findPreference("debug");
		  locationInfo = findPreference("location_info");
		  cradleSetting = findPreference("cradle_setting");
		  debugOff = findPreference("debug_off");
		  
		  initPreference();
		  initEnterNaviDirection();
		  setListeners();
		  
		  setTitle(R.string.main_setting);
		  
		  
		  kit.registerGpsStatusListener(requiredListener = new RequiredListener() {
			
			@Override
			public void onExtDeviceConnectStateChanged(int connectState) {
				if(connectState == PLocProviderKit.CONNECT_STATE_CONNECTED) {
					bluetoothConnectedPref.setSummary(getConnectedDevice());
				} else if (connectState == PLocProviderKit.CONNECT_STATE_ACCEPT || connectState == PLocProviderKit.CONNECT_STATE_CONNECTING) {
					bluetoothConnectedPref.setSummary(R.string.connecting);
				}else {
					bluetoothConnectedPref.setSummary(R.string.bluetooth_connect_no_connected);
				}
			}
		});
		  if(!SharedPreferenceData.APP_DEBUG_MENU.getBoolean()) {
			  offDebugItem();
		  }
	}
	
	private void initEnterNaviDirection() {
		if(TextUtils.isEmpty(SharedPreferenceData.NAVI_APP_PACKAGE.getString())) {
			setEnterNaviDrection(false);
		}
		
		enterNaviDirection.setChecked(SharedPreferenceData.ENTER_NAVI_DRECTION.getBoolean());
	}
	
	private void setEnterNaviDrection(boolean value) {
        SharedPreferenceData.ENTER_NAVI_DRECTION.setValue(value);
        ConntectedControl.setSharedPreferenceToService(this, SharedPreferenceData.ENTER_NAVI_DRECTION, value);
	}


	@Override
	public void setContentView(int layoutResID) {
		RelativeLayout layout = new RelativeLayout(this);
		LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
	    inflater.inflate(layoutResID, layout);
		
	    DebugView debugView = DebugView.addDebugView(layout);
	    debugView.setDebugOnListener(new DebugOnListener() {
				
				@Override
				public void onDebugOn() {
					getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
					onDebugItem();
					new Handler(){
						public void handleMessage(android.os.Message msg) {
							getListView().setTranscriptMode(AbsListView.TRANSCRIPT_MODE_NORMAL);
						};
					}.sendEmptyMessage(1);
				}
		});
		setContentView(layout);
		
		Arrays.fill(clickVersionCount, 0);
	}
	
	void onDebugItem(){
		SharedPreferenceData.APP_DEBUG_MENU.setValue(true);
		if(rootPref.findPreference("debug") == null){
			rootPref.addPreference(debug);
		}
		
	}
	
	void offDebugItem(){
		SharedPreferenceData.APP_DEBUG_MENU.setValue(false);
		if(rootPref.findPreference("debug") != null){
			rootPref.removePreference(debug);
		}
	}
	
	private void initPreference() {
		  
		  loadInstalledMusic();
		  defaultMusicPref.setEnties(getMusicList());
		  defaultMusicPref.seletedIndex(getMusicItemIndex());
		  
		  if(mMusicApps.size()==1 && getMusicItemIndex()<0) {
			  setMusicApp(0); 
			  defaultMusicPref.seletedIndex(0);
		  }
		  
		  loadInstalledNavi();
		  defaultNaviPref.setEnties(getNaviList());
		  defaultNaviPref.seletedIndex(getNaviItemIndex());
		  
		  if(mNaviApps.size()==1 && getNaviItemIndex()<0) {
			  setNavicApp(0); 
			  defaultNaviPref.seletedIndex(0);
		  }

		  version.setSummary(getVersionString(this));
		  version.setEnabled(true);
	}
	
	

	/*
	 * 
	 * for music app
	 * 
	 * */
	private List<ResolveInfo> mMusicApps;
	private void loadInstalledMusic() {
        mMusicApps = MusicTools.getMusicApp(this);
	}
	
	private ArrayList<Item> getMusicList() {
		ArrayList<IconListPreference.Item> list = new ArrayList<IconListPreference.Item>(mMusicApps.size());
		for(ResolveInfo info: mMusicApps){
			Drawable icon = info.activityInfo.loadIcon(getPackageManager());
			String name = info.activityInfo.loadLabel(getPackageManager()).toString();
			IconListPreference.Item item = new IconListPreference.Item(icon, name);
			list.add(item);
		}
		
		return list;
	}
	
	private int getMusicItemIndex() {
		String packageName = SharedPreferenceData.MUSIC_APP_PACKAGE.getString();
		
		for(int i=0; i<mMusicApps.size(); i++) {
			if(packageName.equals(mMusicApps.get(i).activityInfo.packageName)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private void setMusicApp(int index) {
		String packageName = mMusicApps.get(index).activityInfo.packageName;
		SharedPreferenceData.MUSIC_APP_PACKAGE.setValue(packageName);
		ConntectedControl.setSharedPreferenceToService(this, SharedPreferenceData.MUSIC_APP_PACKAGE, packageName);
	}
	
	
	/*
	 * for Navi app
	 * */
	private List<ResolveInfo> mNaviApps;
	private void loadInstalledNavi() {
        mNaviApps = NaviTools.getNaviApp(this);
	}
	
	private ArrayList<Item> getNaviList() {
		ArrayList<IconListPreference.Item> list = new ArrayList<IconListPreference.Item>(mMusicApps.size());
		for(ResolveInfo info: mNaviApps){
			Drawable icon = info.activityInfo.loadIcon(getPackageManager());
			String name = info.activityInfo.loadLabel(getPackageManager()).toString();
			IconListPreference.Item item = new IconListPreference.Item(icon, name);
			list.add(item);
		}
		
		return list;
	}

	private int getNaviItemIndex() {
		String packageName = SharedPreferenceData.NAVI_APP_PACKAGE.getString();
		
		for(int i=0; i<mNaviApps.size(); i++) {
			if(packageName.equals(mNaviApps.get(i).activityInfo.packageName)) {
				return i;
			}
		}
		
		return -1;
	}
	
	private void setNavicApp(int index) {
		String packageName = mNaviApps.get(index).activityInfo.packageName;
		SharedPreferenceData.NAVI_APP_PACKAGE.setValue(packageName);
		ConntectedControl.setSharedPreferenceToService(this, SharedPreferenceData.NAVI_APP_PACKAGE, packageName);
	}

	private void setListeners() {
		bluetoothConnectedPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				openBluetoothSetting();
				return true;
			}
			private void openBluetoothSetting() {
				Intent buletoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
				buletoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			    startActivity(buletoothIntent);
			}

		});
		
		searchDevicePref.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				clickBlueToothItem();
				return true;
			}
		});
		
		useLastLocation.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean)newValue;
				SharedPreferenceData.USE_LAST_LOCATION.setValue(isChecked);
				ConntectedControl.setSharedPreferenceToService(Setting.this, SharedPreferenceData.USE_LAST_LOCATION, isChecked);
				return true;
			}
		});
		
		enterNaviDirection.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			Toast toast = Toast.makeText(Setting.this, "", Toast.LENGTH_SHORT);
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean isChecked = (Boolean)newValue;
				if(isChecked && TextUtils.isEmpty(SharedPreferenceData.NAVI_APP_PACKAGE.getString())) {
					toast.setText(R.string.no_navi_set_tip);
					toast.show();
					enterNaviDirection.setChecked(false);
				} else {
					setEnterNaviDrection(isChecked);
				}
				return true;
			}
		});
		
		cleanLearn.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			

			@Override
			public boolean onPreferenceClick(Preference preference) {
				AlertDialog.Builder dialog = new Builder(Setting.this);
				dialog.setTitle(R.string.reset_learn_state);
				dialog.setMessage(R.string.reset_learn_message);
				dialog.setCancelable(false);
				dialog.setNegativeButton(R.string.no,null);
				dialog.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
//						Toast.makeText(this, R.string.clear_learn, Toast.LENGTH_LONG).show();
						RemoteServiceControl.getInstance().clearLearn();
					}
				});
				dialog.show();
				return true;
			}
		});
		
		defaultNaviPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				setNavicApp((Integer)newValue);
				return true;
			}
		});

		defaultMusicPref.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				setMusicApp((Integer)newValue); 
				return true;
			}
		});
		
		contract.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				clickContract();
				return true;
			}
		});
		
		locationInfo.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				clickLocationInfo();
				return true;
			}
		});
		
		version.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				System.arraycopy(clickVersionCount, 0, clickVersionCount, 1, clickVersionCount.length-1);
				clickVersionCount[0] = System.currentTimeMillis();
				if(System.currentTimeMillis()-clickVersionCount[clickVersionCount.length-1]<1700) {
					Dialog dialog = new Dialog(Setting.this);
					dialog.setTitle("(^_^)!!!, you find an egg.");
					ImageView iv = new ImageView(Setting.this);
					iv.setImageResource(R.drawable.easteregg);
					dialog.setContentView(iv);
					dialog.setCanceledOnTouchOutside(false);
					dialog.show();
				}
				return true;
			}
			
		});
		
		cradleSetting.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				Intent i = new Intent(Setting.this, CradleSetting.class);
				startActivity(i);
				return true;
			}
		});
		debugOff.setOnPreferenceClickListener(new OnPreferenceClickListener() {
			
			@Override
			public boolean onPreferenceClick(Preference preference) {
				offDebugItem();
				return true;
			}
		});

	}
	
	
	@Override
	public boolean onPreferenceTreeLongClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if(preference == bluetoothConnectedPref && (kit.getConnectedDevice() != null)) {
			AlertDialog.Builder b = new AlertDialog.Builder(Setting.this);
			b.setMessage(R.string.disconnect_confirm);
			b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					ConntectedControl.stopCradleConnect(Setting.this);
				}
			});
			b.setNegativeButton(android.R.string.cancel, null);
			b.show();
			return true;
		}
		return false;
	}

	protected void clickLocationInfo() {
		Intent i = new Intent(this, CradleGpsInfo.class);
		startActivity(i);
	}

	/**
	 * for bluetooth
	 * */
	private void clickBlueToothItem() {
		Intent i = new Intent(this, BluetoothConnect.class);
		startActivity(i);
	}
	
	private String getConnectedDevice() {
		BluetoothDevice deviceInfo = kit.getConnectedDevice();
		if(deviceInfo == null){
			if(kit.getExtDeviceConnectionStatus() == 1 || kit.getExtDeviceConnectionStatus() == 2){
				return getString(R.string.connecting);
			}else {
				return getString(R.string.bluetooth_connect_no_connected);
			}
		}
		if(!TextUtils.isEmpty(deviceInfo.getName())) {
			return getString(R.string.bluetooth_connect_connected_device) + " " + deviceInfo.getName() +"("+deviceInfo.getAddress()+")";
		} else {
			return getString(R.string.bluetooth_connect_connected_device) + " "  + deviceInfo.getAddress();
		}
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		bluetoothConnectedPref.setSummary(getConnectedDevice());
	}
	
	@Override
	protected void onDestroy() {
		
		kit.unregisterGpsStatusListener(requiredListener);
		super.onDestroy();
	}

	/**
	 * for app version
	 * */
	private static String getVersionString(Activity c){
	    try {
			ComponentName comp = c.getComponentName();
			PackageInfo packinfo = c.getPackageManager().getPackageInfo(comp.getPackageName(), 0);
			return packinfo.versionName;
			  
		} catch (android.content.pm.PackageManager.NameNotFoundException e) {
			return "0.0.0";
		}

	}
	
	/**
	 * for bluetooth
	 * */
	private void clickContract() {
		Intent i = new Intent(this, Contract.class);
		startActivity(i);
	}



}
