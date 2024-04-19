package com.example.quocduy.tab_layout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.quocduy.ApiCaller;
import com.example.quocduy.R;
import com.example.quocduy.User;

public class EditProfileActivity extends AppCompatActivity {

    private EditText txtOldPassword, txtNewPassword, txtConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtNewPassword = findViewById(R.id.txtNewPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        Button btnChangePassword = findViewById(R.id.btnChangePassword);


        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPassword = txtOldPassword.getText().toString();
                String newPassword = txtNewPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();

                if (TextUtils.isEmpty(oldPassword) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
                    Toast.makeText(EditProfileActivity.this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!newPassword.equals(confirmPassword)) {
                    Toast.makeText(EditProfileActivity.this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Gửi yêu cầu đổi mật khẩu đến máy chủ
                changePassword(oldPassword, newPassword);
            }
        });
    }

    private void changePassword(String oldPassword, String newPassword) {
        ApiCaller apiCaller = ApiCaller.getInstance(this);
        apiCaller.changePassword(User.getId(), oldPassword, newPassword, new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                Toast.makeText(EditProfileActivity.this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                finish();
            }

            @Override
            public void onError(String errorMessage) {
                Toast.makeText(EditProfileActivity.this, "Đổi mật khẩu thất bại: " + errorMessage, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
