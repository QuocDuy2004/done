package com.example.quocduy;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.example.quocduy.ApiCaller;
import com.example.quocduy.Cart;
import com.example.quocduy.DataHolder;
import com.example.quocduy.User;
import com.example.quocduy.tab_layout.HistoryFragment;

import org.json.JSONException;
import org.json.JSONObject;

public class DetailActivity extends AppCompatActivity {
    //    JSONObject itemProductData;
//    ApiCaller apiCaller;
//    String receivedData;
//    int productPrice = 0;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_detail);
//        ImageView btnGoBack = findViewById(R.id.btnGoBack);
//        HorizontalNumberPicker np_channel_nr =
//                findViewById(R.id.horizontalNumberPicker);
//        ImageView imgProduct = findViewById(R.id.imageProduct);
//        TextView txtNameProduct = findViewById(R.id.txtNameProduct);
//        TextView txtProductDescription =
//                findViewById(R.id.txtDescription);
//        TextView txtPriceProduct = findViewById(R.id.txtPriceProduct);
//        LinearLayout btnAddCart = findViewById(R.id.btnaddcart);
//        np_channel_nr.setMax(10);
//        np_channel_nr.setMin(1);
//        apiCaller = ApiCaller.getInstance(getBaseContext());
//// Lấy Intent từ Activity
//        Intent intent = getIntent();
//// Nhận dữ liệu từ Intent
//        if (intent != null) {
//            try {
//                receivedData = intent.getStringExtra("ItemProduct");
//                itemProductData = new JSONObject(receivedData);
//                productPrice = itemProductData.getInt("price");
//                txtPriceProduct.setText(itemProductData.getString("price") + " k");
//                txtNameProduct.setText(itemProductData.getString("title"));
//                txtProductDescription.setText(itemProductData.getString("description"));
//                itemProductData.put("quantity",
//                        np_channel_nr.getValue());
//                itemProductData.put("priceTotal",
//                        productPrice * np_channel_nr.getValue());
//                Picasso.get()
//                        .load(apiCaller.url + "/image/products/" + itemProductData.getString("photo")
//                        )
//                        .placeholder(R.drawable.load)
//                        .error(R.drawable.error_200)
//                        .into(imgProduct);
//            } catch (Error e) {
//                Log.d("Error!", e.toString());
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//        }
//        np_channel_nr.setOnValueChangeListener(new
//                                                       HorizontalNumberPicker.OnValueChangeListener() {
//                                                           @Override
//                                                           public void onValueChange(int newValue) {
//                                                               int priceTotal = productPrice * np_channel_nr.getValue();
//                                                               try {
//                                                                   itemProductData.put("quantity",
//                                                                           np_channel_nr.getValue());
//                                                                   itemProductData.put("priceTotal", priceTotal);
//                                                               } catch (JSONException e) {
//                                                                   throw new RuntimeException(e);
//                                                               }
//                                                               txtPriceProduct.setText(String.valueOf(priceTotal) + " k");
//                                                           }
//                                                       });
//        btnGoBack.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                finish();
//            }
//        });
//        btnAddCart.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(), "Đã thêm vào giỏ hàng !", Toast.LENGTH_SHORT).show();
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//
//                    public void run() {
//                        if (Cart.getId() != 0) {
//                            try {
//                                apiCaller.addCartDetail(Cart.getId(),
//                                        itemProductData.getInt("id"), itemProductData.getInt("quantity"), new
//                                                ApiCaller.ApiResponseListener<JSONObject>() {
//                                                    @Override
//
//                                                    public void onSuccess(JSONObject
//
//                                                                                  response) {
//                                                        Log.d("aaaaaaccccccc", response.toString());
//                                                    }
//
//                                                    @Override
//                                                    public void onError(String
//
//                                                                                errorMessage) {
//                                                    }
//                                                });
//                            } catch (JSONException e) {
//                                throw new RuntimeException(e);
//                            }
//                        } else {
//
//                            apiCaller.addCart(User.getId(), new
//                                    ApiCaller.ApiResponseListener<JSONObject>() {
//                                        @Override
//
//                                        public void onSuccess(JSONObject
//
//                                                                      response) {
//                                            try {
//                                                Cart.setId(response.getInt("id"));
//                                                apiCaller.addCartDetail(Cart.getId(), itemProductData.getInt("id"),
//                                                        itemProductData.getInt("quantity"), new
//                                                                ApiCaller.ApiResponseListener<JSONObject>() {
//                                                                    @Override
//                                                                    public void
//
//                                                                    onSuccess(JSONObject response) {
//                                                                        Log.d("aaaaaaccccccc",
//                                                                                response.toString());
//                                                                    }
//
//                                                                    @Override
//                                                                    public void onError(String
//
//                                                                                                errorMessage) {
//                                                                    }
//                                                                });
//                                            } catch (JSONException e) {
//                                                throw new RuntimeException(e);
//                                            }
//                                        }
//
//                                        @Override
//                                        public void onError(String errorMessage) {
//                                        }
//                                    });
//                        }
//                        finish();
//
//                    }
//                }, 1000);
//            }
//        });
//    }
}