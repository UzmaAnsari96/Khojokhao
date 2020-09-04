package com.khojokhao.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.google.zxing.Result;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONObject;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QrCodeScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    ZXingScannerView mScannerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code_scanner);
        scan();
    }

    public void scan() {
        mScannerView = new ZXingScannerView(this);// Programmatically initialize the scanner view
        setContentView(mScannerView);

        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();         // Start camera

    }

   /* @Override
    public void onPause() {
        super.onPause();
        try {
            mScannerView.stopCamera();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Stop camera on pause
    }
*/
    @Override
    public void handleResult(Result rawResult) {
        // Do something with the result here

        String sendmob = rawResult.getText();
      /*  Intent intent = new Intent();
        intent.putExtra("referral_code","abc");
        setResult(Activity.RESULT_OK, intent);
        finish();*/

        if (sendmob.isEmpty() || TextUtils.isEmpty(sendmob)) {
            Toast.makeText(QrCodeScannerActivity.this, "error", Toast.LENGTH_SHORT).show();
        } else {
            try {
                mScannerView.stopCamera();
                mScannerView.stopCameraPreview();
                mScannerView.resumeCameraPreview(QrCodeScannerActivity.this);

                JSONObject jsonObject = new JSONObject(sendmob);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                String referral_code = jsonObject1.getString("refferal_code");
                Intent intent = new Intent();
                intent.putExtra("referral_code",referral_code);
                setResult(Activity.RESULT_OK, intent);
                finish();

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
