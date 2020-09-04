package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.SearchModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    EditText edt_search;
    ImageView img_back;
    RecyclerView rec_search;
    IOSDialog dialog;
    SearchAdapter adapter;
    ArrayList<SearchModel> productModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
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

        edt_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {

                if (edt_search.getText().toString().equals("")) {
                    productModelArrayList = new ArrayList<SearchModel>();
                    productModelArrayList = new ArrayList<>();
                    rec_search.setVisibility(View.GONE);
                } else {
                    if (edt_search.getText().toString().equals("") || edt_search.getText().length() == 0) {
                        productModelArrayList.clear();
                        productModelArrayList = new ArrayList<SearchModel>();
                        productModelArrayList = new ArrayList<>();
                        rec_search.setVisibility(View.GONE);

                    } else {
                        if (edt_search.getText().length() > 1) {
                            productModelArrayList = new ArrayList<SearchModel>();
                            productModelArrayList = new ArrayList<>();
                            rec_search.setVisibility(View.GONE);

                            String key = edt_search.getText().toString().toLowerCase();
                            getProductList(key);
                            productModelArrayList = new ArrayList<SearchModel>();
                            rec_search.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {

            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(SearchActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        edt_search = findViewById(R.id.edt_search);
        img_back = findViewById(R.id.img_back);
        rec_search = findViewById(R.id.rec_search);
        rec_search.setLayoutManager(new LinearLayoutManager(SearchActivity.this));
        rec_search.setAdapter(new SearchAdapter());
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

    public void getProductList(final String key) {
//        dialog.show();
        if (APIURLs.isNetwork(SearchActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.product_search_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                SearchModel model = new SearchModel();
                                model.setSub_category_id(jsonObject1.getString("sub_category_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setCategory_name(jsonObject1.getString("category_name"));
                                productModelArrayList.add(model);
                            }
                            SearchAdapter adapter = new SearchAdapter();
                            rec_search.setAdapter(adapter);
                            rec_search.setVisibility(View.VISIBLE);
//                            txt_nodata.setVisibility(View.GONE);
                        } else {
                            rec_search.setVisibility(View.GONE);
//                            txt_nodata.setVisibility(View.VISIBLE);
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
                    params.put("key", key);
                    params.put("franchise_id", SharedPref.getVal(SearchActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(SearchActivity.this, "no internet connection");
        }
    }

    public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(SearchActivity.this).inflate(R.layout.search_item_list_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            SearchModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView txt_productName;
            CardView card_product;
            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                txt_productName = itemView.findViewById(R.id.txt_productName);
                card_product = itemView.findViewById(R.id.card_product);
            }

            public void bind(final SearchModel model, final int position) {
                txt_productName.setText(model.getProduct_name());

                card_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(SearchActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getSub_category_id());
                        startActivity(intent);
                        finish();
                    }
                });

            }

        }
    }
}
