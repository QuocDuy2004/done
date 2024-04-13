package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Uri uriImg = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        TextView textView = findViewById(R.id.log);
        textView.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }));

        Button btnRegister = findViewById(R.id.btnRegister);
        EditText txtUser = findViewById(R.id.txtUser);
        EditText txtPass = findViewById(R.id.txtPassWord);
        EditText txtEmail = findViewById(R.id.txtEmail);
        EditText txtPhone = findViewById(R.id.txtPhone);
        ImageView imgUser = findViewById(R.id.imgUser);
        ApiCaller apiCaller = ApiCaller.getInstance(this);


        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtUser.getText().toString().trim().isEmpty()) {
                    txtUser.setError("Vui lòng nhập tên người dùng");
                } else if (txtPass.getText().toString().trim().isEmpty()) {
                    txtPass.setError("Vui lòng nhập mật khẩu");
                } else {
                    User newUser = new User(
                            txtUser.getText().toString(),
                            txtPass.getText().toString(),
                            txtEmail.getText().toString(),
                            txtPhone.getText().toString(),
                            imgUser.getImageMatrix().toString()
                    );

                    apiCaller.addUser(newUser, new ApiCaller.ApiResponseListener<User>() {
                        @Override
                        public void onSuccess(User response) {
                            Toast.makeText(getApplicationContext(), "Đăng ký tài khoản thành công", Toast.LENGTH_SHORT).show();
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                        }

                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(getApplicationContext(), "Đăng ký thất bại! Mã lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        imgUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Chọn Ảnh "), PICK_IMAGE_REQUEST);
            }
        });


    }


    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data) {
        super.onActivityResult (requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST  && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            uriImg = data.getData();
            ImageView imageView = findViewById(R.id.imgUser);
            imageView.setImageURI(uriImg);
        }
    }

}