package com.example.rainly.penphone;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Bundle;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONArray;

import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;


public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {
    private SensorManager sensorManager;

    TextView xCoor; // declare X axis object
    TextView yCoor; // declare Y axis object
    TextView zCoor; // declare Z axis object
    TextView Result;
    TextView Judge;
    Button start;
    double x;
    double y;
    double z;
    ArrayList<Double> xd = new ArrayList<Double>();
    ArrayList<Double> yd = new ArrayList<Double>();
    ArrayList<Double> zd = new ArrayList<Double>();
    ArrayList<Double> xt = new ArrayList<Double>();
    ArrayList<Double> yt = new ArrayList<Double>();
    ArrayList<Double> zt = new ArrayList<Double>();
    Timer timer = new Timer();
    Timer timer2 = new Timer();
    Button end;
    Button trainstart;
    Button trainend;
    Button compare;
    Button clearo;
    Button cleart;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xCoor = (TextView) findViewById(R.id.xcoor); // create X axis object
        yCoor = (TextView) findViewById(R.id.ycoor); // create Y axis object
        zCoor = (TextView) findViewById(R.id.zcoor); // create Z axis object
        Result = (TextView) findViewById(R.id.Store);
        Judge = (TextView)findViewById(R.id.Judge) ;
        start = (Button) findViewById(R.id.Start);
        end = (Button) findViewById(R.id.End);
        trainstart = (Button)findViewById(R.id.trainstart);
        trainend = (Button)findViewById(R.id.trainend);
        compare = (Button)findViewById(R.id.compare);
        clearo = (Button)findViewById(R.id.clearo);
        cleart = (Button)findViewById(R.id.cleart);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        // add listener. The listener will be MyActivity (this) class
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
        start.setOnClickListener(this);
        end.setOnClickListener(this);
        trainstart.setOnClickListener(this);
        trainend.setOnClickListener(this);
        compare.setOnClickListener(this);
        clearo.setOnClickListener(this);
        cleart.setOnClickListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    // 当sensor事件发生时候调用
    public void onSensorChanged(SensorEvent event) {

        // check sensor type
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

            // assign directions
            x = event.values[0];
            y = event.values[1];
            z = event.values[2];

            x = x-Math.sqrt(Math.abs(9*9-y*y));

            xCoor.setText("X: " + x);
            yCoor.setText("Y: " + y);
            zCoor.setText("Z: " + z);
        }
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Start:
                compareTimer();
                start.setEnabled(false);
                break;
            case R.id.End:
                timer.cancel();
                String temp = "";
                break;
            case R.id.trainstart:
                storetemplate();
                trainstart.setEnabled(false);
                break;
            case R.id.trainend:
                timer2.cancel();
                break;
            case R.id.compare:
                Compare();
                break;
            case R.id.clearo:
                timer = new Timer();
                xd = new ArrayList<Double>();
                yd = new ArrayList<Double>();
                zd = new ArrayList<Double>();
                start.setEnabled(true);
                break;
            case R.id.cleart:
                timer2 = new Timer();
                xt = new ArrayList<Double>();
                yt = new ArrayList<Double>();
                zt = new ArrayList<Double>();
                trainstart.setEnabled(true);
                break;
        }
    }

    private void compareTimer() {
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                xd.add(x);
                yd.add(y);
                zd.add(z);
            }
        }, 800, 5);
    }

    private void storetemplate(){
        timer2.schedule(new TimerTask() {
            @Override
            public void run() {
                xt.add(x);
                yt.add(y);
                zt.add(z);
            }
        },800,5);
    }

    public void storedata(){
        int size = xd.size();
        String xstring = "";
        String ystring = "";
        String zstring = "";
        for(int n=0;n<size;n++){
            xstring = xstring + Double.toString(xd.get(n)) + " ";
            ystring = ystring + Double.toString(yd.get(n)) + " ";
            zstring = zstring + Double.toString(zd.get(n)) + " ";
        }
        SharedPreferences sp =getSharedPreferences("data",Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("xdirection",xstring);
        editor.putString("ydirection",ystring);
        editor.putString("zdirection",zstring);
    }

    public void Compare(){
        int sizet = xt.size();
        int sized = xd.size();
        int number  = 400;
        double result;
        double[] temptx = new double[number];
        double[] tempty = new double[number];
        double[] temptz = new double[number];
        double[] tempdx = new double[number];
        double[] tempdy = new double[number];
        double[] tempdz = new double[number];
        double[][] train = new double[3][number];
        double[][] original = new double[3][number];
        for(int n=0;n<number;n++){
            temptx[n] = xt.get(n);
            tempty[n] = yt.get(n);
            temptz[n] = zt.get(n);
            tempdx[n] = xd.get(n);
            tempdy[n] = yd.get(n);
            tempdz[n] = zd.get(n);
        }
        train[0] = temptx;
        train[1] = tempty;
        train[2] = temptz;
        original[0] = tempdx;
        original[1] = tempdy;
        original[2] = tempdz;
        result = corr2(train,original);
        Result.setText(Double.toString(result));
        if(result>0.5)
        {
            Judge.setText("Same");
        }
        else{
            Judge.setText("Different");
        }
    }

    public double corr2(double[][] train,double[][] original){
        int h = train.length;
        int w = train[0].length;
        double[] meant = new double[3];
        double[] meano = new double[3];
        double[] sumt = new double[3];
        double[] sumo = new double[3];
        double[] fenzi = new double[3];
        double[] fenmut = new double[3];
        double[] fenmuo = new double[3];
        double[] fenmu = new double[3];
        double sumtx = 0;
        double sumox = 0;
        double meantx = 0;
        double meanox = 0;
        double fenzix = 0;
        double fenmuox = 0;
        double fenmutx = 0;
        double fenmux = 0;


        for(int n = 0;n<h;n++){
            for(int m=0;m<w;m++){
                sumt[n] = sumt[n] + train[n][m];
                sumo[n] = sumo[n] + original[n][m];
                sumtx = sumtx + train[n][m];
                sumox =sumox + original[n][m];
            }
            meano[n] = sumo[n]/w;
            meant[n] = sumt[n]/w;
        }
        meantx = sumtx/(h*w);
        meanox = sumox/(h*w);

        for(int n =0;n<h;n++) {
            for (int m = 0; m < w; m++) {
                fenzi[n] = fenzi[n] + (train[n][m] - meant[n]) * (original[n][m] - meano[n]);
                fenmut[n] = fenmut[n] + (train[n][m]-meant[n])*(train[n][m]-meant[n]);
                fenmuo[n] = fenmuo[n] + (original[n][m]-meano[n])*(original[n][m]-meano[n]);
                fenzix = fenzix + (train[n][m] - meantx)*(original[n][m]-meanox);
                fenmutx = fenmutx + (train[n][m]-meantx)*(train[n][m]-meantx);
                fenmuox = fenmuox + (original[n][m]-meanox)*(original[n][m]-meanox);
            }
            fenmu[n] = Math.sqrt(fenmut[n]*fenmuo[n]);
        }
        fenmux = Math.sqrt((fenmutx*fenmuox));
        double sum1 = fenzix/fenmux;
        double sum=0;
        sum = fenzi[0]/fenmu[0]+fenzi[2]/fenmu[2];
        return sum;
    }
    @Override
    protected void onDestroy() {
        sensorManager.unregisterListener(this);
        super.onDestroy();
    }

}
