package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    EditText edt_mobile;
    TextView txt_account;
    Button btn_process;
    CheckBox check_tnc;
    IOSDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        onClick();
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

    private void init() {
       /* dialog = new KProgressHUD(LoginActivity.this);
        dialog.create(LoginActivity.this)
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait...")
                .setCancellable(true)
                .setAnimationSpeed(2)
                .setDimAmount(0.8f);*/
        dialog = new IOSDialog.Builder(LoginActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        edt_mobile = findViewById(R.id.edt_mobile);
        check_tnc = findViewById(R.id.check_tnc);
        btn_process = findViewById(R.id.btn_process);
        txt_account = findViewById(R.id.txt_account);
        String nothaveaccount = "Don't have an account ? <font color=#353FDF><u><b>Sign up</b></u></font>";
        txt_account.setText(Html.fromHtml(nothaveaccount));
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

    public void onClick(){
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                }else if (!check_tnc.isChecked()) {
//                    edt_mobile.setError("Please valid enter mobile number");
                   Toast.makeText(LoginActivity.this, "Please accept the Terms & Conditions", Toast.LENGTH_SHORT).show();
                } else{
                   SendOtp();
                }
            }
        });
    }
    public void register(View view){
        startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
    }

    public void forgot(View view){
        startActivity(new Intent(LoginActivity.this,ForgotPasswordActivity.class));
    }

    public void showDialog() {
        final Dialog dialog1 = new Dialog(LoginActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
//        dialog1.getWindow().setDimAmount((float) 0.8);

        ImageView img_close = dialog1.findViewById(R.id.img_close);
        TextView txt_here = dialog1.findViewById(R.id.txt_here);

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        txt_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    public void SendOtp(){
        dialog.show();
        if(APIURLs.isNetwork(LoginActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.LOGIN_OTP_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if(status.equals("1")){
                            SuccessToast(msg);  //Show Success Toast

                            Intent intent = new Intent(LoginActivity.this,OtpActivity.class);
                            intent.putExtra("mobile",edt_mobile.getText().toString());
                            intent.putExtra("otp",jsonObject1.getString("otp"));
                            startActivity(intent);
                            finish();
                        }else{
//                            ErrorToast(msg);  //Show Error Toast
//                            Toast.makeText(LoginActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            showDialog();
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
                    params.put("mobile_no", edt_mobile.getText().toString().trim());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(30000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(LoginActivity.this,"no internet connection");
        }
    }

    public void Terms(View view){
       startActivity(new Intent(LoginActivity.this,TermsConditionActivity.class));
    }
}
