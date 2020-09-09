package com.khojokhao.Activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.khojokhao.Model.CategoryModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class SubSubCategoryListActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt, txt_nodata, txt_cart_count;
    RecyclerView rec_list;
    RelativeLayout rel_cart_count;
    String cat_id = "", cat_name = "";
    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
    IOSDialog dialog;
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    String cost_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub_category_listing);
        cost_id = SharedPref.getVal(SubSubCategoryListActivity.this, SharedPref.customer_id);
        init();
        onClick();
        cat_id = getIntent().getStringExtra("cat_id");
        txt.setText("Sub Sub Category");
        getCategoryList();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        rel_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.VISIBLE);
                lnr_home.setVisibility(View.GONE);

//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);
                startActivity(new Intent(SubSubCategoryListActivity.this, HomeActivity.class));
                finish();

            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

//                lnr_search.setVisibility(View.GONE);
//                lnr_search_selected.setVisibility(View.VISIBLE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

//                startActivity(new Intent(SubSubCategoryListActivity.this, OffersActivity.class));
//                    finish();
            }
        });

        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.GONE);
                lnr_cart_selected.setVisibility(View.VISIBLE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubSubCategoryListActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view cart");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SubSubCategoryListActivity.this, LoginActivity.class);
                            intent.putExtra("from", "sub_cat");
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    startActivity(new Intent(SubSubCategoryListActivity.this, CartActivity.class));
                }
            }
        });

        rel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);


                lnr_subscription.setVisibility(View.GONE);
                lnr_subscription_selected.setVisibility(View.VISIBLE);


                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                if (cost_id.equals("")) {
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(SubSubCategoryListActivity.this);
                    // Setting Dialog Title
                    alertDialog.setTitle("Please Login");
                    // Setting Dialog Message
                    alertDialog.setMessage("Please login to view subscription");
                    // On pressing Settings button
                    alertDialog.setPositiveButton("LOGIN", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(SubSubCategoryListActivity.this, LoginActivity.class);
                            intent.putExtra("from", "sub_cat");
                            startActivity(intent);
                        }
                    });
                    // on pressing cancel button
                    alertDialog.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    // Showing Alert Message
                    alertDialog.show();
                } else {
                    startActivity(new Intent(SubSubCategoryListActivity.this, SubscriptionActivity.class));
                }

            }
        });

        rel_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

//                lnr_search.setVisibility(View.VISIBLE);
//                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.GONE);
                lnr_account_selected.setVisibility(View.VISIBLE);

//                startActivity(new Intent(SubSubCategoryListActivity.this, CategoriesActivity.class));
            }
        });


    }

    private void init() {
        dialog = new IOSDialog.Builder(SubSubCategoryListActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        txt = findViewById(R.id.txt);
        txt_nodata = findViewById(R.id.txt_nodata);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        rec_list = findViewById(R.id.rec_list);
        rel_cart_count = findViewById(R.id.rel_cart_count);

        lnr_home = findViewById(R.id.lnr_home);
        lnr_home_selected = findViewById(R.id.lnr_home_selected);
        lnr_search = findViewById(R.id.lnr_search);
        lnr_search_selected = findViewById(R.id.lnr_search_selected);
        lnr_cart = findViewById(R.id.lnr_cart);
        lnr_cart_selected = findViewById(R.id.lnr_cart_selected);
        lnr_subscription = findViewById(R.id.lnr_subscription);
        lnr_subscription_selected = findViewById(R.id.lnr_subscription_selected);
        lnr_account = findViewById(R.id.lnr_account);
        lnr_account_selected = findViewById(R.id.lnr_account_selected);
        rel_home = findViewById(R.id.rel_home);
        rel_search = findViewById(R.id.rel_search);
        rel_cart = findViewById(R.id.rel_cart);
        rel_subscription = findViewById(R.id.rel_subscription);
        rel_account = findViewById(R.id.rel_account);

        rec_list.setLayoutManager(new GridLayoutManager(SubSubCategoryListActivity.this, 2));

    }

    @Override
    protected void onResume() {
        super.onResume();

        lnr_home_selected.setVisibility(View.VISIBLE);
        lnr_home.setVisibility(View.GONE);

//        lnr_search.setVisibility(View.VISIBLE);
//        lnr_search_selected.setVisibility(View.GONE);

        lnr_cart.setVisibility(View.VISIBLE);
        lnr_cart_selected.setVisibility(View.GONE);

        lnr_subscription.setVisibility(View.VISIBLE);
        lnr_subscription_selected.setVisibility(View.GONE);

        lnr_account.setVisibility(View.VISIBLE);
        lnr_account_selected.setVisibility(View.GONE);
        cartCount();
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

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void cartCount() {
        if (APIURLs.isNetwork(SubSubCategoryListActivity.this)) {
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
                            }
                        } else {
                            Toast.makeText(SubSubCategoryListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(SubSubCategoryListActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubSubCategoryListActivity.this, "no internet connection");
        }
    }

    public void getCategoryList() {
        dialog.show();

        if (APIURLs.isNetwork(SubSubCategoryListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_subcategory_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            categoryModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CategoryModel model = new CategoryModel();
                                model.setCategory_id(jsonObject1.getString("sub_subcategory_id"));
                                model.setCategory_name(jsonObject1.getString("sub_subcategory"));
                                model.setImage(jsonObject1.getString("image"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(SubSubCategoryListActivity.this);
                            rec_list.setAdapter(adapter);
                            rec_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(View.GONE);
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
                    params.put("sub_category_id", cat_id);
                    if (!cost_id.equals("")) {
                        params.put("franchise_id", SharedPref.getVal(SubSubCategoryListActivity.this, SharedPref.fran_code));
                    } else {
                        params.put("franchise_id", "1");
                    }
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SubSubCategoryListActivity.this, "no internet connection");
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
        Context context;

        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_listing_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            CategoryModel model = categoryModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return categoryModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            ImageView img_category;
            TextView txt_categoryName;
            CardView card_item;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);
                img_category = itemView.findViewById(R.id.img_category);
                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                card_item = itemView.findViewById(R.id.card_item);
            }

            public void bind(final CategoryModel model, int position) {
                txt_categoryName.setText(model.getCategory_name());
                Glide.with(SubSubCategoryListActivity.this).load(model.getImage()).into(img_category);
                card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(SubSubCategoryListActivity.this, Sub_SubCategoryWiseProductListActivity.class);
                        intent.putExtra("sub_cat_id", model.getCategory_id());
                        intent.putExtra("sub_cat_name", model.getCategory_name());
                        startActivity(intent);
                    }
                });
            }
        }
    }

}
