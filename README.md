# penPhone Summary

Welcome to penPhone. This project explores feasibility of using a phone's accelerometer data to estimate phone position and draw a representation of movement on the phone's canvas. The ideal functionality is to draw and write arbitrary shapes using only the accelerometer data. We call this Strategy 1. 

## Strategy 1

Strategy 1 Development: The app development process for strategy 1 can be seen in the AccelerometerEtchASketch app. This app is a mashup of functionality from the AccelerometerGolf and FingerPaint apps.

The AccelerometerEtchASketch app was developed in Android Studio 1.5 and use Gradle. You should be able to download and run the app from within Android Studio and with a test device, provided your test device is plugged into your computer. See the sections **Set up your device** and **Run the app from Android Studio** at this link: <http://developer.android.com/training/basics/firstapp/running-app.html>

Strategy 1 Conclusion: The noise problem proved intractable. Because we are double integrating the acceleration to get the position, and our acceleration data has noise, position results quickly become meaningless. We explored concepts of sensor fusion, how linear acceleration is derived from acceleration with gravity, and many filtering strategies. Unfortunately. there is no perfect filter to denoise enough to make such an implementation with the accelerometer data feasible. Our accelerometer is blind to its error. We contrast this to accelerometer and gps data which are often used together in navigation systems to estimate and correct position. For our use -- drawing and writing with the phone -- GPS does not help and the onboard sensors do not provide adequate error correction for denoising. 

## Strategy 2

In Strategy 2 we limit our functionality. We want to recognize a character A-Z drawn by the phone using the accelerometer data. The development strategy of this app is encapsulated in two Android apps developed for capturing and processing accelerometer data. 

  * penPhone (Patricks' app)
  * Xiayu's App

The two Strategy 2 apps were developed in Android Studio 1.5 and use Gradle. You should be able to download and run the apps using the same process mentioned above for the Strategy 1 app. 

## Strategy 2 Feasibility

Our feasibility tests can be seen in the MatlabTests folder. The runMe.m file can be run from within Matlab. The strategy consists of taking the FFT of the accelerometer data, and training an SVM (for each dimension) using the frequency domain data. The SVM requires a consistent dimension vector, so we zero pad the time series to the maximum sample. This is a temporary solution. We anticipate this zero pad strategy decreasing accuracy for the larger number of classes (27) among which we want to distinguish. We would like to replace it with a more accurate and general DSP normalization method to account for varying sampling rates for different phones. However, as a preliminary experiment, runMe provides useful and promising.  If you run the runMe.m file, you can see we get perfect classification for a binary (A or B) classifier with accelerometer data. 

Included in runMe.m are some preliminary notes and ideas on how to classify strings of drawn characters and the issues we may run into when moving from a binary to multiclass SVM (comparing one-v-one and one-v-all).

