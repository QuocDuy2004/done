package com.example.quocduy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class AdminActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    private int selectedTabPosition = 0;

    private static boolean checkLogin = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Kiểm tra nếu người dùng chưa đăng nhập, chuyển hướng đến LoginActivity
        if (checkLogin == false) {
            checkLogin = true;
            startActivity(new Intent(AdminActivity.this, LoginAdminActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_admin);


    }

    private void updateTabBackground() {
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                if (i == selectedTabPosition) {
                    Drawable icon = tab.getIcon();
                    if (icon != null) {
                        icon.setColorFilter(getResources().getColor(R.color.black), PorterDuff.Mode.SRC_IN);
                        tab.setIcon(icon);
                    }
                } else {
                    Drawable icon = tab.getIcon();
                    if (icon != null) {
                        icon.setColorFilter(null);
                        tab.setIcon(icon);
                    }
                }
            }
        }
    }

    // Phương thức để kiểm tra xem người dùng đã đăng nhập chưa
    private boolean isLoggedIn() {
        // Viết mã kiểm tra đăng nhập ở đây, ví dụ:
        // return SessionManager.isLoggedIn(); // SessionManager là một lớp quản lý trạng thái đăng nhập
        // Hoặc kiểm tra một biến trạng thái đăng nhập nào đó đã được thiết lập hay chưa
        return false; // Trả về false mặc định, bạn cần cập nhật logic này dựa trên cách bạn xác định trạng thái đăng nhập
    }
}
