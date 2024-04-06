package com.example.quocduy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.mindrot.jbcrypt.BCrypt;

public class LoginAdminActivity extends AppCompatActivity {
    private EditText emailedit, passedit;
    private Button btnlogin;
    private TextView txtTTK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_admin);

        emailedit = findViewById(R.id.txtEmail);
        passedit = findViewById(R.id.txtPassWord);
        btnlogin = findViewById(R.id.btnDangnhap);
        txtTTK = findViewById(R.id.txtTTK);

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        txtTTK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginAdminActivity.this, RegisterAdminActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {
        String email = emailedit.getText().toString();
        String pass = passedit.getText().toString();

        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
            Toast.makeText(this, "Vui lòng nhập Email và mật khẩu!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Thực hiện truy vấn thông tin người dùng từ database Firebase dựa trên email
        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference().child("users");
        usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        // Lấy vai trò của người dùng từ database
                        String roles = snapshot.child("roles").getValue(String.class);

                        // Kiểm tra nếu vai trò là admin
                        if (roles != null && roles.equals("admin")) {
                            // Lấy mật khẩu đã được mã hóa từ database
                            String hashedPassword = snapshot.child("password").getValue(String.class);

                            // So sánh mật khẩu đã nhập với mật khẩu đã được mã hóa từ database
                            if (BCrypt.checkpw(pass, hashedPassword)) {
                                // Mật khẩu khớp, đăng nhập thành công
                                Toast.makeText(LoginAdminActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginAdminActivity.this, AdminActivity.class);
                                startActivity(intent);
                                finish(); // Kết thúc LoginActivity sau khi đăng nhập thành công
                                return;
                            } else {
                                // Nếu mật khẩu không khớp
                                Toast.makeText(LoginAdminActivity.this, "Mật khẩu không đúng!", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    }
                    // Nếu không tìm thấy vai trò là admin
                    Toast.makeText(LoginAdminActivity.this, "Bạn không có quyền truy cập!", Toast.LENGTH_SHORT).show();
                } else {
                    // Người dùng không tồn tại
                    Toast.makeText(LoginAdminActivity.this, "Người dùng không tồn tại! Vui lòng đăng ký tài khoản.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Xử lý lỗi
                Toast.makeText(LoginAdminActivity.this, "Đã xảy ra lỗi khi đăng nhập! Vui lòng thử lại sau.", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
