package com.example.quocduy;

import androidx.annotation.GravityInt;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.Toolbar;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

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
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tablayout);
        ViewPager2 pager2 = findViewById(R.id.viewPager2);

        TablayoutAdapter adapter = new TablayoutAdapter(MainActivity.this);
        pager2.setAdapter(adapter);

        pager2.setUserInputEnabled(false);

        Bundle extras = getIntent().getExtras();
        if(extras != null){
            if(extras.containsKey("erObject")){
                String userObject =extras.getString("userObject");
                adapter.setMyString(userObject);
            }
        }

        new TabLayoutMediator(tabLayout, pager2, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position) {
                    case 0:
                        tab.setIcon(R.drawable.eye);
                        tab.setText("Home");
                        break;
                    case 1:
                        tab.setIcon(R.drawable.eye);
                        tab.setText("Shop");
                        break;
                    case 2:
                        tab.setIcon(R.drawable.eye);
                        tab.setText("Chat");
                        break;

                    default:
                        tab.setIcon(R.drawable.eye);
                        tab.setText("Home");
                        break;
                }
            }
        }).attach();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                selectedTabPosition = tab.getPosition();
                updateTabBackground();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                // Không cần làm gì khi tab không được chọn
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Không cần làm gì khi tab được chọn lại
            }
        });
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
