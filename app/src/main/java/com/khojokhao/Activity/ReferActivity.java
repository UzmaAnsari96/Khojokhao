package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.khojokhao.BuildConfig;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

public class ReferActivity extends AppCompatActivity {

    ImageView img_back;
    Button btn_refer;
    LinearLayout lnr_copy;
    TextView txt_referralCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        init();
        onClick();
        String code = SharedPref.getVal(ReferActivity.this, SharedPref.referral_code);
        txt_referralCode.setText(SharedPref.getVal(ReferActivity.this, SharedPref.referral_code));
    }

    private void onClick() {
        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        lnr_copy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setClipboard(ReferActivity.this, txt_referralCode.getText().toString());
            }
        });

        btn_refer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "My application name");
                    String shareMessage = "\nLet me recommend you this application \n\nEnter my referral code to get rewards " +
                            SharedPref.getVal(ReferActivity.this, SharedPref.referral_code) + "\n\n";
                    shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID;
/*
                    + "&referrer=" +
                            SharedPref.getVal(ReferActivity.this, SharedPref.referral_code) + "\n\n"*/
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "choose one"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void init() {
        img_back = findViewById(R.id.img_back);
        btn_refer = findViewById(R.id.btn_refer);
        lnr_copy = findViewById(R.id.lnr_copy);
        txt_referralCode = findViewById(R.id.txt_referralCode);
    }

    private void setClipboard(Context context, String text) {
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.HONEYCOMB) {
            android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            clipboard.setText(text);
            Toast.makeText(context, "Copied referral code", Toast.LENGTH_SHORT).show();
        } else {
            android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            android.content.ClipData clip = android.content.ClipData.newPlainText("Copied Text", text);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied referral code", Toast.LENGTH_SHORT).show();
        }
    }

}
