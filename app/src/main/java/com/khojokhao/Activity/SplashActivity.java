package com.khojokhao.Activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;

import com.khojokhao.Constant.PrefManager;
import com.khojokhao.Constant.RuntimePermissionClass;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

public class SplashActivity extends RuntimePermissionClass {

    PrefManager prefManager;
    private static final int REQUEST_PERMISSIONS = 20;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
//        FunctionConstant.GPSRuntime(SplashActivity.this);
        prefManager = new PrefManager(SplashActivity.this);
        /*FunctionConstant.GPSRuntime(SplashActivity.this);
        if (FunctionConstant.InternetRuntime(SplashActivity.this)) {
            startSplash();
            *//*else{
                showSettingsAlert();
            }*//*
        } else {
            Toast.makeText(this, "Please allow permission", Toast.LENGTH_SHORT).show();
        }*/

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            SplashActivity.super.requestAppPermissions(new String[]{
                            Manifest.permission.ACCESS_NETWORK_STATE,
                            Manifest.permission.INTERNET,
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION,
                          /*  Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,*/
                    }, R.string.runtime_permissions_txt
                    , REQUEST_PERMISSIONS);
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode) {
        startSplash();
    }

    public void startSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                /*if(prefManager.isFirstTimeLaunch()){
                    startActivity(new Intent(SplashActivity.this, IntroSlide1Activity.class));
                    finish();
                }else{
//                    startActivity(new Intent(SplashActivity.this,LoginActivity.class));
//                    finish();


                }*/

                if (SharedPref.getBol(SplashActivity.this, SharedPref.isLogin)) {
//                        getDriverStatus();

                    Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {

                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        }, 1000);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SplashActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Permission Needed");
        // Setting Dialog Message
        alertDialog.setMessage("location permission needed to access this application.\n" +
                "Go to settings -> Permissions -> allow location Permission ");
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.show();
    }
}
