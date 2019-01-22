package com.pioneer.cradle.PLocProvider.btConnection;

import java.util.ArrayList;
import java.util.Set;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;

import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;


public class DeviceTools {
//	public static final String FILE_PATH = FileWriterAssistant.getRootPath()+"/user/RW/BTBackup/";
//	public static final String FILE_NAME = "BT_Backup_List.xml";
	
	private static final int deviceClassTest = 0x340404;
	private static final String deviceNameTest = "Cradle 100";
	private static final String deviceName2Test = "Cradle100";
	
	private static final int deviceClass = 0x350408;
	private static final String deviceName = "Pioneer Cradle";
	private static final String deviceName2 = "PioneerCradle";
	
	public static final int MAX_BACKUP_NUMBER = 10;

	private DeviceTools() {
	}

	public static ArrayList<BluetoothDevice> getBackupDevices() {
		ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
		String[] devs = SharedPreferenceData.CRADLE_BACKUP_DEVICES.getString().split(",");
		for(String address : devs) {
			BluetoothDevice dev = getBTDevice(address);
			if(dev != null) {
				list.add(dev);	
			}
		}
		return list;
	}

	public static BluetoothDevice getLastDeviceOfBackup() {
		ArrayList<BluetoothDevice> backupList = DeviceTools
				.getBackupDevices();
		if (backupList == null || backupList.isEmpty()) {
			return null;
		} else {
			return backupList.get(backupList.size() - 1);
		}
	}

	public static boolean deleteDeviceFromBackup(BluetoothDevice device) {
		ArrayList<BluetoothDevice> list = getBackupDevices();
		boolean remove = list.remove(device);
		if(remove) {
			SharedPreferenceData.CRADLE_BACKUP_DEVICES.setValue(getStringFormDevices(list));
		}
		return false;
	}

	public static int getNumOfBackupList() {
		return getBackupDevices().size();
	}

	public static boolean addDevice(BluetoothDevice device) {
		if (device == null) {
			return false;
		}
		ArrayList<BluetoothDevice> list = getBackupDevices();
		list.remove(device);
		
		list.add(device);
		
		if(list.size()>MAX_BACKUP_NUMBER) {
			list.remove(0);
		}
		
		SharedPreferenceData.CRADLE_BACKUP_DEVICES.setValue(getStringFormDevices(list));
		return true;
	}
	
	private static String getStringFormDevices(ArrayList<BluetoothDevice> list){
		StringBuilder sb = new StringBuilder();
		for(BluetoothDevice dev : list) {
			sb.append(dev.getAddress()+",");
		}
		return sb.toString();
	}

	public static ArrayList<BluetoothDevice> toMixList(
			ArrayList<BluetoothDevice> list0, ArrayList<BluetoothDevice> list1) {
		if (list0 == null || list1 == null) {
			return null;
		} else {
			ArrayList<BluetoothDevice> list = new ArrayList<BluetoothDevice>();
			for (BluetoothDevice o : list0) {
				if (list1.contains(o)) {
					list.add(o);
                }
			}
			return list;
		}
	}
	
	public static BluetoothDevice getLastDevice(){
		ArrayList<BluetoothDevice> bondedDevices = DeviceTools.getBondedDevices();
		if(bondedDevices!= null && bondedDevices.size() == 1) {
			return bondedDevices.get(0);
		}
		
		ArrayList<BluetoothDevice> list = DeviceTools.toMixList(getBackupDevices(), bondedDevices);
		
		if (list == null || list.isEmpty()) {
			return null;
		}
		
		return list.get(list.size() - 1);
	}

	public static int getNumOfMixDevices() {
		ArrayList<BluetoothDevice> list = toMixList(getBackupDevices(),
				getBondedDevices());
		if (list == null || list.isEmpty()) {
			return -1;
		} else {
			return list.size();
		}
	}

	public static ArrayList<BluetoothDevice> getBondedDevices() {
		BluetoothAdapter mAdapter = BluetoothAdapter.getDefaultAdapter();
		if (mAdapter == null || !mAdapter.isEnabled()) {
			return null;
		}
		
		Set<BluetoothDevice> setDevice = mAdapter.getBondedDevices();
		ArrayList<BluetoothDevice> devies = new ArrayList<BluetoothDevice>(setDevice.size());
		
		for(BluetoothDevice dev: setDevice) {
			
			if(checkDeviceType(dev) || checkDeviceTypeForPreviousCradle(dev)) {
				devies.add(dev);
			}
			
		}
		return devies;

	}

	private static BluetoothDevice getBTDevice(String address) {
		if(BluetoothAdapter.checkBluetoothAddress(address)) {
			return BluetoothAdapter.getDefaultAdapter().getRemoteDevice(address);
		}
		return null;
	}
	
	public static boolean checkDeviceVaild(BluetoothDevice dev) {
		return checkDeviceBounded(dev) && (checkDeviceType(dev) || checkDeviceTypeForPreviousCradle(dev));
	}
	
	private static boolean checkDeviceBounded(BluetoothDevice info){
		ArrayList<BluetoothDevice> list = DeviceTools.getBondedDevices();
		return list!=null && list.contains(info);
	}
	
	public static boolean checkDeviceType(BluetoothDevice device) {
		if(device == null) {
			return false;
		}
		BluetoothClass majorDeviceClass = device.getBluetoothClass();
		return majorDeviceClass !=null && majorDeviceClass.hashCode() == deviceClass && (deviceName.equals(device.getName()) || deviceName2.equals(device.getName()));
		
	}

	/**
	 * for previous Cradle
	 * 
	 * */
	private static boolean checkDeviceTypeForPreviousCradle(BluetoothDevice device) {
		if(device == null) {
			return false;
		}
		BluetoothClass majorDeviceClass = device.getBluetoothClass();
		return majorDeviceClass.hashCode() == deviceClassTest && (deviceNameTest.equals(device.getName()) || deviceName2Test.equals(device.getName()));
		
	}


}
