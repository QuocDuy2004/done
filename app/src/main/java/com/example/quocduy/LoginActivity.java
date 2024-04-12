package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.quocduy.ApiCaller;
import com.example.quocduy.User;

import org.json.JSONArray;
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
                                        Toast.makeText(getApplicationContext(),
                                                "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
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

                                        Toast.makeText(getApplicationContext(),
                                                "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onError(String errorMessage) {

                                Toast.makeText(getApplicationContext(), "Người dùng không tồn tại !" + errorMessage, Toast.LENGTH_SHORT).show();
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
        editor.commit();
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
}