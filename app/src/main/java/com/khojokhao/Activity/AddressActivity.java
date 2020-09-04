package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
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
import com.khojokhao.Model.AddressModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AddressActivity extends AppCompatActivity {

    CardView card_newAddress, card_address;
    LinearLayout lnr_address;
    ImageView img_back;
    RecyclerView rec_list;
    TextView txt_nodata;
    IOSDialog dialog;
    ArrayList<AddressModel> addressModelArrayList = new ArrayList<>();
    String product_id="",grand_total="",sub_total="",coupon_id="",slot_id="",unit_price="",discount_amt="",coupon_amt="",qty="",
            finalUnit="",finalUnitPrice="",finalDiscount="",walletBalance="",actualBalance="", aid,delivery_charges="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
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
       /* walletBalance = getIntent().getStringExtra("walletBalance");
        actualBalance = getIntent().getStringExtra("actualBalance");*/
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        card_newAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Address");
                // Setting Dialog Message
                alertDialog.setMessage("Do you want to add new address?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
                // on pressing cancel button
                alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                // Showing Alert Message
                alertDialog.show();*/
                Intent intent = new Intent(AddressActivity.this,NewAddressAddingActivity.class);
                intent.putExtra("from","cart");
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
                intent.putExtra("delivery_charges",delivery_charges);
           /*     intent.putExtra("walletBalance",walletBalance);
                intent.putExtra("actualBalance",actualBalance);*/
                startActivity(intent);
                finish();
            }
        });
    }

    public void pickPoints(View view) {
            Intent intent = new Intent(AddressActivity.this,PickPointsListingActivity.class);
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
            intent.putExtra("delivery_charges",delivery_charges);
            /*intent.putExtra("walletBalance",walletBalance);
            intent.putExtra("actualBalance",actualBalance);*/
            startActivity(intent);
            finish();
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

    private void init() {
        dialog = new IOSDialog.Builder(AddressActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        card_newAddress = findViewById(R.id.card_newAddress);
        lnr_address = findViewById(R.id.lnr_address);
        img_back = findViewById(R.id.img_back);
        rec_list = findViewById(R.id.rec_list);
        txt_nodata = findViewById(R.id.txt_nodata);
        rec_list.setLayoutManager(new LinearLayoutManager(AddressActivity.this));
        rec_list.setAdapter(new AddressAdapter());
    }

    public void getAddressList() {
        dialog.show();
        if (APIURLs.isNetwork(AddressActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_address_record, new Response.Listener<String>() {
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
                                    model.setId(jsonObject1.getString("id"));
                                    model.setFlat_no(jsonObject1.getString("flat_no"));
                                    model.setBuilding_name(jsonObject1.getString("building_name"));
                                    model.setLandmark(jsonObject1.getString("landmark"));
                                    model.setArea_name(jsonObject1.getString("area_name"));
                                    model.setPincode(jsonObject1.getString("pincode"));
                                    model.setSector_number(jsonObject1.getString("sector_number"));
                                    model.SetAid(jsonObject1.getString("area_id"));
                                    addressModelArrayList.add(model);
                                }
                                AddressAdapter adapter = new AddressAdapter();
                                rec_list.setAdapter(adapter);
                                lnr_address.setVisibility(View.VISIBLE);
                                txt_nodata.setVisibility(View.GONE);
                            }
                        } else {
                            lnr_address.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
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
                    params.put("customer_id", SharedPref.getVal(AddressActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddressActivity.this, "no internet connection");
        }
    }

    public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AddressActivity.this).inflate(R.layout.address_item_layout, parent, false);
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
            TextView textViewOptions, txt_address;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                textViewOptions = itemView.findViewById(R.id.textViewOptions);
                txt_address = itemView.findViewById(R.id.txt_address);
                card_address = itemView.findViewById(R.id.card_address);
            }

            public void bind(final AddressModel model, final int position) {
                txt_address.setText(model.getFlat_no() +" "+
                        model.getBuilding_name()+" "+model.getLandmark() + " \n" +
                        model.getArea_name() +","+model.getSector_number()+","+model.getPincode());



                textViewOptions.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //creating a popup menu
                        PopupMenu popup = new PopupMenu(AddressActivity.this, textViewOptions);
                        //inflating menu from xml resource
                        popup.inflate(R.menu.options_menu);
                        //adding click listener
                        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {
                                switch (item.getItemId()) {
                                    case R.id.menu_delete:
                                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressActivity.this);
                                        // Setting Dialog Title
                                        alertDialog.setTitle("Delete Address");
                                        // Setting Dialog Message
                                        alertDialog.setMessage("Do you want to delete this address?");
                                        // On pressing Settings button
                                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                deleteAddress(model.getId(),position);
                                                dialog.dismiss();
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
                                        return true;
                                    case R.id.menu_edit:
                                        AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(AddressActivity.this);
                                        // Setting Dialog Title
                                        alertDialog1.setTitle("Update Address");
                                        // Setting Dialog Message
                                        alertDialog1.setMessage("Do you want to update this address?");
                                        // On pressing Settings button
                                        alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(AddressActivity.this,UpdateAddressAddingActivity.class);
                                                intent.putExtra("model",model);
                                                intent.putExtra("from","cart");
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
                                             /*   intent.putExtra("walletBalance",walletBalance);
                                                intent.putExtra("actualBalance",actualBalance);*/
                                                intent.putExtra("delivery_status","0");
                                                startActivity(intent);
                                                finish();
                                                dialog.dismiss();
                                            }
                                        });
                                        // on pressing cancel button
                                        alertDialog1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();
                                            }
                                        });
                                        // Showing Alert Message
                                        alertDialog1.show();
                                        return true;
                                    default:
                                        return false;
                                }
                            }
                        });
                        //displaying the popup
                        popup.show();
                    }
                });

                card_address.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        aid=model.getAid();

                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(AddressActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Address");
                        // Setting Dialog Message
                        alertDialog.setMessage("Do you want to continue with this address?");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(AddressActivity.this, SelectPaymentModeActivity.class);
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
                                intent.putExtra("area_id",aid);
                                /*intent.putExtra("walletBalance",walletBalance);
                                intent.putExtra("actualBalance",actualBalance);*/
                                intent.putExtra("delivery_status","0");
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

            public void deleteAddress(final String id, final int position) {
                dialog.show();
                if (APIURLs.isNetwork(AddressActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.address_delete, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(AddressActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                                    notifyDataSetChanged();
                                    notifyItemRemoved(position);
                                    addressModelArrayList.remove(position);
                                    getAddressList();
                                } else {
                                    Toast.makeText(AddressActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("id", id);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(AddressActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(AddressActivity.this, "no internet connection");
                }
            }
        }
    }
}
