package com.pioneer.cradle.PLocProvider.btConnection;

import java.util.ArrayList;

import com.pioneer.cradle.PLocProvider.tools.SharedPreferenceData;

public class CradleCommand {
	
	private CradleCommand(){}
	
	private static final CradleCommand instance = new CradleCommand();
	
	public static final CradleCommand getInstance() {
		return instance;
	}
	
	private static final String PREFIX_LOG_SNS = "#PCLOG,SNS,";
	private static final String PREFIX_LOG_NMEA = "#PCLOG,NMEA,";
	private static final String PREFIX_LEARN_CLRAE = "#PCLEARN,CLEAR,";
	private static final String PREFIX_LEARN_DMYSET = "#PCLEARN,DMYSET,";
	
	private ArrayList<String> cradleCommads = new ArrayList<String>();
	
	public boolean hasCommads() {
		return (cradleCommads.size()>0);
	}
	
	public void excute(WriteStream invalidStream){
		for(String commad : cradleCommads) {
			invalidStream.write(commad.getBytes());
		}
		
		cradleCommads.clear();
	}
	
	public static interface WriteStream {
		void write(byte[] buffer);
	}
	
	public void setSNSLog(boolean on) {
		clearCommand(PREFIX_LOG_SNS);
		
		StringBuffer sb = new StringBuffer(PREFIX_LOG_SNS);
		sb.append(on?1:0);
		addChecksum(sb);
		cradleCommads.add(sb.toString());
		
	}

	public void setNMEALog(boolean on) {
		clearCommand(PREFIX_LOG_NMEA);
		
		StringBuffer sb = new StringBuffer(PREFIX_LOG_NMEA);
		sb.append(on?1:0);
		addChecksum(sb);
		cradleCommads.add(sb.toString());
	}
	
	public void clearLearn() {
		clearCommand(PREFIX_LEARN_CLRAE);
		
		StringBuffer sb = new StringBuffer(PREFIX_LEARN_CLRAE);
		sb.append(1);
		addChecksum(sb);
		cradleCommads.add(sb.toString());
		
	}
	
	public void setDmyset() {
		clearCommand(PREFIX_LEARN_DMYSET);
		
		StringBuffer sb = new StringBuffer(PREFIX_LEARN_DMYSET);
		sb.append(1);
		addChecksum(sb);
		cradleCommads.add(sb.toString());
		
	}
	
	private void clearCommand(String prefix) {
		String command = findCommands(prefix);
		if(command != null) {
			cradleCommads.remove(command);
		}
	}
	
	
	private String findCommands(String prefix) {
		for(String commad : cradleCommads) {
			if(commad.startsWith(prefix)) {
				return commad;
			}
		}
		return null;
	}
	
	private void addChecksum(StringBuffer sb) {
		int sum = checksum(sb.toString());
		sb.append("*");
		sb.append(String.format("%1$02X", sum));
		sb.append('\n');
	}
	
	public void setSetToCardle(WriteStream invalidStream) {
		setSNSLog(SharedPreferenceData.GPS_SNS_LOG_RECORD.getBoolean());
		setNMEALog(SharedPreferenceData.GPS_NMEA_LOG_RECORD.getBoolean());
		excute(invalidStream);
	}
	
	
	private int checksum(String str){
		int sum = str.charAt(1);
		for(int i=2; i<str.length(); i++) {
			sum  ^= str.charAt(i);
		}
		return sum;
	}


}
