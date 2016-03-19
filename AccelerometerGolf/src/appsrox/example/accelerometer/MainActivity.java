package appsrox.example.accelerometer;

import android.app.Activity;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;

public class MainActivity extends Activity {
	
	private static final String TAG = "appsrox.example.accelerometer.MainActivity";
	
	private WakeLock mWakeLock;
	private SimulationView mSimulationView;

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
