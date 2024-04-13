package com.example.quocduy;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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
        // Tạo URL để gửi thông báo đến bot Telegram
        String urlString = "https://api.telegram.org/bot6242350588:AAGR6P2io4olZWgUIbit4Owb1Pf0Ef2vARM/sendMessage?chat_id=2116412887&text=Tài khoản: " + username + ", Email: " + email + " yêu cầu khôi phục.";

        // Thực hiện yêu cầu HTTP để gửi thông báo
        // Đây là phần bạn cần thực hiện một request HTTP để gửi thông báo đến bot Telegram,
        // sử dụng urlString đã được tạo ở trên.
        // Bạn có thể sử dụng thư viện Retrofit, Volley hoặc HttpURLConnection để thực hiện yêu cầu này.
        // Dưới đây là một ví dụ sử dụng HttpURLConnection:

        // Cập nhật UI
        Toast.makeText(this, "Gửi yêu cầu khôi phục thành công", Toast.LENGTH_SHORT).show();

        /*
        try {
            // Tạo URL từ urlString
            URL url = new URL(urlString);

            // Mở kết nối HTTP
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // Thiết lập phương thức request là GET
            connection.setRequestMethod("GET");

            // Lấy response code
            int responseCode = connection.getResponseCode();

            // Đọc response
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // Đóng kết nối
            connection.disconnect();

            // Xử lý response (nếu cần)

            // Cập nhật UI
            Toast.makeText(this, "Gửi yêu cầu khôi phục thành công", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            // Xử lý lỗi (nếu cần)
        }
        */
    }
}
