package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.droidsonroids.gif.GifImageView;

public class OrderActivity extends AppCompatActivity {
    String receivedData;
    static boolean addSuccess = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

        // Ánh xạ GifImageView từ layout
        GifImageView gifImageView = findViewById(R.id.ordess);

        // Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        if (intent != null) {
            receivedData = intent.getStringExtra("ListItemCart");
        }

        // Thực hiện thêm đơn hàng
        addOrder();
    }

    // Phương thức thêm đơn hàng
    void addOrder() {
        ApiCaller apiCaller = ApiCaller.getInstance(getBaseContext());
        Order order = new Order(User.getId());
        apiCaller.addOrder(order, new ApiCaller.ApiResponseListener<JSONObject>() {
            @Override
            public void onSuccess(JSONObject response) {
                try {
                    Order.setId(response.getInt("id"));
                    JSONArray jsonArray = new JSONArray(receivedData);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        int quantity = jsonArray.getJSONObject(i).getInt("quantity");
                        JSONObject jsonProductObject = jsonArray.getJSONObject(i).getJSONObject("product");
                        int productId = jsonProductObject.getInt("id");
                        OrderDetail orderDetail = new OrderDetail(quantity, Order.getId(), productId);

                        // Thêm chi tiết đơn hàng
                        apiCaller.addOrderDetail(orderDetail, new ApiCaller.ApiResponseListener<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject response) {
                                // Xử lý khi thêm chi tiết đơn hàng thành công
                            }

                            @Override
                            public void onError(String errorMessage) {
                                // Xử lý khi có lỗi khi thêm chi tiết đơn hàng
                            }
                        });
                    }

                    // Xóa giỏ hàng sau khi thêm đơn hàng
                    apiCaller.deleteCart(Cart.getId(), new ApiCaller.ApiResponseListener<String>() {
                        @Override
                        public void onSuccess(String response) {
                            Cart.setId(0);
                            // Đánh dấu thành công khi xóa giỏ hàng
                            addSuccess = true;
                            // Sử dụng Handler để chờ 1 giây trước khi chuyển đến OrderSuccessActivity
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    startActivity(new Intent(OrderActivity.this, OrderSuccessActivity.class));
                                    finish();
                                }
                            }, 1000); // Chờ 1 giây trước khi chuyển đến OrderSuccessActivity
                        }

                        @Override
                        public void onError(String errorMessage) {
                            // Xử lý khi có lỗi khi xóa giỏ hàng
                        }
                    });
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                // Xử lý khi có lỗi khi thêm đơn hàng
            }
        });
    }
}
