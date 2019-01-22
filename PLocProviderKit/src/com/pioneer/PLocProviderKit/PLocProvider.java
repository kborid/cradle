package com.pioneer.PLocProviderKit;


import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.pioneer.PLocProviderKit.aidl.ListenerRegister;
import com.pioneer.PLocProviderKit.interfaces.RemoteCtrlListener;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.PLocProviderKit.interfaces.LocationListener;
import com.pioneer.PLocProviderKit.interfaces.SatelliteListener;

final class PLocProvider {

	private static PLocProvider instance;
	private ListenerRegister myService = null;
	private String packageName = "";
	private boolean hasConnectedToService;
	private static final String TAG = "PLocProvider_message";
//	private boolean m_bQuit = true;
	
	/*package*/ ServiceConnectListener listener;
	
	private PLocProvider(){
	};
	
	/*package*/ static PLocProvider getInstance(){
		if(null == instance){
			instance = new PLocProvider();
		}
		return instance;
	}
	
	private ServiceConnection serviceConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName name, IBinder service) {
			myService = ListenerRegister.Stub.asInterface(service); 
			try {
				myService.pRegisterListener(packageName, onCallBackListener);
				if(listener != null) {
					listener.onServiceConnected(name);
				}
				onCallBackListener.onExtDeviceConnectStateChanged(GetExtDeviceConnectionStatus());
				if(AlertWindow.getInstance().isShowing()) {
					AlertWindow.getInstance().freshView(GetExtDeviceConnectionStatus());
				}
			} catch (RemoteException e) {
				e.printStackTrace();
				Log.d("DEBUG","onServiceConnected remote exception");
			}
			
		}
		
		public void onServiceDisconnected(ComponentName name) {
			try {
				onCallBackListener.onExtDeviceConnectStateChanged(-1);
			} catch (RemoteException e) {
				e.printStackTrace();
			}
		}
		
	};
	private OnCallBackListener onCallBackListener = new OnCallBackListener();
	
	/*package*/ final boolean startServiceConnection(Context context, ServiceConnectListener listener){
		boolean ret = startServiceConnection(context);
		if(ret) {
			this.listener = listener;
			return true;
		}
		return false;
	}
	
	/*package*/ final boolean startServiceConnection(Context context){
		if(hasConnectedToService){
			return false;
		}
		if(null == context){
			return false;
		}
		packageName = context.getPackageName();

		boolean returnValue = false;
		try {
			Class clazz = Class.forName("com.pioneer.cradle.PLocProvider.PLocProviderService");
			Intent serviceIntent = new Intent("com.pioneer.cradle.PLocProvider.PLocProviderService");
			serviceIntent.setClass(context, clazz);
			returnValue = context.getApplicationContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}

		if(!returnValue){
			Log.e("test","Connect Failed");
			return false;
		}else{
			Log.e("test","Connect Success!");
			hasConnectedToService = true;
		}
		return true;
	}
	
	/*package*/ final boolean stopServiceConnection(Context context){
		if(null == context){
			Log.e("PLocProvider","stopServiceConnection::context == null");
			return false;
		}
		
		if(!hasConnectedToService) {
			Log.e("PLocProvider","stopServiceConnection::has not connect to service");
			return false;
		}
		
		try {
			if(myService!=null){
				myService.pUnregisterListener(packageName, onCallBackListener);
			}
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.e("PLocProvider","stopServiceConnection::remote exception");
			return false;
		}
		
		onCallBackListener.unregisterAllListener();
		try{
			hasConnectedToService = false;
			context.getApplicationContext().unbindService(serviceConnection);
		} catch(IllegalArgumentException e) {
			Log.e("PLocProvider","stopServiceConnection::service has not started so don't need to stop it");
			return false;
		}
		return true;
	}
	
	/*package*/ final String getListenerInfo() {
		return onCallBackListener.toString();
	}

	/*package*/ final boolean registerLocationListener(LocationListener listener) {
		return onCallBackListener.registerLocationListener(listener);
	}
	
	/*package*/ final boolean unRegisterLocationListener(LocationListener listener) {
		return onCallBackListener.unregisterLocationListener(listener);
		
	}

	/*package*/ final boolean RegisterRemoteCtrlListener(RemoteCtrlListener listener) {
		return onCallBackListener.registerRemoteCtrlListener(listener);

	}
	
	/*package*/ final boolean UnregisterRemoteCtrlListener(RemoteCtrlListener listener) {
		return onCallBackListener.unregisterRemoteCtrlListener(listener);

	}
	
	/*package*/ final boolean registerGpsStatusListener(RequiredListener listener) {
//		if(listener != null) {
//			listener.onExtDeviceConnectStateChanged(GetExtDeviceConnectionStatus());
//		}
		return onCallBackListener.registerGpsStatusListener(listener);
	}

	/*package*/ final boolean unregisterGpsStatusListener(RequiredListener listener) {
		return onCallBackListener.unregisterGpsStatusListener(listener);
	}
	
	/*package*/ final boolean registerSatelliteListener(SatelliteListener listener){
		return onCallBackListener.registerSatelliteListener(listener);
	}

	/*package*/ final boolean unregisterSatelliteListener(SatelliteListener l){
		return onCallBackListener.unregisterSatelliteListener(l);
	}
	
	/*package*/ int GetExtDeviceConnectionStatus() {
		if(!hasConnectedToService) {
			return PLocProviderKit.CONNECT_STATE_NOT_CONNECT_WITH_SERVICE;
		}
		if(hasConnectedToService && myService==null){
			return PLocProviderKit.CONNECT_STATE_WAITING_SERVICE_REPLY;
		}
		try {
			return myService.pGetExtDeviceConnectionStatus();
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.d(TAG, "pUnregisterLocationListener:RemoteException");
			hasConnectedToService = false;
			return PLocProviderKit.CONNECT_STATE_NOT_CONNECT_WITH_SERVICE;
		}
	}
	
	/*package*/ Location GetLatestLocation() {
		if(!hasConnectedToService || myService==null){
			return null;
		}
		try {
			return myService.pGetLatestLocation();
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.d(TAG, "pUnregisterLocationListener:RemoteException");
			return null;
		}
	}

	/*package*/ BluetoothDevice getConnectedDevice() {
		if(!hasConnectedToService || myService==null){
			return null;
		}
		try {
			return myService.getConnectedDevice();
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.d(TAG, "pUnregisterLocationListener:RemoteException");
			return null;
		}
	}
	
	/*package*/ Bundle order(Bundle order){
		if(!hasConnectedToService || myService==null){
			return null;
		}
		try {
			return myService.order(order);
		} catch (RemoteException e) {
			e.printStackTrace();
			Log.d(TAG, "pUnregisterLocationListener:RemoteException");
			return null;
		}
	}

	
}
