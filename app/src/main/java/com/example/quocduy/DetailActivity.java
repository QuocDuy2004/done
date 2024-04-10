package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.quocduy.R;
import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    private Button addtocart;
    private TextView nameproduct, price, description,score;
    private ImageView picfood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initView();
        getBundle();
        ImageView buttonhome = findViewById(R.id.button_home);

        buttonhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                intent.putExtra("fragmentToLoad", "home");
                startActivity(intent);
                finish();
            }
        });
    }

    private void getBundle() {
        Intent intent = getIntent();
        if (intent != null) {
            // Lấy dữ liệu của sản phẩm từ Intent
            String productString = intent.getStringExtra("product");
            if (productString != null) {
                try {
                    JSONObject productObject = new JSONObject(productString);
                    // Hiển thị thông tin của sản phẩm trên giao diện
                    String name = productObject.getString("title");
                    String priceString = productObject.getString("price");
                    String descriptionString = productObject.getString("description");
                    // Tiếp tục lấy các thông tin khác nếu cần

                    // Hiển thị thông tin lên các TextView
                    nameproduct.setText(name);
                    price.setText(priceString); // Đây là giá trị của giá sản phẩm
                    description.setText(descriptionString);
                    // Tiếp tục hiển thị các thông tin khác nếu cần
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void initView(){
        addtocart = findViewById(R.id.add);
        price = findViewById(R.id.price);
        nameproduct = findViewById(R.id.nameProduct);
        description = findViewById(R.id.description);
        picfood = findViewById(R.id.itempic);
        score = findViewById(R.id.score);
    }
}
