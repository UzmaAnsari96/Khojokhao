package com.khojokhao.Activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AccountActivity extends AppCompatActivity {

    ImageView img_back, img_profile;
    CardView card_mySubscription;
    TextView txt_name, txt_email, txt_mobile, txt_edit;
    IOSDialog dialog;
    String customer_id="",customer_unique_id="",first_name="",last_name="",mobile_no="",email_id="",photo="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        init();
        onClick();
    }

    private void onClick() {

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }

    private void init() {
        dialog = new IOSDialog.Builder(AccountActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        img_profile = findViewById(R.id.img_profile);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_mobile = findViewById(R.id.txt_mobile);
        txt_edit = findViewById(R.id.txt_edit);
        card_mySubscription = findViewById(R.id.card_mySubscription);
    }

    public void dismissDialog() {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void myOrder(View view) {
        startActivity(new Intent(AccountActivity.this,MyOrderActivity.class));
    }

    public void editProfile(View view) {
        startActivity(new Intent(AccountActivity.this,UpdateProfileActivity.class));
    }

    public void shopyBalance(View view) {
        startActivity(new Intent(AccountActivity.this,ShopyBalanceActivity.class));
    }

    public void notification(View view) {
        startActivity(new Intent(AccountActivity.this,NotificationActivity.class));
    }

    public void myCoupons(View view) {
        startActivity(new Intent(AccountActivity.this,CouponsActivity.class));
    }


    public void mySubscription(View view) {
        startActivity(new Intent(AccountActivity.this,SubscriptionActivity.class));
    }

    public void deliveryAddress(View view) {
        startActivity(new Intent(AccountActivity.this,AccountManageAddressActivity.class));
    }

    public void refer(View view) {
        startActivity(new Intent(AccountActivity.this,ReferActivity.class));
    }


    public void aboutUs(View view) {
        startActivity(new Intent(AccountActivity.this,AboutUsActivity.class));
    }


    public void helpnSupport(View view) {
        startActivity(new Intent(AccountActivity.this,HelpAndSupportActivity.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        getProfile();
    }

    public void logout(View view) {
        new AlertDialog.Builder(this)
                .setTitle("Really Logout?")
                .setMessage("Are you sure you want to logout?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        SharedPref.clearData(AccountActivity.this);
                        startActivity(new Intent(AccountActivity.this,LoginActivity.class));
                        finishAffinity();
                    }
                }).create().show();
    }

    public void getProfile() {
        dialog.show();
        if (APIURLs.isNetwork(AccountActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            customer_id = jsonObject1.getString("customer_id");
                            customer_unique_id = jsonObject1.getString("customer_unique_id");
                            first_name = jsonObject1.getString("first_name");
                            last_name = jsonObject1.getString("last_name");
                            mobile_no = jsonObject1.getString("mobile_no");
                            email_id = jsonObject1.getString("email_id");
//                            photo = jsonObject1.getString("photo");

                            setData();

                        } else {
                            Toast.makeText(AccountActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        dismissDialog();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog();
                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(AccountActivity.this,SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AccountActivity.this, "no internet connection");
        }
    }

    public void setData(){
        txt_name.setText(first_name + " "+last_name);
        txt_mobile.setText(mobile_no);
        txt_email.setText(email_id);
        /*Glide.with(AccountActivity.this).load(photo).into(img_profile);*/
        Picasso.with(AccountActivity.this).load(R.drawable.logo).into(img_profile);
    }

}
