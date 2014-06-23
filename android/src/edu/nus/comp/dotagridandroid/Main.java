package edu.nus.comp.dotagridandroid;

import java.io.*;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.*;
import edu.nus.comp.dotagridandroid.appsupport.AppNativeAPI;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class Main extends Activity {
	public static final String applicationDir = "/sdcard/dotagrid";
	public static final String pathToDefaultPkg = "/sdcard/dotagrid/default.zip";
	private GameLogicManager logicManager;
	public Main() {
		super();
		logicManager = new GameLogicManager(this);
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Activity is created here
		// Proceed to MainSurfaceView for more details
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// check if default package exists
		// TODO put default.bin in assets
		File defaultPkg = new File(pathToDefaultPkg);
		if (!defaultPkg.exists() || defaultPkg.isDirectory()) {
			if (defaultPkg.isDirectory())
				defaultPkg.delete();
			defaultPkg = new File(applicationDir);
			if (!defaultPkg.exists())
				defaultPkg.mkdir();
			else if (!defaultPkg.isDirectory())
				throw new RuntimeException("Please remove 'dotagrid' file in your external storage's root folder (REQUIRED)");
			try {
				InputStream in = getAssets().open("default.bin");
				FileOutputStream file = new FileOutputStream(pathToDefaultPkg);
				byte[] buf = new byte[2048];
				int count;
				while ((count = in.read(buf)) != -1)
					file.write(buf, 0, count);
				file.flush();
				file.getFD().sync();
				file.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		logicManager.initiateSoundEngine();
		AppNativeAPI.testJS();
		AppNativeAPI.testSL();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// save game state
		logicManager.saveGame();
		logicManager.close();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// this handler is called before any other
		// TODO lock rotation or whatever
		super.onConfigurationChanged(newConfig);
		Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		switch (display.getRotation()) {
		case Surface.ROTATION_0:
			// natural
		case Surface.ROTATION_90:
			// rotate 90
		case Surface.ROTATION_180:
			// rotate 180
		case Surface.ROTATION_270:
			// rotate 270
		}
	}
	
	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onResume() {
		super.onResume();
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean("test", true);
	}
	
	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onStop() {
		super.onStop();
	}
	
	@Override
	public void onBackPressed() {
		// TODO save game and quit
		ControlEvent e = new ControlEvent(ControlEvent.TYPE_INTERPRETED, null);
		e.extendedType = "Cancel";
		logicManager.processEvent(e);
	}
	
	public GameLogicManager getGameLogicManager() {return logicManager;}
}
