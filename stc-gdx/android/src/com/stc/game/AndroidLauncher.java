package com.stc.game;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import android.*;
import android.content.pm.*;

public class AndroidLauncher extends AndroidApplication {
	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Permissions.setExternalStoragePermission(
			checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
			== PackageManager.PERMISSION_GRANTED);
			
		if(!Permissions.hasExternalStoragePermission()) {
			//Context context = getApplicationContext();
			//Toast toast = Toast.makeText(context, "Custom user levels require storage permission", Toast.LENGTH_LONG);
			//toast.show();
			
			requestPermissions(
				new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
				0);
		}
		
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.hideStatusBar = true;
		config.useImmersiveMode = true;
		Globals.orientation = "portrait";
		initialize(new RevGame(), config);
	}

	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
	{
		if(requestCode == 0) {
			Permissions.setExternalStoragePermission(grantResults[0] == PackageManager.PERMISSION_GRANTED);
		}
	}
}
