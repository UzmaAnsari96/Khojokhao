package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.khojokhao.R;

public class HelpAndSupportActivity extends AppCompatActivity {

    ImageView img_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help_and_support);
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
        img_back = findViewById(R.id.img_back);
    }

    public void call(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel: 6003040736"));
        startActivity(intent);
    }

    public void email(View view) {
        /*try {
            String emailtext = "Hello, my name is " + SharedPref.getVal(ComplaintsActivity.this,SharedPref.customer_name) +
                    " and contact number " + SharedPref.getVal(ComplaintsActivity.this,SharedPref.mobile_no);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:" + "support@sevenseasbazaar.com"));
            intent.putExtra(Intent.EXTRA_SUBJECT, "SevenSeasBazaar app complaint");
            intent.putExtra(Intent.EXTRA_TEXT, emailtext);
            intent.setPackage("com.google.android.gm");
            startActivity(Intent.createChooser(intent, "Send mail"));
        } catch (ActivityNotFoundException e) {
            //TODO smth
        }*/
//        String emailtext = "Hello, my name is " + SharedPref.getVal(ComplaintsActivity.this,SharedPref.customer_name) +
//                " and contact number " + SharedPref.getVal(ComplaintsActivity.this,SharedPref.mobile_no) +". My complaint is ";

        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"support@khojokhao.com"});
//        i.putExtra(Intent.EXTRA_SUBJECT, "SevenSeasBazaar app complaint");
//        i.putExtra(Intent.EXTRA_TEXT   , emailtext);
        i.setPackage("com.google.android.gm");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(HelpAndSupportActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

}
