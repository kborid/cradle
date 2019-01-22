package com.pioneer.cradle.PLocProvider.receiver;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.pioneer.cradle.PLocProvider.ConntectedControl;
import com.pioneer.cradle.PLocProvider.btConnection.ConnectLog;
import com.pioneer.cradle.PLocProvider.btConnection.ConntectHelper;
import com.pioneer.cradle.PLocProvider.btConnection.DeviceTools;

public class PLocBootReceiver extends BroadcastReceiver {

	private Context context = null;
	private BluetoothDevice device = null;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what != 1) {
				return;
			}

			if (device != null) {
				ConntectedControl.startCradleAccept(context, device);
			}
		};
	};

	@Override
	public void onReceive(Context c, Intent intent) {
		this.context = c;
		this.device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
		String actionString = intent.getAction();
		ConnectLog.getInstance().writeGpslogfile("BroadcastReceiver: " + actionString);

		if (!DeviceTools.checkDeviceVaild(device)) {
			return;
		}

		if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(actionString)) {
			startConnect();
		} else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(actionString)) {
			stopConnect();
		} else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(actionString)) {
			int bondStatus = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, -1);
			if (BluetoothDevice.BOND_BONDED == bondStatus) {
				startConnect();
			} else if (BluetoothDevice.BOND_NONE == bondStatus) {
				stopConnect();
			} else {
				Log.d("duan", "bonding~~");
			}
		} else if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(actionString)) {
			int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1);
			if (state == BluetoothAdapter.STATE_TURNING_OFF) {
				stopConnectedForce();
			}
		}
	}

	private void startConnect() {
		if (!handler.hasMessages(1)) {
			handler.sendEmptyMessage(1);
		}
	}

	private void stopConnect() {

		if (ConntectHelper.Instance().isNoConnect()) {
			return;
		}

		BluetoothDevice conntectedDevice = ConntectHelper.Instance().getConnectedDevice();
		if (device.equals(conntectedDevice)) {
			stopConnectedForce();
		}
	}

	private void stopConnectedForce() {
		ConntectHelper.Instance().sendStopCradleMsg();
	}
}
