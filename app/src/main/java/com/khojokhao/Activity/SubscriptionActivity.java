package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.squareup.picasso.Picasso;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.ProductModel;
import com.khojokhao.Model.SubscriptionModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class SubscriptionActivity extends AppCompatActivity {

    RecyclerView rec_sub_list;
    ImageView img_back;
    IOSDialog dialog;
    ArrayList<SubscriptionModel> subscriptionModelArrayList = new ArrayList<>();
    ArrayList<String> dayArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    TextView txt_nodata;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);
        init();
        onClick();
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
        dialog = new IOSDialog.Builder(SubscriptionActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        txt_nodata = findViewById(R.id.txt_nodata);
        rec_sub_list = findViewById(R.id.rec_sub_list);
        img_back = findViewById(R.id.img_back);
        rec_sub_list.setLayoutManager(new LinearLayoutManager(SubscriptionActivity.this, LinearLayoutManager.VERTICAL, false));

    }

    @Override
    protected void onResume() {
        super.onResume();
        getSubscriptionList();
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

    public void getSubscriptionList() {
        dialog.show();
        if (APIURLs.isNetwork(SubscriptionActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.subscription_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            subscriptionModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject dataObject = jsonArray.getJSONObject(i);
                                SubscriptionModel model = new SubscriptionModel();
                                model.setSubscribe_id(dataObject.getString("subscribe_id"));
                                model.setFrom_date(dataObject.getString("from_date"));
                                model.setTo_date(dataObject.getString("to_date"));
//                                model.setDeliver_timing(dataObject.getString("deliver_timing"));
                                model.setSubscribe_mode(dataObject.getString("subscribe_mode"));
                                model.setSubscribe_generate_id(dataObject.getString("subscribe_generate_id"));
                                model.setP_mode(dataObject.getString("p_mode"));
                                model.setPayment_status(dataObject.getString("payment_status"));
                                model.setAddress(dataObject.getString("address"));
                                model.setOrder_date(dataObject.getString("order_date"));
                                model.setOrder_total(dataObject.getString("order_total"));
                                model.setSubscribe_status(dataObject.getString("subscribe_status"));

                                dayArrayList = new ArrayList<>();
                                JSONArray daysArray = dataObject.getJSONArray("day");
                                if (daysArray.length() > 0) {
                                    for (int j = 0; j < daysArray.length(); j++) {
                                        JSONObject daysObject = daysArray.getJSONObject(j);
                                        dayArrayList.add(daysObject.getString("day"));
                                    }
                                    model.setDaysArrayList(dayArrayList);
                                }

                                productModelArrayList = new ArrayList<>();
                                JSONArray productArray = dataObject.getJSONArray("product_details");
                                for (int j = 0; j < productArray.length(); j++) {
                                    JSONObject productObject = productArray.getJSONObject(j);
                                    ProductModel productModel = new ProductModel();
                                    productModel.setProduct_name(productObject.getString("product_name"));
                                    productModel.setQty(productObject.getString("qty"));
                                    productModel.setImage(productObject.getString("image"));
                                    productModel.setDiscount(productObject.getString("discount"));
                                    productModel.setSpi(productObject.getString("spi"));
                                    productModel.setUnit(productObject.getString("unit"));
                                    productModel.setPrice(productObject.getString("unit_price"));
                                    productModel.setSubscribe_status(productObject.getString("subscribe_status"));
                                    productModelArrayList.add(productModel);
                                }
                                model.setProduct_details(productModelArrayList);
                                subscriptionModelArrayList.add(model);
                            }
                            Collections.reverse(subscriptionModelArrayList);
                            SubscriptionAdapter adapter = new SubscriptionAdapter(SubscriptionActivity.this);
                            rec_sub_list.setAdapter(adapter);
                            rec_sub_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(View.GONE);
                        } else {
                            rec_sub_list.setVisibility(View.GONE);
                            txt_nodata.setVisibility(View.VISIBLE);
//                            Toast.makeText(SubscriptionActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SubscriptionActivity.this,SharedPref.customer_id));
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);

        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubscriptionActivity.this, "no internet connection");
        }
    }

    public class SubscriptionAdapter extends RecyclerView.Adapter<SubscriptionAdapter.ItemViewholder> {
        Context context;
        ArrayList<ProductModel> productModelArrayList;
        ArrayList<String> daysArrayList;
        String days = "";
        public SubscriptionAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_my_subscription_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            SubscriptionModel model = subscriptionModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model,position);
        }

        @Override
        public int getItemCount() {
            return subscriptionModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            TextView txt_subscriptionId,txt_day;
            ImageView img_image1, img_image2, img_image3;
            TextView txt_time,txt_mode;
            Button btn_details;
            RecyclerView rec_product;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                txt_subscriptionId = itemView.findViewById(R.id.txt_subscriptionId);
                img_image1 = itemView.findViewById(R.id.img_image1);
                img_image2 = itemView.findViewById(R.id.img_image2);
                img_image3 = itemView.findViewById(R.id.img_image3);
                txt_time = itemView.findViewById(R.id.txt_time);
                txt_time.setVisibility(View.GONE);
                txt_mode = itemView.findViewById(R.id.txt_mode);
                txt_day = itemView.findViewById(R.id.txt_day);
                btn_details = itemView.findViewById(R.id.btn_details);
                rec_product = itemView.findViewById(R.id.rec_product);
                rec_product.setLayoutManager(new LinearLayoutManager(context,RecyclerView.HORIZONTAL,false));
            }

            public void bind(final SubscriptionModel model , int position){
                txt_subscriptionId.setText("Subscription ID : "+model.getSubscribe_generate_id());
                txt_mode.setText("Subscription Mode : " + model.getSubscribe_mode());
                productModelArrayList = new ArrayList<>();

                productModelArrayList = model.getProduct_details();
                ProductAdapter adapter = new ProductAdapter(context,productModelArrayList);
                rec_product.setAdapter(adapter);

                if(model.getSubscribe_mode().equals("Daily")){
                    txt_day.setVisibility(View.VISIBLE);
                    txt_day.setText("Will be delivered Today ");
                }else {
                    txt_day.setVisibility(View.VISIBLE);
                    days = "";
                    daysArrayList = new ArrayList<>();
                    daysArrayList = model.getDaysArrayList();

                    for (int i = 0; i < daysArrayList.size(); i++) {
                        days += daysArrayList.get(i) + ",";
                    }
                    days = days.substring(0, days.length() - 1);
                    txt_day.setText("Will be delivered on "+days);
                }

                /*if(model.getSubscribe_mode().equals("Daily")){
                    txt_time.setText("Will be delivered on " + model.getDeliver_timing());
                }else {
                    txt_time.setText("at " + model.getDeliver_timing());
                }*/
                btn_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(context, SubscriptionDetailsActivity.class);
                        intent.putExtra("model",(Serializable) model);
                        context.startActivity(intent);

                    }
                });
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ItemViewholder> {
        Context context;
        ArrayList<ProductModel> arrayList;

        public ProductAdapter(Context context, ArrayList<ProductModel> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.subscription_product_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            ProductModel model = arrayList.get(position);
            ((ItemViewholder) holder).bind(model,position);
        }

        @Override
        public int getItemCount() {
            return arrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_image;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                img_image = itemView.findViewById(R.id.img_image);
            }

            public void bind(ProductModel model , int position){
                Picasso.with(context).load(model.getImage()).into(img_image);
            }
        }
    }
}
