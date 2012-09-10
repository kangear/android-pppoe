package com.encrypt;


import android.util.Log;

public class Encrypt {
	public native String stringFromEncrypt(String path,String username,String password);
	static{
		try {
			System.loadLibrary("encrypt-jni");
		}	 
		catch (UnsatisfiedLinkError ule) {
			Log.d("Debug","Could not load library!");
		}
	}
}