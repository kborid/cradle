package com.pioneer.PLocProviderKit;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.location.Location;

import com.pioneer.PLocProviderKit.interfaces.RemoteCtrlListener;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.PLocProviderKit.interfaces.LocationListener;
import com.pioneer.PLocProviderKit.interfaces.SatelliteListener;


public class PLocProviderKit {
	
	public static final String KIT_VERSION = "0.0.12";
	
	public static final int CONNECT_STATE_WAITING_SERVICE_REPLY    = -2;
    public static final int CONNECT_STATE_NOT_CONNECT_WITH_SERVICE = -1;	// not connect with service.
    public static final int CONNECT_STATE_NONE 			= 0;	// not connect with gps;
    public static final int CONNECT_STATE_ACCEPT		= 1;	// accept state
    public static final int CONNECT_STATE_CONNECTING	= 2;	// try to connect other remote device
    public static final int CONNECT_STATE_CONNECTED 	= 3; 	// now connected with a remote device

    public static final String CRADLE_ASSIS_PROVIDER_NAME = "PLocProvider";
	public boolean startLocProvider(Context context)	{
		return PLocProvider.getInstance().startServiceConnection(context);
	}
	public static final String getKitVersion(){
		return KIT_VERSION;
	}
	public boolean	stopLocProvider(Context context){
		return PLocProvider.getInstance().stopServiceConnection(context);
	}
	
	public boolean registerGpsStatusListener(RequiredListener listener){
		return PLocProvider.getInstance().registerGpsStatusListener(listener);
	}
	
	public boolean unregisterGpsStatusListener(RequiredListener listener){
		return PLocProvider.getInstance().unregisterGpsStatusListener(listener);
	}
	
	public boolean	registerLocationListener(LocationListener listener){
		return PLocProvider.getInstance().registerLocationListener(listener);
	}
	public boolean	unregisterLocationListener(LocationListener listener){
		return PLocProvider.getInstance().unRegisterLocationListener(listener);
	}		
	public boolean	registerRemoteCtrlListener(RemoteCtrlListener listener){
		return false;
	}		
	public boolean	unregisterRemoteCtrlListener(RemoteCtrlListener listener){
		return false;
	}		
	public int	getExtDeviceConnectionStatus(){
		return PLocProvider.getInstance().GetExtDeviceConnectionStatus();
	}

	public Location	getLatestLocation(){
		return PLocProvider.getInstance().GetLatestLocation();
	}
	
	public boolean registerSatelliteListener(SatelliteListener listener){
		return PLocProvider.getInstance().registerSatelliteListener(listener);
	}

	public boolean unregisterSatelliteListener(SatelliteListener l){
		return PLocProvider.getInstance().unregisterSatelliteListener(l);
	}
	
	public String getListenerInfo() {
		return PLocProvider.getInstance().getListenerInfo();
	}
	
	public BluetoothDevice getConnectedDevice(){
		return PLocProvider.getInstance().getConnectedDevice();
	}
	
	/**
	 * show alert with coordinate x, y.
	 * */
	public void showAlert(Context c) {
		AlertWindow.getInstance().showAlert(c, 0, 0);
	}
	
	/**
	 * dismiss alert
	 * */
	public void dismissAlert() {
		AlertWindow.getInstance().dismissAlert();
	}
	

}
