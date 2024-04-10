package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LandingPageActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);

        sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);

        TextView textView = findViewById(R.id.button_lets_go);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUserLoggedIn()) {
                    startMainActivity();
                } else {
                    startLoginActivity();
                }
            }
        });
    }

    private boolean isUserLoggedIn() {
        return sharedPreferences.getBoolean("loggedIn", false);
    }

    private void startMainActivity() {
        Intent intent = new Intent(LandingPageActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void startLoginActivity() {
        Intent intent = new Intent(LandingPageActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
