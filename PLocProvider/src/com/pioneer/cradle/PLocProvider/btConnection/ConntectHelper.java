package com.pioneer.cradle.PLocProvider.btConnection;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

public class ConntectHelper {

	// External Bluetooth connect
	private final BtGpschannel CradleBtGPS;
	
	private static final ConntectHelper mInstance = new ConntectHelper();
	
	private Handler connectingHandler = new Handler(){
		private int i = 0;
		public void handleMessage(Message msg) {
			if(msg.what != 0 && msg.what != 1) {
				return;
			}
			
			if(msg.what == 0) {
				i = 0;
			}
			
			if(!isConnected()) {
				BluetoothDevice dev = (BluetoothDevice) msg.obj; 
				ConntectHelper.Instance().sendStartCradleMsg(dev);
				if(i < 5) {
					Message msg0 = Message.obtain(msg);
					if(msg0.what != 1) {
						msg0.what = 1;
					}
					connectingHandler.sendMessageDelayed(msg0, 5000);
				} else {
					i = 0;
				}
				i++;
			}
			
		};
		
	};

	
	private ConntectHelper() {
		CradleBtGPS = BtGpschannel.instance();
	}
	
	public static ConntectHelper Instance() {
		return mInstance;
	}
	
	/*
	 *  Get Bluetooth device information
	 */
	public BluetoothDevice getConnectedDevice() {
		return CradleBtGPS.getDevice();
	}
	

	public void beginAcceptConnect(BluetoothDevice device) {
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_AcceptConnect, device);
		handler.sendMessage(mgs);
	}
	public boolean beginAcceptConnect(){
		Log.i("duan", "beginAcceptConnect()");
		if(isConnected()){
			return true;
		}
		
		BluetoothDevice dev = DeviceTools.getLastDevice();
		if (null != dev) {
			beginAcceptConnect(dev);
			return true;
		} else {
			return false;
		}
	}
	public void sendStartCradleMsg(BluetoothDevice device) {
		// send message to start external GPS of Cradle Bluetooth
		ConnectLog.getInstance().writeGpslogfile("connect device="+device);
		if(device != null) {
			CLocationListenerHandler handler = GetHandler();
			Message mgs = handler.obtainMessage(MSG_StartExternalGps, device);
			handler.sendMessage(mgs);
		}
	}

	public void sendStartCradleMsgWithStopCurrentConnect(BluetoothDevice device) {
		// send message to start external GPS of Cradle Bluetooth
		if(device != null) {
			CLocationListenerHandler handler = GetHandler();
			Message mgs = handler.obtainMessage(MSG_StartExternalGpsWithStopCurrentConnect, device);
			handler.sendMessage(mgs);
		}
	}

	public boolean sendStartCradleTry5times(BluetoothDevice device) {
		ConnectLog.getInstance().writeGpslogfile("try 5 times connect device="+device);
		
		boolean alreadyTry = connectingHandler.hasMessages(0)||connectingHandler.hasMessages(1);
		connectingHandler.removeMessages(0);
		connectingHandler.removeMessages(1);
		sendStartCradleMsg(device);
		
		Message msg = connectingHandler.obtainMessage(0, device);
		connectingHandler.sendMessageDelayed(msg, 5000);
		return alreadyTry;
	}

	public void sendStopCradleMsg() {
		// send message to stop external GPS of Cradle Bluetooth
		
		ConnectLog.getInstance().writeGpslogfile("stop connect device");
		connectingHandler.removeMessages(0);
		connectingHandler.removeMessages(1);
		
		CLocationListenerHandler handler = GetHandler();
		Message mgs = handler.obtainMessage(MSG_StopExternalGps);
		handler.sendMessage(mgs);
	}

	public boolean isBTEnabled() {
		return CradleBtGPS.isBTEnabled();
	}

	public void setConntectedNum(int num) {
		CradleBtGPS.setConntectedNum(num);
//		Log.i("duan", "num = " + num);
		if(num != 0 && isNoConnect()) {
			beginAcceptConnect();
		}
	}

	public boolean isConnected() {
		return CradleBtGPS.getState() == BtGpschannel.STATE_CONNECTED;
	}
	
	public int getState() {
		return CradleBtGPS.getState();
	}
	
	public boolean isConnecting() {
		return CradleBtGPS.getState() == BtGpschannel.STATE_CONNECTING;
	}
	
	public boolean isNoConnect() {
		return CradleBtGPS.getState() == BtGpschannel.STATE_NONE;
	}
	
	public boolean isAccepting() {
		return CradleBtGPS.getState() == BtGpschannel.STATE_ACCEPT;
	}
	
	public boolean autoConnect() {
		if(isConnected()){
			return true;
		}
		BluetoothDevice d = DeviceTools.getLastDevice();
		if(d != null) {
			sendStartCradleTry5times(d);
			return true;
		} else {
			return false;
		}
	}

	/*==================================================================*/
	/*
	 * Open and close GPS or Bluetooth in Asynchronous
	 */
	// declare location listener handler class
	private static class CLocationListenerHandler extends Handler {
		public CLocationListenerHandler() {
			super(Looper.getMainLooper());
		}

		public void handleMessage(Message msg) {
			ConntectHelper.Instance().handleMessage(msg);
		}
	}

	// handler message object
	private static final CLocationListenerHandler m_scLocationListenerHandler = new CLocationListenerHandler();

	// CLocationListenerHandler_MSG_ID
	private static final int MSG_OpenInternalGps 	= 800;
	private static final int MSG_CloseInternalGps 	= 801;
	private static final int MSG_StartExternalGps 	= 802;
	private static final int MSG_StartExternalGpsWithStopCurrentConnect 	= 804;
	private static final int MSG_StopExternalGps 	= 803;
	private static final int MSG_AcceptConnect		= 805;

	// handler singleton object new
	private CLocationListenerHandler GetHandler() {
		return m_scLocationListenerHandler;
	}

	// handle message call by CLocationListenerHandler.handleMessage
	private void handleMessage(Message msg) {
		// handle
		switch (msg.what) {
		case MSG_StartExternalGps:
			// file log
			try {
				openExternalCradld((BluetoothDevice)msg.obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MSG_StopExternalGps:
			// file log
			try {
				closeExternalCradle();
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MSG_StartExternalGpsWithStopCurrentConnect:
			// file log
			try {
				openExternalCradldWithStopCurrentConnect((BluetoothDevice)msg.obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		case MSG_AcceptConnect:
			// file log
			try {
				Log.i("duan","acceptCradld()");
				acceptCradld((BluetoothDevice)msg.obj);
			} catch (Exception e) {
				e.printStackTrace();
			}
			break;
		default:
			// ERROR
			break;
		}
	}
	
	private void acceptCradld(BluetoothDevice device) {
		CradleBtGPS.startToAccept(device);
	}
	/*
	 * Open external connect with cradle by Bluetooth
	 */
	private void openExternalCradld(BluetoothDevice device) {
//		CradleBtGPS.startToConnect(device);
		CradleBtGPS.startToAccept(device);
	}
	
	/*
	 * Open external connect with cradle by Bluetooth
	 */
	private void openExternalCradldWithStopCurrentConnect(BluetoothDevice device) {
		CradleBtGPS.startToConnectWithStopCurrentConnect(device);
	}
	
	/*
	 * Close external connect with cradle by Bluetooth
	 */
	private void closeExternalCradle() {
		// Check state
		if (CradleBtGPS.getState() != BtGpschannel.STATE_NONE) {
			// disconnect External Cradle
			CradleBtGPS.stop();
		}
	}


}
