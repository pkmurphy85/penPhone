package com.example.patrick.penphone;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Patrick on 4/21/16.
 */
public class WriteSDCard {

    /**
     * Method to check whether external media available and writable. This is adapted from
     * http://developer.android.com/guide/topics/data/data-storage.html#filesExternal
     */

    public void checkExternalMedia() {
        boolean mExternalStorageAvailable = false;
        boolean mExternalStorageWriteable = false;
        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state)) {
            // Can read and write the media
            mExternalStorageAvailable = mExternalStorageWriteable = true;
            if (mExternalStorageAvailable)
                Log.d("Extstorage: ", "true");
            else
                Log.d("Extstorage: ", "false");
        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            // Can only read the media
            mExternalStorageAvailable = true;
            mExternalStorageWriteable = false;
        } else {
            // Can't read or write
            mExternalStorageAvailable = mExternalStorageWriteable = false;
        }
    }

    //currently write fft of x, y, and z data to file. Need to take magnitude
    public void writeToSDFile(ArrayList<Double> xfftMag, ArrayList<Double> yfftMag, ArrayList<Double> zfftMag) {
        try {

            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/svmTestSamples/");
            dir.mkdir();

            File file = new File(dir, DateFormat.getDateTimeInstance().format(new Date()) + ".csv");
            FileOutputStream outputStream = new FileOutputStream(file);

            PrintWriter out = new PrintWriter(outputStream);
            String TestString = "X, Y, Z\n";
            for (int i = 1; i < xfftMag.size(); i++) {
                TestString += Double.toString(xfftMag.get(i)) + ", ";
                TestString += Double.toString(yfftMag.get(i)) + ", ";
                TestString += Double.toString(zfftMag.get(i));
                TestString += "\n";
            }
            out.println(TestString);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
