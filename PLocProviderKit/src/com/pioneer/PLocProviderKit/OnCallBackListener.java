package com.pioneer.PLocProviderKit;

import java.util.ArrayList;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;

import com.pioneer.PLocProviderKit.aidl.pOnCallBackListener;
import com.pioneer.PLocProviderKit.interfaces.LocationListener;
import com.pioneer.PLocProviderKit.interfaces.RemoteCtrlListener;
import com.pioneer.PLocProviderKit.interfaces.RequiredListener;
import com.pioneer.PLocProviderKit.interfaces.SatelliteListener;
import com.pioneer.PLocProviderKit.util.SatelliteData;

class OnCallBackListener extends pOnCallBackListener.Stub{

	ArrayList<LocationListener> locListener = new ArrayList<LocationListener>(10);
	ArrayList<RequiredListener> reqListener = new ArrayList<RequiredListener>(10);
	ArrayList<RemoteCtrlListener> remoteListener = new ArrayList<RemoteCtrlListener>(10);
	ArrayList<SatelliteListener> satelliteListener = new ArrayList<SatelliteListener>(10);
	
	private MainLoopHandle handler;
	
	public OnCallBackListener() {
		handler = new MainLoopHandle();
	}
	
	public boolean registerGpsStatusListener(RequiredListener listener){
		if(listener != null) {
			Message msg = handler.obtainMessage(addStateListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		
		return false;
	}
	
	public boolean unregisterGpsStatusListener(RequiredListener listener){
		if(listener != null && reqListener.contains(listener)) {
			Message msg = handler.obtainMessage(removeStateListenr, listener);
			handler.sendMessage(msg);
			return true;
		} else {
			return false;
		}
	}
	
	public boolean	registerLocationListener(LocationListener listener){
		if(listener != null) {
			Message msg = handler.obtainMessage(addLocationeListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		Log.e("PLocProvider","onCallBackListener::LocationListener is null");
		return false;
	}
	public boolean	unregisterLocationListener(LocationListener listener){
		if(listener != null && locListener.contains(listener)) {
			Message msg = handler.obtainMessage(removeLocationeListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		
		return false;
	}	
	
	public boolean	registerRemoteCtrlListener(RemoteCtrlListener listener){
		if(listener != null) {
			Message msg = handler.obtainMessage(addRemoteListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		Log.e("PLocProvider","onCallBackListener::RemoteCtrlListener is null");
		return false;
	}		
	public boolean	unregisterRemoteCtrlListener(RemoteCtrlListener listener){
		if(listener != null && remoteListener.contains(listener)) {
			Message msg = handler.obtainMessage(removeRemoteListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		
		return false;
	}
	
	public boolean registerSatelliteListener(SatelliteListener listener){
		if(listener != null) {
			Message msg = handler.obtainMessage(addSatelliteListenr, listener);
			handler.sendMessage(msg);
			return true;
		}
		Log.e("PLocProvider","onCallBackListener::SatelliteListener is null");
		return false;
	}

	public boolean unregisterSatelliteListener(SatelliteListener l){
		if(l != null && satelliteListener.contains(l)) {
			Message msg = handler.obtainMessage(removeSatelliteListenr, l);
			handler.sendMessage(msg);
			return true;
		}
		return false;
	}
	
	public void unregisterAllListener() {
		Message msg = handler.obtainMessage(clearAll);
		handler.sendMessage(msg);
	}

	@Override
	public void onReceiveRemoteCtrl(int info) throws RemoteException {
		
		if(!remoteListener.isEmpty()) {
			Message msg = handler.obtainMessage(onReceiveRemoteCtrl, info);
			handler.sendMessage(msg);
		}
	}
	@Override
	public void onReceiveLocationInfo(String nmea)
			throws RemoteException {
		
		if(!locListener.isEmpty()) {
			Message msg = handler.obtainMessage(onReceiveLocationInfo, nmea);
			handler.sendMessage(msg);
		}

	}
	@Override
	public void onLocationChanged(Location l) throws RemoteException {
		if(!locListener.isEmpty() || AlertWindow.getInstance().isShowing()) {
			Message msg = handler.obtainMessage(onLocationChanged, l);
			handler.sendMessage(msg);
		}
	}
	@Override
	public void onExtDeviceConnectStateChanged(int connectState)
			throws RemoteException {
		if(!reqListener.isEmpty() || AlertWindow.getInstance().isShowing()) {
			Message msg = handler.obtainMessage(onExtDeviceConnectStateChanged, connectState);
			handler.sendMessage(msg);
		}
	}
	@Override
	public void onSatelliteChanged(String satellites) throws RemoteException {
		if(satellites == null) {
			return;
		}
		if(!satelliteListener.isEmpty()) {
			Message msg = handler.obtainMessage(onSatelliteChanged, satellites);
			handler.sendMessage(msg);
		}
	}

	@Override
	public String toString() {
		return "All LocationListener = " + locListener.toString() 
				+ ", All RequiredListener = "+reqListener.toString()
				+", All RemoteCtrlListener = "+remoteListener.toString()
				+", All SatelliteListener = "+satelliteListener.toString();
	}
	
	private static final int onReceiveRemoteCtrl = 1;
	private static final int onReceiveLocationInfo = 2;
	private static final int onLocationChanged = 3;
	private static final int onExtDeviceConnectStateChanged = 4;
	private static final int onSatelliteChanged = 5;
	
	private static final int addStateListenr = 6;
	private static final int addLocationeListenr = 7;
	private static final int addRemoteListenr = 8;
	private static final int addSatelliteListenr = 9;
	private static final int removeStateListenr = 10;
	private static final int removeLocationeListenr = 11;
	private static final int removeRemoteListenr = 12;
	private static final int removeSatelliteListenr = 13;
	
	private static final int clearAll = 14;
	
	class MainLoopHandle extends Handler{
		MainLoopHandle(){
			super(Looper.getMainLooper());
		}
		
		@Override
		public void handleMessage(Message msg) {
			
			if(msg.what == onReceiveRemoteCtrl){
				for(RemoteCtrlListener l : remoteListener) {
					l.onReceiveRemoteCtrl((Integer)msg.obj);
				}
			} else if(msg.what == onReceiveLocationInfo) {
				for(LocationListener l : locListener) {
					String info = (String)msg.obj;
					l.onReceiveLocationInfo(info);
				}
			} else if(msg.what == onLocationChanged) {
				for(LocationListener listener : locListener) {
					listener.onReceiveLocationInfo((Location)msg.obj);
				}
				
				if(AlertWindow.getInstance().isShowing()) {
					AlertWindow.getInstance().freshView((Location)msg.obj);
				}
			} else if(msg.what == onExtDeviceConnectStateChanged) {
				for(RequiredListener listener : reqListener) {
					listener.onExtDeviceConnectStateChanged((Integer)msg.obj);
				}
				
				if(AlertWindow.getInstance().isShowing()) {
					AlertWindow.getInstance().freshView((Integer)msg.obj);
				}

			} else if(msg.what == onSatelliteChanged) {
				String satellites = (String)msg.obj;
				if(satellites == null) {
					return;
				}
				for(SatelliteListener listener : satelliteListener) {
					listener.onRecive(SatelliteData.getObject(satellites));

				}
			} else if(msg.what == addStateListenr) {
				Object listener = msg.obj;
				if(listener instanceof RequiredListener) {
					if(!reqListener.contains(listener)) {
						reqListener.add((RequiredListener)listener);
						if(listener != null) {
							((RequiredListener)listener).onExtDeviceConnectStateChanged(
									PLocProvider.getInstance().GetExtDeviceConnectionStatus());
						}
					}
				}
			} else if(msg.what == addLocationeListenr) {
				Object listener = msg.obj;
				if(listener instanceof LocationListener) {
					if(!locListener.contains(listener)) {
						locListener.add((LocationListener) listener);
					}
				} 
			} else if(msg.what == addRemoteListenr) {
				Object listener = msg.obj;
				if(listener instanceof RemoteCtrlListener) {
					if(!remoteListener.contains(listener)) {
						remoteListener.add((RemoteCtrlListener) listener);
					}
				}
			} else if(msg.what == addStateListenr){
				Object listener = msg.obj;
				if(listener instanceof SatelliteListener) {
					if(!satelliteListener.contains(listener)) {
						satelliteListener.add((SatelliteListener) listener);
					}
				}
			} else if(msg.what == removeStateListenr) {
				Object listener = msg.obj;
				if(listener instanceof RequiredListener) {
					reqListener.remove((RequiredListener)listener);
				}
			} else if(msg.what == removeLocationeListenr) {
				Object listener = msg.obj;
				if(listener instanceof LocationListener) {
					locListener.remove((LocationListener) listener);
				}
			} else if(msg.what == removeRemoteListenr) {
				Object listener = msg.obj;
				if(listener instanceof RemoteCtrlListener) {
					remoteListener.remove((RemoteCtrlListener) listener);
				} 
			} else if(msg.what == removeStateListenr) {
				Object listener = msg.obj;
				if(listener instanceof SatelliteListener) {
					satelliteListener.remove((SatelliteListener) listener);
				}
			} else if(msg.what == clearAll) { 
				locListener.clear();
				remoteListener.clear();
				reqListener.clear();
				satelliteListener.clear();
			}
		}
	}
	
	
}
