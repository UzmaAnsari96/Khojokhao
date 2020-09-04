package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.DatePicker;
import android.widget.EditText;
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
import com.bumptech.glide.Glide;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.squareup.picasso.Picasso;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Model.ProductModel;
import com.khojokhao.Model.SubscriptionModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionDetailsActivity extends AppCompatActivity {

    RecyclerView rec_sub_detail_list;
    ImageView img_subspause;
    Button btn_resubscribe;
    SubscriptionModel model;
    ArrayList<String> dayArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    IOSDialog dialog;
    ImageView img_back;
    LinearLayout lnr_pauseSubscription,lnr_endSubscription;
    TextView txt_placedDate,txt_address,txt_paymentMode,txt_amount,txt_status,txt_subsendDate,txt_wholePause;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription_details);
        init();
        model = (SubscriptionModel) getIntent().getSerializableExtra("model");
        if(model.getSubscribe_mode().equals("Daily")){}else {
            dayArrayList = new ArrayList<>();
            dayArrayList = model.getDaysArrayList();
        }

        productModelArrayList = new ArrayList<>();
        productModelArrayList = model.getProduct_details();

        setData();
        onClick();
        getCurrentDate();
    }

    public void setData(){
        rec_sub_detail_list.setAdapter(new SubListingAdapter(SubscriptionDetailsActivity.this));
        String payment_status = model.getPayment_status();
        String payment_mode = model.getP_mode();
        if(payment_status.equals("0")){
            txt_status.setText("Unpaid");
        }else{
            txt_status.setText("Paid");
        }

        if(payment_mode.equals("0")){
            txt_paymentMode.setText("Cash");
        }else{
            txt_paymentMode.setText("Online Payment");
        }

        txt_address.setText(model.getAddress());
        txt_amount.setText("Rs."+model.getOrder_total());
        txt_placedDate.setText(model.getOrder_date());
        txt_subsendDate.setText(model.getTo_date());

        if(model.getSubscribe_status().equals("1")){
            Glide.with(SubscriptionDetailsActivity.this).load(R.drawable.ic_play_circle_outline_black_24dp).into(img_subspause);
            txt_wholePause.setText("Resume this whole subscription");
        }else{
            Glide.with(SubscriptionDetailsActivity.this).load(R.drawable.ic_pause_circle_outline_black_24dp).into(img_subspause);
            txt_wholePause.setText("Pause this whole subscription");
        }

    }

    public void getCurrentDate(){
        Date today = new Date();
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        String dateToStr = format.format(today);

        if(dateToStr.equals(model.getTo_date())){
            btn_resubscribe.setVisibility(View.VISIBLE);
        }else{
            btn_resubscribe.setVisibility(View.GONE);
        }
    }

    private void init() {
        dialog = new IOSDialog.Builder(SubscriptionDetailsActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        rec_sub_detail_list = findViewById(R.id.rec_sub_detail_list);
        img_back = findViewById(R.id.img_back);
        lnr_pauseSubscription = findViewById(R.id.lnr_pauseSubscription);
        lnr_endSubscription = findViewById(R.id.lnr_endSubscription);
        txt_placedDate = findViewById(R.id.txt_placedDate);
        txt_address = findViewById(R.id.txt_address);
        txt_paymentMode = findViewById(R.id.txt_paymentMode);
        txt_amount = findViewById(R.id.txt_amount);
        txt_status = findViewById(R.id.txt_status);
        txt_subsendDate = findViewById(R.id.txt_subsendDate);
        img_subspause = findViewById(R.id.img_subspause);
        txt_wholePause = findViewById(R.id.txt_wholePause);
        btn_resubscribe = findViewById(R.id.btn_resubscribe);
        rec_sub_detail_list.setLayoutManager(new LinearLayoutManager(SubscriptionDetailsActivity.this));
    }

    public void addMore(View view){
        Intent intent = new Intent(SubscriptionDetailsActivity.this,AddMoreCategoryActivity.class);
        intent.putExtra("subs_id",model.getSubscribe_id());
        startActivity(intent);
    }
    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnr_pauseSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_wholePause.getText().equals("Resume this whole subscription")){
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");
                    // Setting Dialog Message
                    alertDialog.setMessage("Do you want to resume subscription ?");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            resumeSubscription(model.getSubscribe_id());
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
                }else {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Alert");
                    // Setting Dialog Message
                    alertDialog.setMessage("Do you want to pause subscription?");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                            pauseSubscription(model.getSubscribe_id());
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
            }
        });

        lnr_endSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Alert");
                // Setting Dialog Message
                alertDialog.setMessage("Do you want to end subscription ?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        endSubscription(model.getSubscribe_id());
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

        btn_resubscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                // Setting Dialog Title
                alertDialog.setTitle("Alert!!");
                // Setting Dialog Message
                alertDialog.setMessage("Do you want to Re-subscribe ?");
                // On pressing Settings button
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        showDialog(model.getTo_date());
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

    public void showDialog(String fromdate) {
        final Dialog dialog1 = new Dialog(SubscriptionDetailsActivity.this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.resubscription_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);

        ImageView img_close = dialog1.findViewById(R.id.img_close);
        EditText edt_fromdate = dialog1.findViewById(R.id.edt_fromdate);
        final TextView txt_todate = dialog1.findViewById(R.id.txt_todate);
        Button btn_process = dialog1.findViewById(R.id.btn_process);

        edt_fromdate.setText(fromdate);

        txt_todate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(SubscriptionDetailsActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        int year1 = view.getYear();
                        int month = view.getMonth();
                        int day = view.getDayOfMonth();

                        Calendar calendar = Calendar.getInstance();
                        calendar.set(year1, month, day);

                        try {
                            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
                            String strFromDate = format.format(calendar.getTime());
                            txt_todate.setText(strFromDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(txt_todate.getText().toString().equals("")){
                    Toast.makeText(SubscriptionDetailsActivity.this, "Select to date", Toast.LENGTH_SHORT).show();
                }else{
                    dialog1.dismiss();
                    reSubscription(model.getSubscribe_id(),txt_todate.getText().toString());
                }
            }
        });
        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
            }
        });
        dialog1.show();
    }

    public void pauseSubscription(final String subscribe_id) {
        dialog.show();
        if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.all_product_pause, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Subscription pause successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("subscribe_id", subscribe_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
        }
    }

    public void resumeSubscription(final String subscribe_id) {
        dialog.show();
        if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.all_product_resume, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Subscription resumed successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("subscribe_id", subscribe_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
        }
    }

    public void endSubscription(final String subscribe_id) {
        dialog.show();
        if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.all_product_end, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Subscription end successfully", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SubscriptionDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("subscribe_id", subscribe_id);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
        }
    }

    public void reSubscription(final String subscribe_id, final String enddate) {
        dialog.show();
        if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.re_book_subscribe, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(SubscriptionDetailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(SubscriptionDetailsActivity.this, SubscriptionActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SubscriptionDetailsActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
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
                    params.put("subscribe_id", subscribe_id);
                    params.put("end_date", enddate);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
        }
    }

    public class SubListingAdapter extends RecyclerView.Adapter<SubListingAdapter.ItemViewholder> {
        Context context;

        public SubListingAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_subscription_details_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model,position);
        }


        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_image,img_pause;
            TextView txt_productname,txt_price,txt_unit,txt_qty,txt_pause;
            LinearLayout lnr_pause,lnr_end;
            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                img_image = itemView.findViewById(R.id.img_image);
                txt_productname = itemView.findViewById(R.id.txt_productname);
                txt_price = itemView.findViewById(R.id.txt_price);
                txt_unit = itemView.findViewById(R.id.txt_unit);
                txt_qty = itemView.findViewById(R.id.txt_qty);
                lnr_pause = itemView.findViewById(R.id.lnr_pause);
                img_pause = itemView.findViewById(R.id.img_pause);
                lnr_end = itemView.findViewById(R.id.lnr_end);
                txt_pause = itemView.findViewById(R.id.txt_pause);
            }

            public void bind(final ProductModel model , int position){
                Picasso.with(context).load(model.getImage()).into(img_image);
                txt_productname.setText(model.getProduct_name());
                txt_price.setText("Rs."+model.getPrice());
                txt_unit.setText(model.getUnit());
                txt_qty.setText("Quantity : " + model.getQty());

                if(model.getSubscribe_status().equals("1")){
                    Glide.with(SubscriptionDetailsActivity.this).load(R.drawable.ic_play_circle_outline_black_24dp).into(img_pause);
                    txt_pause.setText("Resume Subscription");
                }else{
                    Glide.with(SubscriptionDetailsActivity.this).load(R.drawable.ic_pause_circle_outline_black_24dp).into(img_pause);
                    txt_pause.setText("Pause Subscription");
                }

                lnr_pause.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(txt_pause.getText().equals("Resume Subscription")){
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert");
                            // Setting Dialog Message
                            alertDialog.setMessage("Do you want to resume subscription for this product?");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    resumeProduct(model.getSpi());
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
                        }else {
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                            // Setting Dialog Title
                            alertDialog.setTitle("Alert");
                            // Setting Dialog Message
                            alertDialog.setMessage("Do you want to pause subscription for this product?");
                            // On pressing Settings button
                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    pauseProduct(model.getSpi());
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
                    }
                });

                lnr_end.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubscriptionDetailsActivity.this);
                        // Setting Dialog Title
                        alertDialog.setTitle("Alert");
                        // Setting Dialog Message
                        alertDialog.setMessage("Do you want to end subscription for this product?");
                        // On pressing Settings button
                        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               endProduct(model.getSpi());
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

            public void pauseProduct(final String spi) {
                dialog.show();
                if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.single_product_pause, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(context, "Subscription pause for product successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("spi", spi);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);

                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
                }
            }

            public void resumeProduct(final String spi) {
                dialog.show();
                if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.single_product_resume, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(context, "Subscription resume for product successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("spi", spi);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);

                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
                }
            }

            public void endProduct(final String spi) {
                dialog.show();
                if (APIURLs.isNetwork(SubscriptionDetailsActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.single_product_end, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    Toast.makeText(context, "Subscription end for product successfully", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(SubscriptionDetailsActivity.this,SubscriptionActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("spi", spi);
                            return params;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(SubscriptionDetailsActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);

                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(SubscriptionDetailsActivity.this, "no internet connection");
                }
            }
        }
    }
}
