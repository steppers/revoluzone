package com.stc.core;

public class Permissions
{
	
	private static boolean external_storage = true;
	
	public static void setExternalStoragePermission(boolean hasPermission) {
		external_storage = hasPermission;
	}
	
	public static boolean hasExternalStoragePermission() {
		return external_storage;
	}
	
}
