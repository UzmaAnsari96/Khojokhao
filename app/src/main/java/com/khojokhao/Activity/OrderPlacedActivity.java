package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.khojokhao.R;

public class OrderPlacedActivity extends AppCompatActivity {

    TextView txt_message;
    String OrderId="";
    TextView txt_cong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_placed);
        init();
        OrderId = getIntent().getStringExtra("OrderId");

        if(OrderId.equals("")){
            txt_cong.setText("Thank You !! your order has been placed successfully.");
        }else {
            txt_cong.setText("Thank You !! your order has been placed successfully.");
            txt_message.setText("Order id is #" + OrderId);
        }
    }

    private void init() {
        txt_message = findViewById(R.id.txt_message);
        txt_cong = findViewById(R.id.txt_cong);
    }

    public void goToHome(View view) {

        startActivity(new Intent(OrderPlacedActivity.this, HomeActivity.class));
        finish();

    }
}
