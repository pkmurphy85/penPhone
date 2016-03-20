package appsrox.example.accelerometer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.Display;
import android.view.Surface;
import android.view.View;
import android.view.WindowManager;

//Imports from Paint
import android.view.MotionEvent;
import android.view.Menu;
import android.view.MenuItem;


public class SimulationView extends View implements SensorEventListener {
	
	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private Display mDisplay;
	
	private Bitmap mBallBitmap;
	private static final int BALL_SIZE = 50;

    private float mXOrigin;
    private float mYOrigin;
    
    private float mHorizontalBound;
    private float mVerticalBound;    
    
    private float mSensorX;
    private float mSensorY;
    private float mSensorZ;
    private long mSensorTimeStamp;
    
    private Particle mBall = new Particle();

    //Paint app declarations -- Left out context
    private Canvas mCanvas;
    private Path mPath;
    private Paint mPaint;
	private Bitmap mBitmap;
	private Paint   mBitmapPaint;

	public SimulationView(Context context) {
		super(context);
		
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        mDisplay = mWindowManager.getDefaultDisplay();
		
		mSensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        
        Bitmap ball = BitmapFactory.decodeResource(getResources(), R.drawable.ball);
        mBallBitmap = Bitmap.createScaledBitmap(ball, BALL_SIZE, BALL_SIZE, true); //scale our ball to the size we want

		//Inserts from Painting App
        mPath = new Path();
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);

		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setColor(Color.GREEN);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeJoin(Paint.Join.ROUND);
		mPaint.setStrokeCap(Paint.Cap.ROUND);
		mPaint.setStrokeWidth(12);

        Options opts = new Options();
        opts.inDither = true;
        opts.inPreferredConfig = Bitmap.Config.RGB_565;
	}
	
	public void startSimulation() { mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_UI); }
	
	public void stopSimulation() {
		mSensorManager.unregisterListener(this);
	}

	@Override
	public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() != Sensor.TYPE_ACCELEROMETER)
            return;
        
		switch (mDisplay.getRotation()) {
		case Surface.ROTATION_0:
			mSensorX = event.values[0];
			mSensorY = event.values[1];
			break;
		case Surface.ROTATION_90:
			mSensorX = -event.values[1];
			mSensorY = event.values[0];
			break;
		case Surface.ROTATION_180:
			mSensorX = -event.values[0];
			mSensorY = -event.values[1];
			break;
		case Surface.ROTATION_270:
			mSensorX = event.values[1];
			mSensorY = -event.values[0];
			break;
		}
		mSensorZ = event.values[2];
		mSensorTimeStamp = event.timestamp;

	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mXOrigin = w * 0.5f;
        mYOrigin = h * 0.5f;
        
        mHorizontalBound = (w - BALL_SIZE) * 0.5f;
        mVerticalBound = (h - BALL_SIZE) * 0.5f;

		mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
		mCanvas = new Canvas(mBitmap);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

        mBall.updatePosition(mSensorX, mSensorY, mSensorZ, mSensorTimeStamp);
        mBall.resolveCollisionWithBounds(mHorizontalBound, mVerticalBound);
        canvas.drawBitmap(mBallBitmap, (mXOrigin - BALL_SIZE/2) + mBall.mPosX, (mYOrigin - BALL_SIZE/2) - mBall.mPosY, null);

		//Link the golf app to the paint app
		touch_move((mXOrigin - BALL_SIZE / 2) + mBall.mPosX, (mYOrigin - BALL_SIZE / 2) - mBall.mPosY);

		//From paint app
		canvas.drawBitmap( mBitmap, 0, 0, mBitmapPaint);
		canvas.drawPath( mPath,  mPaint);
        invalidate();
	}


    //From Paint App -- leaving in touch_start and touch_up for later
    private float mX, mY; //the path

    private void touch_start(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void touch_move(float x, float y) {
        mPath.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
        mX = x;
        mY = y;

    }

    private void touch_up() {
        mPath.lineTo(mX, mY);
        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();
    }

}
