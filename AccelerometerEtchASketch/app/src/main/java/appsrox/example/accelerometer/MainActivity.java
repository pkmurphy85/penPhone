package appsrox.example.accelerometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

//Paint Imports
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;



public class MainActivity extends Activity {
	
	private static final String TAG = "appsrox.example.accelerometer.MainActivity";
	
	private WakeLock mWakeLock;
	private SimulationView mSimulationView;

	//Insert from Paint App
	private Paint mPaint;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		PowerManager mPowerManager = (PowerManager) getSystemService(POWER_SERVICE);
        mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, TAG);
        
        mSimulationView = new SimulationView(this);
        setContentView(mSimulationView);

	}

	@Override
	protected void onResume() {
		super.onResume();
		mWakeLock.acquire();
		mSimulationView.startSimulation();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mSimulationView.stopSimulation();
		mWakeLock.release();
	}	
}
