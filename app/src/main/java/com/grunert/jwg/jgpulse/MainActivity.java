package com.grunert.jwg.jgpulse;

import android.app.Activity;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrixColorFilter;
import android.hardware.Camera;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends AppCompatActivity {
    Camera mCamera = null;
    CameraPreview mPreview;
    private static final AtomicBoolean processing = new AtomicBoolean(false);
    private static TextView textViewl;
    private static TextView textViewc;
    private static TextView textViewr;
    private static int averageIndex = 0;
    private static final int averageArraySize = 4;
    private static final int[] averageArray = new int[averageArraySize];
    private static int beatsIndex = 0;
    private static final int beatsArraySize = 3;
    private static final int[] beatsArray = new int[beatsArraySize];
    private static double beats = 0;
    private static long startTime = 0;
    private static SquareScaleImageView needle;

    public static enum TYPE {
        GREEN, RED
    };

    private static TYPE currentType = TYPE.GREEN;

    public static TYPE getCurrent() {
        return currentType;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textViewl = (TextView) findViewById(R.id.textViewl);
        textViewc = (TextView) findViewById(R.id.textViewc);
        textViewr = (TextView) findViewById(R.id.textViewr);
        needle = (SquareScaleImageView) findViewById(R.id.needle);
    }

    @Override
    protected void onResume() {
        super.onResume();
        startTime = System.currentTimeMillis();


        try {
            mCamera = Camera.open(0);
            Camera.Parameters mCameraParameters = mCamera.getParameters();
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCameraParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            mCamera.setParameters(mCameraParameters);
        }
        catch (Exception e) {
            Toast.makeText(getApplicationContext(),"Can't open camera " + e.getMessage(),Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }

        if (mCamera != null) {
            setCameraDisplayOrientation(this,0,mCamera);
            mPreview = new CameraPreview(this, mCamera);
            mCamera.setPreviewCallback(previewCallback);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            mCamera.startPreview();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            releaseCamera();              // release the camera immediately on pause event
        }
    }

    private static Camera.PreviewCallback previewCallback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            if (data == null) throw new NullPointerException();
            Camera.Size size = camera.getParameters().getPreviewSize();
            if (size == null) throw new NullPointerException();
           // if (!processing.compareAndSet(false, true)) return;
            int width = size.width;
            int height = size.height;

            int imgAvg = ImageProcessing.decodeYUV420SPtoRedAvg(data.clone(), height, width);
            // Log.i("Jens","imgAvg = "+imgAvg);

            textViewl.setText("imgAvg = "+imgAvg);

            int averageArrayAvg = 0;
            int averageArrayCnt = 0;
            for (int i = 0; i < averageArray.length; i++) {
                if (averageArray[i] > 0) {
                    averageArrayAvg += averageArray[i];
                    averageArrayCnt++;
                }
            }

            int rollingAverage = (averageArrayCnt > 0) ? (averageArrayAvg / averageArrayCnt) : 0;

            TYPE newType = currentType;
            if (imgAvg < rollingAverage) {
                newType = TYPE.RED;
                if (newType != currentType) {
                    beats++;
                    // Log.d(TAG, "BEAT!! beats="+beats);
                    textViewc.setText("BEAT!! beats = " + beats);
                    rotateNeedle(needle, 90);
                } else {
                    // rotateNeedle(needle,0);
                }
            } else if (imgAvg > rollingAverage) {
                newType = TYPE.GREEN;
            }

            if (averageIndex == averageArraySize) averageIndex = 0;
            averageArray[averageIndex] = imgAvg;
            averageIndex++;

            // Transitioned from one state to another to the same
            if (newType != currentType) {
                currentType = newType;
            }

            long endTime = System.currentTimeMillis();
            double totalTimeInSecs = (endTime - startTime) / 1000d;
            if (totalTimeInSecs >= 10) {
                double bps = (beats / totalTimeInSecs);
                int dpm = (int) (bps * 60d);
                if (dpm < 30 || dpm > 180) {
                    startTime = System.currentTimeMillis();
                    beats = 0;
                    processing.set(false);
                    return;
                }

                // Log.d(TAG,
                // "totalTimeInSecs="+totalTimeInSecs+" beats="+beats);

                if (beatsIndex == beatsArraySize) beatsIndex = 0;
                beatsArray[beatsIndex] = dpm;
                beatsIndex++;

                int beatsArrayAvg = 0;
                int beatsArrayCnt = 0;
                for (int i = 0; i < beatsArray.length; i++) {
                    if (beatsArray[i] > 0) {
                        beatsArrayAvg += beatsArray[i];
                        beatsArrayCnt++;
                    }
                }
                int beatsAvg = (beatsArrayAvg / beatsArrayCnt);
                textViewr.setText(String.valueOf(beatsAvg));
                startTime = System.currentTimeMillis();
                beats = 0;
            }
        }
    };

    private void releaseCamera() {
        if (mCamera != null){
            mCamera.release();        // release the camera for other applications
            mCamera = null;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info = new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        //int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        // do something for phones running an SDK before lollipop
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360; // compensate the mirror
        } else { // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }

        camera.setDisplayOrientation(result);
    }

    private static void rotateNeedle(ImageView iv, int differential_angel){
        int angel;

        // iv.setRotation(differential_angel);
        // iv.animate().rotation(differential_angel).setDuration(100).setInterpolator(new AccelerateDecelerateInterpolator()).start();
        iv.setDrawingCacheEnabled(true);
        RotateAnimation rotate = new RotateAnimation(0, 90, Animation.RELATIVE_TO_SELF,
                0.5f,  Animation.RELATIVE_TO_SELF, 0.5f);
        rotate.setInterpolator(new AccelerateDecelerateInterpolator());
        rotate.setDuration(500);
        rotate.setRepeatMode(Animation.REVERSE);
        rotate.setRepeatCount(0);

        // iv.startAnimation(rotate);

        iv.setAnimation(rotate);
        rotate.start();

        angel = Math.abs(differential_angel);

       /*
        * 5x4 matrix for transforming the color+alpha components of a Bitmap.
        * The matrix is stored in a single array, and its treated as follows:
        * [  a, b, c, d, e,
        *   f, g, h, i, j,
        *   k, l, m, n, o,
        *   p, q, r, s, t ]
        *
        * When applied to a color [r, g, b, a], the resulting color is computed
        * as (after clamping)
        * R' = a*R + b*G + c*B + d*A + e;
        * G' = f*R + g*G + h*B + i*A + j;
        * B' = k*R + l*G + m*B + n*A + o;
        * A' = p*R + q*G + r*B + s*A + t;
        */

        float redValue = ((float)angel*255f/180f)/255;
        float greenValue = ((float)angel*-255f/180f+255f)/255;
        float blueValue = 0;

        float[] colorMatrix = {
                redValue, 0, 0, 0, 0,  //red
                0, greenValue, 0, 0, 0, //green
                0, 0, blueValue, 0, 0,  //blue
                0, 0, 0, 1, 0    //alpha
        };

        ColorFilter colorFilter = new ColorMatrixColorFilter(colorMatrix);
        iv.setColorFilter(colorFilter);
    }
}
