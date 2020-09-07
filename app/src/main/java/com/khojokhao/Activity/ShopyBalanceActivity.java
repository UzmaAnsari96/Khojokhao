package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.khojokhao.Model.WalletHistoryModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShopyBalanceActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_balance;
    IOSDialog dialog;
    RecyclerView rec_list;
    TextView txt_history;
    ArrayList<WalletHistoryModel> walletHistoryModelArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopy_balance);
        init();
        onClick();
        walletHistory();
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
        dialog = new IOSDialog.Builder(ShopyBalanceActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        txt_balance = findViewById(R.id.txt_balance);
        txt_history = findViewById(R.id.txt_history);
        rec_list = findViewById(R.id.rec_list);
        rec_list.setLayoutManager(new LinearLayoutManager(ShopyBalanceActivity.this));
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

    @Override
    protected void onResume() {
        super.onResume();
        walletBalance();
    }

    public void walletBalance(){
        dialog.show();
        if(APIURLs.isNetwork(ShopyBalanceActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_wallet, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject.getString("status");
                        if(status.equals("1")){
                            String balance = jsonObject1.getString("wallet");
                            txt_balance.setText("Khojo Khao Available Balance : "+"Rs."+balance);
                        }else{
                            Toast.makeText(ShopyBalanceActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ShopyBalanceActivity.this,SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(ShopyBalanceActivity.this,"no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
    }

    public void walletHistory(){
        dialog.show();
        if(APIURLs.isNetwork(ShopyBalanceActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.wallet_history, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if(status.equals("1")){
                            walletHistoryModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                if(!jsonObject1.getString("debit").equals("0")) {
                                    WalletHistoryModel model = new WalletHistoryModel();
                                    model.setCustomer_id(jsonObject1.getString("customer_id"));
                                    model.setOpening_bal(jsonObject1.getString("opening_bal"));
                                    model.setClosing_bal(jsonObject1.getString("closing_bal"));
                                    model.setCredit(jsonObject1.getString("credit"));
                                    model.setDebit(jsonObject1.getString("debit"));
                                    model.setC_d_date(jsonObject1.getString("c_d_date"));
                                    walletHistoryModelArrayList.add(model);
                                }
                            }
                            if(walletHistoryModelArrayList.size() != 0) {
                                WalletAdapter adapter = new WalletAdapter();
                                rec_list.setAdapter(adapter);
                                rec_list.setVisibility(View.VISIBLE);
                                txt_history.setVisibility(View.VISIBLE);
                            }else{
                                rec_list.setVisibility(View.GONE);
                                txt_history.setVisibility(View.GONE);
                            }
                        }else{
                            rec_list.setVisibility(View.GONE);
                            txt_history.setVisibility(View.GONE);
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
                    params.put("customer_id", SharedPref.getVal(ShopyBalanceActivity.this,SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(ShopyBalanceActivity.this,"no internet connection");
            /*Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();*/
        }
    }

    public class WalletAdapter extends RecyclerView.Adapter<WalletAdapter.ViewHolder>{

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ShopyBalanceActivity.this).inflate(R.layout.wallet_history_item_layout,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            WalletHistoryModel model = walletHistoryModelArrayList.get(position);
            ((ViewHolder) holder).bind(model,position);
        }

        @Override
        public int getItemCount() {
            return walletHistoryModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_date,txt_sign,txt_amount,txt_type;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                txt_date = itemView.findViewById(R.id.txt_date);
                txt_sign = itemView.findViewById(R.id.txt_sign);
                txt_amount = itemView.findViewById(R.id.txt_amount);
                txt_type = itemView.findViewById(R.id.txt_type);
            }

            public void bind(WalletHistoryModel model, int position){
                txt_date.setText(model.getC_d_date());
                if(model.getCredit().equals("0") || model.getCredit().equals("null")||model.getCredit().equals("")){
                    txt_type.setText("Use with order");
                    txt_sign.setText("-");
                    txt_sign.setTextColor(getResources().getColor(R.color.red));
                    txt_amount.setText(model.getDebit());
                }else if(model.getDebit().equals("0") || model.getDebit().equals("null")||model.getDebit().equals("")){
                    txt_type.setText("Sign up");
                    txt_sign.setText("+");
                    txt_sign.setTextColor(getResources().getColor(R.color.colorGreen));
                    String amt = model.getCredit();
                    txt_amount.setText(amt);
                }
            }
        }
    }
}
