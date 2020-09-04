package com.khojokhao.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.bumptech.glide.Glide;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.smarteist.autoimageslider.IndicatorAnimations;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;
import com.squareup.picasso.Picasso;
import com.khojokhao.Adapter.HomeSliderAdapter;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.GPSTracker;
import com.khojokhao.Constant.PrefManager;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.BannerModel;
import com.khojokhao.Model.CategoryModel;
import com.khojokhao.Model.ProductModel;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class HomeActivity extends AppCompatActivity {

    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
    RecyclerView recyc_recommended, rec_category;
    LinearLayout lnr_recommend;
    TextView txt_cart_count, txt_address;
    RelativeLayout rel_cart_count;
    PrefManager prefManager;
    ArrayList<BannerModel> bannerModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    IOSDialog dialog;
    Double latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        prefManager = new PrefManager(HomeActivity.this);
        prefManager.setFirstTimeLaunch(false);
        SharedPref.putBol(HomeActivity.this, SharedPref.isLogin, true);

        init();
        onclick();
        getBanners();
        getCategoryList();

        if (!SharedPref.getBol(HomeActivity.this, SharedPref.appExit)) {
            if (!SharedPref.getBol(HomeActivity.this, SharedPref.pincodeChecked)) {
                showPincodeDialog();
            }
        }
    }

    public void Slider() {
        SliderView sliderView = findViewById(R.id.imageSlider);

        HomeSliderAdapter adapter = new HomeSliderAdapter(this, bannerModelArrayList);

        sliderView.setSliderAdapter(adapter);

        sliderView.setIndicatorAnimation(IndicatorAnimations.WORM);
        //set indicator animation by using SliderLayout.IndicatorAnimations. :WORM or THIN_WORM or COLOR or DROP or FILL or NONE or SCALE or SCALE_DOWN or SLIDE and SWAP!!
        sliderView.setSliderTransformAnimation(SliderAnimations.SIMPLETRANSFORMATION);
        sliderView.setAutoCycleDirection(SliderView.AUTO_CYCLE_DIRECTION_BACK_AND_FORTH);

        sliderView.setIndicatorSelectedColor(Color.WHITE);
        sliderView.setIndicatorUnselectedColor(Color.GRAY);
        sliderView.setScrollTimeInSec(4); //set scroll delay in seconds :
        sliderView.startAutoCycle();
    }

    public void getBanners() {
        if (APIURLs.isNetwork(HomeActivity.this)) {

            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.BANNER_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            bannerModelArrayList = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                BannerModel model = new BannerModel();
                                model.setBanner_id(jsonObject1.getString("banner_id"));
                                model.setBanner(jsonObject1.getString("banner"));
                                bannerModelArrayList.add(model);
                            }
                            Slider();
                        } else {
//                            ErrorToast("Error"); //Error Toast
                        }
                    } catch (JSONException e) {
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
                    params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
                    return params;
                }
            };


            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
        }
    }

    public void getCurrentLocation() {
        try {
            Geocoder geocoder;
            List<Address> addresses;
            latitude = new GPSTracker(this).getLatitude();
            longitude = new GPSTracker(this).getLongitude();

            if (latitude != 0.0) {
                try {

                    geocoder = new Geocoder(this, Locale.getDefault());
                    addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String add = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                txt_location.setText(address);
                    String pin = addresses.get(0).getPostalCode();

//                    if (pin.equals("400614") || pin.equals("400705") || pin.equals("400706") || pin.equals("410208") || pin.equals("410210")) {
                    txt_address.setText(add);
//                    } else {
//                        txt_address.setText(add);
//                        new AlertDialog.Builder(HomeActivity.this)
//                                .setTitle("Service unavailable")
//                                .setMessage("Currently we are not providing service in your area.")
//
//                                // Specifying a listener allows you to take an action before dismissing the dialog.
//                                // The dialog is automatically dismissed when a dialog button is clicked.
//                                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        // Continue with delete operation
//                                        dialog.dismiss();
//                                    }
//                                })
//                                .show();
//                    }

                } catch (Exception es) {
                    es.printStackTrace();
                }

            }
            /*else {
                showGpsDisabledDialog();
            }*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(HomeActivity.this);
        // Setting Dialog Title
        alertDialog.setTitle("Permission Needed");
        // Setting Dialog Message
        alertDialog.setMessage("location permission needed to access this application.\n" +
                "Go to settings -> Permissions -> allow location Permission ");
        // On pressing Settings button
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
                dialog.cancel();
            }
        });
        // Showing Alert Message
        alertDialog.setCancelable(false);
        alertDialog.show();
    }

    public void showGpsDisabledDialog() {
        if (!((Activity) HomeActivity.this).isFinishing()) {
            new AlertDialog.Builder(HomeActivity.this)
                    .setMessage("GPS Disabled , to Continue, turn on your device location ")

                    // Specifying a listener allows you to take an action before dismissing the dialog.
                    // The dialog is automatically dismissed when a dialog button is clicked.
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            startActivity(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"));
                            dialog.dismiss();
                        }
                    })

                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            // Continue with delete operation
                            dialog.dismiss();
                        }
                    })

                    /*// A null listener allows the button to dismiss the dialog and take no further action.
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finishAffinity();
                        }
                    })*/
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false)
                    .show();
        }
    }

    public void getCategoryList() {
        dialog.show();
        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.CATEGORY_URL, new Response.Listener<String>() {
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
                                model.setCategory_id(jsonObject1.getString("category_id"));
                                model.setCategory_name(jsonObject1.getString("category_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setSubcategorey(jsonObject1.getString("subcategorey"));
                                categoryModelArrayList.add(model);
                            }
                            CategoryAdapter adapter = new CategoryAdapter(HomeActivity.this);
                            rec_category.setAdapter(adapter);
                        } else {
//                            ErrorToast("Error"); //Error Toast
//                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
        }
    }

   /* private void recommendedItems() {

//        dialog.setMessage("Loading...");
//        dialog.setCancelable(false);
//        dialog.show();

        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.RESTAURANT_NEARBY_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
//                            nearByRestaurantModel = new ArrayList<>();
                            JSONArray jsonArray = jsonObject.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {

//                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
////                                NearByRestaurantModel model = new NearByRestaurantModel();
//                                model.setId(jsonObject1.getString("id"));
//                                model.setName(jsonObject1.getString("r_name"));
////                                model.setCuisines(jsonObject1.getString("Cuisines"));
//                                model.setRating(jsonObject1.getString("rating"));
//                                model.setDelivery_time(jsonObject1.getString("delivery_time"));
//                                model.setImage(jsonObject1.getString("r_image"));
//
//
//                                model.setCoupondCode(jsonObject1.getString("c_code"));
//                                model.setOffer_amount(jsonObject1.getString("offer_amount"));
//                                model.setDiscount_type(jsonObject1.getString("discount_type"));
//
//                                JSONArray jsonArray1 = jsonObject1.getJSONArray("cuisines1");
//                                if (jsonArray1.length() > 0) {
//                                    cuisinesModels = new ArrayList<>();
//                                    for (int j = 0; j < jsonArray1.length(); j++) {
//
//                                        JSONObject jsonObject2 = jsonArray1.getJSONObject(j);
//                                        CuisinesModel model1 = new CuisinesModel();
//                                        model1.setId(jsonObject2.getString("id"));
//                                        model1.setName(jsonObject2.getString("name"));
//                                        cuisinesModels.add(model1);
////
//                                    }
//                                    model.setArrayList(cuisinesModels);
//                                }
//                                nearByRestaurantModel.add(model);
                            }
////                            rel_loader.setVisibility(View.GONE);
////                            lin_recyc_nearby.setVisibility(View.VISIBLE);



                            recyc_recommended.setAdapter(new RecommendedAdapter(HomeActivity.this));
                            recyc_recommended.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

//                            Toast.makeText(HomeActivity.this, jsonObject.getString("msg"), Toast.LENGTH_SHORT).show();

                        } else if (status.equals("0")) {
//                            lin_recyc_nearby.setVisibility(View.GONE);

                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                        }
//                        dialog.dismiss();
                    } catch (JSONException e) {
//                        dialog.dismiss();
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
//                    dialog.dismiss();

                    error.printStackTrace();
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
//                    params.put("mobile_no",edt_mobile.getText().toString());
//                    params.put("password",edt_password.getText().toString());
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
//            dialog.dismiss();
            Toast.makeText(this, "no internet connection", Toast.LENGTH_SHORT).show();
        }
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        lnr_home_selected.setVisibility(View.VISIBLE);
        lnr_home.setVisibility(View.GONE);

        lnr_search.setVisibility(View.VISIBLE);
        lnr_search_selected.setVisibility(View.GONE);

        lnr_cart.setVisibility(View.VISIBLE);
        lnr_cart_selected.setVisibility(View.GONE);

        lnr_subscription.setVisibility(View.VISIBLE);
        lnr_subscription_selected.setVisibility(View.GONE);

        lnr_account.setVisibility(View.VISIBLE);
        lnr_account_selected.setVisibility(View.GONE);
        cartCount();

        getrecommendList();
        if (FunctionConstant.GPSRuntime(HomeActivity.this)) {
            getCurrentLocation();
        } else {
            showSettingsAlert();
        }
    }

    public void offers(View view) {
        startActivity(new Intent(HomeActivity.this, OffersProductListActivity.class));
    }

    private void onclick() {
        rel_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.VISIBLE);
                lnr_home.setVisibility(View.GONE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);
            }
        });

        rel_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.GONE);
                lnr_search_selected.setVisibility(View.VISIBLE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
//                finish();
            }
        });

        rel_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.GONE);
                lnr_cart_selected.setVisibility(View.VISIBLE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                startActivity(new Intent(HomeActivity.this, CartActivity.class));
            }
        });

        rel_subscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);


                lnr_subscription.setVisibility(View.GONE);
                lnr_subscription_selected.setVisibility(View.VISIBLE);


                lnr_account.setVisibility(View.VISIBLE);
                lnr_account_selected.setVisibility(View.GONE);

                startActivity(new Intent(HomeActivity.this, SubscriptionActivity.class));
            }
        });

        rel_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lnr_home_selected.setVisibility(View.GONE);
                lnr_home.setVisibility(View.VISIBLE);

                lnr_search.setVisibility(View.VISIBLE);
                lnr_search_selected.setVisibility(View.GONE);

                lnr_cart.setVisibility(View.VISIBLE);
                lnr_cart_selected.setVisibility(View.GONE);

                lnr_subscription.setVisibility(View.VISIBLE);
                lnr_subscription_selected.setVisibility(View.GONE);

                lnr_account.setVisibility(View.GONE);
                lnr_account_selected.setVisibility(View.VISIBLE);

                startActivity(new Intent(HomeActivity.this, AccountActivity.class));
            }
        });
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

    public void vegetables(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Vegetables");
        startActivity(intent);
    }

    public void fruits(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Fruits");
        startActivity(intent);
    }

    public void beverages(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Beverages");
        startActivity(intent);
    }

    public void dairyProduct(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Dairy Product");
        startActivity(intent);
    }

    public void recommend(View view) {
        Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
        intent.putExtra("category", "Recommended");
        startActivity(intent);
    }

    private void init() {
        dialog = new IOSDialog.Builder(HomeActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        lnr_recommend = findViewById(R.id.lnr_recommend);
        txt_cart_count = findViewById(R.id.txt_cart_count);
        txt_address = findViewById(R.id.txt_address);
        txt_address.setSelected(true);
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
        recyc_recommended = findViewById(R.id.recyc_recommended);
        rec_category = findViewById(R.id.rec_category);
        rec_category.setLayoutManager(new GridLayoutManager(HomeActivity.this, 2));
        recyc_recommended.setLayoutManager(new LinearLayoutManager(HomeActivity.this, LinearLayoutManager.HORIZONTAL, false));

    }

    public void search(View view) {
        startActivity(new Intent(HomeActivity.this, SearchActivity.class));
//        finish();
    }

    public void showPincodeDialog() {
        final Dialog dialog1 = new Dialog(this);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.setContentView(R.layout.pincode_dialog_layout);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog1.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog1.getWindow().setLayout(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        dialog1.getWindow().setGravity(Gravity.CENTER);
        dialog1.getWindow().setDimAmount((float) 0.8);
        dialog1.setCancelable(false);
        dialog1.setCanceledOnTouchOutside(false);
        final EditText edt_pincode = dialog1.findViewById(R.id.edt_pincode);
        Button btn_process = dialog1.findViewById(R.id.btn_process);

        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog1.dismiss();
                if (edt_pincode.getText().toString().equals("")) {
                    Toast.makeText(HomeActivity.this, "Please enter pincode", Toast.LENGTH_SHORT).show();
                } else {
                    checkPincode(edt_pincode.getText().toString());
                }
            }
        });
        dialog1.show();
    }

    public void cartCount() {
        if (APIURLs.isNetwork(HomeActivity.this)) {
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
//                            Toast.makeText(HomeActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(HomeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
        }
    }

    public void checkPincode(final String pincode) {
        dialog.show();
        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.check_pincode, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("0")) {
                            SharedPref.putBol(HomeActivity.this, SharedPref.pincodeChecked, true);
                            new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("Service unavailable")
                                    .setMessage("Currently we are not available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else {
                            SharedPref.putBol(HomeActivity.this, SharedPref.pincodeChecked, true);
                            new AlertDialog.Builder(HomeActivity.this)
                                    .setTitle("Service available")
                                    .setMessage("We are available in your area.")

                                    // Specifying a listener allows you to take an action before dismissing the dialog.
                                    // The dialog is automatically dismissed when a dialog button is clicked.
                                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // Continue with delete operation
                                            dialog.dismiss();
                                        }
                                    })
                                    .show();
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
                    params.put("pincode", pincode);
                    params.put("franchise_id", SharedPref.getVal(HomeActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
        }
    }

    public void getrecommendList() {
        dialog.show();
        if (APIURLs.isNetwork(HomeActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.view_product_list, new Response.Listener<String>() {
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
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("qty"));

                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                                if (jsonArray1.length() > 0) {
                                    JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                    model.setUnit(jsonObject2.getString("unit"));
                                    model.setPrice(jsonObject2.getString("price"));
                                    model.setDiscount(jsonObject2.getString("discount"));
                                }

                                productModelArrayList.add(model);
                            }
                            RecommendedAdapter adapter = new RecommendedAdapter(HomeActivity.this);
                            recyc_recommended.setAdapter(adapter);
                            recyc_recommended.setVisibility(View.VISIBLE);
                            lnr_recommend.setVisibility(View.VISIBLE);
                        } else {
                            lnr_recommend.setVisibility(View.GONE);
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
                    params.put("customer_id", SharedPref.getVal(HomeActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(HomeActivity.this, "no internet connection");
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
                Glide.with(HomeActivity.this).load(model.getImage()).into(img_category);
                card_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getSubcategorey().equals("1")) {
                            Intent intent = new Intent(HomeActivity.this, SubCategoryListingActivity.class);
                            intent.putExtra("cat_id", model.getCategory_id());
                            intent.putExtra("cat_name", model.getCategory_name());
                            startActivity(intent);
                        } else {
                            Intent intent = new Intent(HomeActivity.this, CategoryWiseProductListActivity.class);
                            intent.putExtra("sub_cat_id", model.getCategory_id());
                            intent.putExtra("sub_cat_name", model.getCategory_name());
                            intent.putExtra("from", "cat");
                            startActivity(intent);
                        }
                    }
                });
            }
        }
    }

    public class RecommendedAdapter extends RecyclerView.Adapter<RecommendedAdapter.ItemViewholder> {
        Context context;

        public RecommendedAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recommended_home_item_layout, parent, false);
            ItemViewholder itemViewholder = new ItemViewholder(view);
            return itemViewholder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemViewholder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ItemViewholder) holder).bind(model, position);


        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        public class ItemViewholder extends RecyclerView.ViewHolder {
            TextView txt_productName, txt_weight, txt_price;
            ImageView img_product;
            RelativeLayout lnr_details;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_price = itemView.findViewById(R.id.txt_price);
                img_product = itemView.findViewById(R.id.img_product);
                lnr_details = itemView.findViewById(R.id.lnr_details);
            }

            public void bind(final ProductModel model, int position) {
                Picasso.with(context).load(model.getImage()).into(img_product);
                txt_weight.setText(model.getUnit());
                txt_price.setText("Rs." + model.getPrice());
                String productName = model.getProduct_name();
                if (productName.length() > 10) {
                    productName = productName.substring(0, 10);
                    txt_productName.setText(productName + "...");
                } else {
                    txt_productName.setText(productName);
                }

                lnr_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, ProductDetailsActivity.class);
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                    }
                });
            }
        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        SharedPref.putBol(HomeActivity.this, SharedPref.appExit, true);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
                SharedPref.putBol(HomeActivity.this, SharedPref.appExit, false);
                SharedPref.putBol(HomeActivity.this, SharedPref.pincodeChecked, false);
            }
        }, 2000);
    }
}
