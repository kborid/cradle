package com.pioneer.cradle.PLocProvider.Screen;

import java.util.ArrayList;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.R;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.btConnection.DeviceTools;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class BluetoothConnect extends LongClickPreferenceActivity {
    
	private BluetoothDevicePreference connectedDevice;
	private PreferenceCategory pairedDevice;
	
	private PLocProviderKit kit = new PLocProviderKit();
	
	private BroadcastReceiver broadcastReceiver;
	private RequiredListener requiredListener;
	
	private Button cradleSreach;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.cradle_connect);
		initPreference();
		initViews();
		updatePairedDevices();
		registerReceiver();
		setTitle(R.string.bluetooth_connect);
		kit.registerGpsStatusListener(requiredListener = new RequiredListener() {
			@Override
			public void onExtDeviceConnectStateChanged(int connectState) {

				updateConnectionState(connectState);
			}

		});
	}
	private void updateConnectionState(int connectState) {
		if(connectState == PLocProviderKit.CONNECT_STATE_CONNECTED){
			updateConntectedDevice();
			Toast.makeText(BluetoothConnect.this, R.string.connect_scuccess, Toast.LENGTH_SHORT).show();
		} else if(connectState == PLocProviderKit.CONNECT_STATE_NONE) {
			updateConntectedDevice();
			Toast.makeText(BluetoothConnect.this, R.string.connect_fail, Toast.LENGTH_SHORT).show();
		} else if(connectState == PLocProviderKit.CONNECT_STATE_CONNECTING 
				|| connectState == PLocProviderKit.CONNECT_STATE_ACCEPT) {
			BluetoothConnect.this.setDeviceConnecting(kit.getConnectedDevice());
		} else {
			updateConntectedDevice();
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		updatePairedDevices();
		updateConnectionState(kit.getExtDeviceConnectionStatus());
	}
	
	private void initViews() {
		cradleSreach = (Button) findViewById(R.id.cradle_search);
		cradleSreach.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				openBluetoothSetting();
			}
		});
	}
	
	private void openBluetoothSetting() {
		Intent buletoothIntent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
		buletoothIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    startActivity(buletoothIntent);
	}


	@Override
	public void setContentView(int layoutResID) {
		LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    View view = inflater.inflate(R.layout.bluetooth_connect, null);
	    ViewGroup content = (ViewGroup) view.findViewById(R.id.content);
	    inflater.inflate(layoutResID, content);
		super.setContentView(view);
	}
	
	private void initPreference() {
		connectedDevice = new BluetoothDevicePreference(this, kit.getConnectedDevice());
		((PreferenceCategory)findPreference("connected_device")).addPreference(connectedDevice);
		pairedDevice = (PreferenceCategory) findPreference("paried_device");
	}
	
	private void updatePairedDevices(){
		ArrayList<BluetoothDevice> devices = DeviceTools.getBondedDevices();
		pairedDevice.removeAll();
		  
		if(devices == null){
			return;
		}
		for(BluetoothDevice dev: devices) {
			pairedDevice.addPreference(new BluetoothDevicePreference(this, dev));
		}
	}
	
	private void updateConntectedDevice() {
		connectedDevice.setDevice(kit.getConnectedDevice());
	}
	
	private Preference getConnectedDevice(){
		return connectedDevice;
	}
	
	private void setDeviceConnecting(BluetoothDevice device) {
		String devNameString = "";
		if(null != device) {
			devNameString += device.getName();
		}
//		getConnectedDevice().setTitle(devNameString + " " + getString(R.string.connecting));
		getConnectedDevice().setTitle(R.string.connecting);
		getConnectedDevice().setSummary(null);
	}
	
	@Override
	protected void onDestroy() {
		cancelReceiver();
		kit.unregisterGpsStatusListener(requiredListener);
		super.onDestroy();
	}
	
	private void registerReceiver(){
		if(broadcastReceiver== null){
			broadcastReceiver = new BroadcastReceiver() {
				
				@Override
				public void onReceive(Context context, Intent intent) {
					// TODO Auto-generated method stub
					if(intent.getAction().equals(BluetoothAdapter.ACTION_STATE_CHANGED) 
							|| intent.getAction().equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){
						updatePairedDevices();
						updateConntectedDevice();
					}
				}
			};
			
			IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
			filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
			registerReceiver(broadcastReceiver, filter);
			
		}
	}
	
	
	private void cancelReceiver(){
		if(broadcastReceiver != null){
			unregisterReceiver(broadcastReceiver);
			broadcastReceiver = null;
		}
		
	}
	
	
	class BluetoothDevicePreference extends Preference implements Preference.OnPreferenceClickListener{
		private BluetoothDevice device;

		public BluetoothDevicePreference(Context context, BluetoothDevice device) {
			super(context);
			setDevice(device);
			setOnPreferenceClickListener(this);
		}
		
		public final void setDevice(BluetoothDevice device) {
			this.device = device;
			if(device != null) {
				setTitle(device.getName());
				setSummary(device.getAddress());
			} else {
				setTitle(R.string.no_connect);
				if(ConntectHelper.Instance().isBTEnabled()){
					setSummary(null);
				} else {
					setSummary(R.string.bt_off_tip);
				}
			}
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			if(device != null && !device.equals(kit.getConnectedDevice())) {
				ConntectedControl.startCradleAccept(BluetoothConnect.this, device);
				setDeviceConnecting(device);
			} else if(device != null && device.equals(kit.getConnectedDevice())) {
				Toast.makeText(BluetoothConnect.this, R.string.connect_scuccess, Toast.LENGTH_SHORT).show();
			}
			return true;
		}
		
	}


	@Override
	public boolean onPreferenceTreeLongClick(PreferenceScreen preferenceScreen,
			Preference preference) {
		if(preference == connectedDevice && connectedDevice.device != null) {
			AlertDialog.Builder b = new AlertDialog.Builder(BluetoothConnect.this);
			b.setMessage(R.string.disconnect_confirm);
			b.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					
					ConntectedControl.stopCradleConnect(BluetoothConnect.this);
				}
			});
			b.setNegativeButton(android.R.string.cancel, null);
			b.show();
			return true;
		}
		return false;
	}

}
