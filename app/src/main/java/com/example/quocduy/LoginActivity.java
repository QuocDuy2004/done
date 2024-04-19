package com.example.quocduy;

import static com.android.volley.VolleyLog.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.quocduy.ApiCaller;
import com.example.quocduy.User;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.json.JSONException;
import org.json.JSONObject;
import org.mindrot.jbcrypt.BCrypt;

public class LoginActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ImageView loginGG = findViewById(R.id.btnGoogleSignIn);

        // Cấu hình Google Sign-In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken("1083016507594-8m9tb3obkk7s8s14a9ndad8vs6d2cs56.apps.googleusercontent.com") // Thay YOUR_WEB_CLIENT_ID bằng ID Client mới
                .requestEmail()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Đăng nhập bằng Google khi click vào nút đăng nhập bằng Google
        loginGG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        // Các bước xử lý khi người dùng click vào nút Đăng nhập
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
                startActivity(new Intent(LoginActivity.this, ForgotActivity.class));
            }
        });

        // Xử lý đăng nhập thông thường
        btnDN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Kiểm tra các trường dữ liệu đầu vào
                if (TextUtils.isEmpty(txtUserName.getText().toString())) {
                    txtUserName.setError("Vui lòng nhập tài khoản");
                    return;
                }

                if (TextUtils.isEmpty(txtPassword.getText().toString())) {
                    txtPassword.setError("Vui lòng nhập mật khẩu");
                    return;
                }

                // Gọi API để kiểm tra đăng nhập
                apiCaller.makeStringRequest(apiCaller.url +
                        "/users/name/" + txtUserName.getText().toString(), new
                        ApiCaller.ApiResponseListener<String>() {
                            @Override
                            public void onSuccess(String response) {
                                try {
                                    JSONObject jsonObject = new JSONObject(response);
                                    User.setId(jsonObject.getInt("id"));
                                    // Kiểm tra mật khẩu đã mã hóa với mật khẩu người dùng nhập vào
                                    if (BCrypt.checkpw(txtPassword.getText().toString(), jsonObject.getString("pass"))) {
                                        User.setNamewelcome(jsonObject.getString("username"));
                                        // Hiển thị thông báo đăng nhập thành công
                                        showToast("Đăng nhập thành công");
                                        if (checkboxRemember.isChecked()) {
                                            setUser(LoginActivity.this,
                                                    txtUserName.getText().toString().trim(),
                                                    txtPassword.getText().toString().trim(), checkboxRemember.isChecked());
                                        } else {
                                            setUser(LoginActivity.this, "", "",
                                                    checkboxRemember.isChecked());
                                        }

                                        // Chuyển sang màn hình chính
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        intent.putExtra("userObject", response);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        // Thông báo mật khẩu không chính xác
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

        // Chuyển sang màn hình đăng ký khi người dùng click vào nút Đăng ký
        txtRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    // Đăng nhập bằng Google
    private void signInWithGoogle() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }

    // Xử lý kết quả trả về từ việc đăng nhập bằng Google
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                // Lấy thông tin tài khoản Google
                String email = account.getEmail();
                String displayName = account.getDisplayName();
                // Tiếp tục xử lý đăng nhập hoặc hiển thị UI đăng nhập thành công
                showToast("Đăng nhập bằng Google thành công");

                // Gửi ID token đến máy chủ của bạn để xác thực
                String idToken = account.getIdToken();
                // Gọi API để xác thực ID token và xử lý đăng nhập
                // Gọi hàm để xử lý đăng nhập
                handleGoogleSignIn(idToken);
            } catch (ApiException e) {
                // Xử lý lỗi khi đăng nhập bằng Google không thành công
                Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
                Toast.makeText(this, "Đăng nhập bằng Google thành công", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
            }
        }
    }
    private void handleGoogleSignIn(String idToken) {
        // Gọi API của ứng dụng của bạn để xác thực thông tin từ tài khoản Google
        // Sau đó xử lý đăng nhập trên máy chủ
        // Ví dụ:
        ApiCaller.getInstance(this).authenticateWithGoogle(idToken, new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                // Xác thực thành công, xử lý đăng nhập
                // Chuyển người dùng đến màn hình chính
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                // Xác thực không thành công, hiển thị thông báo lỗi hoặc xử lý khác
                showToast(errorMessage);
            }
        });
    }


    // Lưu thông tin người dùng đã đăng nhập
    public void setUser(Context context, String username, String password, boolean rememberMe) {
        SharedPreferences prefs = context.getSharedPreferences("myUserPackage", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("username", username);
        editor.putString("password", password);
        editor.putBoolean("remember", rememberMe);
        editor.apply();
    }

    // Lấy tên người dùng đã lưu
    public String getUsername(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("myUserPackage", 0);
        return prefs.getString("username", "");
    }

    // Lấy mật khẩu người dùng đã lưu
    public String getPassword(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("myUserPackage", 0);
        return prefs.getString("password", "");
    }

    // Kiểm tra trạng thái "Nhớ tài khoản"
    public boolean getRememberMe(Context context) {
        SharedPreferences prefs = context.getSharedPreferences("myUserPackage", 0);
        return prefs.getBoolean("remember", false);
    }

    // Hiển thị thông báo
    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
