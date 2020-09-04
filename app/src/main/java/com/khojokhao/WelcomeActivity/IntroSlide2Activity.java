package com.khojokhao.WelcomeActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.khojokhao.R;

public class IntroSlide2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_slide2);
    }

    public void slide3(View view){
        startActivity(new Intent(IntroSlide2Activity.this,IntroSlide3Activity.class));
        finish();
    }

    public void slide1(View view){
        startActivity(new Intent(IntroSlide2Activity.this,IntroSlide1Activity.class));
        finish();
    }
}
