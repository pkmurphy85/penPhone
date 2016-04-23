package com.example.patrick.penphone;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import org.jtransforms.fft.DoubleFFT_1D;

import java.util.ArrayList;

public class CaptureData extends Activity implements SensorEventListener,
        View.OnClickListener {
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;
    private SensorManager sensorManager;
    private Button btnStart, btnStop, btnUpload;
    private boolean started = false;
    protected ArrayList<AccelData> sensorData;
    private LinearLayout layout;
    private View mChart;
    protected ArrayList<Double> xfftMag;
    protected ArrayList<Double> yfftMag;
    protected ArrayList<Double> zfftMag;
    protected CharSequence selectedLetter;

    public ArrayList getSensorData() {
        return sensorData;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_capture_data);
        //layout = (LinearLayout) findViewById(R.id.chart_container);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensorData = new ArrayList();
        xfftMag = new ArrayList();
        yfftMag = new ArrayList();
        zfftMag = new ArrayList();

        btnStart = (Button) findViewById(R.id.btnStart);
        btnStop = (Button) findViewById(R.id.btnStop);
        btnUpload = (Button) findViewById(R.id.btnUpload);
        btnStart.setOnClickListener(this);
        btnStop.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnStart.setEnabled(true);
        btnStop.setEnabled(false);
        //int const MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;
        if (sensorData == null || sensorData.size() == 0) {
            btnUpload.setEnabled(false);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (started == true) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (started) {
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            long timestamp = System.currentTimeMillis();
            AccelData data = new AccelData(timestamp, x, y, z);
            sensorData.add(data);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart:
                btnStart.setEnabled(false);
                btnStop.setEnabled(true);
                btnUpload.setEnabled(false);
                sensorData = new ArrayList();

                started = true;
                Sensor accel = sensorManager
                        .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
                sensorManager.registerListener(this, accel,
                        SensorManager.SENSOR_DELAY_NORMAL);
                break;
            case R.id.btnStop:
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnUpload.setEnabled(true);
                started = false;
                sensorManager.unregisterListener(this);
                final CharSequence[] letters = {"A","B","C"};
                AlertDialog.Builder letterDialog = new AlertDialog.Builder(CaptureData.this);
                letterDialog.setTitle("Which letter did you draw");
                letterDialog.setItems(letters, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        selectedLetter = letters[which];
                    }
                });

                letterDialog.show();
                break;
            case R.id.btnUpload:
                //Permissions required for Marshmallow
                if (ContextCompat.checkSelfPermission(CaptureData.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CaptureData.this,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(CaptureData.this,
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }

                if (ContextCompat.checkSelfPermission(CaptureData.this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {

                    // Should we show an explanation?
                    if (ActivityCompat.shouldShowRequestPermissionRationale(CaptureData.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)) {

                        // Show an expanation to the user *asynchronously* -- don't block
                        // this thread waiting for the user's response! After the user
                        // sees the explanation, try again to request the permission.

                    } else {

                        // No explanation needed, we can request the permission.

                        ActivityCompat.requestPermissions(CaptureData.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE);

                        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                        // app-defined int constant. The callback method gets the
                        // result of the request.
                    }
                }


                calcFFT(sensorData);
                WriteSDCard sdCard = new WriteSDCard(getApplicationContext());
                sdCard.writeToSDFile(selectedLetter, xfftMag,yfftMag,zfftMag);
                break;
            default:
                break;
        }

    }

    public void calcFFT(ArrayList sensorData){

        int len = sensorData.size();
        Log.d("CalcFFT", Integer.toString(len));
        if(sensorData.size() % 2 != 0)
            len = sensorData.size()-1;
        DoubleFFT_1D X = new DoubleFFT_1D(len);
        DoubleFFT_1D Y = new DoubleFFT_1D(len);
        DoubleFFT_1D Z = new DoubleFFT_1D(len);

        double[] xfft = new double[len*2];
        double[] yfft = new double[len*2];
        double[] zfft = new double[len*2];

        for(int i = 0; i<len-1; i++){
            AccelData data = new AccelData();
            data = (AccelData) sensorData.get(i);
            xfft[i] = ((AccelData) sensorData.get(i)).getX();
            yfft[i] = ((AccelData) sensorData.get(i)).getY();
            zfft[i] = ((AccelData) sensorData.get(i)).getZ();

        }
        X.realForwardFull(xfft);
        Y.realForwardFull(yfft);
        Z.realForwardFull(zfft);

        for(int i = 1; i<(xfft.length/2 - 1); i++){
            //xfftMag.add(xfft[i]);
            //yfftMag.add(yfft[i]);
            //zfftMag.add(zfft[i]);
            xfftMag.add(Math.sqrt(Math.pow(xfft[2 * i], 2) + Math.pow(xfft[2 * i + 1], 2)));
            yfftMag.add(Math.sqrt(Math.pow(yfft[2 * i], 2) + Math.pow(yfft[2 * i + 1], 2)));
            zfftMag.add(Math.sqrt(Math.pow(zfft[2 * i], 2) + Math.pow(zfft[2 * i + 1], 2)));

        }

    }
}