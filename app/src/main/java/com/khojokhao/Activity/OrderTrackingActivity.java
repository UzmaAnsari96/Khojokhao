package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.gmail.samehadar.iosdialog.IOSDialog;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class OrderTrackingActivity extends AppCompatActivity {

    ImageView img_back;
    TextView txt_orderId, txt_help, txt_cancelOrder, txt_orderAccept, txt_orderAcceptMessage, txt_orderDispatchMessage, txt_orderDeliverMessage,
            txt_viewItems, txt_shippingAddress, txt_paidBy, txt_unitPrice, txt_discount, txt_couponCharges, txt_subTotal, txt_grandTotal, txt_oderDispatch,
            txt_orderDelivered,txt_walletCharges;
    RelativeLayout rel_accept_internal, rel_dispatch_internal, rel_orderDelivered_internal;
    View view_orderAccept, view_dispatch;
    LinearLayout lnr_discount, lnr_coupon,lnr_walletBalance;
    String order_id = "";
    IOSDialog dialog;
    String order_id1 = "", p_mode = "", item = "", order_status = "", deliver_address = "", total_bag = "", bag_discount = "",
            order_total = "", coupon_value = "", day = "", slot_timing = "", wallet_use = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_tracking);
        init();
        order_id = getIntent().getStringExtra("order_id");
        onClick();
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        txt_cancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Integer.parseInt(order_status)<=1) {
                    AlertDialog.Builder alertDialog1 = new AlertDialog.Builder(OrderTrackingActivity.this);
                    // Setting Dialog Message
                    alertDialog1.setMessage("Are you sure?\nYou want to cancel this order.");
                    // On pressing Settings button
                    alertDialog1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(OrderTrackingActivity.this,CancelOrderActivity.class);
                            intent.putExtra("oid",order_id);
                            startActivity(intent);
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
                }
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(OrderTrackingActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_back = findViewById(R.id.img_back);
        txt_orderId = findViewById(R.id.txt_orderId);
        txt_help = findViewById(R.id.txt_help);
        txt_cancelOrder = findViewById(R.id.txt_cancelOrder);
        txt_orderAccept = findViewById(R.id.txt_orderAccept);
        txt_orderAcceptMessage = findViewById(R.id.txt_orderAcceptMessage);
        txt_orderDispatchMessage = findViewById(R.id.txt_orderDispatchMessage);
        txt_orderDeliverMessage = findViewById(R.id.txt_orderDeliverMessage);
        txt_viewItems = findViewById(R.id.txt_viewItems);
        txt_shippingAddress = findViewById(R.id.txt_shippingAddress);
        txt_paidBy = findViewById(R.id.txt_paidBy);
        txt_unitPrice = findViewById(R.id.txt_unitPrice);
        txt_couponCharges = findViewById(R.id.txt_couponCharges);
        rel_accept_internal = findViewById(R.id.rel_accept_internal);
        rel_dispatch_internal = findViewById(R.id.rel_dispatch_internal);
        rel_orderDelivered_internal = findViewById(R.id.rel_orderDelivered_internal);
        txt_oderDispatch = findViewById(R.id.txt_oderDispatch);
        txt_orderDelivered = findViewById(R.id.txt_orderDelivered);
        view_orderAccept = findViewById(R.id.view_orderAccept);
        view_dispatch = findViewById(R.id.view_dispatch);
        lnr_discount = findViewById(R.id.lnr_discount);
        lnr_coupon = findViewById(R.id.lnr_coupon);
        lnr_walletBalance = findViewById(R.id.lnr_walletBalance);
        txt_walletCharges = findViewById(R.id.txt_walletCharges);
        txt_discount = findViewById(R.id.txt_discount);
        txt_subTotal = findViewById(R.id.txt_subTotal);
        txt_grandTotal = findViewById(R.id.txt_grandTotal);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getOrderList();
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

    public void getOrderList() {
        dialog.show();
        if (APIURLs.isNetwork(OrderTrackingActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.order_summary, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            order_id1 = jsonObject1.getString("order_id");
                            p_mode = jsonObject1.getString("p_mode");
                            item = jsonObject1.getString("item");
                            order_status = jsonObject1.getString("order_status");
                            deliver_address = jsonObject1.getString("deliver_address");
                            total_bag = jsonObject1.getString("total_bag");
                            bag_discount = jsonObject1.getString("bag_discount");
                            order_total = jsonObject1.getString("order_total");
                            coupon_value = jsonObject1.getString("coupon_value");
                            day = jsonObject1.getString("day");
                            slot_timing = jsonObject1.getString("slot_timing");
                            wallet_use = jsonObject1.getString("wallet_use");

                            if(Integer.parseInt(order_status)<=1) {
                                txt_cancelOrder.setVisibility(View.VISIBLE);
                            }else{
                                txt_cancelOrder.setVisibility(View.GONE);
                            }
                            setData();

                        } else {
                            Toast.makeText(OrderTrackingActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("oid", order_id);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(OrderTrackingActivity.this, "no internet connection");
        }
    }

    public void showProductItems(View view) {
        Intent intent = new Intent(OrderTrackingActivity.this, OrderItemsActivity.class);
        intent.putExtra("oid", order_id);
        intent.putExtra("order_id", order_id1);
        startActivity(intent);
    }

    public void setData() {
        txt_orderId.setText("ORDER #" + order_id1);
        if (order_status.equals("0")) {
            setOrdered();
        } else if (order_status.equals("1")) {
            setOrderedApproved();
        } else if (order_status.equals("2")) {
            setOrderedApproved();
            setOrderedDispatch();
        } else if (order_status.equals("3")) {
            setOrderedApproved();
            setOrderedDispatch();
            setOrderedDelivered();
        } else if (order_status.equals("4")) {
            setOrderedApproved();
            setOrderedDispatch();
            setOrderedCancelled();
        }

        txt_shippingAddress.setText(deliver_address);
        if (p_mode.equals("1")) {
            txt_paidBy.setText("Pay Online");
        } else if (p_mode.equals("0")) {
            txt_paidBy.setText("Pay By Cash");
        }else if (p_mode.equals("3")) {
            txt_paidBy.setText("Pay By Wallet");
        }

        txt_unitPrice.setText("Rs."+total_bag);

        if (bag_discount.equals("0")) {
            lnr_discount.setVisibility(View.GONE);
        } else {
            lnr_discount.setVisibility(View.VISIBLE);
            txt_discount.setText("Rs -" + bag_discount);
        }
        if (wallet_use.equals("0")) {
            lnr_walletBalance.setVisibility(View.GONE);
        } else {
            lnr_walletBalance.setVisibility(View.VISIBLE);
            txt_walletCharges.setText("Rs -" + wallet_use);
        }

        if (coupon_value.equals("0")) {
            lnr_coupon.setVisibility(View.GONE);
        } else {
            lnr_coupon.setVisibility(View.VISIBLE);
            txt_couponCharges.setText("Rs -"+coupon_value);
        }

        txt_subTotal.setText("Rs."+order_total);
        txt_grandTotal.setText("Rs."+order_total);
    }

    private void setOrdered() {
        txt_orderAccept.setText("Ordered");
        rel_accept_internal.setBackground(getResources().getDrawable(R.drawable.green_circle_background_layout));
        txt_orderAcceptMessage.setText("Your Order #" + order_id1 + " is Ordered.");
        txt_orderDispatchMessage.setVisibility(View.GONE);
        txt_orderDeliverMessage.setVisibility(View.GONE);
        txt_oderDispatch.setText("Order Dispatch");
        txt_orderDelivered.setText("Order Delivered");
    }

    private void setOrderedApproved() {
        txt_orderAccept.setText("Order Accepted");
        rel_accept_internal.setBackground(getResources().getDrawable(R.drawable.green_circle_background_layout));
        txt_orderAcceptMessage.setText("Your Order #" + order_id1 + " is Accepted.");
        txt_orderDispatchMessage.setVisibility(View.GONE);
        txt_orderDeliverMessage.setVisibility(View.GONE);
        txt_oderDispatch.setText("Order Dispatch");
        txt_orderDelivered.setText("Order Delivered");
        view_orderAccept.setBackgroundColor(getResources().getColor(R.color.colorGreen));
    }

    private void setOrderedDispatch() {
        txt_oderDispatch.setTextColor(getResources().getColor(R.color.colorGreen));
        txt_oderDispatch.setText("Order Dispatch");
        txt_orderDispatchMessage.setVisibility(View.VISIBLE);
        view_orderAccept.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        view_dispatch.setBackgroundColor(getResources().getColor(R.color.colorGreen));
        rel_dispatch_internal.setBackground(getResources().getDrawable(R.drawable.green_circle_background_layout));
        txt_orderDispatchMessage.setText("Your Order #" + order_id1 + " is Dispatch.\nYour order is deliver by " + day + " in between " + slot_timing +".");
    }

    private void setOrderedDelivered() {
        txt_orderDelivered.setTextColor(getResources().getColor(R.color.colorGreen));
        txt_orderDelivered.setText("Order Delivered");
        txt_orderDeliverMessage.setVisibility(View.VISIBLE);
        rel_orderDelivered_internal.setBackground(getResources().getDrawable(R.drawable.green_circle_background_layout));
        txt_orderDeliverMessage.setText("Your Order #" + order_id1 + " is Delivered.");
    }

    private void setOrderedCancelled() {
        txt_orderDelivered.setTextColor(getResources().getColor(R.color.colorGreen));
        txt_orderDelivered.setText("Order Cancelled");
        txt_orderDeliverMessage.setVisibility(View.VISIBLE);
        rel_orderDelivered_internal.setBackground(getResources().getDrawable(R.drawable.green_circle_background_layout));
        txt_orderDeliverMessage.setText("Your Order #" + order_id1 + " is Cancelled.");
    }

}
