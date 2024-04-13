package com.example.quocduy.tab_layout;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.example.quocduy.Cart;
import com.example.quocduy.ProfileActivity;
import com.example.quocduy.User;
import com.google.android.flexbox.FlexboxLayout;
import com.squareup.picasso.Picasso;
import com.example.quocduy.DetailActivity;
import com.example.quocduy.R;
import com.example.quocduy.ApiCaller;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
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
    private TextView categoryAll;

    public HomeFragment() {
// Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of* this fragment using the provided parameters.
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

        ImageView imageView = view.findViewById(R.id.picproavfile);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                startActivity(intent);
                Bundle bundle = new Bundle();
                bundle.putString("key", "Đây là String");
                setArguments(bundle);
            }
        });

        // Tìm TextView có id là allproduct
        TextView allProductTextView = view.findViewById(R.id.allproduct);
// Đặt sự kiện lắng nghe click cho TextView allproduct
        allProductTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi tạo fragment ShopFragment
                Fragment shopFragment = new ShopFragment();
                // Khởi tạo transaction để thực hiện thay thế fragment hiện tại bằng ShopFragment
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                // Thực hiện thay thế fragment hiện tại bằng ShopFragment
                transaction.replace(R.id.allproduct, shopFragment);
                // Thêm fragment hiện tại vào ngăn xếp trở lại, để có thể quay lại fragment trước đó khi ấn nút back
                transaction.addToBackStack(null);
                // Commit transaction
                transaction.commit();
            }
        });

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
        progressDialog.setMessage("Đăng tải!");
        progressDialog.show();
        new CountDownTimer(2000, 1000) {
            // Thời gian còn lại được cập nhật sau mỗi 1 giây (1000milliseconds)
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                progressDialog.dismiss();
            }
        }.start();


        apiCaller = ApiCaller.getInstance(getContext());
        linearLayout = view.findViewById(R.id.hcontainer);
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

//        categoryAll.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                linearLayout.removeAllViews();
//                if (jsonArrayDataProduct != null) {
//                    try {
//                        for (int i = 0; i <
//                                jsonArrayDataProduct.length(); i++) {
//                            addItemProduct( inflater, linearLayout,
//                                    jsonArrayDataProduct.getJSONObject(i));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    System.out.println("Chuỗi JSON không hợp lệ.");
//                }
//            }
//        });
        apiCaller.makeStringRequest(apiCaller.url + "/carts/user/" +
                User.getId(), new ApiCaller.ApiResponseListener<String>() {
            @Override
            public void onSuccess(String response) {
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(response);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObjectCart =
                                jsonArray.getJSONObject(i);
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

    private void addItemProduct(View view, LayoutInflater inflater,
                                LinearLayout linearLayout, JSONObject jsonObject) {
        View item = inflater.inflate(R.layout.item_product_home,
                linearLayout, false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        TextView textPrice = item.findViewById(R.id.txtPriceProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
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
        });
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