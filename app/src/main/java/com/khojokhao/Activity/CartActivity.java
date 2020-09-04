package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Paint;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.bumptech.glide.Glide;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.GPSTracker;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.CartModel;
import com.khojokhao.Model.SlotModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CartActivity extends AppCompatActivity {

    RecyclerView rec_cartList, rec_savelater, rec_slot;
    ImageView img_back, img_remove;
    Button btn_pay;
    LinearLayout lnr_discount, lnr_cartempty, lnr_cartDetails, lnr_saverlater, lnr_coupon, lnr_walletBalance;
    TextView edt_address, txt_nodata, txt_unitPrice, txt_discountCharges, txt_subTotal,
            txt_grandTotal, txt_saveLater1, txt_time, txt_couponCode, txt_couponCharges, txt_walletCharges, txt_address, txt_mincart,txt_delivery;
    IOSDialog dialog;
    ArrayList<CartModel> productModelArrayList = new ArrayList<>();
    ArrayList<CartModel> savelaterModelArrayList = new ArrayList<>();
    ArrayList<SlotModel> slotModelArrayList = new ArrayList<>();
    ScrollView scrollView;
    String selectedTime = "", selectedSlotId = "";
    int selectedIndex = -1;
    String total_bag = "", bag_discount = "", order_total = "", delivery_charges = "";
    private final int COUPON_USED = 1;
    String coupon_name = "", coupon_id = "", coupon_value = "", capping_value = "";
    String product_id = "";
    String finalQty = "", finalUnit = "", finalUnitPrice = "", finalDiscount = "";
    ArrayList<String> productIdArrayList = new ArrayList<>();
    ArrayList<String> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> unitpriceArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();
    String walletBalance = "", payBalance = "", miniCart = "";
    Double walletdouble;
    Double subTotaldouble;
    Double grandTotaldouble;
    Double latitude, longitude;
    double minCart = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        init();
        onClick();
//        minimuncart();
//        walletBalance();
//        getProductList();
        getSaveLaterList();
    }

    private void onClick() {

        img_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                coupon_name = "";
                coupon_id = "";
                coupon_value = "";
                capping_value = "";
                getProductList();
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minCart = Double.parseDouble(miniCart);
                if (grandTotaldouble >= minCart) {
                    getSelectedTime();
                    if (selectedIndex == -1) {
                        Toast.makeText(CartActivity.this, "Please select delivery time", Toast.LENGTH_SHORT).show();
                    } else {
                        String discount_amt = "", coupon_amt = "";
                        if (lnr_discount.getVisibility() == View.VISIBLE) {
                            discount_amt = txt_discountCharges.getText().toString();
                            discount_amt = discount_amt.substring(1, discount_amt.length());
                        } else {
                            discount_amt = "";
                        }

                        if (lnr_coupon.getVisibility() == View.VISIBLE) {
                            coupon_amt = txt_couponCharges.getText().toString();
                            coupon_amt = coupon_amt.substring(1, coupon_amt.length());
                        } else {
                            coupon_amt = "";
                        }

                        /*Comma Separated Product Id*/
                        for (int i = 0; i < productIdArrayList.size(); i++) {
                            product_id += productIdArrayList.get(i) + ",";
                        }
                        product_id = product_id.substring(0, product_id.length() - 1);

                        /*Comma Separated Qty*/
                        for (int i = 0; i < qtyArrayList.size(); i++) {
                            finalQty += qtyArrayList.get(i) + ",";
                        }
                        finalQty = finalQty.substring(0, finalQty.length() - 1);

                        /*Comma Separated Unit*/
                        for (int i = 0; i < unitArrayList.size(); i++) {
                            finalUnit += unitArrayList.get(i) + ",";
                        }
                        finalUnit = finalUnit.substring(0, finalUnit.length() - 1);

                        /*Comma Separated Unit Price*/
                        for (int i = 0; i < unitpriceArrayList.size(); i++) {
                            finalUnitPrice += unitpriceArrayList.get(i) + ",";
                        }
                        finalUnitPrice = finalUnitPrice.substring(0, finalUnitPrice.length() - 1);

                        /*Comma Separated Discount*/
                        for (int i = 0; i < discountArrayList.size(); i++) {
                            finalDiscount += discountArrayList.get(i) + ",";
                        }
                        finalDiscount = finalDiscount.substring(0, finalDiscount.length() - 1);

                        Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                        intent.putExtra("product_id", product_id);
                        intent.putExtra("grand_total", txt_grandTotal.getText().toString());
                        intent.putExtra("sub_total", txt_subTotal.getText().toString());
                        intent.putExtra("coupon_id", coupon_id);
                        intent.putExtra("slot_id", selectedSlotId);
                        intent.putExtra("unit_price", txt_unitPrice.getText().toString());
                        intent.putExtra("discount_amt", discount_amt);
                        intent.putExtra("coupon_amt", coupon_amt);
                        intent.putExtra("qty", finalQty);
                        intent.putExtra("finalUnit", finalUnit);
                        intent.putExtra("finalUnitPrice", finalUnitPrice);
                        intent.putExtra("finalDiscount", finalDiscount);
                        intent.putExtra("delivery_charges", delivery_charges);
                        /*intent.putExtra("walletBalance", String.valueOf(walletdouble));
                        intent.putExtra("actualBalance", payBalance);*/
                        startActivity(intent);
                        finish();
                    }
                } else {
//                    txt_mincart.setVisibility(View.GONE);
                    Toast.makeText(CartActivity.this, "Grand total should be greater then minimum cart value i.e. " + String.valueOf(minCart), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    public void getCurrentLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            latitude = new GPSTracker(this).getLatitude();
            longitude = new GPSTracker(this).getLongitude();

            if (latitude != 0.0) {
                try {

                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                txt_address.setText(add);
                    String pin = addresses.get(0).getPostalCode();

//                    if (pin.equals("400614") || pin.equals("400705") || pin.equals("400706") || pin.equals("410208") || pin.equals("410210")) {

//                    } else {
//                        txt_address.setText(add);
//                        new AlertDialog.Builder(CartActivity.this)
//                                .setTitle("Service unavailable")
//                                .setMessage("Currently we are not provide service in your area.")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                    }

                } catch (Exception es) {
                    es.printStackTrace();
                }

            } else {
//                showGpsDisabledDialog();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(CartActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Allow gps location");
        // Setting Dialog Message
        alertDialog.setMessage("Please Allow Location permission");
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
        if (!((Activity) CartActivity.this).isFinishing()) {
            new AlertDialog.Builder(CartActivity.this)
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

    private void init() {
        dialog = new IOSDialog.Builder(CartActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        scrollView = findViewById(R.id.scrollView);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_cartList = findViewById(R.id.rec_cartList);
        rec_savelater = findViewById(R.id.rec_savelater);
        img_back = findViewById(R.id.img_back);
        img_remove = findViewById(R.id.img_remove);
        btn_pay = findViewById(R.id.btn_pay);
        txt_address = findViewById(R.id.txt_address);
        txt_address.setSelected(true);
        txt_mincart = findViewById(R.id.txt_mincart);
        txt_unitPrice = findViewById(R.id.txt_unitPrice);
        txt_discountCharges = findViewById(R.id.txt_discountCharges);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);
        lnr_discount = findViewById(R.id.lnr_discount);
        lnr_cartempty = findViewById(R.id.lnr_cartempty);
        lnr_cartDetails = findViewById(R.id.lnr_cartDetails);
        lnr_saverlater = findViewById(R.id.lnr_saverlater);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        txt_saveLater1 = findViewById(R.id.txt_saveLater);
        txt_couponCharges = findViewById(R.id.txt_couponCharges);
        txt_time = findViewById(R.id.txt_time);
        rec_slot = findViewById(R.id.rec_slot);
        txt_couponCode = findViewById(R.id.txt_couponCode);
        txt_walletCharges = findViewById(R.id.txt_walletCharges);
        lnr_walletBalance = findViewById(R.id.lnr_walletBalance);
        txt_delivery = findViewById(R.id.txt_delivery);
       /* String add = SharedPref.getVal(CartActivity.this, SharedPref.Address);
        edt_address.setText(add);*/

        rec_cartList.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        rec_savelater.setLayoutManager(new LinearLayoutManager(CartActivity.this));
        rec_slot.setLayoutManager(new LinearLayoutManager(CartActivity.this));
    }

    public void pickPoints(View view) {
        minCart = Double.parseDouble(miniCart);
        if (grandTotaldouble >= minCart) {
            getSelectedTime();
            if (selectedIndex == -1) {
                Toast.makeText(CartActivity.this, "Please select delivery time", Toast.LENGTH_SHORT).show();
            } else {
                String discount_amt = "", coupon_amt = "";
                if (lnr_discount.getVisibility() == View.VISIBLE) {
                    discount_amt = txt_discountCharges.getText().toString();
                    discount_amt = discount_amt.substring(1, discount_amt.length());
                } else {
                    discount_amt = "";
                }

                if (lnr_coupon.getVisibility() == View.VISIBLE) {
                    coupon_amt = txt_couponCharges.getText().toString();
                    coupon_amt = coupon_amt.substring(1, coupon_amt.length());
                } else {
                    coupon_amt = "";
                }

                /*Comma Separated Product Id*/
                for (int i = 0; i < productIdArrayList.size(); i++) {
                    product_id += productIdArrayList.get(i) + ",";
                }
                product_id = product_id.substring(0, product_id.length() - 1);

                /*Comma Separated Qty*/
                for (int i = 0; i < qtyArrayList.size(); i++) {
                    finalQty += qtyArrayList.get(i) + ",";
                }
                finalQty = finalQty.substring(0, finalQty.length() - 1);

                /*Comma Separated Unit*/
                for (int i = 0; i < unitArrayList.size(); i++) {
                    finalUnit += unitArrayList.get(i) + ",";
                }
                finalUnit = finalUnit.substring(0, finalUnit.length() - 1);

                /*Comma Separated Unit Price*/
                for (int i = 0; i < unitpriceArrayList.size(); i++) {
                    finalUnitPrice += unitpriceArrayList.get(i) + ",";
                }
                finalUnitPrice = finalUnitPrice.substring(0, finalUnitPrice.length() - 1);

                /*Comma Separated Discount*/
                for (int i = 0; i < discountArrayList.size(); i++) {
                    finalDiscount += discountArrayList.get(i) + ",";
                }
                finalDiscount = finalDiscount.substring(0, finalDiscount.length() - 1);

                Intent intent = new Intent(CartActivity.this, PickPointsListingActivity.class);
                intent.putExtra("product_id", product_id);
                intent.putExtra("grand_total", txt_grandTotal.getText().toString());
                intent.putExtra("sub_total", txt_subTotal.getText().toString());
                intent.putExtra("coupon_id", coupon_id);
                intent.putExtra("slot_id", selectedSlotId);
                intent.putExtra("unit_price", txt_unitPrice.getText().toString());
                intent.putExtra("discount_amt", discount_amt);
                intent.putExtra("coupon_amt", coupon_amt);
                intent.putExtra("qty", finalQty);
                intent.putExtra("finalUnit", finalUnit);
                intent.putExtra("finalUnitPrice", finalUnitPrice);
                intent.putExtra("finalDiscount", finalDiscount);
                intent.putExtra("delivery_charges", delivery_charges);
               /* intent.putExtra("walletBalance", String.valueOf(walletdouble));
                intent.putExtra("actualBalance", payBalance);*/
                startActivity(intent);
                finish();
            }
        } else {
//                    txt_mincart.setVisibility(View.GONE);
            Toast.makeText(CartActivity.this, "Grand total should be greater then minimum cart value i.e. " + String.valueOf(minCart), Toast.LENGTH_SHORT).show();
        }

    }

    public void startShopping(View view) {
        startActivity(new Intent(CartActivity.this, HomeActivity.class));
    }

    public void useCoupon(View view) {
        Intent intent = new Intent(CartActivity.this, CouponListActivity.class);
        startActivityForResult(intent, COUPON_USED);
    }

    public void dismissDialog() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        }, 2000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void getSelectedTime() {
        if (selectedIndex != -1) {
            selectedTime = slotModelArrayList.get(selectedIndex).getSlot_timing();
            selectedSlotId = slotModelArrayList.get(selectedIndex).getSlot_id();
        } else {
            Toast.makeText(this, "Please select expected time", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == COUPON_USED) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK

                coupon_name = data.getStringExtra("coupon_name");
                coupon_id = data.getStringExtra("coupon_id");
                coupon_value = data.getStringExtra("coupon_value");
                capping_value = data.getStringExtra("capping_value");

                getProductList();

            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (FunctionConstant.GPSRuntime(CartActivity.this)) {
            getCurrentLocation();
        } else {
            showSettingsAlert();
        }
        minimuncart();
    }

    public void walletBalance() {
        dialog.show();
        if (APIURLs.isNetwork(CartActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_wallet, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            walletBalance = jsonObject1.getString("wallet");
                            if (walletBalance.equals("0")) {
                                lnr_walletBalance.setVisibility(View.GONE);
                            } else {
                                lnr_walletBalance.setVisibility(View.VISIBLE);
                            }
                            txt_walletCharges.setText("- " + walletBalance);
                            getProductList();
                        } else {
                            Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
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
                    params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
    }

    public void minimuncart() {
        dialog.show();
        if (APIURLs.isNetwork(CartActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.minimum_amount_limit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            miniCart = jsonObject1.getString("min_limit");
                            /*if(walletBalance.equals("0")){
                                lnr_walletBalance.setVisibility(View.GONE);
                            }else{
                                lnr_walletBalance.setVisibility(View.VISIBLE);
                            }
                            txt_walletCharges.setText("- "+walletBalance);
                            getProductList();*/
                            getProductList();
                        } else {
                            Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        dismissDialog();
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
                    params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
//        return miniCart;
    }

    public void getProductList() {
        dialog.show();
        if (APIURLs.isNetwork(CartActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTPRODUCTLIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                            slotModelArrayList = new ArrayList<>();
                            productIdArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitpriceArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();
                            if (jsonObject.has("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    CartModel model = new CartModel();
                                    model.setProduct_id(jsonObject1.getString("product_id"));
                                    model.setProduct_name(jsonObject1.getString("product_name"));
                                    model.setImage(jsonObject1.getString("image"));
                                    model.setQty(jsonObject1.getString("qty"));
                                    model.setUnit(jsonObject1.getString("unit"));
                                    model.setPrice(jsonObject1.getString("price"));
                                    model.setDiscount(jsonObject1.getString("discount"));
                                    model.setCart_id(jsonObject1.getString("cart_id"));
                                    model.setDiscountPrice(jsonObject1.getString("price1"));
                                    model.setDiscount_amt(jsonObject1.getString("discount_amt"));
                                    model.setUnit_price(jsonObject1.getString("unit_price"));
                                    productModelArrayList.add(model);
                                }
                                ProductAdapter adapter = new ProductAdapter(CartActivity.this);
                                rec_cartList.setAdapter(adapter);
                                lnr_cartDetails.setVisibility(View.VISIBLE);
                                lnr_cartempty.setVisibility(View.GONE);
                            }

                            if (jsonObject.has("slot")) {
                                JSONArray slotArray = jsonObject.getJSONArray("slot");
                                for (int i = 0; i < slotArray.length(); i++) {
                                    JSONObject slotObject1 = slotArray.getJSONObject(i);
                                    SlotModel model = new SlotModel();
                                    model.setSlot_id(slotObject1.getString("slot_id"));
                                    model.setDay(slotObject1.getString("day"));
                                    model.setSlot_timing(slotObject1.getString("slot_timing"));
                                    slotModelArrayList.add(model);
                                }
                                SlotAdapter adapter = new SlotAdapter(CartActivity.this);
                                rec_slot.setAdapter(adapter);
                                /*lnr_cartDetails.setVisibility(View.VISIBLE);
                                lnr_cartempty.setVisibility(View.GONE);*/
                            }

                            if (jsonObject.has("bag_details")) {
                                JSONArray orderArray = jsonObject.getJSONArray("bag_details");
                                JSONObject orderObject1 = orderArray.getJSONObject(0);
                                total_bag = orderObject1.getString("total_bag");
                                bag_discount = orderObject1.getString("bag_discount");
                                order_total = orderObject1.getString("order_total");
                                delivery_charges = orderObject1.getString("delivery_charges");

                                if (bag_discount.equals("0")) {
                                    lnr_discount.setVisibility(View.GONE);
                                } else {
                                    lnr_discount.setVisibility(View.VISIBLE);
                                    txt_discountCharges.setText("-" + bag_discount);
                                }
                                if (delivery_charges.equals("0")) {
                                    txt_delivery.setText("FREE");
                                } else {
                                    txt_delivery.setText("Rs. +" + delivery_charges);
                                }

                                txt_unitPrice.setText("Rs." + total_bag);
                                txt_subTotal.setText("Rs." + order_total);
                                try {
                                    if (!capping_value.equals("")) {
                                        img_remove.setVisibility(View.VISIBLE);
                                        if (Integer.parseInt(order_total) >= Integer.parseInt(capping_value)) {
                                            txt_couponCode.setText(coupon_name);
                                            lnr_coupon.setVisibility(View.VISIBLE);
                                            txt_couponCharges.setText("-" + coupon_value);
                                            int finalAmount = Integer.parseInt(order_total) - Integer.parseInt(coupon_value);
                                            order_total = String.valueOf(finalAmount);
                                            txt_grandTotal.setText("Rs." + order_total);
                                            txt_subTotal.setText("Rs." + order_total);
                                        } else {
                                            lnr_coupon.setVisibility(View.GONE);
                                            txt_couponCode.setText("");
                                            Toast.makeText(CartActivity.this, "coupon code not applicable", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        img_remove.setVisibility(View.GONE);
                                        lnr_coupon.setVisibility(View.GONE);
                                        txt_couponCode.setText("");
                                    }
//                                    walletdouble = Double.parseDouble(walletBalance);
                                   /* subTotaldouble = Double.parseDouble(order_total);
                                    grandTotaldouble = 0.0;*/

                                    /*if (subTotaldouble >= walletdouble) {
                                        grandTotaldouble = subTotaldouble - walletdouble;
                                        payBalance = String.valueOf(walletdouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        walletdouble = 0.0;
                                        payBalance = "";
                                    } else {
                                        walletdouble = walletdouble - subTotaldouble;
                                        payBalance = String.valueOf(subTotaldouble);
                                        txt_walletCharges.setText("- " + payBalance);
                                        grandTotaldouble = 0.0;
                                    }*/
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                txt_grandTotal.setText("Rs." + String.valueOf(order_total));
                                grandTotaldouble = Double.parseDouble(order_total);
                                minCart = Double.parseDouble(miniCart);
                                if (grandTotaldouble >= minCart) {
                                    txt_mincart.setVisibility(View.GONE);
                                } else {
                                    txt_mincart.setVisibility(View.VISIBLE);
                                    txt_mincart.setText("Grand total should be greater then minimum cart value i.e. " + String.valueOf(minCart));
                                }
                            }
                        } else {
                            lnr_cartDetails.setVisibility(View.GONE);
                            lnr_cartempty.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
        }
    }

    public void getSaveLaterList() {
        dialog.show();
        if (APIURLs.isNetwork(CartActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SAVELATERPRODUCTLIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            savelaterModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CartModel model = new CartModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setUnit(jsonObject1.getString("unit"));
                                model.setPrice(jsonObject1.getString("price"));
                                model.setDiscount(jsonObject1.getString("discount"));
                                model.setCart_id(jsonObject1.getString("save_id"));

                                savelaterModelArrayList.add(model);
                            }
                            SaveLaterAdapter adapter = new SaveLaterAdapter(CartActivity.this);
                            rec_savelater.setAdapter(adapter);
                            lnr_saverlater.setVisibility(View.VISIBLE);
                            /*lnr_cartempty.setVisibility(View.GONE);*/
                            txt_saveLater1.setText("SAVE FOR LATER (" + savelaterModelArrayList.size() + ")");
                        } else {
                            lnr_saverlater.setVisibility(View.GONE);
                            /*lnr_cartempty.setVisibility(View.VISIBLE);*/
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
                    params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        int qty = 0;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.search_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            CartModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product, lnr_remove;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_saveLater, rel_removeCart;
            CardView card_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                btn_subscribe.setVisibility(View.GONE);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                lnr_remove = itemView.findViewById(R.id.lnr_remove);
                rel_saveLater = itemView.findViewById(R.id.rel_saveLater);
                rel_removeCart = itemView.findViewById(R.id.rel_removeCart);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                card_list = itemView.findViewById(R.id.card_list);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_count = itemView.findViewById(R.id.txt_count);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                lnr_remove.setVisibility(View.VISIBLE);
            }

            public void bind(final CartModel model, final int position) {

                productIdArrayList.add(model.getProduct_id());
                /*   if(unitArrayList.size()<0) {*/
                unitArrayList.add(model.getUnit());
//                }

                /* if(unitpriceArrayList.size()<0) {*/
                unitpriceArrayList.add(model.getUnit_price());
//                }

                /*if(discountArrayList.size()<0){*/
                discountArrayList.add(model.getDiscount());
//                }

                Glide.with(CartActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    } else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        txt_discountprice.setText("Rs." + model.getDiscountPrice());
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        rel_cart.setVisibility(View.VISIBLE);
                        btn_cart.setVisibility(View.GONE);
                    }
                });

                rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (qty >= 1) {
                            qty--;
                            if (qty != 0) {
                                txt_count.setText(String.valueOf(qty));
                                card_list.setVisibility(View.VISIBLE);
                                addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());
                                for (int i = 0; i < qtyArrayList.size(); i++) {
                                    if (position == i) {
                                        qtyArrayList.set(i, String.valueOf(qty));
                                    }
                                }

                              /*  if(unitArrayList.size()>0) {
                                    for (int i = 0; i < unitArrayList.size(); i++) {
                                        if (position == i) {
                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
                                        }
                                    }
                                }

                                if(unitpriceArrayList.size()>0) {
                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                        if (position == i) {
                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                        }
                                    }
                                }

                                if(discountArrayList.size()>0) {
                                    for (int i = 0; i < discountArrayList.size(); i++) {
                                        if (position == i) {
                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                        }
                                    }
                                }*/
                            } else {
                                removeCart(model.getCart_id(), position);
                                for (int i = 0; i < qtyArrayList.size(); i++) {
                                    if (position == i) {
                                        qtyArrayList.set(i, String.valueOf(qty));
                                    }
                                }

                               /* if(unitArrayList.size()>0) {
                                    for (int i = 0; i < unitArrayList.size(); i++) {
                                        if (position == i) {
                                            unitArrayList.set(i, String.valueOf(model.getUnit()));
                                        }
                                    }
                                }

                                if(unitpriceArrayList.size()>0) {
                                    for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                        if (position == i) {
                                            unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                        }
                                    }
                                }

                                if(discountArrayList.size()>0) {
                                    for (int i = 0; i < discountArrayList.size(); i++) {
                                        if (position == i) {
                                            discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                        }
                                    }
                                }*/
                            }
                        }
                    }
                });

                rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        qty++;
                        txt_count.setText(String.valueOf(qty));
                        addCart(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString());

                        for (int i = 0; i < qtyArrayList.size(); i++) {
                            if (position == i) {
                                qtyArrayList.set(i, String.valueOf(qty));
                            }
                        }

/*
                        if(unitArrayList.size()>0) {
                            for (int i = 0; i < unitArrayList.size(); i++) {
                                if (position == i) {
                                    unitArrayList.set(i, String.valueOf(model.getUnit()));
                                }
                            }
                        }

                        if(unitpriceArrayList.size()>0) {
                            for (int i = 0; i < unitpriceArrayList.size(); i++) {
                                if (position == i) {
                                    unitpriceArrayList.set(i, String.valueOf(model.getUnit_price()));
                                }
                            }
                        }

                        if(discountArrayList.size()>0) {
                            for (int i = 0; i < discountArrayList.size(); i++) {
                                if (position == i) {
                                    discountArrayList.set(i, String.valueOf(model.getDiscount()));
                                }
                            }
                        }*/
                    }
                });

                qtyArrayList.add(txt_count.getText().toString());

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(CartActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);

                    }
                });
                rel_removeCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeCart(model.getCart_id(), position);
                    }
                });

                rel_saveLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveLater(model.getProduct_id(), model.getUnit(), model.getUnit_price(), model.getDiscount(), txt_count.getText().toString(), position);
                    }
                });
            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1) {
                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    qty = 0;
                                    getProductList();
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }

            public void removeCart(final String cart_id, final int position) {
                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REMOVECART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    qty = 0;
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    productModelArrayList.remove(position);
                                    if (productModelArrayList.size() < 0) {
                                        lnr_cartempty.setVisibility(View.VISIBLE);
                                        lnr_cartDetails.setVisibility(View.GONE);
                                    } else {
                                        lnr_cartempty.setVisibility(View.GONE);
                                        lnr_cartDetails.setVisibility(View.VISIBLE);
                                    }
                                    getProductList();
                                    txt_couponCode.setText("");
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("cart_id", cart_id);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }

            public void saveLater(final String prod_id, final String unit, final String price, final String discount, final String qty1, final int position) {
                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SAVEFORLATER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                String msg = jsonObject1.getString("msg");
                                if (status.equals("1")) {
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    productModelArrayList.remove(position);
                                    getSaveLaterList();
                                    getProductList();
                                    txt_couponCode.setText("");
                                    coupon_name = "";
                                    coupon_id = "";
                                    coupon_value = "";
                                    capping_value = "";
                                    lnr_coupon.setVisibility(View.GONE);
                                    txt_couponCharges.setText("");
                                } else {
                                    Toast.makeText(CartActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }
        }
    }

    public class SaveLaterAdapter extends RecyclerView.Adapter<SaveLaterAdapter.ViewHolder> {
        Context context;
        Double actualAmout;
        Double discount;
        Double payableAmt;
        int qty = 0;

        public SaveLaterAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.search_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            CartModel model = savelaterModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return savelaterModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product, img_savelater;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count, txt_saveLater;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product, lnr_remove;
            RelativeLayout rel_saveLater, rel_removeCart;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_cart.setVisibility(View.GONE);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_count = itemView.findViewById(R.id.txt_count);
                txt_saveLater = itemView.findViewById(R.id.txt_saveLater);
                img_savelater = itemView.findViewById(R.id.img_savelater);
                rel_saveLater = itemView.findViewById(R.id.rel_saveLater);
                rel_removeCart = itemView.findViewById(R.id.rel_removeCart);
                lnr_remove = itemView.findViewById(R.id.lnr_remove);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                btn_subscribe.setVisibility(View.GONE);
                lnr_remove.setVisibility(View.VISIBLE);
                rel_cart.setVisibility(View.VISIBLE);
                txt_saveLater.setText("MOVE TO CART");
                Glide.with(CartActivity.this).load(R.drawable.carts).into(img_savelater);
            }

            public void bind(final CartModel model, final int position) {
                Glide.with(CartActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    } else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        actualAmout = Double.parseDouble(model.getPrice());
                        discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                        payableAmt = actualAmout - discount;
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt * 100) / 100);
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                /*if (model.getFlag().equals("0")) {
                    txt_stock.setVisibility(View.VISIBLE);
                } else if (model.getFlag().equals("1")) {
                    txt_stock.setVisibility(View.GONE);
                }*/

                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                rel_saveLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), txt_count.getText().toString());
                    }
                });

                rel_removeCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        removeSaveLater(model.getCart_id(), position);
                    }
                });

                /*btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(model.getFlag().equals("1")) {
                            rel_cart.setVisibility(View.VISIBLE);
                            btn_cart.setVisibility(View.GONE);
                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getGst(), model.getDiscount(), txt_count.getText().toString());
                        }else{
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                });*/

                /*rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (qty >= 1) {
                            qty--;
                            if (qty != 0) {
                                txt_count.setText(String.valueOf(qty));
                                addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());
                            } else {
                                rel_cart.setVisibility(View.GONE);
                                btn_cart.setVisibility(View.VISIBLE);
                                addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());
                            }
                        }
                    }
                });*/

                /*rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty++;
                        txt_count.setText(String.valueOf(qty));
                        addCart(model.getProduct_id(),model.getUnit(),model.getPrice(),model.getGst(),model.getDiscount(),txt_count.getText().toString());

                    }
                });*/

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(CartActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);

                    }
                });

                /*btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFlag().equals("0")) {
                            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                        } else if (model.getFlag().equals("1")) {
                            btn_subscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_layout));
                            btn_subscribe.setTextColor(getResources().getColor(R.color.colorWhite));
                            lin_day.setVisibility(View.VISIBLE);
                            card_bottom.setVisibility(View.GONE);
                        }
                    }
                });*/
            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1) {
                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    /*qty = 0;*/
                                    getProductList();
                                    getSaveLaterList();
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("customer_id", SharedPref.getVal(CartActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }

            public void removeSaveLater(final String cart_id, final int position) {
                dialog.show();
                if (APIURLs.isNetwork(CartActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REMOVESAVELATER_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    savelaterModelArrayList.remove(position);
                                    if (savelaterModelArrayList.size() < 0) {
                                        lnr_saverlater.setVisibility(View.GONE);
                                    } else {
                                        lnr_saverlater.setVisibility(View.VISIBLE);
                                    }
                                    getSaveLaterList();
                                } else {
                                    Toast.makeText(CartActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("save_id", cart_id);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(CartActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(CartActivity.this, "no internet connection");
                }
            }
        }
    }

    public class SlotAdapter extends RecyclerView.Adapter<SlotAdapter.ViewHolder> {
        Context context;
        private RadioButton selectedRadioButton;

        public SlotAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(CartActivity.this).inflate(R.layout.slot_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            SlotModel model = slotModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return slotModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            RadioButton rb_time;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                rb_time = itemView.findViewById(R.id.rb_time);
            }

            public void bind(final SlotModel model, final int position) {
                txt_time.setText("Expected Delivery by " + model.getDay());
                rb_time.setText(model.getSlot_timing());
                rb_time.setChecked(false);
                /*selectedRadioButton.setChecked(false);*/
                slotModelArrayList.get(position).setSelected(false);

                if (selectedIndex != -1) {
                    if (selectedIndex == position) {
                        rb_time.setChecked(true);
                        slotModelArrayList.get(position).setSelected(true);
                    } else {
                        rb_time.setChecked(false);
                    }
                }

                rb_time.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        // Set unchecked all other elements in the list, so to display only one selected radio button at a time
                        for (SlotModel model : slotModelArrayList)
                            model.setSelected(false);

                        // Set "checked" the model associated to the clicked radio button
                        slotModelArrayList.get(position).setSelected(true);
                        /*Toast.makeText(context, timingModelArrayList.get(position).getDay(), Toast.LENGTH_SHORT).show();*/

                        // If current view (RadioButton) differs from previous selected radio button, then uncheck selectedRadioButton
                        if (null != selectedRadioButton && !v.equals(selectedRadioButton))
                            selectedRadioButton.setChecked(false);

                        // Replace the previous selected radio button with the current (clicked) one, and "check" it
                        selectedIndex = position;
                        selectedRadioButton = (RadioButton) v;
                        selectedRadioButton.setChecked(true);
                        notifyDataSetChanged();
                    }
                });
            }

        }
    }
}
