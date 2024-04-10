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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.example.quocduy.ApiCaller;
import com.example.quocduy.DetailActivity;
import com.example.quocduy.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    /**
     * A simple {@link Fragment} subclass.
     * Use the {@link HomeFragment#newInstance} factory method to
     * create an instance of this fragment.
     */

    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private ApiCaller apiCaller;
    LinearLayout linearLayout;
    LinearLayout LinearPopular;
    JSONObject userObject;
    private String userName;

    public HomeFragment() {
// Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
// TODO: Rename and change types and number of parameters
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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container,

                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container,
                false);
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
        apiCaller = ApiCaller.getInstance(getContext());
        linearLayout = view.findViewById(R.id.hcontainer);
//            LinearPopular=view.findViewById(R.id.LinearPopular);
        LinearLayout categoryViewContainer =
                view.findViewById(R.id.linearlayout);
        TextView txtWelcome = view.findViewById(R.id.txtWelcome);
        txtWelcome.setText("Xin chào! " + userName);
        apiCaller.makeStringRequest(apiCaller.url + "/slideShows", new
                ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {

                        JSONArray jsonArrayData =
                                apiCaller.stringToJsonArray(response);
// System.out.println(response);
// Kiểm tra và in ra các phần tử trong mảng JSONArray
                        if (jsonArrayData != null) {
                            addBanner(view, jsonArrayData);
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
        apiCaller.makeStringRequest(apiCaller.url + "/products", new
                ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
// Xử lý dữ liệu response ở đây
                        JSONArray jsonArrayData =
                                apiCaller.stringToJsonArray(response);
// System.out.println(response);
// Kiểm tra và in ra các phần tử trong mảng JSONArray
                        if (jsonArrayData != null) {
                            try {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    addItemProduct(view, inflater, linearLayout,
                                            jsonArrayData.getJSONObject(i));
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
// Xử lý lỗi nếu có
                        Log.e("Error", errorMessage);
                    }
                });
        apiCaller.makeStringRequest(apiCaller.url + "/categories", new
                ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        JSONArray jsonArrayData =
                                apiCaller.stringToJsonArray(response);
// System.out.println(response);
// Kiểm tra và in ra các phần tử trong mảng JSONArray
                        if (jsonArrayData != null) {
                            try {
                                for (int i = 0; i < jsonArrayData.length(); i++) {
                                    addItemCategory(view, inflater,
                                            categoryViewContainer, jsonArrayData.getJSONObject(i));
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
// Xử lý lỗi nếu có
                        Log.e("Error", errorMessage);
                    }
                });
        return view;
    }

    private void addItemProduct(View view, LayoutInflater inflater,
                                LinearLayout linearLayout, JSONObject jsonObject) {
        View item = inflater.inflate(R.layout.item_product_home,
                linearLayout, false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        //
        LinearLayout touchLinear = item.findViewById(R.id.itemContainer);
        String imageProduct = "";
        try {
            textName.setText(jsonObject.getString("title").toString());
            textPrice.setText(jsonObject.getString("price").toString());
            imageProduct = jsonObject.getString("photo").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }


        touchLinear.setOnClickListener(new View.OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               Intent intent = new Intent(getActivity(), DetailActivity.class);
                                               intent.putExtra("key", jsonObject.toString());
                                               getActivity().startActivity(intent);
                                               Bundle bundle = new Bundle();
                                               bundle.putString("key", "Đây là String");
                                               setArguments(bundle);
                                           }

                                       }
        );

        Picasso.get()
                .load(apiCaller.url + "/image/products/" + imageProduct)
                .placeholder(R.drawable.load)
                .error(R.drawable.error_200)
                .into(imgProduct);
        linearLayout.addView(item);
    }

    private void addItemCategory(View view, LayoutInflater inflater,
                                 LinearLayout categoryContainer, JSONObject jsonObject) {
        View item = inflater.inflate(R.layout.item_category_home,
                categoryContainer, false);
        TextView textName = item.findViewById(R.id.txtNameCategory);
        ImageView imgProduct = item.findViewById(R.id.imageCategory);
// LinearLayout touchLinear =item.findViewById(R.id.itemContainer);
        String imageCategory = "";
        try {
            textName.setText(jsonObject.getString("title").toString());
            imageCategory = jsonObject.getString("photo").toString();
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Picasso.get()
                .load(apiCaller.url + "/image/categories/" + imageCategory)
                .placeholder(R.drawable.load)
                .error(R.drawable.error_200)
                .into(imgProduct);
        categoryContainer.addView(item);
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