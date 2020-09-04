package com.khojokhao.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.khojokhao.Model.AddressModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PickPointsListingActivity extends AppCompatActivity {

    RecyclerView rec_list;
    ImageView img_back;
    IOSDialog dialog;
    ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    String product_id="",grand_total="",sub_total="",coupon_id="",slot_id="",unit_price="",discount_amt="",coupon_amt="",qty="",
            finalUnit="",finalUnitPrice="",finalDiscount="",walletBalance="",actualBalance="",delivery_charges="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick_points_listing);
        init();
        getIntentData();
        onClick();
        getAddressList();
    }

    private void getIntentData(){
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
        delivery_charges = getIntent().getStringExtra("delivery_charges");
        /*walletBalance = getIntent().getStringExtra("walletBalance");
        actualBalance = getIntent().getStringExtra("actualBalance");*/
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
        dialog = new IOSDialog.Builder(PickPointsListingActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        rec_list = findViewById(R.id.rec_list);
        rec_list.setLayoutManager(new LinearLayoutManager(PickPointsListingActivity.this));
        rec_list.setAdapter(new PickPointsListingActivity.AddressAdapter());
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

    public void getAddressList() {
        dialog.show();
        if (APIURLs.isNetwork(PickPointsListingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.pickup_point, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            addressModelArrayList = new ArrayList<>();
                            if (jsonObject.has("result")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                    AddressModel model = new AddressModel();
                                    model.setId(jsonObject1.getString("pick_up_id"));
                                    model.setPickup_address(jsonObject1.getString("pickup_address"));
                                    addressModelArrayList.add(model);
                                }
                                AddressAdapter adapter = new AddressAdapter();
                                rec_list.setAdapter(adapter);
                                rec_list.setVisibility(View.VISIBLE);

                            }
                        } else {
                            rec_list.setVisibility(View.GONE);
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
                    params.put("customer_id", SharedPref.getVal(PickPointsListingActivity.this, SharedPref.customer_id));
                    params.put("franchise_id",SharedPref.getVal(PickPointsListingActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(PickPointsListingActivity.this, "no internet connection");
        }
    }

    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(PickPointsListingActivity.this).inflate(R.layout.address_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            AddressModel model = addressModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return addressModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView textViewOptions, txt_address,txt_select;
            CardView card_address;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewOptions = itemView.findViewById(R.id.textViewOptions);
                txt_address = itemView.findViewById(R.id.txt_address);
                card_address = itemView.findViewById(R.id.card_address);
                txt_select = itemView.findViewById(R.id.txt_select);
                textViewOptions.setVisibility(View.GONE);
            }

            public void bind(final AddressModel model, final int position) {
                txt_address.setText(model.getPickup_address());
                txt_select.setText("Select Pickup point address");

                card_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(PickPointsListingActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Address");
                        // Setting Dialog Message
                        alertDialog.setMessage("Do you want to continue with this address?");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(PickPointsListingActivity.this, SelectPaymentModeActivity.class);
                                intent.putExtra("product_id",product_id);
                                intent.putExtra("grand_total",grand_total);
                                intent.putExtra("sub_total",sub_total);
                                intent.putExtra("coupon_id",coupon_id);
                                intent.putExtra("slot_id",slot_id);
                                intent.putExtra("unit_price",unit_price);
                                intent.putExtra("discount_amt",discount_amt);
                                intent.putExtra("coupon_amt",coupon_amt);
                                intent.putExtra("qty",qty);
                                intent.putExtra("address",txt_address.getText().toString());
                                intent.putExtra("finalUnit",finalUnit);
                                intent.putExtra("finalUnitPrice",finalUnitPrice);
                                intent.putExtra("finalDiscount",finalDiscount);
                                intent.putExtra("delivery_charges",delivery_charges);
                               /* intent.putExtra("walletBalance",walletBalance);
                                intent.putExtra("actualBalance",actualBalance);*/
                                intent.putExtra("delivery_status","1");
                                startActivity(intent);
                                finish();
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
                });
            }
        }
    }
}
