package com.pioneer.cradle.PLocProvider;


import android.app.Service;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.os.RemoteException;
import android.text.TextUtils;
import android.widget.Toast;

import com.pioneer.PLocProviderKit.RemoteOrder;
import com.pioneer.PLocProviderKit.aidl.ListenerRegister;
import com.pioneer.PLocProviderKit.aidl.pOnCallBackListener;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;
import com.pioneer.cradle.nmea.NMEA0183Kit;

public class PLocProviderService extends Service{
	
	private long restartTime = 0;

	
	private IServiceAIDL mService = new IServiceAIDL();
	
	@Override
	public IBinder onBind(Intent intent) {
		return mService;
	}
	@Override
	public boolean onUnbind(Intent intent) {
		return super.onUnbind(intent);
	}
	
	@Override
	public void onRebind(Intent intent) {
		super.onRebind(intent);
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(intent == null) {
			restartTime = System.currentTimeMillis();
		}
		
		
		if(System.currentTimeMillis()-restartTime > 5000 && ConntectedControl.onStart(this, intent)) {
			return START_STICKY;
		}
		
		if(ConntectHelper.Instance().isNoConnect()) {
			startExternalGPSConnect();
			
		} 
		return START_STICKY;
	}
	
	private void startExternalGPSConnect(){
		if(ConntectHelper.Instance().isBTEnabled()) {
//	        ConntectHelper.Instance().autoConnect();
			ConntectHelper.Instance().beginAcceptConnect();
		} 
	}
	
	private void stopExternalGPSConnect() {
		ConntectHelper.Instance().sendStopCradleMsg();
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		acquireWakeLock();
		ConntectHelper.Instance().setConntectedNum(0);
	}
	
	@Override
	public void onDestroy() {
		recordLastLocationInfo();
		stopExternalGPSConnect();
		releaseWakeLock();
		ConntectHelper.Instance().setConntectedNum(0);
		super.onDestroy();
	}
	
	private class IServiceAIDL extends ListenerRegister.Stub{

		@Override
		public int pGetExtDeviceConnectionStatus() throws RemoteException {
			return ConntectHelper.Instance().getState();
		}

		@Override
		public boolean pRegisterListener(String arg0, pOnCallBackListener arg1)
				throws RemoteException {
			return ServiceCallbackControl.getInstance().addCallbackListener(arg0, arg1);
		}

		@Override
		public boolean pUnregisterListener(String arg0, pOnCallBackListener arg1)
				throws RemoteException {
			return ServiceCallbackControl.getInstance().removeCallbackListener(arg0);
		}

		@Override
		public Location pGetLatestLocation() throws RemoteException {
			
			Location tmpData = NMEA0183Kit.getLocation();
			if((Double.compare(tmpData.getLatitude(), 0)>0 && Double.compare(tmpData.getLongitude(), 0)>0)) {
				return tmpData;
			}
			return null;
		}

		@Override
		public BluetoothDevice getConnectedDevice() throws RemoteException {
			BluetoothDevice info = ConntectHelper.Instance().getConnectedDevice();
			return (info != null) ? info : null;
		}

		@Override
		public Bundle order(Bundle arg0) throws RemoteException {
			return RemoteOrder.order(arg0);
		}
		
	}
	
	private WakeLock mWakeLock;
	private void acquireWakeLock()	{
		if (null == mWakeLock)
		{
			PowerManager pm = (PowerManager)this.getSystemService(Context.POWER_SERVICE);
			mWakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK|PowerManager.ON_AFTER_RELEASE, "PLocProvider");
			if (null != mWakeLock)
			{
				mWakeLock.acquire();
			}
		}
	}
	private void releaseWakeLock()	{
		if (null != mWakeLock)
		{
			mWakeLock.release();
			mWakeLock = null;
		}
	}
	
	private void recordLastLocationInfo(){
		String CradleMessage = NMEA0183Kit.getCradleMessage();
		if(!TextUtils.isEmpty(CradleMessage)) {
			SharedPreferenceData.CRADLE_BACKUP_MESSAGE.setValue(CradleMessage);
		}
	}
	
}
