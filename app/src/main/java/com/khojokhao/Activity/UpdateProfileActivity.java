package com.khojokhao.Activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.material.snackbar.Snackbar;
import com.khojokhao.Constant.APIURLs;
import com.khojokhao.Constant.FunctionConstant;
import com.khojokhao.Constant.SharedPref;
import com.khojokhao.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class UpdateProfileActivity extends AppCompatActivity {

    ImageView img_profile, img_back;
    EditText edt_fname, edt_lname, edt_email, edt_mobile, edt_pincode;
    Button btn_update;
    TextView txt_dob;
    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;
    String[] PERMISSIONS = {Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.INTERNET,};
    int PERMISSION_ALL = 1;
    public static final int PERMISSIONS_MULTIPLE_REQUEST = 123;
    public static int CAMERA1 = 100;
    private File destination = null;
    private InputStream inputStreamImg;
    private String imgPath = null;
    Uri selectedImage;
    Bitmap bitmap = null;
    String get_imagee1 = "";
    int var = 0;
    String uid = "", imagee = "", typee = "";
    String encodedImage = "";
    IOSDialog dialog;
    String customer_id = "", customer_unique_id = "", first_name = "", last_name = "", mobile_no = "", email_id = "", pincode = "", photo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profile);
        init();
        onClick();
        getProfile();
    }

    private void onClick() {
        img_profile.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(UpdateProfileActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(UpdateProfileActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA1);
                } else {
                    var = 1;
                    checkPermission1();

                }
            }
        });

        img_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edt_fname.getText().toString().equals("")) {
                    edt_fname.setError("Please enter full name");
                    edt_fname.requestFocus();
                } /*else if (edt_lname.getText().toString().equals("")) {
                    edt_lname.setError("Please enter last name");
                    edt_lname.requestFocus();
                } else if (edt_email.getText().toString().equals("")) {
                    edt_email.setError("Please enter email id");
                    edt_email.requestFocus();
                } else if (!FunctionConstant.isValidEmail(edt_email.getText().toString())) {
                    edt_email.setError("Please enter valid email id");
                    edt_email.requestFocus();
                }*/ else if (edt_pincode.getText().toString().equals("")) {
                    edt_pincode.setError("Please enter pincode");
                } else if (edt_pincode.getText().toString().length() != 6) {
                    edt_pincode.setError("Please enter valid pincode");
                } else {
                    updateProfile();
                }
            }
        });
    }

    private void init() {
        dialog = new IOSDialog.Builder(UpdateProfileActivity.this)
                .setMessageContent("Please wait...")
                .setMessageColorRes(R.color.white)
                .setCancelable(false)
                .build();
        img_profile = findViewById(R.id.img_profile);
        img_back = findViewById(R.id.img_back);
        edt_fname = findViewById(R.id.edt_fname);
        edt_lname = findViewById(R.id.edt_lname);
        edt_email = findViewById(R.id.edt_email);
        edt_mobile = findViewById(R.id.edt_mobile);
        edt_pincode = findViewById(R.id.edt_pincode);
//        txt_dob = findViewById(R.id.txt_dob);
        btn_update = findViewById(R.id.btn_update);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void checkPermission1() {
        if (ContextCompat.checkSelfPermission(UpdateProfileActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                + ContextCompat.checkSelfPermission(UpdateProfileActivity.this,
                Manifest.permission.CAMERA)
                + ContextCompat.checkSelfPermission(UpdateProfileActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale
                    (UpdateProfileActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale
                            (UpdateProfileActivity.this, Manifest.permission.CAMERA) || ActivityCompat.shouldShowRequestPermissionRationale
                    (UpdateProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(UpdateProfileActivity.this.findViewById(android.R.id.content),
                        "Please Grant Permissions for read external storage",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View v) {
                                requestPermissions(
                                        new String[]{Manifest.permission
                                                .READ_EXTERNAL_STORAGE, Manifest.permission
                                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                                        PERMISSIONS_MULTIPLE_REQUEST);
                            }
                        }).show();
            } else {
                requestPermissions(
                        new String[]{Manifest.permission
                                .READ_EXTERNAL_STORAGE, Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSIONS_MULTIPLE_REQUEST);
            }
        } else {
            if (var == 1) {
                img_profile.setImageDrawable(getResources().getDrawable(R.drawable.avtar));
                selectImage();
            }
        }
    }

    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();

            if (1 == 1) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateProfileActivity.this);
                builder.setTitle("Select Option");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Take Photo")) {
                            dialog.dismiss();
                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(intent, PICK_IMAGE_CAMERA);
                        } else if (options[item].equals("Choose From Gallery")) {
                            dialog.dismiss();
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                        }
                    }
                });
                builder.show();
            } else
                ActivityCompat.requestPermissions(UpdateProfileActivity.this, PERMISSIONS, PERMISSION_ALL);
        } catch (Exception e) {
            Toast.makeText(UpdateProfileActivity.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        inputStreamImg = null;
        if (requestCode == PICK_IMAGE_CAMERA) {
            if (data == null) {
            } else {
                selectedImage = data.getData();
                try {

                    Uri selectedImage = data.getData();
                    bitmap = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);


                    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                    destination = new File(Environment.getExternalStorageDirectory() + "/" +
                            getString(R.string.app_name), "IMG_" + timeStamp + ".jpg");
                    FileOutputStream fo;
                    File storageDir = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES);
                    File image = File.createTempFile(
                            timeStamp,  // prefix
                            ".jpg",         // suffix
                            storageDir);
                    imgPath = "file:" + image.getAbsolutePath();

                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    imgPath = destination.getAbsolutePath();
                    try {
                        destination.createNewFile();
                        fo = new FileOutputStream(destination);
                        fo.write(bytes.toByteArray());
                        fo.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    imgPath = destination.getAbsolutePath();
                    if (var == 1) {
                        img_profile.setImageBitmap(bitmap);
                        byte[] imageBytes = bytes.toByteArray();
                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        get_imagee1 = encodedImage;
                        imagee = get_imagee1;
                        typee = "profile";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        } else if (requestCode == PICK_IMAGE_GALLERY) {

            if (data == null) {

            } else {
                selectedImage = data.getData();

                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                    imgPath = getRealPathFromURI(selectedImage);
                    destination = new File(imgPath.toString());
                    String encodedImage;
                    byte[] imageBytes = bytes.toByteArray();
                    encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                    get_imagee1 = encodedImage;

                    imgPath = destination.getAbsolutePath();
                    if (var == 1) {
                        img_profile.setImageBitmap(bitmap);
                        encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
                        get_imagee1 = encodedImage;
                        imagee = get_imagee1;
                        typee = "profile";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

    }

    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Audio.Media.DATA};
        Cursor cursor = managedQuery(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
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

    public void getProfile() {
        dialog.show();
        if (APIURLs.isNetwork(UpdateProfileActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.customer_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            JSONArray jsonArray = jsonObject.getJSONArray("result");
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            customer_id = jsonObject1.getString("customer_id");
                            customer_unique_id = jsonObject1.getString("customer_unique_id");
                            first_name = jsonObject1.getString("first_name");
                            last_name = jsonObject1.getString("last_name");
                            mobile_no = jsonObject1.getString("mobile_no");
                            email_id = jsonObject1.getString("email_id");
                            pincode = jsonObject1.getString("customer_pincode");
//                            photo = jsonObject1.getString("photo");

                            setData();

                        } else {
                            Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(UpdateProfileActivity.this, SharedPref.customer_id));
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(UpdateProfileActivity.this, "no internet connection");
        }
    }

    public void setData() {
        edt_fname.setText(first_name);
        edt_lname.setText(last_name);
        edt_mobile.setText(mobile_no);
        edt_email.setText(email_id);
        edt_pincode.setText(pincode);
//        Picasso.with(UpdateProfileActivity.this).load(photo).into(img_profile);
//        get_imagee1 = convertUrlToBase64(photo);
    }

    public String convertUrlToBase64(String url) {
        URL newurl;
        Bitmap bitmap;
        String base64 = "";
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            newurl = new URL(url);
            bitmap = BitmapFactory.decodeStream(newurl.openConnection().getInputStream());
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            base64 = Base64.encodeToString(outputStream.toByteArray(), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

    public void updateProfile() {
        dialog.show();
        if (APIURLs.isNetwork(UpdateProfileActivity.this)) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, APIURLs.update_profile, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("result");
                        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                        String status = jsonObject1.getString("status");
                        if (status.equals("1")) {
                            Toast.makeText(UpdateProfileActivity.this, jsonObject1.getString("msg"), Toast.LENGTH_SHORT).show();
                            /*getProfile();*/
                            startActivity(new Intent(UpdateProfileActivity.this, AccountActivity.class));
                            finish();
                        } else {
                            Toast.makeText(UpdateProfileActivity.this, "Error", Toast.LENGTH_SHORT).show();
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
                    params.put("customer_id", SharedPref.getVal(UpdateProfileActivity.this, SharedPref.customer_id));
                    params.put("first_name", edt_fname.getText().toString());
                    params.put("last_name", edt_lname.getText().toString());
                    params.put("mobile_no", edt_mobile.getText().toString());
                    params.put("email_id", edt_email.getText().toString());
                    params.put("customer_pincode", edt_pincode.getText().toString());
//                    params.put("photo", get_imagee1);
                    return params;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(this);
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            requestQueue.add(stringRequest);
        } else {
            dismissDialog();
            FunctionConstant.noInternetDialog(UpdateProfileActivity.this, "no internet connection");
        }
    }

}
