package com.pioneer.cradle.PLocProvider;

import java.util.Arrays;
import java.util.HashMap;

import android.location.Location;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import com.pioneer.PLocProviderKit.aidl.pOnCallBackListener;
import com.pioneer.PLocProviderKit.util.SatelliteData;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.btConnection.GpsLog;
import com.pioneer.cradle.nmea.NMEA0183Kit;

public class ServiceCallbackControl {
	
	private static ServiceCallbackControl instance = new ServiceCallbackControl();
	
	
	private RemoteCallbackList<pOnCallBackListener> callback;
	private HashMap<String, callbackNode> callbackNodeList;
	
	public static ServiceCallbackControl getInstance(){return instance;}
	
	private Location gpsData;
	private SatelliteData[] sates;
	
	private ServiceCallbackControl(){
		callback = new RemoteCallbackList<pOnCallBackListener>();
		callbackNodeList = new HashMap<String, ServiceCallbackControl.callbackNode>();
	};
	
	private static final int deliveryLocationEventString = 1;
	private static final int deliveryLocationEvent = 2;
	private static final int deliveryLocationSatellites = 3;
	private static final int deliveryRemoteEvent = 4;
	private static final int deliveryRequiredEvent = 5;
	
	private Handler handler = new Handler(Looper.getMainLooper()){
		public void handleMessage(android.os.Message msg) {
			switch(msg.what){
			case deliveryLocationEventString:
				deliveryLocationEventForMainLooper((String)msg.obj);
				break;
			case deliveryLocationEvent:
				deliveryLocationEventForMainLooper((Location)msg.obj);
				break;
			case deliveryLocationSatellites:
				deliveryLocationSatellitesForMainLooper((SatelliteData[])msg.obj);
				break;
			case deliveryRemoteEvent:
				deliveryRemoteEventForMainLooper(msg.arg1);
				break;
			case deliveryRequiredEvent:
				deliveryRequiredEventForMainLooper(msg.arg1);
				break;
			}
		};
	};
	
	public void deliveryLocationEventFromNmea(String nmea){
		Message msg = handler.obtainMessage(deliveryLocationEventString,  nmea);
		handler.sendMessage(msg);
	}
	
	public void deliveryRemoteEvent(int commandID){
		Message msg = handler.obtainMessage(deliveryRemoteEvent, commandID, 0);
		handler.sendMessage(msg);
	}
	
	public void deliveryRequiredEvent(int curState){
		Message msg = handler.obtainMessage(deliveryRequiredEvent, curState, 0);
		handler.sendMessage(msg);
	}
	
	private void deliveryLocationEvent(Location info){
		Message msg = handler.obtainMessage(deliveryLocationEvent, info);
		handler.sendMessage(msg);
	}
	
	private void deliveryLocationSatellites(SatelliteData[] sates){
		Message msg = handler.obtainMessage(deliveryLocationSatellites, sates);
		handler.sendMessage(msg);
	}
	
	public void deliveryLocationToOtherApp() {
		
		Location tmpData;
		SatelliteData[] tempSates;
		tmpData = NMEA0183Kit.getLocation();
		tempSates = NMEA0183Kit.getSatalliteData();
		
		if(Double.compare(tmpData.getLatitude(), 0)>0 && Double.compare(tmpData.getLongitude(), 0)>0){
			if(!compareLocation(gpsData, tmpData)) {
				gpsData = tmpData;
				deliveryLocationEvent(gpsData);
			}
		}
		
		if(tempSates!=null && !Arrays.equals(sates, tempSates)) {
			sates = tempSates;
			deliveryLocationSatellites(tempSates);
		}
	}
	
	private boolean compareLocation(Location l1, Location l2){
		if(l1 == null || l2==null){
			return false;
		}
		
		return (l1.getTime()==l2.getTime());
	}

	
	private void deliveryLocationEventForMainLooper(String nmea){
		int count = callback.beginBroadcast();
		ConntectHelper.Instance().setConntectedNum(count);

		while(count > 0){
			count--;
			pOnCallBackListener cb = callback.getBroadcastItem(count);
			try {
				cb.onReceiveLocationInfo(nmea);
			} catch (RemoteException e) {
				removeCallbackNode((String)callback.getBroadcastCookie(count));
				e.printStackTrace();
				Log.d("duan","deliveryLocationEvent error");
			}
		}
		callback.finishBroadcast();
	}
	
	
	private void deliveryLocationEventForMainLooper(Location info){

		int count = callback.beginBroadcast();
		while(count > 0){
			count--;
			pOnCallBackListener cb = callback.getBroadcastItem(count);
			try {
				cb.onLocationChanged(info);
				
				GpsLog.getInstance().writeGpslogfile(info.toString());
				
			} catch (RemoteException e) {
				removeCallbackNode((String)callback.getBroadcastCookie(count));
				e.printStackTrace();
				Log.d("duan","deliveryLocationEvent error");
			}
		}
		callback.finishBroadcast();
	}
	
	private void deliveryLocationSatellitesForMainLooper(SatelliteData[] sates){

		int count = callback.beginBroadcast();
		while(count > 0){
			count--;
			pOnCallBackListener cb = callback.getBroadcastItem(count);
			try {
				if(sates != null && sates.length != 0) {
					cb.onSatelliteChanged(SatelliteData.getInputStream(sates));
					
					GpsLog.getInstance().writeGpslogfile(Arrays.toString(sates));

				}
			} catch (RemoteException e) {
				removeCallbackNode((String)callback.getBroadcastCookie(count));
				e.printStackTrace();
				Log.d("duan","deliveryLocationEvent error");
			}
		}
		callback.finishBroadcast();
	}
	

	public void deliveryRemoteEventForMainLooper(int commandID){
		int count = callback.beginBroadcast();
		while(count > 0){
			count--;
			pOnCallBackListener cb = callback.getBroadcastItem(count);
			try {
				cb.onReceiveRemoteCtrl(commandID);
			} catch (RemoteException e) {
				removeCallbackNode((String)callback.getBroadcastCookie(count));
				e.printStackTrace();
				Log.d("duan","RemoteCtrlCallbackList error");
			}
		}
		callback.finishBroadcast();
	}
	
	public void deliveryRequiredEventForMainLooper(int curState){
		int count = callback.beginBroadcast();
		while(count > 0){
			count--;
			pOnCallBackListener cb = callback.getBroadcastItem(count);
			try {
				cb.onExtDeviceConnectStateChanged(curState);
			} catch (RemoteException e) {
				removeCallbackNode((String)callback.getBroadcastCookie(count));
				e.printStackTrace();
				Log.d("duan","deliveryRequiredEvent error");
			}
		}
		callback.finishBroadcast();
	}

	private void removeCallbackNode(String appName){
		if(!callbackNodeList.containsKey(appName)){
			return;
		}
		
		callbackNode node = callbackNodeList.get(appName);
		if(null == node){
			return;
		}
		if(null != node.getCallbackNode()){
			callback.unregister(node.getCallbackNode());
		}
		callbackNodeList.remove(node);
	}
	
	public boolean addCallbackListener(String app, pOnCallBackListener l){
		callback.register(l, app);
		ConntectHelper.Instance().setConntectedNum(1);
		return true;
	}
	
	public boolean removeCallbackListener(String app) {
		if(!callbackNodeList.containsKey(app)){
			return false;
		}
		callbackNode node = callbackNodeList.get(app);
		if(null == node){
			return false;
		}
		pOnCallBackListener l = node.getCallbackNode();
		if(null == l){
			return false;
		}
		callback.unregister(l);
		node.setCallbackListener(null);
		if(node.isEmpty()){
			callbackNodeList.remove(node);
		}
		
		return true;
	}
	
	protected class callbackNode{
		String key;
		pOnCallBackListener callbackListener;
		
		callbackNode(String key){
			this.key = key;
		}

		public boolean isEmpty() {
			return null == callbackListener;
		}

		public pOnCallBackListener getCallbackNode() {
			return callbackListener;
		}

		public void setCallbackListener(pOnCallBackListener Callback) {
			this.callbackListener = Callback;
		}
		
		
	}
}
