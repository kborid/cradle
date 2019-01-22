package com.pioneer.cradle.PLocProvider.fileAccess;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.os.Environment;

public class FileWriterAssistant {

	public static final boolean writeToSDCard(String path, String name, byte[] data) {
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			File saveFile = new File(path, name);
			File ParentPath = new File(saveFile.getParent());
			FileOutputStream outStream;
			try {
				if (!ParentPath.exists()) {
					ParentPath.mkdirs();
				}
				if (!saveFile.exists()) {
					saveFile.createNewFile();
				}

				outStream = new FileOutputStream(saveFile);
				outStream.write(data);
				outStream.close();
				return true;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				outStream = null;
				saveFile = null;
				ParentPath = null;
				ParentPath = null;
			}
		}
		return false;
	}
	
	public static final String getRootPath(){
		return getSDCardPath()+"/cradleAssist";
	}
	public static String getSDCardPath(){
		return StorageAdapter.getCurrentStorageDirectory().getPath();
	}
}
