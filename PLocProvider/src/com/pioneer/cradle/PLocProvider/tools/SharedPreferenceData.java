/**
 * 
 */
package com.pioneer.cradle.PLocProvider.tools;


import com.pioneer.cradle.PLocProvider.LocationApp;

import android.app.Activity;
import android.content.SharedPreferences;


/**
 * @author liuzhaofeng
 *
 */
public enum SharedPreferenceData {
	ENTER_NAVI_DRECTION(false),
	AGREE_CONTRACT(false),
	MUSIC_APP_PACKAGE(""),
	NAVI_APP_PACKAGE(""),
	CRADLE_BACKUP_DEVICES(""),
	CRADLE_BACKUP_MESSAGE(""),
	APP_DEBUG_MENU(false),
	GPS_LOG_RECORD(true),
	GPS_SNS_LOG_RECORD(false),
	GPS_NMEA_LOG_RECORD(false),
	USE_LAST_LOCATION(false),
	;

	private static final String STORE_NAME = "CradleAssist";
	
	private Object obj;

	private SharedPreferenceData(float obj){
		this.obj = obj;
	}
	private SharedPreferenceData(int obj){
		this.obj = obj;
	}
	private SharedPreferenceData(String obj){
		this.obj = obj;
	}
	private SharedPreferenceData(boolean obj){
		this.obj = obj;
	}
	
	private void setDefVal(){
		if(obj != null){
			if(obj instanceof Boolean){
				setValue(this, (Boolean)obj);
			} else if(obj instanceof Integer){
				setValue(this, (Integer)obj);
			} else if(obj instanceof Float){
				setValue(this, (Float)obj);
			} else if(obj instanceof String){
				setValue(this, (String)obj);
			}
		}
	}
	
	/**
	 * initialize all datas
	 * */
	public static void setDefaultValues(){
		for(SharedPreferenceData d: values()){
			d.setDefVal();
		}
	}
	
	/**
	 * save data that type is int
	 * */
	public static void setValue(SharedPreferenceData key, int value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putInt(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is int
	 * */
	public void setValue(int value){
		setValue(this, value);
	}
	
	/**
	 * save data that type is string 
	 * */
	public static void setValue(SharedPreferenceData key, String value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putString(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is string 
	 * */
	public void setValue(String value){
		setValue(this, value);
	}
	
	/**
	 * save data that type is boolean 
	 * */
	public static void setValue(SharedPreferenceData key, boolean value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putBoolean(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is boolean 
	 * */
	public void setValue(boolean value){
		setValue(this, value);
	}
	
	
	/**
	 * save data that type is float
	 * */
	public static void setValue(SharedPreferenceData key, float value){
		checkValue(key, value);
		SharedPreferences.Editor editor = getEditor();
		editor.putFloat(key.name(), value);
		editor.commit();
	}
	
	/**
	 * save data that type is float
	 * */
	public void setValue(float value){
		setValue(this, value);
	}
	
	/**
	 * 
	 * get int-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static int getInt(SharedPreferenceData key, int defValue){
		return getSharedPreferences().getInt(key.name(), defValue);
	}
	
	/**
	 * 
	 * get int-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or -1
	 * */
	public static int getInt(SharedPreferenceData key){
		if(key.obj == null){
			return getInt(key, -1);
		} else {
			return getInt(key, ((Integer)key.obj));
		}
	}
	
	/**
	 * 
	 * get int-type data from local<br/>
	 * 
	 * example int i = SharedPreferenceData.KEY_EXAMPLE.getString()
	 * 
	 * @return the value, if the data is not exist the method will return  default value or -1
	 * */
	public int getInt(){
		return getInt(this);
	}
	
	/**
	 * 
	 * get String-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static String getString(SharedPreferenceData key, String defValue){
		return getSharedPreferences().getString(key.name(), defValue);
	}
	
	/**
	 * 
	 * get String-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or ""
	 * */
	public static String getString(SharedPreferenceData key){
		if(key.obj == null){
			return getString(key, "");
		}else{
			return getString(key, key.obj.toString());
		}
	}
	
	/**
	 * 
	 * get String-type data from local<br/>
	 * 
	 * example String s = SharedPreferenceData.KEY_EXAMPLE.getString()
	 * 
	 * @return the value, if the data is not exist the method will return default value or ""
	 * */
	public String getString(){
		return getString(this);
	}

	/**
	 * 
	 * get boolean-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static boolean getBoolean(SharedPreferenceData key, boolean defValue){
		return getSharedPreferences().getBoolean(key.name(), defValue);
	}
	
	/**
	 * 
	 * get boolean-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or false
	 * */
	public static boolean getBoolean(SharedPreferenceData key){
		if(key.obj == null){
			return getBoolean(key, false);
		} else {
			return getBoolean(key, (Boolean)key.obj);
		}
	}
	
	/**
	 * 
	 * get boolean-type data from local<br/>
	 * 
	 * example boolean b = SharedPreferenceData.KEY_EXAMPLE.getBoolean()
	 * 
	 * @return the value, if the data is not exist the method will return default value or false
	 * */
	public boolean getBoolean(){
		return getBoolean(this);
	}


	/**
	 * 
	 * get float-type data from local
	 * 
	 * @param key the field you want to get
	 * @param defValue default value if data is not exist
	 * 
	 * @return the value
	 * */
	public static float getFloat(SharedPreferenceData key, float defValue){
		return getSharedPreferences().getFloat(key.name(), defValue);
	}
	/**
	 * 
	 * get float-type data from local
	 * 
	 * @param key the field you want to get
	 * 
	 * @return the value, if the data is not exist the method will return default value or 0f
	 * */
	public static float getFloat(SharedPreferenceData key){
		if(key.obj == null){
			return getFloat(key, 0f);
		} else {
			return getFloat(key, (Float)key.obj);
		}
	}
	
	/**
	 * 
	 * get float-type data from local<br/>
	 * 
	 * etc. float f = SharedPreferenceData.KEY_EXAMPLE.getFloat()
	 * 
	 * @return the value, if the data is not exist the method will return default value or 0f
	 * */
	public float getFloat(){
		return getFloat(this);
	}

	
	private static SharedPreferences getSharedPreferences() {
		return LocationApp.getInstance().getApplicationContext().getSharedPreferences(STORE_NAME, Activity.MODE_PRIVATE);
	}
	
	
	private static SharedPreferences.Editor getEditor() {
		return LocationApp.getInstance().getApplicationContext().getSharedPreferences(STORE_NAME, Activity.MODE_PRIVATE).edit();
	}
	
	private static void checkValue(SharedPreferenceData key, Object value){
		if(key.obj!=null && value!=null && !value.getClass().equals(value.getClass())){
			throw new IllegalArgumentException(
					"SharedPreferences key = "+key.name()+", " +
					"data type = "+key.obj.getClass().getName()+", " +
					"value type = "+value.getClass().getName()+", " +
					"type not match!");
		}
	}
	
}
