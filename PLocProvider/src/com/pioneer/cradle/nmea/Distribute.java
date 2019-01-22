/**
 * 
 */
package com.pioneer.cradle.nmea;

import java.util.LinkedList;

/**
 * @author liuzhaofeng
 *
 */
class Distribute {
	
	public static final Distribute instance = new Distribute();
	private Distribute(){};
	
	private LinkedList<Frame> frameList = new LinkedList<Frame>();
	
	boolean receiveNmeaInfo(String str) {
		String statements[] = str.split("\n");
		for(String statement : statements) {
			if(statement.startsWith("$GPGGA")) {
				frameList.add(new Frame());
			}
			
			if(frameList.size()!=0) {
				if(statement.startsWith("$GP")) {
					frameList.getLast().add(statement);
				} else {
					frameList.getLast().append(statement);
				}
			}
		}
		
		
		return decode();
	}

	private boolean decode(){
		if(frameList.size() == 0){
			return false;
		}
		
		if(frameList.size() == 1) {
			Frame frame = frameList.getFirst();
			if(!frame.checkComplete()) {
				return false;
			}
		}
		
		
		Frame frame = frameList.removeFirst();
		boolean decodeOneFrame = frame.decode();
		decodeOneFrame |= decode();
		
		return decodeOneFrame;
	}

}
