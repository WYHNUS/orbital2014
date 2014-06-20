package edu.nus.comp.dotagridandroid;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.*;
import edu.nus.comp.dotagridandroid.appsupport.AppNativeAPI;
import edu.nus.comp.dotagridandroid.logic.GameLogicManager;
import edu.nus.comp.dotagridandroid.ui.event.ControlEvent;

public class Main extends Activity {
	private GameLogicManager logicManager = new GameLogicManager(this);
	public Main() {
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Activity is created here
		// Proceed to MainSurfaceView for more details
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		AppNativeAPI.testJS();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// save game state
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
