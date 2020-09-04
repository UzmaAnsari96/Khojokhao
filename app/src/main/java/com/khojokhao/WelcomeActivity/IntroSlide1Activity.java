package com.khojokhao.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.khojokhao.Activity.LoginActivity;
import com.khojokhao.Adapter.SliderAdapter;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.GPSTracker;
import com.khojokhao.Constant.PrefManager;
import com.khojokhao.R;

import java.util.List;
import java.util.Locale;

public class IntroSlide1Activity extends AppCompatActivity {

    PrefManager prefManager;
    ImageView background_img;

    RelativeLayout rel_allow;
    double latitude, longitude;
    boolean isPermitted =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slide1);

        prefManager = new PrefManager(IntroSlide1Activity.this);
        FunctionConstant.GPSRuntime(IntroSlide1Activity.this);
        init();
        Slider();
    }

    private void Slider() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        SliderAdapter adapter = new SliderAdapter(this);

        sliderView.setSliderAdapter(adapter);

//        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_RIGHT);

//        sliderView.setIndicatorSelectedColor(Color.WHITE);
//        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(2); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    private void init() {
        background_img = findViewById(R.id.background_img);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(FunctionConstant.GPSRuntime(IntroSlide1Activity.this)){
            getCurrentLocation();
        }
        /*else{
            showSettingsAlert();
        }*/
    }

    public void slide2(View view) {
//        background_img.setImageBitmap(null);
//        background_img.setBackground(getResources().getDrawable(R.drawable.fruit_bg));
        startActivity(new Intent(IntroSlide1Activity.this, LoginActivity.class));
        finish();
        prefManager.setFirstTimeLaunch(false);
    }

    public void locationPermission(View view){
                if(FunctionConstant.GPSRuntime(IntroSlide1Activity.this)){
                    getCurrentLocation();
                }else{
                   showSettingsAlert();
                }

    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(IntroSlide1Activity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Allow gps location");
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
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }

    public void showGpsDisabledDialog() {
        if (!((Activity) IntroSlide1Activity.this).isFinishing()) {
            new AlertDialog.Builder(IntroSlide1Activity.this)
                    .setMessage("GPS Disabled , to Continue, turn on your device location ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                            dialog.dismiss();
                        }
                    })

                    /*// A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }
    }

    public void getCurrentLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            latitude = new GPSTracker(this).getLatitude();
            longitude = new GPSTracker(this).getLongitude();

            if(latitude != 0.0) {
                try {

                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                txt_location.setText(address);
                    String pin = addresses.get(0).getPostalCode();

                } catch (Exception es) {
                    es.printStackTrace();
                }

            }else{
                showGpsDisabledDialog();
            }
//            startActivity(new Intent(IntroSlide1Activity.this, HomeActivity.class));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
