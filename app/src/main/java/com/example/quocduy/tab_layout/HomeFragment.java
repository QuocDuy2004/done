package com.example.quocduy.tab_layout;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.quocduy.ApiCaller;
import com.example.quocduy.R;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

public class HomeFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ApiCaller apiCaller;
    private FlexboxLayout flexboxLayout;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiCaller = ApiCaller.getInstance(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        flexboxLayout = view.findViewById(R.id.flexboxLayout);
        fetchProducts();
        return view;
    }

    private void fetchProducts() {
        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArrayData = apiCaller.stringToJsonArray(response);
                if (jsonArrayData != null) {
                    try {
                        for (int i = 0; i < jsonArrayData.length(); i++) {
                            String nameProduct = jsonArrayData.getJSONObject(i).getString("title");
                            String priceProduct = jsonArrayData.getJSONObject(i).getString("price");
                            String imageProduct = jsonArrayData.getJSONObject(i).getString("photo");
                            addItemProduct(nameProduct, priceProduct, imageProduct);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.e("Error", "JSON không hợp lệ");
                }
            }

            @Override
            public void onError(String errorMessage) {
                Log.e("Error", errorMessage);
                // Xử lý khi gọi API không thành công
            }
        });
    }

    private void addItemProduct(String name, String price, String img) {
        View item = LayoutInflater.from(getContext()).inflate(R.layout.item_product_cardview, flexboxLayout);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        textName.setText(name);
        textPrice.setText(price);

        Picasso.get()
                .load(apiCaller.url + "/image/product/" + img)
                .placeholder(R.drawable.downloading_200)
                .error(R.drawable.error_200)
                .into(imgProduct);

        flexboxLayout.addView(item);
    }
}
