package com.example.patrick.penphone;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.support.v7.widget.Toolbar;

import com.example.patrick.penphone.CaptureData;
import com.example.patrick.penphone.R;

import org.opencv.ml.SVM;

public class MainActivity extends Activity {

    static{ System.loadLibrary("opencv_java3"); }

    private Button btnCapture, btnTrain, btnWrite;
    private static final int MY_PERMISSIONS_REQUEST_EXTERNAL_STORAGE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        btnCapture = (Button) findViewById(R.id.btnCapture);
        btnTrain = (Button) findViewById(R.id.btnTrain);
        btnWrite = (Button) findViewById(R.id.btnWrite);
        btnCapture.setEnabled(true);
        btnTrain.setEnabled(true);
        btnWrite.setEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent captureIntent = new Intent(MainActivity.this, CaptureData.class);
                startActivity(captureIntent);
            }
        });

        btnTrain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add new activity here for SVM training and classification

            }
        });

        btnWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Add new activity here to make predicitons on samples and print output to canvas

            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}