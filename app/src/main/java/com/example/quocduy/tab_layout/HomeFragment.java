package com.example.quocduy.tab_layout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.fragment.app.Fragment;

import com.example.quocduy.ApiCaller;
import com.example.quocduy.DetailActivity;
import com.example.quocduy.R;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {
//
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//    private ApiCaller apiCaller;
//    FlexboxLayout flexboxLayout;
//    JSONObject userObject;
//    private String mParam1;
//    private String mParam2;
//    private String url;
//    private static final int REQUEST_CODE_PICK_IMAGE = 1;
//
//    private String userName;
//
//    public HomeFragment() {
//    }
//
//    // Factory method to create a new instance of HomeFragment
//    public static HomeFragment newInstance(String param1, String param2) {
//        HomeFragment fragment = new HomeFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view = inflater.inflate(R.layout.fragment_home, container, false);
//
//        // Initialize ApiCaller
//        apiCaller = ApiCaller.getInstance(getContext());
//
//        // Make API request for slideShows
//        apiCaller.makeStringRequest(apiCaller.url + "/slideShows", new ApiCaller.ApiResponseListener<String>() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    addBanner(view, jsonArray);
//                } catch (JSONException e) {
//                    Log.e("Error", "JSONException: " + e.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("Error", errorMessage);
//            }
//        });
//
//        // Make API request for products
//        apiCaller.makeStringRequest(apiCaller.url + "/products", new ApiCaller.ApiResponseListener<String>() {
//            @Override
//            public void onSuccess(String response) {
//                try {
//                    JSONArray jsonArray = new JSONArray(response);
//                    FlexboxLayout flexboxLayout = view.findViewById(R.id.flexContainer);
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        addItemProduct(view, inflater, flexboxLayout, jsonArray.getJSONObject(i));
//                    }
//                } catch (JSONException e) {
//                    Log.e("Error", "JSONException: " + e.getMessage());
//                }
//            }
//
//            @Override
//            public void onError(String errorMessage) {
//                Log.e("Error", errorMessage);
//            }
//        });
//
//        return view;
//    }
//
//    private void addItemProduct(View view, LayoutInflater inflater, FlexboxLayout flexboxLayout, JSONObject jsonObject) {
//        View item = inflater.inflate(R.layout.item_product_home, flexboxLayout, false);
//        TextView textName = item.findViewById(R.id.txtNameProduct);
//        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
//        ImageView imgProduct = item.findViewById(R.id.imageProduct);
//        LinearLayout touchLinear = item.findViewById(R.id.itemContainer);
//        String imageProduct = "";
//        try {
//            textName.setText(jsonObject.getString("title"));
//            textPrice.setText(jsonObject.getString("price"));
//            imageProduct = jsonObject.getString("photo");
//        } catch (JSONException e) {
//            throw new RuntimeException(e);
//        }
//
//        touchLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getActivity(), DetailActivity.class);
//                intent.putExtra("key", jsonObject.toString());
//                getActivity().startActivity(intent);
//                Bundle bundle = new Bundle();
//                bundle.putString("key", "Đây là String");
//                setArguments(bundle);
//            }
//        });
//        Picasso.get()
//                .load(apiCaller.url + "/image/products/" + imageProduct)
//                .placeholder(R.drawable.downloading_200)
//                .error(R.drawable.eye)
//                .into(imgProduct);
//        flexboxLayout.addView(item);
//    }
//
//    private void addBanner(View view, JSONArray jsonArray) {
//        ViewFlipper viewFlipper = view.findViewById(R.id.bannerView);
//        for (int i = 0; i < jsonArray.length(); i++) {
//            ImageView imageView = new ImageView(getContext());
//            try {
//                Picasso.get().load(apiCaller.url + "/image/slideShows" + jsonArray.getJSONObject(i).getString("photo")).into(imageView);
//            } catch (JSONException e) {
//                throw new RuntimeException(e);
//            }
//            viewFlipper.addView(imageView);
//        }
//        viewFlipper.setFlipInterval(1500);
//        viewFlipper.setAutoStart(true);
//    }
}
