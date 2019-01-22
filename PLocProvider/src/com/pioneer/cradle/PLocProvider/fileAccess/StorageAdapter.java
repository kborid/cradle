package com.pioneer.cradle.PLocProvider.fileAccess;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Environment;

/*
 * StorageAdapter used for adjust which storage is most  preferential
 * supposed INTERNAL_STORAGE_DIRECTORY and PHONE_STORAGE_DIRECTORY is the two sides of a coin *
 * if INTERNAL_STORAGE_DIRECTORY or PHONE_STORAGE_DIRECTORY exists, one of them is preferred than EXTERNAL_STORAGE_STATE
 * As INTERNAL_STORAGE_DIRECTORY or PHONE_STORAGE_DIRECTORY exists, it is supposed that its state is working.
 * In SDK2.2,  something may should be changed, it is a unstable and worrying point.
 */
public class StorageAdapter {

	private static final File INTERNAL_STORAGE_DIRECTORY = getDirectory("INTERNAL_STORAGE");

	private static final File PHONE_STORAGE_DIRECTORY = getDirectory("PHONE_STORAGE");

	private static File mCurrentStorage = null;
	
	public static File getCurrentStorageDirectory() {
		// if mCurrentStorage exists, use it
		if(mCurrentStorage != null)
			return mCurrentStorage;
		
		// use internal storage first
		if (INTERNAL_STORAGE_DIRECTORY != null)
			mCurrentStorage = INTERNAL_STORAGE_DIRECTORY;

//		else if (PHONE_STORAGE_DIRECTORY != null)
//			mCurrentStorage =  PHONE_STORAGE_DIRECTORY;
//
//		else 
			mCurrentStorage = Environment.getExternalStorageDirectory();
		
		return mCurrentStorage;
	}

	public static String getCurrentStorageState() {
		// use internal storage first
		if (INTERNAL_STORAGE_DIRECTORY == mCurrentStorage) {
			// return _getSystemProperty("INTERNAL_STORAGE_STATE");
			return Environment.MEDIA_MOUNTED;

		}

		if (PHONE_STORAGE_DIRECTORY == mCurrentStorage) {
			// return _getSystemProperty("PHONE_STORAGE_DIRECTORY");
			return Environment.MEDIA_MOUNTED;
		}

		return Environment.getExternalStorageState();
	}

	@SuppressWarnings("unused")
	private static String _getSystemProperty(String key) {
		try {
			Class<?> SystemProperties;
			SystemProperties = Class.forName("android.os.SystemProperties");
			Method get;
			Class<?>[] pa = new Class[1];
			pa[0] = java.lang.String.class;
			get = SystemProperties.getDeclaredMethod("get", pa);
			if (get != null) {
				return (String) get.invoke(null, key);
			}

		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File getRootDirectory() {
		return Environment.getRootDirectory();
	}
	
	/**
	 * Gets the Android data directory.
	 */
	public static File getDataDirectory() {
		return Environment.getDataDirectory();
	}

	/**
	 * Gets the Android external storage directory.
	 */
	public static File getExternalStorageDirectory() {
		return Environment.getExternalStorageDirectory();
	}

	/**
	 * Gets the Android Download/Cache content directory.
	 */
	public static File getDownloadCacheDirectory() {
		return Environment.getDownloadCacheDirectory();
	}

	static File getDirectory(String variableName) {
		String path = System.getenv(variableName);
		return path == null ? null : new File(path);
	}
	
}
