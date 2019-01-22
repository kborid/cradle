package com.pioneer.cradle.nmea;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.location.Location;

import com.pioneer.PLocProviderKit.PLocProviderKit;
import com.pioneer.PLocProviderKit.util.SatelliteData;

class Frame extends ArrayList<String> {
	private static final float speedRate = 1.852f/3.6f; //1Knot==1.852km/h, 1m/s = 3.6km/h
	/**
	 * cradle message format:
	 * #PIOCDL,<1>,<2>,<3>,<4>,<5>,<6>,<7>,*<8>
	 * <1>: latitude
	 * <2>: N or S
	 * <3>: longitude
	 * <4>: E or W
	 * <5>: bearing
	 * <6>: UTC Date
	 * <7>: UTC time
	 * <8>: check digit
	 * */
	private static final String cradleMessageHead = "#PIOCDL";

	/**
	 * 
	 */
	private static final long serialVersionUID = 5693104449437628849L;
	
	private int index;
	private int[] useSatellitId = new int[12];
	private Location location;
	private SatelliteData[] satas;
	private StringBuilder cradleMessage;
	
	private boolean isValid;
	
	boolean checkComplete() {
		return size()>=5 && get(0).startsWith("$GPGGA") 
				&& get(1).startsWith("$GPRMC")
				&& get(2).startsWith("$GPVTG")
				&& get(3).startsWith("$GPGSA") 
				&& checkGSVComplete();
		//if send message to cradle and Cradle send message back, how can I check complete?
	}
	
	private boolean checkGSVComplete() {
		int count = size() - 4;
		if(count<=0){
			return false;
		}
		String GSVStart = "$GPGSV,"+count;
		for(int i=0; i<count; i++) {
			if(!get(4+i).startsWith(GSVStart+","+(i+1))){
				return false;
			}
		}
		
		String lastgsv = get(size()-1);
		if(!checkDigit(lastgsv)){
			return false;
		}
		
		return true;
	}
	
	public boolean append(String s) {
		if(size() == 0) {
			return false;
		}
		
		String laststatements = (get(size()-1) + s);
		if(checkDigit(laststatements)) {
			set(size()-1, laststatements);
			return true;
		} else {
			return false;
		}
	}
	
	private boolean checkDigit(String str) {
		String[] sp = str.split("\\*");
		if(sp.length == 2){
			return checksum(sp[0]) == checkbit(sp[1]);
		}
		return false;
	}
	
	private int checksum(String str){
		int sum = str.charAt(1);
		for(int i=2; i<str.length(); i++) {
			sum  ^= str.charAt(i);
		}
		return sum;
	}
	
	private int checkbit(String str) {
		try{
			return Integer.parseInt(str.trim(), 16);
		} catch (NumberFormatException e){
			e.printStackTrace();
			return -1;
		} 
	}
	
	boolean decode() {
		if(!checkComplete()){
			return false;
		}
		
		index = 0;
		location = new Location(PLocProviderKit.CRADLE_ASSIS_PROVIDER_NAME);
		satas = new SatelliteData[(size()-4)*4];
		
		cradleMessage = new StringBuilder(cradleMessageHead+",");
		
		Arrays.fill(useSatellitId, -1);
		for(String str: this) {
			if(checkDigit(str)) {
				decodeStatement(str);
			}
		}
		
		fillDataToDataInfo();
		return true;
	}
	
	private void fillDataToDataInfo() {
		DataInfo.instance.setValid(isValid);
		if(isValid) {
			if(Double.compare(location.getLatitude(), 0)>0 && Double.compare(location.getLongitude(), 0)>0) {
				DataInfo.instance.locationInfo.set(location);
				String msgInfo = cradleMessage.toString();
				
				if(checkCradleMessage(msgInfo)) {
					DataInfo.instance.setCradleMessage(msgInfo);
				}
			}
		}
		
		Arrays.fill(DataInfo.instance.satellite, null);
		System.arraycopy(satas, 0, DataInfo.instance.satellite, 0, satas.length);

	}
	
	private boolean checkCradleMessage(String str) {
		int  commaCount = 0;
		for(int i=0; i<str.length(); i++){
			if(str.charAt(i) == ','){
				commaCount++;
			}
		}
		
		if(commaCount != 7){
			return false;
		}
		
		return checkDigit(str);
	}

	private void decodeStatement(String str){
		if(str.startsWith("$GPGGA")) {
			decodeGGA(str);
		} else if(str.startsWith("$GPRMC")) {
			decodeRMC(str);
		} else if(str.startsWith("$GPGSA")) {
			devodeGSA(str);
		} else if(str.startsWith("$GPGSV")) {
			decodeGSV(str);
		} 
	}

	private void decodeGGA(String str){
		str = str.substring(0, str.indexOf("*"));
		String[] fields = str.split(",");
//		if(fields[6].equals("0")) {
//			return;
//		}
		
		double lat = Integer.parseInt(fields[2].substring(0,2))+Double.parseDouble(fields[2].substring(2))/60;
		lat = fields[3].equals("N") ? lat : -lat;
		
		double lon = Integer.parseInt(fields[4].substring(0,3))+Double.parseDouble(fields[4].substring(3))/60;
		lon = fields[5].equals("E") ? lon : -lon;
		
		cradleMessage.append(fields[2]+",");
		cradleMessage.append(fields[3]+",");
		cradleMessage.append(fields[4]+",");
		cradleMessage.append(fields[5]+",");
		
		isValid = !"0".equals(fields[6]);
		
		float accuracy = Float.parseFloat(fields[8]);
		
		double alt = Double.parseDouble(fields[9]);
		
		location.setLatitude(lat);
		location.setLongitude(lon);
		location.setAltitude(alt);
		if(accuracy>0.5 && accuracy<99.9) {
			location.setAccuracy(accuracy * 10);
		}
		
	}

	private void decodeRMC(String str){
		str = str.substring(0, str.indexOf("*"));
		String[] fields = str.split(",");
		if(fields[2].equals("V")) {
			return;
		}
		
		float speed = Float.parseFloat(fields[7]) * speedRate;
		float bearing = Float.parseFloat(fields[8]);
		
		long time = getTime(fields[9]+fields[1].substring(0,6));
		
		location.setSpeed(speed);
		location.setBearing(bearing);
		location.setTime(time);
		
		cradleMessage.append(fields[8]+",");
		cradleMessage.append(fields[9]+",");
		cradleMessage.append(fields[1]);
		
		int sum = checksum(cradleMessage.toString());
		
		cradleMessage.append("*");
		cradleMessage.append(String.format("%1$02X", sum));
		cradleMessage.append('\n');
		
	}
	
	private static long getTime(String timestr) {
		SimpleDateFormat foo = new SimpleDateFormat("ddMMyyHHmmss");
		Calendar c = Calendar.getInstance(TimeZone.getTimeZone("Etc/UTC"));
		foo.setCalendar(c);
		try {
			Date date = foo.parse(timestr);
			return date.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return 0;
		}

	}
	
	private void devodeGSA(String str) {
		str = str.substring(0, str.indexOf("*"));
		String[] fields = str.split(",");
		for(int i=0; i<12; i++) {
			if(!"".equals(fields[3+i].trim())){
				try {
					useSatellitId[i] = Integer.parseInt(fields[3+i].trim());
				} catch (NumberFormatException e) {
					e.printStackTrace();
					//do nothing
				}
			}
		}
	}

	private boolean checkUseId(int id){
		for(int uesId : useSatellitId){
			if(uesId == id){
				return true;
			}
		}
		return false;
	}

	private void decodeGSV(String str){
		str = str.substring(0, str.indexOf("*"));
		String[] fields = str.split(",");
		
		int count = (fields.length-3)/4;
		
		
		for(int i=0; i<count; i++) {
			int id = Integer.parseInt(fields[4+i*4]);
			float elevation = Float.parseFloat(fields[5+i*4]);
			float azimuth = Float.parseFloat(fields[6+i*4]);
			float snr = Float.parseFloat(fields[7+i*4]);
			
			SatelliteData data = new SatelliteData(id);
			data.setInfo(checkUseId(id), elevation, azimuth, snr);
			satas[index] = data;
			index++;
		}
	}

}
