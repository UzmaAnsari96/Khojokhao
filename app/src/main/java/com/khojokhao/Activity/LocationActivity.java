package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.GPSTracker;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

import java.util.List;
import java.util.Locale;

public class LocationActivity extends AppCompatActivity {

    RelativeLayout rel_allow;
    double latitude, longitude;
    boolean isPermitted =false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        init();
        onClick();
    }

    public void init(){
        rel_allow = findViewById(R.id.rel_allow);
    }

    public void getCurrentLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            latitude = new GPSTracker(this).getLatitude();
            longitude = new GPSTracker(this).getLongitude();

            try {

                geocoder = new Geocoder(this, Locale.getDefault());
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                txt_location.setText(address);
                SharedPref.putVal(LocationActivity.this, SharedPref.Address, add);


            } catch (Exception es) {
                es.printStackTrace();
            }

            startActivity(new Intent(LocationActivity.this,HomeActivity.class));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void onClick(){
        rel_allow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FunctionConstant.GPSRuntime(LocationActivity.this)){
                    getCurrentLocation();
                }else{
                    Toast.makeText(LocationActivity.this, "Please Allow Location permission", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
