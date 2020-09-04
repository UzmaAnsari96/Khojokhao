package com.khojokhao.Constant;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import androidx.core.app.ActivityCompat;

public class FunctionConstant {

    public static boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean GPSRuntime(Activity activity) {
        boolean granted = false;
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1);
                granted = false;
            } else {
                // Write you code here if permission already given.
                granted = true;

            }
        } catch (Exception e) {
            granted = false;
            System.out.print("");
        }
        return granted;
    }

    public static boolean InternetRuntime(Activity activity) {
        boolean granted = false;
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_NETWORK_STATE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.INTERNET, Manifest.permission.ACCESS_NETWORK_STATE}, 1);
                granted = false;
            } else {
                // Write you code here if permission already given.
                granted = true;

            }
        } catch (Exception e) {
            granted = false;
            System.out.print("");
        }
        return granted;
    }

    public static boolean picRuntime(Activity activity) {
        boolean granted = false;
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.CAMERA}, 1);
                granted = false;
            } else {
                // Write you code here if permission already given.
                granted = true;

            }
        } catch (Exception e) {
            granted = false;
            System.out.print("");
        }
        return granted;
    }

    public static boolean SmsRuntime(Activity activity) {
        boolean granted = false;
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(activity, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_SMS,
                        Manifest.permission.RECEIVE_SMS}, 1);
                granted = false;
            } else {
                // Write you code here if permission already given.
                granted = true;

            }
        } catch (Exception e) {
            granted = false;
            System.out.print("");
        }
        return granted;
    }

    public static boolean CallRuntime(Activity activity) {
        boolean granted = false;
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, 1);
                granted = false;
            } else {
                // Write you code here if permission already given.
                granted = true;

            }
        } catch (Exception e) {
            granted = false;
            System.out.print("");
        }
        return granted;
    }

    public static void noInternetDialog(Activity a, String message){
        AlertDialog.Builder dialog = new AlertDialog.Builder(a);
        /*dialog.setTitle(title);*/
        dialog.setMessage(message);
        dialog.setNeutralButton("Ok", null);
        dialog.create().show();

    }
}
