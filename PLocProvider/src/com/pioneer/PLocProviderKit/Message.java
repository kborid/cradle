package com.pioneer.PLocProviderKit;

import java.io.Serializable;

import android.os.Bundle;


public class Message implements Serializable {
	private static final String messagekey = "messageKey";
	/**
	 * 
	 */
	private static final long serialVersionUID = 3044756684335917525L;
	
	public final int orderkey;
	
	public Message(int orderkey) {
		this.orderkey = orderkey;
	}
	
	public String str;
	public Serializable obj;
	
	public int arg0;
	public int arg1;
	
	public static Message obtain(int order) {
		Message msg = new Message(order);
		return msg;
	}
	
	public static Message obtain(int order, String str) {
		Message msg = new Message(order);
		msg.str = str;
		return msg;
	}
	
	public static Message obtain(int order, Serializable obj) {
		Message msg = new Message(order);
		msg.obj = obj;
		return msg;
	}
	
	public static Message obtain(int order, int arg0, int arg1) {
		Message msg = new Message(order);
		msg.arg0 = arg0;
		msg.arg1 = arg1;
		return msg;
	}
	
	public Bundle obtainBundle(){
		Bundle b = new Bundle();
		b.putSerializable(messagekey, this);
		return b;
	}

	public static Bundle obtainBundle(Message msg){
		Bundle b = new Bundle();
		b.putSerializable(messagekey, msg);
		return b;
	}

	public static Message getMessage(Bundle b) {
		return (Message) b.get(messagekey);
	}
}
