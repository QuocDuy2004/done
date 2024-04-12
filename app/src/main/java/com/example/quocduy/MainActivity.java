package com.example.quocduy;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TabLayout tabLayout = findViewById(R.id.tablayout);
        ViewPager2 pager2 = findViewById(R.id.viewPager2);
        TablayoutAdapter adapter = new TablayoutAdapter(this);
        pager2.setAdapter(adapter);
        pager2.setUserInputEnabled(false);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            if (extras.containsKey("userObject")) {
                String userObject = extras.getString("userObject");
                adapter.setMyString(userObject);
            }
        }
        new TabLayoutMediator(tabLayout, pager2, new
                TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int
                            position) {
                        switch (position){
                            case 0:
                                tab.setIcon(R.drawable.home);
// tab.setText("Home");
                                break;
                            case 1:
                                tab.setIcon(R.drawable.shop);
// tab.setText("History");
                                break;
                            case 2:
                                tab.setIcon(R.drawable.chat);
// tab.setText("Account");
                                break;
                        }
                    }
                }).attach();
    }
}