package com.khojokhao.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
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
import java.util.Random;

public class RegisterActivity extends AppCompatActivity {

    EditText edt_fname, edt_lname, edt_email, edt_mobile, edt_pincode,edt_referralCode,statename,cityename;
    Button btn_process;
    IOSDialog dialog;
    private static final String symbols = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    ImageView img_scanner;
    final int QR_SCANNER = 1;
    private static final int REQUEST_OPERATOR = 1;
    private static final int REQUEST_OPERATORR = 2;
    String ssityid="NA",ssityname,scityid,scityname;
    String eid="NA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
        onClick();
        FunctionConstant.picRuntime(RegisterActivity.this);
    }

    public static String generateReferralCode(Random r) {
        while (true) {
            char[] password = new char[r.nextBoolean() ? 8 : 8];
            boolean hasUpper = false, hasDigit = false;
            for (int i = 0; i < password.length; i++) {
                char ch = symbols.charAt(r.nextInt(symbols.length()));
                if (Character.isUpperCase(ch))
                    hasUpper = true;
                else if (Character.isDigit(ch))
                    hasDigit = true;
                password[i] = ch;
            }
            if (hasUpper && hasDigit) {
                return new String(password);
            }
        }
    }

    private void onClick() {

        img_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FunctionConstant.picRuntime(RegisterActivity.this)) {
                    startActivityForResult(new Intent(RegisterActivity.this, QrCodeScannerActivity.class), QR_SCANNER);
                }else{
                    Toast.makeText(RegisterActivity.this, "Camera Permission Required", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {

                    eid=edt_email.getText().toString();
                    if(eid.equals("")||eid.equals("null")||eid.isEmpty())
                    {
                        eid="NA";
                    }

                }
                catch (Exception e)
                {
                    eid="NA";
                }


                if (edt_fname.getText().toString().equals("")) {
                    edt_fname.setError("Please enter full name");
                    edt_fname.requestFocus();
                } /*else if (edt_lname.getText().toString().equals("")) {
                    edt_lname.setError("Please enter last name");
                    edt_lname.requestFocus();
                }*/

                else if (statename.getText().toString().equals("")) {
                    statename.setError("Please select State name");
                    statename.requestFocus();
                }

                else if (cityename.getText().toString().equals("")) {
                    cityename.setError("Please select City name");
                    cityename.requestFocus();
                }

                /*else if (edt_email.getText().toString().equals("")) {
                    edt_email.setError("Please enter email id");
                    edt_email.requestFocus();
                }*/ /*else if (!FunctionConstant.isValidEmail(edt_email.getText().toString())) {
                    edt_email.setError("Please valid enter email id");
                    edt_email.requestFocus();
                }*/ else if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_pincode.getText().toString().equals("")) {
                    edt_pincode.setError("Please enter pincode");
                } else if (edt_pincode.getText().toString().length() != 6) {
                    edt_pincode.setError("Please enter valid pincode");
                }else {
                    RegisterUser();
                }
            }
        });


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

    private void init() {
        dialog = new IOSDialog.Builder(RegisterActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_pincode = findViewById(R.id.edt_pincode);
        edt_referralCode = findViewById(R.id.edt_referralCode);
        btn_process = findViewById(R.id.btn_process);
        img_scanner = findViewById(R.id.img_scanner);
        statename = findViewById(R.id.statename);
        cityename = findViewById(R.id.cityename);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == QR_SCANNER) {
            if (resultCode == RESULT_OK) { // Activity.RESULT_OK
                String referral_code = data.getStringExtra("referral_code");
                edt_referralCode.setText(referral_code);
            }
        }

        if(requestCode==REQUEST_OPERATOR)
        {

            try {
                ssityid = data.getStringExtra(CircleSelectionActivity.RESULT_CIRCLCODE);
                ssityname= data.getStringExtra(CircleSelectionActivity.RESULT_CIRCLEID);

                statename.setText(ssityid);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        }


        if(requestCode==REQUEST_OPERATORR)
        {

            try {
                scityid = data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLCODE);
                scityname= data.getStringExtra(CircleSelectionActivity2.RESULT_CIRCLEID);

                cityename.setText(scityid);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }



        }

    }

    public void showDialog(String msg) {
        final Dialog dialog1 = new Dialog(RegisterActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.register_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
//        dialog1.getWindow().setDimAmount((float) 0.8);

        ImageView img_close = dialog1.findViewById(R.id.img_close);
        TextView txt_here = dialog1.findViewById(R.id.txt_here);
        TextView txt_register = dialog1.findViewById(R.id.txt_register);
        txt_register.setText(msg);
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });

        txt_here.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this,LoginActivity.class));
                dialog1.dismiss();
            }
        });

        dialog1.show();
    }

    public void RegisterUser() {
        dialog.show();
        if (APIURLs.isNetwork(RegisterActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.REGISTRATION_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            SuccessToast("your account has been successfully created"); // Success Toast
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        } else {
//                            ErrorToast(jsonObject1.getString("msg")); // Error Toast
                            showDialog(jsonObject1.getString("msg"));
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
                    Random random = new Random();
                    String referralCode = generateReferralCode(random);
                    params.put("customer_fname", edt_fname.getText().toString().trim());
                    params.put("customer_lname", "");
                    params.put("city_id", scityname);
                    params.put("customer_email", "");
                    params.put("customer_phone", edt_mobile.getText().toString().trim());
                    params.put("customer_pincode", edt_pincode.getText().toString().trim());
                    params.put("referral_code", referralCode);
                    params.put("apply_rc", edt_referralCode.getText().toString());
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(RegisterActivity.this, "no internet connection");
        }
    }


    public  void state(View v)
    {
        Intent i =new Intent(RegisterActivity.this,CircleSelectionActivity.class);
        startActivityForResult(i, REQUEST_OPERATOR);
    }


    public  void city(View v)
    {

        if(ssityid.equals("NA"))
        {
            Toast.makeText(RegisterActivity.this, "Kindly Select State Name First", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Intent intent = new Intent(RegisterActivity.this, CircleSelectionActivity3.class);
            intent.putExtra("sid",ssityname);
            startActivityForResult(intent, REQUEST_OPERATORR);

        }

    }


}
