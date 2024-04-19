package com.example.quocduy.tab_layout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.quocduy.Cart;
import com.example.quocduy.DetailActivity;
import com.example.quocduy.R;
import com.example.quocduy.ApiCaller;
import com.example.quocduy.User;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ShopFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ApiCaller apiCaller;
    FlexboxLayout flexboxLayout;
    LinearLayout LinearPopular;
    JSONObject userObject;
    private String userName;
    private TextView categoryAll;

    public ShopFragment() {
    }

    public static ShopFragment newInstance(String param1, String param2) {
        ShopFragment fragment = new ShopFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shop, container, false);


        Bundle bundle = getArguments();
        if (bundle != null) {
            String userString = bundle.getString("userObject");
            try {
                userObject = new JSONObject(userString);
                userName = userObject.getString("username");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Đang tải dữ liệu vui lòng đợi !");
        progressDialog.show();
        new CountDownTimer(2000, 1000) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();

        apiCaller = ApiCaller.getInstance(getContext());
        flexboxLayout = view.findViewById(R.id.flexboxLayout);
        TextView txtWelcome = view.findViewById(R.id.txtWelcome);
        txtWelcome.setText("Xin chào! " + userName);
        apiCaller.makeStringRequest(apiCaller.url + "/slideShows", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArrayData = apiCaller.stringToJsonArray(response);
                if (jsonArrayData != null) {
                    addBanner(view, jsonArrayData);
                }
            }

            @Override
            public void onError(String errorMessage) {
            }
        });
        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArrayData = apiCaller.stringToJsonArray(response);
                if (jsonArrayData != null) {
                    try {
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            addItemProduct(view, inflater, flexboxLayout, jsonArrayData.getJSONObject(i));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Chuỗi JSON không hợp lệ.");
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
            }
        });

        apiCaller.makeStringRequest(apiCaller.url + "/carts/user/" + User.getId(), new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCart = jsonArray.getJSONObject(i);
                        Cart.setId(jsonObjectCart.getInt("id"));
                        Log.d("cartId: ", String.valueOf(Cart.getId()));
                    }
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }

            @Override
            public void onError(String errorMessage) {
                System.out.print(errorMessage);
            }
        });

        return view;
    }

    private void addItemProduct(View view, LayoutInflater inflater, FlexboxLayout flexboxLayout, JSONObject jsonObject) {
        View item = inflater.inflate(R.layout.item_product_shop, flexboxLayout, false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        TextView textDescriptionProduct = item.findViewById(R.id.txtDescriptionProduct); // TextView mô tả sản phẩm
        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        LinearLayout touchLinear = item.findViewById(R.id.itemContainer);
        String imageProduct = "";
        try {
            textName.setText(jsonObject.getString("title").toString());

            // Lấy mô tả sản phẩm từ dữ liệu JSON
            String description = jsonObject.getString("description");
            textDescriptionProduct.setText(description);

            // Lấy giá sản phẩm từ dữ liệu JSON
            double price = jsonObject.getDouble("price");
            // Format giá theo kiểu số thập phân hoặc số nguyên và thêm đơn vị VNĐ
            String formattedPrice = String.format("%,.0f VNĐ", price); // Định dạng kiểu số nguyên
            // String formattedPrice = String.format("%,.2f VNĐ", price); // Định dạng kiểu số thập phân với 2 chữ số sau dấu phẩy
            textPrice.setText(formattedPrice);

            imageProduct = jsonObject.getString("photo").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        touchLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("ItemProduct", jsonObject.toString());
                startActivity(intent);
            }
        });
        Picasso.get()
                .load(apiCaller.url + "/image/products/" + imageProduct)
                .placeholder(R.drawable.load)
                .error(R.drawable.error_200)
                .into(imgProduct);
        flexboxLayout.addView(item);
    }



    private void addBanner(View view, JSONArray jsonArray) {
        ViewFlipper viewFlipper = view.findViewById(R.id.bannerView);
        for (int i = 0; i < jsonArray.length(); i++) {
            ImageView imageView = new ImageView(getContext());
            try {
                Picasso.get().load(apiCaller.url + "/image/slideShows/" + jsonArray.getJSONObject(i).getString("photo")).into(imageView);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            viewFlipper.addView(imageView);
        }
        viewFlipper.setFlipInterval(1500);
        viewFlipper.setAutoStart(true);
    }
}
