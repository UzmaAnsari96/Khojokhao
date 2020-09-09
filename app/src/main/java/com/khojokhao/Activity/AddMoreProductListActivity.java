package com.khojokhao.Activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.Model.CategoryModel;
import com.khojokhao.Model.ProductModel;
import com.khojokhao.Model.QuntityModel;
import com.khojokhao.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static android.view.View.GONE;

public class AddMoreProductListActivity extends AppCompatActivity {

    ImageView img_back, img_close;
    TextView txt_categoryName, txt_fromdate, txt_todate, txt_nodata, txt_cart_count;
    RecyclerView rec_list;
    CardView card_bottom, card_mySubscription;
    LinearLayout lin_day, lin_one, lin_two, lin_three, lin_four, lin_s, lin_s_sel, lin_m,
            lin_m_sel, lin_t, lin_t_sel, lin_w, lin_w_sel, lin_th, lin_th_sel, lin_f, lin_f_sel, lin_sat, lin_sat_sel;
    LinearLayout lnr_home, lnr_home_selected, lnr_search, lnr_search_selected,
            lnr_cart, lnr_cart_selected, lnr_subscription, lnr_subscription_selected, lnr_account, lnr_account_selected;
    LinearLayout lnr_selectDays;
    RelativeLayout rel_home, rel_search, rel_cart, rel_subscription, rel_account;
    String category = "";
    String strFromDate = "", strToDate = "";
    private int mYear, mMonth, mDay;
    RelativeLayout rel_from, rel_to;
    RelativeLayout rel_cart_count;
    String sub_cat_id = "", sub_cat_name = "", subs_id = "",from="";
    IOSDialog dialog;
    int qty = 0, subsQty = 0;
    ArrayList<ProductModel> productModelArrayList = new ArrayList<>();
    ArrayList<CategoryModel> categoryModelArrayList = new ArrayList<>();
    String subscription_type = "", weeklyDays = "", customizeDays = "";
    ArrayList<String> productId = new ArrayList<>();
    ArrayList<String> daysArrayList = new ArrayList<>();
    ArrayList<QuntityModel> qtyArrayList = new ArrayList<>();
    ArrayList<String> unitPriceArrayList = new ArrayList<>();
    ArrayList<String> unitArrayList = new ArrayList<>();
    ArrayList<String> discountArrayList = new ArrayList<>();
    Button btn_submit;
    String customizeDay = "", strProductId = "", strQty = "", strUnitPrice = "", strUnit = "", strDiscount = "";
    Button btn_add;
    QuntityModel qtymodel;
    ProductAdapter adapter;
    RecyclerView rec_subcategory;
    ImageView img_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_add_more_product_list);
        init();
        onClick();
        sub_cat_id = getIntent().getStringExtra("sub_cat_id");
        sub_cat_name = getIntent().getStringExtra("sub_cat_name");
        subs_id = getIntent().getStringExtra("subs_id");
        from = getIntent().getStringExtra("from");
        txt_categoryName.setText(sub_cat_name);
    }

    private void init() {
        dialog = new IOSDialog.Builder(AddMoreProductListActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();

        img_back = findViewById(R.id.img_back);
        img_close = findViewById(R.id.img_close);
        txt_categoryName = findViewById(R.id.txt_categoryName);
        txt_nodata = findViewById(R.id.txt_nodata);
        lin_day = findViewById(R.id.lin_day);
        lin_one = findViewById(R.id.lin_one);
        lin_two = findViewById(R.id.lin_two);
        lin_three = findViewById(R.id.lin_three);
        lin_four = findViewById(R.id.lin_four);
        btn_submit = findViewById(R.id.btn_submit);
        btn_add = findViewById(R.id.btn_add);

        txt_cart_count = findViewById(R.id.txt_cart_count);
        rel_cart_count = findViewById(R.id.rel_cart_count);

        lnr_selectDays = findViewById(R.id.lnr_selectDays);
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

        lin_s = findViewById(R.id.lin_s);
        lin_s_sel = findViewById(R.id.lin_s_sel);
        lin_m = findViewById(R.id.lin_m);
        lin_m_sel = findViewById(R.id.lin_m_sel);
        lin_t = findViewById(R.id.lin_t);
        lin_t_sel = findViewById(R.id.lin_t_sel);
        lin_w = findViewById(R.id.lin_w);
        lin_w_sel = findViewById(R.id.lin_w_sel);
        lin_th = findViewById(R.id.lin_th);
        lin_th_sel = findViewById(R.id.lin_th_sel);
        lin_f = findViewById(R.id.lin_f);
        lin_f_sel = findViewById(R.id.lin_f_sel);
        lin_sat = findViewById(R.id.lin_sat);
        lin_sat_sel = findViewById(R.id.lin_sat_sel);

        rec_list = findViewById(R.id.rec_list);

        txt_fromdate = findViewById(R.id.txt_fromdate);
        txt_todate = findViewById(R.id.txt_todate);
        rel_from = findViewById(R.id.rel_from);
        rel_to = findViewById(R.id.rel_to);
        rec_subcategory = findViewById(R.id.rec_subcategory);
        img_filter = findViewById(R.id.img_filter);

        card_bottom = findViewById(R.id.card_bottom);
        card_bottom.setVisibility(View.GONE);
        card_mySubscription = findViewById(R.id.card_mySubscription);
        rec_list.setLayoutManager(new LinearLayoutManager(AddMoreProductListActivity.this));
        rec_subcategory.setLayoutManager(new LinearLayoutManager(AddMoreProductListActivity.this, RecyclerView.HORIZONTAL, false));
    }

    private void onClick() {

        img_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                card_mySubscription.setVisibility(View.GONE);
                lin_day.setVisibility(View.VISIBLE);
                customizeDay = "";
                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* if (daysArrayList.size() > 0) {
                 *//*Comma Separated Days*//*
                    for (int i = 0; i < daysArrayList.size(); i++) {
                        customizeDay += daysArrayList.get(i) + ",";
                    }
                    customizeDay = customizeDay.substring(0, customizeDay.length() - 1);
                }*/
                if (productId.size() > 0) {
                    /*Comma Separated Product Id*/
                    for (int i = 0; i < productId.size(); i++) {
                        strProductId += productId.get(i) + ",";
                    }
                    strProductId = strProductId.substring(0, strProductId.length() - 1);
                }
                if (qtyArrayList.size() > 0) {
                    /*Comma Separated Qty*/
                    for (int i = 0; i < qtyArrayList.size(); i++) {
                        strQty += qtyArrayList.get(i).getQty() + ",";
                    }
                    strQty = strQty.substring(0, strQty.length() - 1);
                }
                if (unitPriceArrayList.size() > 0) {
                    /*Comma Separated Unit Price*/
                    for (int i = 0; i < unitPriceArrayList.size(); i++) {
                        strUnitPrice += unitPriceArrayList.get(i) + ",";
                    }
                    strUnitPrice = strUnitPrice.substring(0, strUnitPrice.length() - 1);
                }
                if (unitArrayList.size() > 0) {
                    /*Comma Separated Unit*/
                    for (int i = 0; i < unitArrayList.size(); i++) {
                        strUnit += unitArrayList.get(i) + ",";
                    }
                    strUnit = strUnit.substring(0, strUnit.length() - 1);
                }
                if (discountArrayList.size() > 0) {
                    /*Comma Separated Unit*/
                    for (int i = 0; i < discountArrayList.size(); i++) {
                        strDiscount += discountArrayList.get(i) + ",";
                    }
                    strDiscount = strDiscount.substring(0, strDiscount.length() - 1);
                }

                subscribed();
            }
        });


        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
//
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lin_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);
                lin_day.setVisibility(View.GONE);
                subscription_type = "Weekly";
                lnr_selectDays.setVisibility(View.VISIBLE);

                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });

        lin_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);
                lin_day.setVisibility(View.GONE);
                subscription_type = "daily";
                lnr_selectDays.setVisibility(View.GONE);


            }
        });
        lin_three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.VISIBLE);

                lin_day.setVisibility(View.GONE);
                subscription_type = "Custom";
                lnr_selectDays.setVisibility(View.VISIBLE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                daysArrayList = new ArrayList<>();
            }
        });
        lin_four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*card_bottom.setVisibility(View.GONE);*/
                card_mySubscription.setVisibility(View.GONE);
                lin_day.setVisibility(View.GONE);


                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);

                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);

                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);

                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);

                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);

                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);

                weeklyDays = "";
                customizeDay = "";
                daysArrayList = new ArrayList<>();
                getProductList();
            }
        });

        rel_from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(AddMoreProductListActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                            strFromDate = format.format(calendar.getTime());
                            txt_fromdate.setText(strFromDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        rel_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(AddMoreProductListActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                            strToDate = format.format(calendar.getTime());
                            txt_todate.setText(strToDate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
                datePickerDialog.show();
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            }
        });

        lin_s.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {
                    lin_s.setVisibility(View.GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Sunday";

                } else {
                    lin_s.setVisibility(View.GONE);
                    lin_s_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Sunday");
                }

            }
        });
        lin_s_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_s.setVisibility(View.VISIBLE);
                lin_s_sel.setVisibility(View.GONE);

                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Sunday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });

        lin_m.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subscription_type.equals("Weekly")) {
                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Monday";

                } else {
                    lin_m.setVisibility(View.GONE);
                    lin_m_sel.setVisibility(View.VISIBLE);
                    daysArrayList.add("Monday");
                }
            }
        });
        lin_m_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_m.setVisibility(View.VISIBLE);
                lin_m_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Monday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Tuesday";

                } else {
                    lin_t.setVisibility(View.GONE);
                    lin_t_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Tuesday");
                }

            }
        });
        lin_t_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_t.setVisibility(View.VISIBLE);
                lin_t_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Tuesday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_w.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Wednesday";

                } else {
                    lin_w.setVisibility(View.GONE);
                    lin_w_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Wednesday");
                }

            }
        });
        lin_w_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_w.setVisibility(View.VISIBLE);
                lin_w_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Wednesday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_th.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Thursday";

                } else {
                    lin_th.setVisibility(View.GONE);
                    lin_th_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Thursday");
                }
            }
        });
        lin_th_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_th.setVisibility(View.VISIBLE);
                lin_th_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Thursday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_f.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    lin_sat.setVisibility(View.VISIBLE);
                    lin_sat_sel.setVisibility(View.GONE);

                    weeklyDays = "Friday";

                } else {
                    lin_f.setVisibility(View.GONE);
                    lin_f_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Friday");
                }
            }
        });
        lin_f_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_f.setVisibility(View.VISIBLE);
                lin_f_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Friday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });


        lin_sat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (subscription_type.equals("Weekly")) {

                    lin_s.setVisibility(View.VISIBLE);
                    lin_s_sel.setVisibility(View.GONE);

                    lin_m.setVisibility(View.VISIBLE);
                    lin_m_sel.setVisibility(View.GONE);

                    lin_t.setVisibility(View.VISIBLE);
                    lin_t_sel.setVisibility(View.GONE);

                    lin_w.setVisibility(View.VISIBLE);
                    lin_w_sel.setVisibility(View.GONE);

                    lin_th.setVisibility(View.VISIBLE);
                    lin_th_sel.setVisibility(View.GONE);

                    lin_f.setVisibility(View.VISIBLE);
                    lin_f_sel.setVisibility(View.GONE);

                    lin_sat.setVisibility(View.GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    weeklyDays = "Saturday";

                } else {
                    lin_sat.setVisibility(View.GONE);
                    lin_sat_sel.setVisibility(View.VISIBLE);

                    daysArrayList.add("Saturday");
                }
            }
        });
        lin_sat_sel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lin_sat.setVisibility(View.VISIBLE);
                lin_sat_sel.setVisibility(View.GONE);
                for (int i = 0; i < daysArrayList.size(); i++) {
                    if (daysArrayList.contains("Saturday")) {
                        daysArrayList.remove(i);
                    }
                }
            }
        });

        if (card_bottom.getVisibility() == View.VISIBLE) {
            rec_list.setClickable(false);
        }
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
                startActivity(new Intent(AddMoreProductListActivity.this, HomeActivity.class));

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

                startActivity(new Intent(AddMoreProductListActivity.this, SearchActivity.class));
//                finish();
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

                startActivity(new Intent(AddMoreProductListActivity.this, CartActivity.class));
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

                startActivity(new Intent(AddMoreProductListActivity.this, SubscriptionActivity.class));
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

                startActivity(new Intent(AddMoreProductListActivity.this, AccountActivity.class));
            }
        });

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

        /*cartCount();*/
        if (from.equals("cat")) {
            getProductList();
        } else {
            getCategoryList();

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
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
        },2000);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public void subscribed() {
        dialog.show();
        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.add_more_product, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(AddMoreProductListActivity.this, "Product add in subscription successfully", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(AddMoreProductListActivity.this, SubscriptionActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(AddMoreProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("subscribe_id", subs_id);
                    params.put("from_date", strFromDate);
                    params.put("to_date", strToDate);
                    params.put("product_id", strProductId);
                    params.put("qty", strQty);
                    params.put("unit", strUnit);
                    params.put("unit_price", strUnitPrice);
                    params.put("discount", strDiscount);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }

    }

    public void addSubscription(View view) {
        card_mySubscription.setVisibility(View.GONE);
        lin_day.setVisibility(View.GONE);
        startActivity(new Intent(AddMoreProductListActivity.this, SubscriptionActivity.class));
    }


    public void getCategoryList() {
        dialog.show();
        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
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
                            CategoryAdapter adapter = new CategoryAdapter(AddMoreProductListActivity.this);
                            rec_subcategory.setAdapter(adapter);
                            rec_subcategory.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(GONE);
                            img_filter.setVisibility(View.VISIBLE);
                        } else {
                            /*
                            txt_nodata.setVisibility(View.VISIBLE);*/
                            rec_subcategory.setVisibility(View.GONE);
                            img_filter.setVisibility(GONE);
                            if (from.equals("cat")) {
                                getProductList();
                            } else if (from.equals("sub_cat")) {
                                getsubProductList();
                            }
                        }
                        dismissDialog();
                    } catch (JSONException e) {
                        dismissDialog();
                        e.printStackTrace();
                        if (from.equals("cat")) {
                            getProductList();
                        } else if (from.equals("sub_cat")) {
                            getsubProductList();
                        }
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    dismissDialog();
                    error.printStackTrace();
                    if (from.equals("cat")) {
                        getProductList();
                    } else if (from.equals("sub_cat")) {
                        getsubProductList();
                    }
                }
            }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    HashMap<String, String> params = new HashMap<>();
                    params.put("sub_category_id", sub_cat_id);
                    params.put("franchise_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }
    }

    public void getProductList() {
        dialog.show();
        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.PRODUCTLIST_URL, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setType(jsonObject1.getString("type"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("price"));
                                model.setDiscount(jsonObject2.getString("discount"));

                                productModelArrayList.add(model);
                            }
                            adapter = new ProductAdapter(AddMoreProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("category_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                    params.put("franchise_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }
    }

    public void getsubProductList() {
        dialog.show();

        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_category_wise_product_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setType(jsonObject1.getString("type"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("price"));
                                model.setDiscount(jsonObject2.getString("discount"));

                                productModelArrayList.add(model);
                            }
                            adapter = new ProductAdapter(AddMoreProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("sub_category_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                    params.put("franchise_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }
    }

    public void getsubsubProductList(final String sub_cat_id) {
        dialog.show();

        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.sub_subcategory_wise_product_list, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            productModelArrayList = new ArrayList<>();
                          /*  productId = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            unitArrayList = new ArrayList<>();
                            qtyArrayList = new ArrayList<>();
                            discountArrayList = new ArrayList<>();*/
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                                ProductModel model = new ProductModel();
                                model.setProduct_id(jsonObject1.getString("product_id"));
                                model.setProduct_name(jsonObject1.getString("product_name"));
                                model.setImage(jsonObject1.getString("image"));
                                model.setFlag(jsonObject1.getString("flag"));
                                model.setQty(jsonObject1.getString("qty"));
                                model.setType(jsonObject1.getString("type"));
                                JSONArray jsonArray1 = jsonObject1.getJSONArray("price_details");
                                JSONObject jsonObject2 = jsonArray1.getJSONObject(0);
                                model.setUnit(jsonObject2.getString("unit"));
                                model.setPrice(jsonObject2.getString("price"));
                                model.setDiscount(jsonObject2.getString("discount"));

                                productModelArrayList.add(model);
                            }
                            adapter = new ProductAdapter(AddMoreProductListActivity.this);
                            rec_list.setAdapter(adapter);
//                            filter = adapter.getFilter();
//                            adapter.setHasStableIds(true);
//                            rec_list.setItemViewCacheSize(productModelArrayList.size()-1);
//                            rec_list.setHasFixedSize(true);
                            rec_list.setVisibility(View.VISIBLE);
                            txt_nodata.setVisibility(GONE);
                        } else {
                            rec_list.setVisibility(GONE);
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
                    params.put("sub_subcategory_id", sub_cat_id);
                    params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                    params.put("franchise_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.fran_code));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }
    }

    public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ItemViewholder> {
        Context context;
        int selectedPos = 0;

        public CategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_sub_category_item_layout, parent, false);
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
            TextView txt_categoryName;
            View view;
            LinearLayout lnr_category;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                view = itemView.findViewById(R.id.view);
                lnr_category = itemView.findViewById(R.id.lnr_category);
            }

            public void bind(final CategoryModel model, final int position) {
                txt_categoryName.setText(model.getCategory_name());

                if (selectedPos == position) {
                    view.setVisibility(View.VISIBLE);
                    getsubsubProductList(model.getCategory_id());
                } else {
                    view.setVisibility(GONE);
                }

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectedPos = position;
                        notifyDataSetChanged();
                    }
                });
            }
        }
    }

    public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ItemViewholder> {
        Context context;
        int selectedPos = 0;

        public SubCategoryAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ItemViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_sub_category_dialog_item_layout, parent, false);
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
            TextView txt_categoryName;
            ImageView img_category;
            LinearLayout lnr_category;

            public ItemViewholder(@NonNull View itemView) {
                super(itemView);

                txt_categoryName = itemView.findViewById(R.id.txt_categoryName);
                img_category = itemView.findViewById(R.id.img_category);
                lnr_category = itemView.findViewById(R.id.lnr_category);
            }

            public void bind(final CategoryModel model, final int position) {
                Picasso.with(AddMoreProductListActivity.this).load(model.getImage()).into(img_category);
                txt_categoryName.setText(model.getCategory_name());

                lnr_category.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        dialog1.dismiss();
                        getsubsubProductList(model.getCategory_id());
                    }
                });
            }
        }
    }

    public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
        Context context;
        Double actualAmout;
        Double discount;
        Double payableAmt;

        public ProductAdapter(Context context) {
            this.context = context;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(AddMoreProductListActivity.this).inflate(R.layout.search_item_layout, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
            ProductModel model = productModelArrayList.get(position);
            ((ViewHolder) holder).bind(model, position);
        }

        @Override
        public int getItemCount() {
            return productModelArrayList.size();
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView img_product;
            TextView txt_productName, txt_weight, txt_season, txt_actualprice, txt_stock, txt_discountprice, txt_discount, txt_count, txt_subscribecount;
            Button btn_cart, btn_subscribe;
            LinearLayout lnr_product;
            RelativeLayout rel_discount, rel_cart, rel_minus, rel_plus, rel_subscribe, rel_subscribeminus, rel_subscribeplus;
            ProgressBar progressBar;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                img_product = itemView.findViewById(R.id.img_product);
                txt_productName = itemView.findViewById(R.id.txt_productName);
                txt_weight = itemView.findViewById(R.id.txt_weight);
                txt_season = itemView.findViewById(R.id.txt_season);
                txt_actualprice = itemView.findViewById(R.id.txt_actualprice);
                txt_discountprice = itemView.findViewById(R.id.txt_discountprice);
                txt_stock = itemView.findViewById(R.id.txt_stock);
                btn_cart = itemView.findViewById(R.id.btn_cart);
                btn_subscribe = itemView.findViewById(R.id.btn_subscribe);
                lnr_product = itemView.findViewById(R.id.lnr_product);
                rel_discount = itemView.findViewById(R.id.rel_discount);
                rel_minus = itemView.findViewById(R.id.rel_minus);
                rel_plus = itemView.findViewById(R.id.rel_plus);
                rel_subscribe = itemView.findViewById(R.id.rel_subscribe);
                rel_subscribeminus = itemView.findViewById(R.id.rel_subscribeminus);
                rel_subscribeplus = itemView.findViewById(R.id.rel_subscribeplus);
                rel_cart = itemView.findViewById(R.id.rel_cart);
                txt_discount = itemView.findViewById(R.id.txt_discount);
                txt_count = itemView.findViewById(R.id.txt_count);
                txt_subscribecount = itemView.findViewById(R.id.txt_subscribecount);
                txt_actualprice.setPaintFlags(txt_actualprice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                progressBar = itemView.findViewById(R.id.progressBar);
                btn_cart.setVisibility(GONE);
            }

            public void bind(final ProductModel model, final int position) {
                Glide.with(AddMoreProductListActivity.this).load(model.getImage()).into(img_product);
                txt_productName.setText(model.getProduct_name());
                txt_weight.setText(model.getUnit());
                try {
                    if (model.getDiscount().equals("0")) {
                        txt_discountprice.setText("Rs." + model.getPrice());
                        txt_actualprice.setVisibility(View.GONE);
                        rel_discount.setVisibility(View.GONE);
                    } else {
                        txt_actualprice.setVisibility(View.VISIBLE);
                        rel_discount.setVisibility(View.VISIBLE);
                        actualAmout = Double.parseDouble(model.getPrice());
                        discount = (actualAmout * Double.parseDouble(model.getDiscount())) / 100;
                        payableAmt = actualAmout - discount;
                        DecimalFormat twoDForm = new DecimalFormat("#");
                        payableAmt = Double.valueOf(twoDForm.format(payableAmt));
                        txt_discountprice.setText("Rs." + (int) Math.round(payableAmt * 100) / 100);
                        txt_actualprice.setText("Rs." + model.getPrice());
                        txt_discount.setText(model.getDiscount() + "% off");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (model.getFlag().equals("0")) {
                    txt_stock.setVisibility(View.VISIBLE);
                } else if (model.getFlag().equals("1")) {
                    txt_stock.setVisibility(View.GONE);
                }


                if (model.getType().equals("0")) {
                    btn_subscribe.setVisibility(View.INVISIBLE);
                }


                if (model.getQty().equals("0")) {
                    rel_cart.setVisibility(View.GONE);
                    btn_cart.setVisibility(View.VISIBLE);
                } else {
                    rel_cart.setVisibility(View.VISIBLE);
                    btn_cart.setVisibility(View.GONE);
                    qty = Integer.valueOf(model.getQty());
                    txt_count.setText(String.valueOf(qty));
                }

                btn_cart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFlag().equals("1")) {
                            rel_cart.setVisibility(View.VISIBLE);
                            btn_cart.setVisibility(View.GONE);
                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(), txt_count.getText().toString());
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qtymodel = new QuntityModel();
                        qtymodel.setId(model.getProduct_id());
                        qtymodel.setQty(txt_subscribecount.getText().toString());

                        qtyArrayList.add(qtymodel);
                        if (model.getFlag().equals("1")) {
                            rel_subscribe.setVisibility(View.VISIBLE);
                            btn_subscribe.setVisibility(View.GONE);
                            lin_day.setVisibility(View.VISIBLE);
                          /*  if (productId.size() > 0) {
                                for (int i = 0; i < productId.size(); i++) {
                                    if (productId.get(i).equals(model.getProduct_id())) {
                                        productId.remove(i);
                                    }
                                }
                            }
                            if (unitPriceArrayList.size() > 0) {
                                for (int i = 0; i < unitPriceArrayList.size(); i++) {
                                    if (unitPriceArrayList.get(i).equals(model.getPrice())) {
                                        unitPriceArrayList.remove(i);
                                    }
                                }
                            }
                            if (unitArrayList.size() > 0) {
                                for (int i = 0; i < unitArrayList.size(); i++) {
                                    if (unitArrayList.get(i).equals(model.getUnit())) {
                                        unitArrayList.remove(i);
                                    }
                                }
                            }
                            if (discountArrayList.size() > 0) {
                                for (int i = 0; i < discountArrayList.size(); i++) {
                                    if (discountArrayList.get(i).equals(model.getDiscount())) {
                                        discountArrayList.remove(i);
                                    }
                                }
                            }*/
                            productId.add(model.getProduct_id());
                            unitPriceArrayList.add(model.getPrice());
                            unitArrayList.add(model.getUnit());
                            discountArrayList.add(model.getDiscount());
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                rel_minus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (model.getFlag().equals("1")) {
                            qty = Integer.parseInt(txt_count.getText().toString());
                            if (qty >= 1) {
                                qty--;
                                if (qty != 0) {
                                    if (qty >= 1) {
//                                        progressBar.setVisibility(View.VISIBLE);
                                        txt_count.setVisibility(GONE);
                                        txt_count.setText(String.valueOf(qty));
                                        addCart(model.getProduct_id(), model.getUnit(), model.getPrice(),
                                                model.getDiscount(), txt_count.getText().toString());
                                    }
                                    /*else if(qty==1){
                                        rel_cart.setVisibility(View.GONE);
                                        btn_cart.setVisibility(View.VISIBLE);
                                        removeCart(model.getProduct_id(),position);
                                    }*/
                                } else {
                                    rel_cart.setVisibility(GONE);
                                    btn_cart.setVisibility(View.VISIBLE);
                                    removeCart(model.getProduct_id(), position);
                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }


                    }
                });

                rel_plus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        qty = Integer.parseInt(txt_count.getText().toString());
                        if (model.getFlag().equals("1")) {
                            qty = Integer.parseInt(txt_count.getText().toString());
                            qty++;
                            /* progressBar.setVisibility(View.VISIBLE);*/
                            txt_count.setVisibility(GONE);
                            txt_count.setText(String.valueOf(qty));
                            addCart(model.getProduct_id(), model.getUnit(), model.getPrice(), model.getDiscount(),
                                    txt_count.getText().toString());
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                rel_subscribeminus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                        if (model.getFlag().equals("1")) {
                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                            if (subsQty >= 1) {
                                subsQty--;
                                if (subsQty != 0) {
                                    if (subsQty > 1) {
                                        txt_subscribecount.setText(String.valueOf(subsQty));
                                    }
                                    txt_subscribecount.setText(String.valueOf(subsQty));
                                    for (int i = 0; i < qtyArrayList.size(); i++) {
                                        String id = qtyArrayList.get(i).getId();
                                        if (model.getProduct_id().equals(id)) {
                                            qtymodel = new QuntityModel();
                                            qtymodel.setId(id);
                                            qtymodel.setQty(String.valueOf(subsQty));
                                            qtyArrayList.remove(i);
                                            qtyArrayList.add(i, qtymodel);
                                        }
                                    }
                                } else {
                                    rel_subscribe.setVisibility(View.GONE);
                                    btn_subscribe.setVisibility(View.VISIBLE);
                                    for (int i = 0; i < qtyArrayList.size(); i++) {
                                       /* if (position == i) {
                                            qtyArrayList.set(i, String.valueOf(subsQty));
                                        }*/
                                        String id = qtyArrayList.get(i).getId();
                                        if (model.getProduct_id().equals(id)) {
                                            qtymodel = new QuntityModel();
                                            qtymodel.setId(id);
                                            qtymodel.setQty(String.valueOf(subsQty));
                                            if (subsQty == 0) {
                                                qtyArrayList.remove(i);
                                                productId.remove(i);
                                                unitPriceArrayList.remove(i);
                                                unitArrayList.remove(i);
                                                discountArrayList.remove(i);
                                            } else {
                                                qtyArrayList.remove(i);
                                                qtyArrayList.add(i, qtymodel);
                                            }
                                        }
                                    }

                                    if (qtyArrayList.size() == 0) {
                                        lin_day.setVisibility(View.GONE);
                                        card_mySubscription.setVisibility(View.GONE);
                                        qtyArrayList = new ArrayList<>();
                                        productId = new ArrayList<>();
                                        unitPriceArrayList = new ArrayList<>();
                                        unitArrayList = new ArrayList<>();
                                        discountArrayList = new ArrayList<>();
                                    }

                                 /*   if (productId.size() > 0) {
                                        for (int i = 0; i < productId.size(); i++) {
                                            if (productId.get(i).equals(model.getProduct_id())) {
                                                productId.remove(i);
                                            }
                                        }
                                    }
                                    if (unitPriceArrayList.size() > 0) {
                                        for (int i = 0; i < unitPriceArrayList.size(); i++) {
                                            if (unitPriceArrayList.get(i).equals(model.getPrice())) {
                                                unitPriceArrayList.remove(i);
                                            }
                                        }
                                    }
                                    if (unitArrayList.size() > 0) {
                                        for (int i = 0; i < unitArrayList.size(); i++) {
                                            if (unitArrayList.get(i).equals(model.getUnit())) {
                                                unitArrayList.remove(i);
                                            }
                                        }
                                    }
                                    if (discountArrayList.size() > 0) {
                                        for (int i = 0; i < discountArrayList.size(); i++) {
                                            if (discountArrayList.get(i).equals(model.getDiscount())) {
                                                discountArrayList.remove(i);
                                            }
                                        }
                                    }
*/

                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                rel_subscribeplus.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                        if (model.getFlag().equals("1")) {
                            subsQty = Integer.parseInt(txt_subscribecount.getText().toString());
                            subsQty++;
                            txt_subscribecount.setText(String.valueOf(subsQty));
                            for (int i = 0; i < qtyArrayList.size(); i++) {
                               /* if (position == i) {
                                    qtyArrayList.set(i, String.valueOf(subsQty));
                                }*/
                                String id = qtyArrayList.get(i).getId();
                                if (model.getProduct_id().equals(id)) {
                                    qtymodel = new QuntityModel();
                                    qtymodel.setId(id);
                                    qtymodel.setQty(String.valueOf(subsQty));
                                    qtyArrayList.remove(i);
                                    qtyArrayList.add(i, qtymodel);
                                }
                            }
                        } else {
                            Toast.makeText(context, "out of stock", Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                lnr_product.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(new Intent(AddMoreProductListActivity.this, ProductDetailsActivity.class));
                        intent.putExtra("prod_id", model.getProduct_id());
                        startActivity(intent);
                        clickEntry(model.getProduct_id());

                    }
                });

                /*btn_subscribe.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (model.getFlag().equals("0")) {
                            Toast.makeText(context, "Out of stock", Toast.LENGTH_SHORT).show();
                        } else if (model.getFlag().equals("1")) {
                            btn_subscribe.setBackgroundDrawable(getResources().getDrawable(R.drawable.green_button_layout));
                            btn_subscribe.setTextColor(getResources().getColor(R.color.colorWhite));

                        }
                    }
                });*/
            }

            public void removeCart(final String product_id, final int position) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);

                if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.remove_product_qty, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    qty = 0;
                                    getProductList();
                                    cartCount();
                                    /*rel_cart.setVisibility(View.GONE);
                                    btn_cart.setVisibility(View.VISIBLE);*/
                                } else {
                                    Toast.makeText(AddMoreProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);
                            } catch (JSONException e) {
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
                            progressBar.setVisibility(GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("product_id", product_id);
                            params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(AddMoreProductListActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
                }
            }

            public void addCart(final String prod_id, final String unit, final String price, final String discount, final String qty1) {
//                dialog.show();
                progressBar.setVisibility(View.VISIBLE);
                txt_count.setVisibility(GONE);

                if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.ADDCART_URL, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                    txt_count.setText(qty1);
                                    qty = 0;
                                    cartCount();
                                } else {
                                    Toast.makeText(AddMoreProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                }
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);

                            } catch (JSONException e) {
//                                dismissDialog();
                                progressBar.setVisibility(GONE);
                                txt_count.setVisibility(View.VISIBLE);
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            dismissDialog();
                            progressBar.setVisibility(GONE);
                            txt_count.setVisibility(View.VISIBLE);
                            error.printStackTrace();
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            HashMap<String, String> params = new HashMap<>();
                            params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                            params.put("product_id", prod_id);
                            params.put("unit", unit);
                            params.put("price", price);
                            params.put("discount", discount);
                            params.put("qty", qty1);
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(AddMoreProductListActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
                }
            }

            public void clickEntry(final String product_id) {
                dialog.show();
                if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.view_product_entry, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.getJSONArray("result");
                                JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                                String status = jsonObject1.getString("status");
                                if (status.equals("1")) {
                                } else {
                                    Toast.makeText(AddMoreProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                            params.put("product_id", product_id);
                            params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                            return params;
                        }
                    };

                    RequestQueue requestQueue = Volley.newRequestQueue(AddMoreProductListActivity.this);
                    stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    requestQueue.add(stringRequest);
                } else {
                    dismissDialog();
                    FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
                }
            }
        }
    }

    public void cartCount() {
        if (APIURLs.isNetwork(AddMoreProductListActivity.this)) {
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
                            Toast.makeText(AddMoreProductListActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(AddMoreProductListActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(AddMoreProductListActivity.this, "no internet connection");
        }
    }

}
