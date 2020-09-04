package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.khojokhao.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    EditText edt_mobile;
    Button btn_process;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        init();
        onClick();
    }

    private void init() {
        edt_mobile = findViewById(R.id.edt_mobile);
        btn_process = findViewById(R.id.btn_process);
    }

    public void onClick(){
        btn_process.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_mobile.getText().toString().equals("")) {
                    edt_mobile.setError("Please enter mobile number");
                    edt_mobile.requestFocus();
                } else if (edt_mobile.getText().length() != 10) {
                    edt_mobile.setError("Please valid enter mobile number");
                    edt_mobile.requestFocus();
                } else{
                    startActivity(new Intent(ForgotPasswordActivity.this,OtpActivity.class));
                }
            }
        });
    }

    public void ErrorToast(String message) {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.error_toast_layout,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        TextView txt_message = (TextView) layout.findViewById(R.id.txt_message);
        txt_message.setText(message);

        Toast toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.BOTTOM, 0, 150);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
