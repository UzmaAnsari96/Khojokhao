package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class CancelOrderActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_orderId;
    RadioGroup rg_cancel;
    RadioButton rb_reason1, rb_reason2, rb_reason3, rb_reason4, rb_reason5, rb_reason6, rb_reason7, rb_reason8;
    EditText edt_reason;
    Button btn_submit;
    String reason = "", order_id = "";
    IOSDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancel_order);
        init();
        onClick();
        order_id = getIntent().getStringExtra("oid");
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rg_cancel.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_reason1:
                        reason = rb_reason1.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason2:
                        reason = rb_reason2.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason3:
                        reason = rb_reason3.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason4:
                        reason = rb_reason4.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason5:
                        reason = rb_reason5.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason6:
                        reason = rb_reason6.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason7:
                        reason = rb_reason7.getText().toString();
                        edt_reason.setVisibility(View.GONE);
                        break;
                    case R.id.rb_reason8:
                        edt_reason.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_reason.getVisibility() == View.VISIBLE) {
                    reason = edt_reason.getText().toString();
                }

                if (reason.equals("")) {
                    Toast.makeText(CancelOrderActivity.this, "Please select cancellation reason", Toast.LENGTH_SHORT).show();
                } else {
                    CancelOrder();
                }
            }

        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(CancelOrderActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        txt_orderId = findViewById(R.id.txt_orderId);
        rg_cancel = findViewById(R.id.rg_cancel);
        rb_reason1 = findViewById(R.id.rb_reason1);
        rb_reason2 = findViewById(R.id.rb_reason2);
        rb_reason3 = findViewById(R.id.rb_reason3);
        rb_reason4 = findViewById(R.id.rb_reason4);
        rb_reason5 = findViewById(R.id.rb_reason5);
        rb_reason6 = findViewById(R.id.rb_reason6);
        rb_reason7 = findViewById(R.id.rb_reason7);
        rb_reason8 = findViewById(R.id.rb_reason8);
        edt_reason = findViewById(R.id.edt_reason);
        btn_submit = findViewById(R.id.btn_submit);
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

    public void CancelOrder() {
        dialog.show();
        if (APIURLs.isNetwork(CancelOrderActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.cancel_order, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(CancelOrderActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CancelOrderActivity.this, OrderTrackingActivity.class);
                            intent.putExtra("order_id",order_id);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(CancelOrderActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("oid", order_id);
                    params.put("cancel_resion", reason);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(CancelOrderActivity.this, "no internet connection");
        }
    }
}
