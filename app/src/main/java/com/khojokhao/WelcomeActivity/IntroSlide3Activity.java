package com.khojokhao.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khojokhao.Activity.LoginActivity;
import com.khojokhao.R;

public class IntroSlide3Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slide3);
    }

    public void slide2(View view){
        startActivity(new Intent(IntroSlide3Activity.this,IntroSlide2Activity.class));
        finish();
    }

    public void gotit(View view){
        startActivity(new Intent(IntroSlide3Activity.this, LoginActivity.class));
        finish();
    }
}
