package com.example.quocduy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class ForgotActivity extends AppCompatActivity {

    private EditText txtUser, txtEmail;
    private Button btnForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        // Ánh xạ các thành phần trong layout
        txtUser = findViewById(R.id.txtUser);
        txtEmail = findViewById(R.id.txtEmail);
        btnForgot = findViewById(R.id.btnForgot);

        // Thiết lập sự kiện click cho nút btnForgot
        btnForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lấy thông tin tài khoản và email từ EditText
                String username = txtUser.getText().toString();
                String email = txtEmail.getText().toString();

                // Kiểm tra xem các trường đã được nhập hay chưa
                if (TextUtils.isEmpty(username)) {
                    txtUser.setError("Vui lòng nhập tài khoản");
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    txtEmail.setError("Vui lòng nhập email");
                    return;
                }

                // Gửi thông báo đến bot Telegram
                sendTelegramMessage(username, email);
            }
        });
    }

    // Phương thức để gửi thông báo đến bot Telegram
    private void sendTelegramMessage(String username, String email) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Tạo URL để gửi thông báo đến bot Telegram
                    String urlString = "https://api.telegram.org/bot6242350588:AAGR6P2io4olZWgUIbit4Owb1Pf0Ef2vARM/sendMessage?chat_id=2116412887&text=Tài khoản: " + URLEncoder.encode(username, "UTF-8") + "%0AEmail: " + URLEncoder.encode(email, "UTF-8") + "%0A yêu cầu khôi phục.";

                    // Tạo URL từ urlString
                    URL url = new URL(urlString);

                    // Mở kết nối HTTP
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Thiết lập phương thức request là GET
                    connection.setRequestMethod("GET");

                    // Lấy response code
                    int responseCode = connection.getResponseCode();

                    // Đóng kết nối
                    connection.disconnect();

                    // Cập nhật UI trên luồng UI chính
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Hiển thị thông báo khôi phục thành công
                            Toast.makeText(ForgotActivity.this, "Gửi yêu cầu khôi phục thành công", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                    // Xử lý lỗi nếu có
                }
            }
        }).start();
    }
}
