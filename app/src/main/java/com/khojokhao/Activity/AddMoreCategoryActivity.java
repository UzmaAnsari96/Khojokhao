package com.khojokhao.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
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

public class AddMoreCategoryActivity extends AppCompatActivity {

    RecyclerView rec_category;
    ImageView img_back;
    IOSDialog dialog;
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    String subs_id="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_more_category);
        init();
        subs_id = getIntent().getStringExtra("subs_id");
        getCategoryList();
    }

    private void init() {
        dialog = new IOSDialog.Builder(AddMoreCategoryActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        rec_category = findViewById(R.id.rec_category);
        rec_category.setLayoutManager(new GridLayoutManager(AddMoreCategoryActivity.this,2));
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


    public void getCategoryList(){
        dialog.show();
        if(APIURLs.isNetwork(AddMoreCategoryActivity.this)){
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CATEGORY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if(status.equals("1")){
                            categoryModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i <jsonArray.length() ; i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                CategoryModel model = new CategoryModel();
                                model.setCategory_id(jsonObject1.getString("category_id"));
                                model.setCategory_name(jsonObject1.getString("category_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("subcategorey"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(AddMoreCategoryActivity.this);
                            rec_category.setAdapter(adapter);
                        }else{
                            Toast.makeText(AddMoreCategoryActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("franchise_id", SharedPref.getVal(AddMoreCategoryActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        }else{
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreCategoryActivity.this,"no internet connection");
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
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            CategoryModel model = categoryModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model,position);
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

            public void bind(final CategoryModel model , int position){
                txt_categoryName.setText(model.getCategory_name());
                Glide.with(AddMoreCategoryActivity.this).load(model.getImage()).into(img_category);
                card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getSubcategorey().equals("1")) {
                            Intent intent = new Intent(AddMoreCategoryActivity.this, AddMoreSubCategoryActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            intent.putExtra("subs_id",subs_id);
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(AddMoreCategoryActivity.this, AddMoreProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            intent.putExtra("subs_id",subs_id);
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }
}
