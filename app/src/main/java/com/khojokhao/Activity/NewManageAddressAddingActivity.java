package com.khojokhao.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewManageAddressAddingActivity extends AppCompatActivity {

    ImageView img_back,img_right,img_wrong;
    RelativeLayout rel_check;
    EditText edt_flatno,edt_bldgName,edt_landmark,edt_area,edt_pincode,edt_sector;
    IOSDialog dialog;
    Button btn_submit;
    String from = "";
    String from_date="",to_date="",subscribe_mode="",product_id="",qty="",day="",unit_price="",unit="",discount="",finalUnit="",finalUnitPrice="",finalDiscount="";
    String grand_total="",sub_total="",coupon_id="",slot_id="",discount_amt="",coupon_amt="",walletBalance="",actualBalance="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_address);
        init();
        onClick();
       /* from = getIntent().getStringExtra("from");
        if(from.equals("subs")){
            getIntentDataSubs();
        }else{
            getIntentDataCart();
        }*/

    }

    private void getIntentDataSubs(){
        from_date = getIntent().getStringExtra("from_date");
        to_date = getIntent().getStringExtra("to_date");
        subscribe_mode = getIntent().getStringExtra("subscribe_mode");
        product_id = getIntent().getStringExtra("product_id");
        qty = getIntent().getStringExtra("qty");
        day = getIntent().getStringExtra("day");
        unit_price = getIntent().getStringExtra("unit_price");
        unit = getIntent().getStringExtra("unit");
        discount = getIntent().getStringExtra("discount");
       /* finalUnit = getIntent().getStringExtra("finalUnit");
        finalUnitPrice = getIntent().getStringExtra("finalUnitPrice");
        finalDiscount = getIntent().getStringExtra("finalDiscount");*/

    }

    private void getIntentDataCart(){
        product_id = getIntent().getStringExtra("product_id");
        grand_total = getIntent().getStringExtra("grand_total");
        sub_total = getIntent().getStringExtra("sub_total");
        coupon_id = getIntent().getStringExtra("coupon_id");
        slot_id = getIntent().getStringExtra("slot_id");
        unit_price = getIntent().getStringExtra("unit_price");
        discount_amt = getIntent().getStringExtra("discount_amt");
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        qty = getIntent().getStringExtra("qty");
        finalUnit = getIntent().getStringExtra("finalUnit");
        finalUnitPrice = getIntent().getStringExtra("finalUnitPrice");
        finalDiscount = getIntent().getStringExtra("finalDiscount");
        walletBalance = getIntent().getStringExtra("walletBalance");
        actualBalance = getIntent().getStringExtra("actualBalance");
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        edt_pincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(edt_pincode.getText().length() == 6){
                   /* if (edt_pincode.getText().toString().equals("400614") || edt_pincode.getText().toString().equals("400705") ||
                            edt_pincode.getText().toString().equals("400706") || edt_pincode.getText().toString().equals("410208") || edt_pincode.getText().toString().equals("410210")) {
                       *//* txt_address.setText(add);*//*
                    } else {
                        new AlertDialog.Builder(NewManageAddressAddingActivity.this)
                                .setTitle("Service unavailable")
                                .setMessage("Currently we are not provide service in your area.")

                                // Specifying a listener allows you to take an action before dismissing the dialog.
                                // The dialog is automatically dismissed when a dialog button is clicked.
                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // Continue with delete operation
                                        dialog.dismiss();
                                        edt_pincode.setText("");
                                    }
                                })
                                .show();
                    }*/

                    checkPincode(edt_pincode.getText().toString());

                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(edt_flatno.getText().toString().equals("")){
                    edt_flatno.setError("Please enter flatno/house no");
                    edt_flatno.requestFocus();
                }else if(edt_bldgName.getText().toString().equals("")){
                    edt_bldgName.setError("Please enter building name");
                    edt_bldgName.requestFocus();
                }else if(edt_landmark.getText().toString().equals("")){
                    edt_landmark.setError("Please enter landmark");
                    edt_landmark.requestFocus();
                }else if(edt_sector.getText().toString().equals("")){
                    edt_sector.setError("Please enter sector");
                    edt_sector.requestFocus();
                }else if(edt_pincode.getText().toString().equals("")){
                    edt_pincode.setError("Please enter pincode");
                    edt_pincode.requestFocus();
                }else if(edt_pincode.getText().length()!=6){
                    edt_pincode.setError("Please enter valid pincode");
                    edt_pincode.requestFocus();
                }else{
                    addNewAddress();
                }
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(NewManageAddressAddingActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        edt_flatno = findViewById(R.id.edt_flatno);
        edt_bldgName = findViewById(R.id.edt_bldgName);
        edt_landmark = findViewById(R.id.edt_landmark);
        edt_area = findViewById(R.id.edt_area);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_sector = findViewById(R.id.edt_sector);
        btn_submit = findViewById(R.id.btn_submit);
        rel_check = findViewById(R.id.rel_check);
        img_right = findViewById(R.id.img_right);
        img_wrong = findViewById(R.id.img_wrong);
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

    public void checkPincode(final String pincode) {
        dialog.show();
        if (APIURLs.isNetwork(NewManageAddressAddingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.check_pincode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if(status.equals("0")){
                            rel_check.setVisibility(View.VISIBLE);
                            img_right.setVisibility(View.GONE);
                            img_wrong.setVisibility(View.VISIBLE);
                            new AlertDialog.Builder(NewManageAddressAddingActivity.this)
                                    .setTitle("Service unavailable")
                                    .setMessage("Currently we are not available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                            edt_pincode.setText("");
                                        }
                                    })
                                    .show();
                        }else{
                            rel_check.setVisibility(View.VISIBLE);
                            img_right.setVisibility(View.VISIBLE);
                            img_wrong.setVisibility(View.GONE);
                            new AlertDialog.Builder(NewManageAddressAddingActivity.this)
                                    .setTitle("Service available")
                                    .setMessage("We are available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
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
                    params.put("pincode", pincode);
                    params.put("franchise_id", SharedPref.getVal(NewManageAddressAddingActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(NewManageAddressAddingActivity.this, "no internet connection");
        }
    }

    public void addNewAddress(){
        dialog.show();
        if (APIURLs.isNetwork(NewManageAddressAddingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_address, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(NewManageAddressAddingActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(NewManageAddressAddingActivity.this,AccountManageAddressActivity.class));
                            /* if(from.equals("subs")){
                                Intent intent = new Intent(NewManageAddressAddingActivity.this, SubscriptionAddressActivity.class);
                                intent.putExtra("from_date",from_date);
                                intent.putExtra("to_date",to_date);
                                intent.putExtra("subscribe_mode",subscribe_mode);
                                intent.putExtra("product_id",product_id);
                                intent.putExtra("day",day);
                                intent.putExtra("unit_price",unit_price);
                                intent.putExtra("unit",unit);
                                intent.putExtra("discount",discount);
                                intent.putExtra("qty",qty);
                                startActivity(intent);
                                finish();
                            }else {
                                Intent intent = new Intent(NewManageAddressAddingActivity.this,AddressActivity.class);
                                intent.putExtra("product_id",product_id);
                                intent.putExtra("grand_total",grand_total);
                                intent.putExtra("sub_total",sub_total);
                                intent.putExtra("coupon_id",coupon_id);
                                intent.putExtra("slot_id",slot_id);
                                intent.putExtra("unit_price",unit_price);
                                intent.putExtra("discount_amt",discount_amt);
                                intent.putExtra("coupon_amt",coupon_amt);
                                intent.putExtra("qty",qty);
                                intent.putExtra("finalUnit",finalUnit);
                                intent.putExtra("finalUnitPrice",finalUnitPrice);
                                intent.putExtra("finalDiscount",finalDiscount);
                                intent.putExtra("walletBalance",walletBalance);
                                intent.putExtra("actualBalance",actualBalance);
                                startActivity(intent);
                                finish();
                            }*/
                            finish();
                        } else {
                            Toast.makeText(NewManageAddressAddingActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(NewManageAddressAddingActivity.this, SharedPref.customer_id));
                    params.put("flat_no", edt_flatno.getText().toString());
                    params.put("building_name", edt_bldgName.getText().toString());
                    params.put("landmark", edt_landmark.getText().toString());
                    params.put("area_name", edt_area.getText().toString());
                    params.put("sector_number", edt_sector.getText().toString());
                    params.put("pincode", edt_pincode.getText().toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(NewManageAddressAddingActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(NewManageAddressAddingActivity.this, "no internet connection");
        }
    }

}
