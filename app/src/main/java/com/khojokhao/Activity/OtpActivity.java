package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.google.firebase.iid.FirebaseInstanceId;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import in.aabhasjindal.otptextview.OTPListener;
import in.aabhasjindal.otptextview.OtpTextView;

public class OtpActivity extends AppCompatActivity {

    OtpTextView txt_otp;
    Button btn_process;
    String enter_otp="";
    String mobile="" , otp="";
    IOSDialog dialog;
    TextView txt_resend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);
        init();
        mobile = getIntent().getStringExtra("mobile");
        otp = getIntent().getStringExtra("otp");
        onClick();
    }

    private void onClick() {
        txt_otp.setOtpListener(new OTPListener() {
            @Override
            public void onInteractionListener() {

            }

            @Override
            public void onOTPComplete(String otp) {
                enter_otp = otp;
            }
        });

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(enter_otp.equals("")){
                    ErrorToast("Please enter otp");
                }else if(!enter_otp.equals(otp)){
                    ErrorToast("Please valid enter otp");
                }else{
                    LoginUser();
                }
            }
        });

        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendOtp();
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(OtpActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
       /* dialog.create(OtpActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f);*/
        txt_otp = findViewById(R.id.txt_otp);
        btn_process = findViewById(R.id.btn_process);
        txt_resend = findViewById(R.id.txt_resend);
    }

    public void SuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.sucess_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void ErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }

    public void dismissDialog(){
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(dialog!=null && dialog.isShowing()){
            dialog.dismiss();
        }
    }

    public void LoginUser(){
        dialog.show();
        if(APIURLs.isNetwork(OtpActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.LOGIN_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if(status.equals("1")){
                            SuccessToast(jsonObject1.getString("msg")); //Success Toast

                            /*Save values in shared preference*/
                            SharedPref.putVal(OtpActivity.this,SharedPref.customer_id,jsonObject1.getString("customer_id"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.customer_unique_id,jsonObject1.getString("customer_unique_id"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.mobile_no,jsonObject1.getString("mobile_no"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.email_id,jsonObject1.getString("email_id"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.customer_name,jsonObject1.getString("customer_name"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.token,jsonObject1.getString("token"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.referral_code,jsonObject1.getString("referral_code"));
                            SharedPref.putVal(OtpActivity.this,SharedPref.fran_code,jsonObject1.getString("franchise_id"));

                            startActivity(new Intent(OtpActivity.this,HomeActivity.class));
                            finish();
                        }else{
                            ErrorToast(jsonObject1.getString("msg")); //Error Toast
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
                    params.put("mobile_no", mobile);
                    params.put("token", FirebaseInstanceId.getInstance().getToken());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(OtpActivity.this,"no internet connection");
        }
    }

    public void SendOtp(){
        dialog.show();
        if(APIURLs.isNetwork(OtpActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.LOGIN_OTP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if(status.equals("1")){
                            SuccessToast("Otp sent successfully");  //Show Success Toast
                            otp = jsonObject1.getString("otp");
                        }else{
                            ErrorToast("Something went wrong");  //Show Error Toast
//                            Toast.makeText(LoginActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    params.put("mobile_no", mobile);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(OtpActivity.this,"no internet connection");
        }
    }

}
