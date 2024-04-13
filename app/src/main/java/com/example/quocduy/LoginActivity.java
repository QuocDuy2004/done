package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quocduy.ApiCaller;
import com.example.quocduy.User;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Button btnDN = findViewById(R.id.btnDangnhap);
        TextView txtRegister = findViewById(R.id.reg);
        EditText txtUserName = findViewById(R.id.txtUser);
        EditText txtPassword = findViewById(R.id.txtPassWord);
        CheckBox checkboxRemember = findViewById(R.id.remember);
        TextView txtForgot = findViewById(R.id.txtforgot);
        ApiCaller apiCaller = ApiCaller.getInstance(this);

        checkboxRemember.setChecked(getRememberMe(LoginActivity.this));
        if (checkboxRemember.isChecked()) {
            txtUserName.setText(getUsername(LoginActivity.this));
            txtPassword.setText(getPassword(LoginActivity.this));
        }
        if (getUsername(LoginActivity.this).equals("")) {
            checkboxRemember.setChecked(false);
            txtUserName.setText("");
            txtPassword.setText("");
        }
        txtForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new
                        Intent(LoginActivity.this, ForgotActivity.class));
            }
        });
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                EditText txtPassWord = findViewById(R.id.txtPassWord);
//                ImageView showPassword = findViewById(R.id.showPassword);
//
//                // Biến để theo dõi trạng thái hiển thị mật khẩu
//                final boolean[] isPasswordVisible = {false};
//
//                // Sự kiện click vào ImageView "showPassword"
//                showPassword.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        // Đảo ngược trạng thái hiển thị mật khẩu
//                        isPasswordVisible[0] = !isPasswordVisible[0];
//
//                        // Thay đổi inputType của EditText tương ứng
//                        if (isPasswordVisible[0]) {
//                            txtPassWord.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
//                            showPassword.setImageResource(R.drawable.eye_off); // Đổi icon để chỉ trạng thái ẩn mật khẩu
//                        } else {
//                            txtPassWord.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
//                            showPassword.setImageResource(R.drawable.eye); // Đổi icon để chỉ trạng thái hiển thị mật khẩu
//                        }
//                    }
//                });


                // Kiểm tra xem các trường đã được nhập hay chưa
                if (TextUtils.isEmpty(txtUserName.getText().toString())) {
                    txtUserName.setError("Vui lòng nhập tài khoản");
                    return;
                }

                if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    txtPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                apiCaller.makeStringRequest(apiCaller.url +
                        "/users/name/" + txtUserName.getText().toString(), new
                        ApiCaller.ApiResponseListener<String>() {
                            @Override

                            public void onSuccess(String response) {

                                try {
                                    JSONObject jsonObject = new
                                            JSONObject(response);
                                    User.setId(jsonObject.getInt("id"));
                                    if
                                    (
                                            BCrypt.checkpw(txtPassword.getText().toString(), jsonObject.getString("pass"))) {
                                        User.setNamewelcome(jsonObject.getString("username"));
                                        // Hiển thị thông báo tài khoản đúng
                                        showToast("Đăng nhập thành công");
                                        if (checkboxRemember.isChecked()) {
                                            setUser(LoginActivity.this,
                                                    txtUserName.getText().toString().trim(),
                                                    txtPassword.getText().toString().trim(), checkboxRemember.isChecked());
                                        } else {

                                            setUser(LoginActivity.this, "", "",
                                                    checkboxRemember.isChecked());
                                        }

                                        Intent intent = new
                                                Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("userObject", response);

                                        startActivity(intent);
                                        finish();

                                    } else {
                                        // Thông báo sai mật khẩu
                                        txtPassword.setError("Sai mật khẩu");
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {
                                // Thông báo người dùng không tồn tại
                                txtUserName.setError("Người dùng không tồn tại");
                            }
                        });
            }
        });

        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    public void setUser(Context context, String username, String
            password, boolean rememberMe) {
        SharedPreferences prefs =
                context.getSharedPreferences("myUserPackage", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("remember", rememberMe);
        editor.apply();
    }

    public String getUsername(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences("myUserPackage", 0);
        return prefs.getString("username", "");
    }

    public String getPassword(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences("myUserPackage", 0);
        return prefs.getString("password", "");
    }

    public boolean getRememberMe(Context context) {
        SharedPreferences prefs =
                context.getSharedPreferences("myUserPackage", 0);
        return prefs.getBoolean("remember", false);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
