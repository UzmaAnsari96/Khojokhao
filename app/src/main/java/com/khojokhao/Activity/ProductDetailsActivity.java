package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
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
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.ProductModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductDetailsActivity extends AppCompatActivity {

    RecyclerView rec_product, rec_package;
    ScrollView scrollView;
    CardView card_toolbar;
    Button btn_cart, btn_saveLater;
    TextView txt_productName, txt_weight, txt_desc, txt_stock, txt_cart_count, txt_discountprice, txt_actualprice, txt_discount,
            txt_bottomcount, txt_fact, txt_benefits;
    ImageView img_back, img_product;
    LinearLayout lnr_package, lnr_nutrition, lnr_benefits;
    RelativeLayout rel_cart_count, rel_toolbarcart, rel_discount, rel_minus, rel_plus, rel_bottomcart;
    String prod_id = "";
    IOSDialog dialog;
    String productImage = "", product_id = "", product_name = "", flag = "", description = "", unit = "", price = "", qty = "", gst = "", discount = "",
            save_flag = "", nutrition_fact = "", health_benefit = "";
    ArrayList<ProductModel> pricemodelArrayList = new ArrayList<>();
    ArrayList<String> stringArrayList = new ArrayList<>();
    Double actualAmout;
    Double discountOff;
    Double payableAmt;
    int quantity = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        init();
        prod_id = getIntent().getStringExtra("prod_id");
        onClick();
        getProductDetails();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rel_toolbarcart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProductDetailsActivity.this, CartActivity.class));
            }
        });

        btn_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    rel_bottomcart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    addCart(product_id, unit,price, gst, discount, String.valueOf(quantity));
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rel_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    if (quantity >= 1) {
                        quantity--;
                        if (quantity != 0) {
                            txt_bottomcount.setText(String.valueOf(quantity));
                            addCart(product_id, unit,price, gst, discount, String.
                                    valueOf(quantity));
                        } else {
                            rel_bottomcart.setVisibility(View.GONE);
                            btn_cart.setVisibility(View.VISIBLE);
                            addCart(product_id, unit,price, gst, discount, String.valueOf(quantity));
                        }
                    }
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rel_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equals("1")) {
                    quantity++;
                    txt_bottomcount.setText(String.valueOf(quantity));
                    addCart(product_id, unit,price, gst, discount, String.valueOf(quantity));
                } else {
                    Toast.makeText(ProductDetailsActivity.this, "out of stock", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void init() {
        dialog = new IOSDialog.Builder(ProductDetailsActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        scrollView = findViewById(R.id.scrollView);
        img_back = findViewById(R.id.img_back);
        txt_productName = findViewById(R.id.txt_productName);
        rel_cart_count = findViewById(R.id.rel_cart_count);
        rel_toolbarcart = findViewById(R.id.rel_toolbarcart);
        txt_weight = findViewById(R.id.txt_weight);
        /*txt_price = findViewById(R.id.txt_price);*/
        txt_desc = findViewById(R.id.txt_desc);
        txt_bottomcount = findViewById(R.id.txt_bottomcount);
        txt_fact = findViewById(R.id.txt_fact);
        txt_benefits = findViewById(R.id.txt_benefits);
        rel_bottomcart = findViewById(R.id.rel_bottomcart);
        txt_stock = findViewById(R.id.txt_stock);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        img_product = findViewById(R.id.img_product);
        card_toolbar = findViewById(R.id.card_toolbar);
        txt_discountprice = findViewById(R.id.txt_discountprice);
        txt_actualprice = findViewById(R.id.txt_actualprice);
        btn_cart = findViewById(R.id.btn_cart);
        btn_saveLater = findViewById(R.id.btn_saveLater);
        lnr_package = findViewById(R.id.lnr_package);
        lnr_nutrition = findViewById(R.id.lnr_nutrition);
        lnr_benefits = findViewById(R.id.lnr_benefits);
        rel_minus = findViewById(R.id.rel_minus);
        rel_plus = findViewById(R.id.rel_plus);
        rec_product = findViewById(R.id.rec_product);
        txt_discount = findViewById(R.id.txt_discount);
        rel_discount = findViewById(R.id.rel_discount);
        rec_product.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this, LinearLayoutManager.HORIZONTAL, false));
        rec_product.setAdapter(new ProductSliderAdapter());
        rec_package = findViewById(R.id.rec_package);
        rec_package.setLayoutManager(new LinearLayoutManager(ProductDetailsActivity.this));

        txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

    }

    @Override
    protected void onResume() {
        super.onResume();
        cartCount();
    }

    public void addCart(final String prod_id, final String unit, final String price, final String gst, final String discount, final String qty1) {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
//                            quantity = 1;
                            cartCount();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    params.put("product_id", prod_id);
                    params.put("unit", unit);
                    params.put("price", price);
                    params.put("discount", discount);
                    params.put("qty", qty1);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public void saveLater(final String prod_id, final String unit, final String price, final String gst, final String discount, final String qty1) {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.SAVEFORLATER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        String msg = jsonObject1.getString("msg");
                        if (status.equals("1")) {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
                            quantity = 1;
                            cartCount();
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, msg, Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    params.put("product_id", prod_id);
                    params.put("unit", unit);
                    params.put("price", price);
                    params.put("gst", gst);
                    params.put("discount", discount);
                    params.put("qty", qty1);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public void SuccessToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.sucess_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 0);
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
        toast.setGravity(Gravity.BOTTOM, 0, 50);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
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

    public void cartCount() {
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CARTCOUNT_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            String count = jsonObject1.getString("count");
                            if (count.equals("0")) {
                                rel_cart_count.setVisibility(View.GONE);
                            } else {
                                rel_cart_count.setVisibility(View.VISIBLE);
                                txt_cart_count.setText(count);
                               /* txt_bottomcount.setText(count);
                                quantity = Integer.parseInt(count);*/
                            }
                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public void getProductDetails() {
        dialog.show();
        if (APIURLs.isNetwork(ProductDetailsActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.PRODUCTDETAILS_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            productImage = jsonObject1.getString("image");
                            product_id = jsonObject1.getString("product_id");
                            product_name = jsonObject1.getString("product_name");
                            flag = jsonObject1.getString("flag");
                            description = jsonObject1.getString("description");
                            qty = jsonObject1.getString("qty");
                            save_flag = jsonObject1.getString("save_flag");
                            nutrition_fact = jsonObject1.getString("nutrition_fact");
                            health_benefit = jsonObject1.getString("health_benefit");

                            if (nutrition_fact.equals("")) {
                                lnr_nutrition.setVisibility(View.GONE);
                            } else {
                                lnr_nutrition.setVisibility(View.VISIBLE);
                                txt_fact.setText(HtmlCompat.fromHtml(nutrition_fact, HtmlCompat.FROM_HTML_MODE_COMPACT));
                            }
                            if (health_benefit.equals("")) {
                                lnr_benefits.setVisibility(View.GONE);
                            } else {
                                lnr_benefits.setVisibility(View.VISIBLE);
                                txt_benefits.setText(HtmlCompat.fromHtml(health_benefit, HtmlCompat.FROM_HTML_MODE_COMPACT));
                            }
                            txt_productName.setText(product_name);
                            txt_desc.setText(HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT));

                            Glide.with(ProductDetailsActivity.this).load(productImage).into(img_product);

                            if (flag.equals("0")) {
                                txt_stock.setVisibility(View.VISIBLE);
                                txt_stock.setText("Out of stock");
                                txt_stock.setTextColor(getResources().getColor(R.color.red));
                            } else if (flag.equals("1")) {
                                txt_stock.setVisibility(View.VISIBLE);
                                txt_stock.setText("Available in stock");
                                txt_stock.setTextColor(getResources().getColor(R.color.colorGreen));
                            }
                            if (save_flag.equals("0")) {
                                btn_saveLater.setText("Save for later");
                            } else if (save_flag.equals("1")) {
                                btn_saveLater.setText("Saved");
                            }

                            btn_saveLater.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (save_flag.equals("0")) {
                                        saveLater(product_id, unit, price, gst, discount, String.valueOf(quantity));
                                    } else if (save_flag.equals("1")) {
                                        /*btn_saveLater.setText("Saved");*/
                                    }

                                }
                            });

                            if (qty.equals("0")) {
                                btn_cart.setVisibility(View.VISIBLE);
                                rel_bottomcart.setVisibility(View.GONE);
                            } else {
                                btn_cart.setVisibility(View.GONE);
                                rel_bottomcart.setVisibility(View.VISIBLE);
                                quantity = Integer.parseInt(qty);
                                txt_bottomcount.setText(String.valueOf(quantity));
                            }
                            JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                            if (jsonArray1.length() > 0) {
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                unit = jsonObject2.getString("unit");
                                price = jsonObject2.getString("price");
                                discount = jsonObject2.getString("discount");

                                if (discount.equals("0")) {
                                    txt_discountprice.setText("Rs." + price);
                                    txt_actualprice.setVisibility(View.GONE);
                                    rel_discount.setVisibility(View.GONE);
                                } else {
                                    txt_actualprice.setVisibility(View.VISIBLE);
                                    rel_discount.setVisibility(View.VISIBLE);
                                    actualAmout = Double.parseDouble(price);
                                    discountOff = (actualAmout * Double.parseDouble(discount)) / 100;
                                    payableAmt = actualAmout - discountOff;
                                    DecimalFormat twoDForm = new DecimalFormat("#");
                                    payableAmt = Double.valueOf(twoDForm.format(payableAmt));
                                    txt_discountprice.setText("Rs." + (int) Math.round(payableAmt * 100) / 100);
//                                    txt_discountprice.setText("Rs."+String.valueOf(payableAmt));
                                    txt_actualprice.setText("Rs." + price);
                                    txt_discount.setText(discount + "% off");
                                }
                                /*txt_price.setText(price + "/-");*/
                                txt_weight.setText(unit);
                                if (jsonArray1.length() > 1) {
                                    pricemodelArrayList = new ArrayList<>();
                                    for (int i = 0; i < jsonArray1.length(); i++) {
                                        JSONObject jsonObject3 = jsonArray1.getJSONObject(i);
                                        ProductModel model = new ProductModel();
                                        model.setPrice(jsonObject3.getString("price"));
                                        model.setUnit(jsonObject3.getString("unit"));
                                        model.setDiscount(jsonObject3.getString("discount"));
                                        pricemodelArrayList.add(model);
                                    }
                                    ProductpackageAdapter adapter = new ProductpackageAdapter();
                                    rec_package.setAdapter(adapter);
                                    lnr_package.setVisibility(View.VISIBLE);
                                } else {
                                    lnr_package.setVisibility(View.GONE);
                                }
                            }

                            JSONArray jsonArray2 = jsonObject1.getJSONArray("thumbnail");
                            if (jsonArray2.length() > 0) {
                                stringArrayList = new ArrayList<>();
                                stringArrayList.add(productImage);
                                for (int i = 0; i < jsonArray2.length(); i++) {
                                    JSONObject jsonObject2 = jsonArray2.getJSONObject(i);
                                    String image = jsonObject2.getString("thumbnail_image");
                                    stringArrayList.add(image);
                                }
                                ProductSliderAdapter sliderAdapter = new ProductSliderAdapter();
                                rec_product.setAdapter(sliderAdapter);
                                rec_product.setVisibility(View.VISIBLE);
                            } else {
                                rec_product.setVisibility(View.GONE);
                            }

                        } else {
                            Toast.makeText(ProductDetailsActivity.this, "error", Toast.LENGTH_SHORT).show();
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
                    params.put("product_id", prod_id);
                    params.put("customer_id", SharedPref.getVal(ProductDetailsActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(ProductDetailsActivity.this, "no internet connection");
        }
    }

    public class ProductSliderAdapter extends RecyclerView.Adapter<ProductSliderAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_slider_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
            Glide.with(ProductDetailsActivity.this).load(stringArrayList.get(position)).into(holder.img_product);

            holder.img_product.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Glide.with(ProductDetailsActivity.this).load(stringArrayList.get(position)).into(img_product);
                }
            });
        }

        @Override
        public int getItemCount() {
            return stringArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
            }
        }
    }

    public class ProductpackageAdapter extends RecyclerView.Adapter<ProductpackageAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(ProductDetailsActivity.this).inflate(R.layout.product_package_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ProductModel model = pricemodelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return pricemodelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product1;
            TextView txt_productName1, txt_price1;
            CardView card_list;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product1 = itemView.findViewById(R.id.img_product);
                txt_productName1 = itemView.findViewById(R.id.txt_productName);
                txt_price1 = itemView.findViewById(R.id.txt_price);
                card_list = itemView.findViewById(R.id.card_list);
            }

            public void bind(final ProductModel model, int position) {
                if(model.getDiscount().equals("0")){
                    txt_productName1.setText(product_name + " " + model.getUnit());
                    txt_price1.setText("Rs." + model.getPrice());
                }else{
                    actualAmout = Double.parseDouble(model.getPrice());
                    discountOff = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                    payableAmt = actualAmout - discountOff;
                    txt_price1.setText("Rs." + String.valueOf(payableAmt));
                }

                Glide.with(ProductDetailsActivity.this).load(productImage).into(img_product1);

                card_list.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            if (model.getDiscount().equals("0")) {
                                unit = model.getUnit();
                                price = model.getPrice();
                                txt_discountprice.setText("Rs." + model.getPrice());
                                txt_weight.setText(model.getUnit());
                                txt_actualprice.setVisibility(View.GONE);
                                rel_discount.setVisibility(View.GONE);
                            } else {
                                txt_actualprice.setVisibility(View.VISIBLE);
                                rel_discount.setVisibility(View.VISIBLE);
                                actualAmout = Double.parseDouble(model.getPrice());
                                discountOff = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                                payableAmt = actualAmout - discountOff;
                                txt_discountprice.setText("Rs." + String.valueOf(payableAmt));
                                txt_actualprice.setText("Rs." + model.getPrice());
                                txt_discount.setText(model.getDiscount() + "% off");
                                txt_weight.setText(model.getUnit());
                                unit = model.getUnit();
                                price = String.valueOf(payableAmt);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                       /* txt_price.setText(model.getPrice()+"/-");
                        txt_weight.setText(model.getUnit());*/
                    }
                });
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
