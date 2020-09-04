package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.razorpay.Checkout;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class SelectPaymentModeActivity extends AppCompatActivity {

    ProgressDialog pDialog;
    String status2;
    ImageView img_back;
    TextView txt_unitPrice, txt_discountCharges, txt_couponCharges, txt_subTotal, txt_grandTotal, txt_TotalSaving,
            txt_deliveryAddress, txt_walletCharges, txt_walletAmount, txt_delivery;
    LinearLayout lnr_coupon, lnr_saving, lnr_discount, lnr_walletBalance, lnr_useWallet, lnr_paymode;
    RelativeLayout rel_cash;
    CheckBox chk_useWallet;
    CardView card_cash, card_online;
    ImageView img_cashSelect, img_onlineSelect;
    Button btn_pay;
    String product_id = "", grand_total = "", sub_total = "", coupon_id = "", slot_id = "", unit_price = "", discount_amt = "", coupon_amt = "", qty = "", address = "", finalUnit = "", finalUnitPrice = "",
            finalDiscount = "", actualBalance = "", walletBalance = "", delivery_status = "", delivery_charges = "";
    String totalSaving = "", area_id;
    IOSDialog dialog;
    String paymentFlag = "", payBalance = "", fid;
    Double walletdouble;
    Double subTotaldouble;
    Double grandTotaldouble;
    static int min, max, create_otp;
    String order_id, razorpayPaymentIDMain, payamt, rid = "NA";
    int capt = 1;
    private Object AlertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_payment_mode);
        init();
        getIntentData();
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        card_cash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_cashSelect.setVisibility(View.VISIBLE);
                img_onlineSelect.setVisibility(View.GONE);
                paymentFlag = "0";
            }
        });
        card_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                img_cashSelect.setVisibility(View.GONE);
                img_onlineSelect.setVisibility(View.VISIBLE);
                paymentFlag = "1";
            }
        });

        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (paymentFlag.equals("")) {
                    Toast.makeText(SelectPaymentModeActivity.this, "Please select payment method", Toast.LENGTH_SHORT).show();
                } else {

                    if (paymentFlag.equals("0")) {


                        fid = SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.fran_code);
                        new LoginAsynck2().execute();

                        // placeOrder();
                    } else {

                        String grand = txt_grandTotal.getText().toString();
                        grand = grand.substring(3, grand.length());
                        String wallet = "";
                        if (grand.equals("0.0")) {
                            grand = sub_total;
                        }

                        try {

                            double a = Double.parseDouble(grand);
                            double b = a;
                            b = b * 100;
                            payamt = String.valueOf(b);

                            // payamt = String.valueOf(Integer.parseInt(grand)*100);

                            min = 100000;
                            max = 999999;
                            Random r = new Random();
                            create_otp = r.nextInt(max - min + 1) + min;
                            order_id = String.valueOf(create_otp);
                            startPayment();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }


                }
            }
        });

        chk_useWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    try {
                        lnr_walletBalance.setVisibility(View.VISIBLE);
                        walletdouble = Double.parseDouble(walletBalance);
                        subTotaldouble = Double.parseDouble(grand_total);
                        grandTotaldouble = 0.0;

                        if (subTotaldouble >= walletdouble) {
                            grandTotaldouble = subTotaldouble - walletdouble;
                            payBalance = String.valueOf(walletdouble);
                            txt_walletCharges.setText("- " + payBalance);
                            walletdouble = 0.0;
                            payBalance = "";
                            txt_grandTotal.setText("Rs " + String.valueOf(grandTotaldouble));
                            lnr_paymode.setVisibility(View.VISIBLE);
                        } else {
                            walletdouble = walletdouble - subTotaldouble;
                            payBalance = String.valueOf(subTotaldouble);
                            txt_walletCharges.setText("- " + payBalance);
                            grandTotaldouble = 0.0;
                            txt_grandTotal.setText("Rs " + String.valueOf(grandTotaldouble));
                            lnr_paymode.setVisibility(View.GONE);
                            paymentFlag = "3";
                        }
//                        setData();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    paymentFlag = "";
                    lnr_walletBalance.setVisibility(View.GONE);
                    lnr_paymode.setVisibility(View.VISIBLE);
                    txt_grandTotal.setText("Rs " + grand_total);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        walletBalance();
    }


    private class LoginAsynck2 extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new android.app.ProgressDialog(SelectPaymentModeActivity.this);
            pDialog.setMessage("Please Wait...");
            pDialog.setCancelable(false);
            pDialog.setIndeterminate(false);
            pDialog.show();
        }

        @Override
        protected String doInBackground(String... arg0) {
            OkHttpClient client2 = new OkHttpClient();
            client2 = new OkHttpClient.Builder()
                    .connectTimeout(120, TimeUnit.SECONDS)
                    .writeTimeout(120, TimeUnit.SECONDS)
                    .readTimeout(120, TimeUnit.SECONDS)
                    .build();

            try {
                RequestBody formBody = new FormBody.Builder()
                        .add("franchise_id", fid)

                        .build();
                okhttp3.Request.Builder builder = new okhttp3.Request.Builder();
                builder.url(APIURLs.order_limit);
                builder.post(formBody);
                okhttp3.Request request = builder.build();
                okhttp3.Response response = client2.newCall(request).execute();
                return response.body().string();
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;

        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (pDialog.isShowing())
                pDialog.dismiss();

            try {
                JSONObject jsn = new JSONObject(result);
                JSONArray test = jsn.optJSONArray("result");
                JSONObject test1 = test.getJSONObject(0);

                status2 = test1.optString("status").toString();


                if (status2.equals("1")) {
                    placeOrder();
                } else {
                    Toast.makeText(SelectPaymentModeActivity.this, "we are unavailable deliver now due to heavy traffic", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private void getIntentData() {

        delivery_status = getIntent().getStringExtra("delivery_status");
        product_id = getIntent().getStringExtra("product_id");
        List<String> items = Arrays.asList(product_id.split("\\s*,\\s*"));
        String position = "";
        for (int i = 0; i < items.size(); i++) {
            String itemstr = items.get(i);
            if (Collections.frequency(items, itemstr) > 1) {
                items.remove(i);
            }
        }
        product_id = TextUtils.join(", ", items);

        grand_total = getIntent().getStringExtra("grand_total");
        grand_total = grand_total.substring(3, grand_total.length());

        sub_total = getIntent().getStringExtra("sub_total");
        sub_total = sub_total.substring(3, sub_total.length());
        delivery_charges = getIntent().getStringExtra("delivery_charges");

        coupon_id = getIntent().getStringExtra("coupon_id");
        slot_id = getIntent().getStringExtra("slot_id");
        area_id = getIntent().getStringExtra("area_id");

        unit_price = getIntent().getStringExtra("unit_price");
        unit_price = unit_price.substring(3, unit_price.length());

        discount_amt = getIntent().getStringExtra("discount_amt");
        if (discount_amt.equals("")) {
            discount_amt = "0";
        }
        coupon_amt = getIntent().getStringExtra("coupon_amt");
        if (coupon_amt.equals("")) {
            coupon_amt = "0";
        }
        qty = getIntent().getStringExtra("qty");
        address = getIntent().getStringExtra("address");
        finalUnit = getIntent().getStringExtra("finalUnit");
        finalUnitPrice = getIntent().getStringExtra("finalUnitPrice");
        finalDiscount = getIntent().getStringExtra("finalDiscount");
      /*  actualBalance = getIntent().getStringExtra("actualBalance");
        walletBalance = getIntent().getStringExtra("walletBalance");*/

       /* if(actualBalance.equals("")){
            lnr_walletBalance.setVisibility(View.GONE);
        }else{
            lnr_walletBalance.setVisibility(View.VISIBLE);
        }
*/

        try {
            if (!coupon_amt.equals("") || !discount_amt.equals("")) {
                //int saving = Integer.parseInt(coupon_amt) + Integer.parseInt(discount_amt);
                double saving = Double.parseDouble(coupon_amt) + Double.parseDouble(discount_amt);
                totalSaving = String.valueOf(saving);
            }
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void setData() {
        dialog.show();
        if (delivery_status.equals("1")) {
            rel_cash.setVisibility(View.GONE);
        } else {
            rel_cash.setVisibility(View.VISIBLE);
        }
        txt_grandTotal.setText("Rs." + grand_total);
        txt_subTotal.setText("Rs." + sub_total);
        txt_unitPrice.setText("Rs." + unit_price);
//        txt_walletCharges.setText("- " + actualBalance);

        if (discount_amt.equals("") || discount_amt.equals("0")) {
            lnr_discount.setVisibility(View.GONE);
        } else {
            lnr_discount.setVisibility(View.VISIBLE);
            txt_discountCharges.setText("-" + discount_amt);
        }
        if (coupon_amt.equals("") || coupon_amt.equals("0")) {
            lnr_coupon.setVisibility(View.GONE);
        } else {
            lnr_coupon.setVisibility(View.VISIBLE);
            txt_couponCharges.setText("-" + coupon_amt);
        }

        if (delivery_charges.equals("0")) {
            txt_delivery.setText("FREE");
        } else {
            txt_delivery.setText("Rs. +" + delivery_charges);
        }
        txt_deliveryAddress.setText(address);
        if (totalSaving.equals("") || totalSaving.equals("0")) {
            lnr_saving.setVisibility(View.GONE);
        } else {
            lnr_saving.setVisibility(View.VISIBLE);
            txt_TotalSaving.setText("Rs." + totalSaving);
        }
        dismissDialog();
    }

    private void init() {
        dialog = new IOSDialog.Builder(SelectPaymentModeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        txt_unitPrice = findViewById(R.id.txt_unitPrice);
        txt_discountCharges = findViewById(R.id.txt_discountCharges);
        txt_couponCharges = findViewById(R.id.txt_couponCharges);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);
        txt_TotalSaving = findViewById(R.id.txt_TotalSaving);
        txt_walletCharges = findViewById(R.id.txt_walletCharges);
        txt_deliveryAddress = findViewById(R.id.txt_deliveryAddress);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        lnr_saving = findViewById(R.id.lnr_saving);
        lnr_discount = findViewById(R.id.lnr_discount);
        card_cash = findViewById(R.id.card_cash);
        card_online = findViewById(R.id.card_online);
        img_cashSelect = findViewById(R.id.img_cashSelect);
        img_onlineSelect = findViewById(R.id.img_onlineSelect);
        lnr_walletBalance = findViewById(R.id.lnr_walletBalance);
        lnr_useWallet = findViewById(R.id.lnr_useWallet);
        lnr_paymode = findViewById(R.id.lnr_paymode);
        chk_useWallet = findViewById(R.id.chk_useWallet);
        txt_walletAmount = findViewById(R.id.txt_walletAmount);
        txt_delivery = findViewById(R.id.txt_delivery);
        btn_pay = findViewById(R.id.btn_pay);
        rel_cash = findViewById(R.id.rel_cash);
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

    public void walletBalance() {
        dialog.show();
        if (APIURLs.isNetwork(SelectPaymentModeActivity.this)) {
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
                                lnr_useWallet.setVisibility(View.GONE);
                            } else {
                                lnr_useWallet.setVisibility(View.VISIBLE);
                                txt_walletAmount.setText("Available Balance Rs. " + walletBalance);
                            }
                            /*
                            txt_walletCharges.setText("- " + walletBalance);*/
//                            getProductList();
                        } else {
                            Toast.makeText(SelectPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SelectPaymentModeActivity.this, "no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
    }

    public void placeOrder() {
        dialog.show();
        if (APIURLs.isNetwork(SelectPaymentModeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.placeOrder, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SelectPaymentModeActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            String OrderId = jsonObject1.getString("order_generate_id");
                            Intent intent = new Intent(SelectPaymentModeActivity.this, OrderPlacedActivity.class);
                            intent.putExtra("OrderId", OrderId);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SelectPaymentModeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    String grand = txt_grandTotal.getText().toString();
                    grand = grand.substring(3, grand.length());
                    String wallet = "";
                    if (grand.equals("0.0")) {
                        grand = sub_total;
                    }
                    if (chk_useWallet.isChecked()) {
                        wallet = txt_walletCharges.getText().toString();
                        wallet = wallet.substring(2, wallet.length());
                    } else {
                        wallet = "0.0";
                    }
                    if(delivery_charges.equals("null") || delivery_charges == null){
                        delivery_charges = "0";
                    }
                    HashMap<String, String> params = new HashMap<>();
                    params.put("customer_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_id));
                    params.put("franchise_id", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.fran_code));
                    params.put("total_bag", unit_price);
                    params.put("bag_discount", discount_amt);
                    params.put("order_total", grand);
                    params.put("slot_id", slot_id);
                    params.put("coupon_id", coupon_id);
                    params.put("deliver_address", address);
                    params.put("flag", paymentFlag);
                    params.put("product_id", product_id);
                    params.put("qty", qty);
                    params.put("unit", finalUnit);
                    params.put("unit_price", finalUnitPrice);
                    params.put("discount", finalDiscount);
                    params.put("minus_wallet", wallet);
                    params.put("delivery_status", delivery_status);
                    params.put("rid", rid);
                    params.put("area_id", area_id);
                    params.put("delivery_charges", delivery_charges);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SelectPaymentModeActivity.this, "no internet connection");
        }
    }


    public void startPayment() {

        final Activity activity = this;

        final Checkout co = new Checkout();


        try {
            JSONObject options = new JSONObject();
            options.put("name", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.customer_name));
            options.put("description", "Khojo Khao");
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", "INR");
            options.put("amount", payamt);


            JSONObject preFill = new JSONObject();
            preFill.put("email", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.email_id));
            preFill.put("contact", SharedPref.getVal(SelectPaymentModeActivity.this, SharedPref.mobile_no));
            preFill.put("payment_capture", capt);
            preFill.put("order_id", order_id);

            options.put("prefill", preFill);

            co.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error com payment: " + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
            e.printStackTrace();
        }

    }


    @SuppressWarnings("unused")

    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_LONG).show();
            razorpayPaymentIDMain = razorpayPaymentID;
            rid = razorpayPaymentID;
            placeOrder();
        } catch (Exception e) {

        }
    }


    @SuppressWarnings("unused")

    public void onPaymentError(int code, String response) {
        try {

            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_LONG).show();

        } catch (Exception e) {

        }
    }


}
