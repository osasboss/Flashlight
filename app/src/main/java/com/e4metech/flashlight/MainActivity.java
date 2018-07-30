package com.e4metech.flashlight;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {
    private ImageView flashLight;
    private static final int CAMERA_REQUEST = 50;
    private boolean flashLightStatus = false;
    private ImageView image;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //ads below
        MobileAds.initialize(this,"ca-app-pub-3570004499550636~2698910603");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        image = findViewById(R.id.light_switch);
        flashLight = (ImageView) findViewById(R.id.light_switch);

        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
        final boolean hasCameraFlash = getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);

        if (!hasCameraFlash) {
            AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                    .create();
            alert.setTitle("Error !!");
            alert.setMessage("Your device doesn't support flash light!");
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    // close the application
                    finish();
                    System.exit(0);
                }
            });
            alert.show();
            return;
        }

        flashLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                {
                    if (flashLightStatus)
                        turnLightOff();
                    else
                        turnLightOn();
                }
            }
        });
    }

    private void turnLightOn() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cam_Id = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cam_Id, true);
            flashLightStatus = true;
            image.setImageResource(R.drawable.off);
        } catch (CameraAccessException e) {
        }
    }

    private void turnLightOff() {
        CameraManager cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            String cam_Id = cameraManager.getCameraIdList()[0];
            cameraManager.setTorchMode(cam_Id, false);
            flashLightStatus = false;
            image.setImageResource(R.drawable.on);
        } catch (CameraAccessException e) {
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case CAMERA_REQUEST:
                if (!(grantResults.length > 0) && !(grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    AlertDialog alert = new AlertDialog.Builder(MainActivity.this)
                            .create();
                    alert.setTitle("Error !!");
                    alert.setMessage("Camera permission not granted!");
                    alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // close the application
                            finish();
                            System.exit(0);
                        }
                    });
                    alert.show();
                    return;
                }
                break;
        }
    }
}