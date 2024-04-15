package com.example.quocduy.tab_layout;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.example.quocduy.DetailActivity;
import com.example.quocduy.OrderActivity;
import com.example.quocduy.R;
import com.example.quocduy.ApiCaller;
import com.example.quocduy.Cart;
import com.example.quocduy.DataHolder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    LinearLayout cartContainerGlobal;
    LayoutInflater inflaterGlobal;
    ApiCaller apiCaller;
    View viewContainer;
    JSONArray jsonArrayCart;
    int priceTotalNumber = 0;

    public HistoryFragment() {
// Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HisFragment.
     */

    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
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
    public void onResume() {
        super.onResume();
        apiCaller.makeStringRequest(apiCaller.url +
                "/cartDetails/cart/" + Cart.getId(), new
                ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        try {
                            jsonArrayCart = new JSONArray(response);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                        cartContainerGlobal.removeAllViews();
                        priceTotalNumber = 0;
                        for (int i = 0; i < jsonArrayCart.length(); i++) {
                            try {
                                int idItemCart =
                                        jsonArrayCart.getJSONObject(i).getInt("id");
                                Log.d("dataaaa",
                                        jsonArrayCart.getJSONObject(i).toString());
                                Log.d("dataaaaId", String.valueOf(idItemCart));
                                JSONObject itemProductData = new
                                        JSONObject(jsonArrayCart.getJSONObject(i).getString("product"));
                                int quantity =
                                        jsonArrayCart.getJSONObject(i).getInt("quantity");
                                int price = itemProductData.getInt("price");
                                itemProductData.put("quantity", quantity);
                                itemProductData.put("priceTotal", price *
                                        quantity);
                                addItemCart(inflaterGlobal, cartContainerGlobal,
                                        itemProductData.toString(), i, idItemCart);
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup
            container,
                             Bundle savedInstanceState) {
// Inflate the layout for this fragment
        viewContainer = inflater.inflate(R.layout.fragment_history,
                container, false);
        inflaterGlobal = inflater;
        cartContainerGlobal =
                viewContainer.findViewById(R.id.cartContainer);
        apiCaller = ApiCaller.getInstance(getContext());
        Button btnOrder = viewContainer.findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), OrderActivity.class);
                intent.putExtra("ListItemCart",
                        jsonArrayCart.toString());
                startActivity(intent);
                getActivity().finish();
            }
        });
        return viewContainer;
    }

    public void addItemCart(LayoutInflater inflater, LinearLayout
            cartContainer, String data, int index, int idItemCart) {
        int priceItem = 0;
        View item = inflater.inflate(R.layout.item_cart, cartContainer,
                false);
        TextView textName = item.findViewById(R.id.txtNameProduct);
        ImageView imgProduct = item.findViewById(R.id.imageProduct);
        TextView txtToal = item.findViewById(R.id.txtToal);
        TextView txtQuantity = item.findViewById(R.id.txtQuantity);
        ImageView deleteItemCart =
                item.findViewById(R.id.deleteItemCart);
        TextView priceTotal =
                viewContainer.findViewById(R.id.priceTotal);
        try {
            JSONObject itemProductData = new JSONObject(data);
            textName.setText(itemProductData.getString("title"));
            txtToal.setText(itemProductData.getString("priceTotal") + " k");
            txtQuantity.setText("Số lượng: "+itemProductData.getString("quantity"));
            priceItem = itemProductData.getInt("priceTotal");
            priceTotalNumber += priceItem;
            priceTotal.setText(String.valueOf(priceTotalNumber) + " k");
            Picasso.get()
                    .load(apiCaller.url + "/image/products/" + itemProductData.getString("photo")
                    )
                    .placeholder(R.drawable.downloading_200)
                    .error(R.drawable.error_200)
                    .into(imgProduct);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        int finalPriceItem = priceItem;
        int finalIdItemCart = idItemCart;
        deleteItemCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(item, cartContainer, priceTotal,
                        index, finalPriceItem, finalIdItemCart);
            }
        });
        cartContainer.addView(item);
    }

    public void removeItem(View item, LinearLayout cartContainer, TextView
            priceTotal, int index, int priceItem, int idItemCart) {
        Log.d("MyFragment", String.valueOf(index));
        apiCaller.deleteItemCart(idItemCart, new
                ApiCaller.ApiResponseListener<String>() {
                    @Override
                    public void onSuccess(String response) {
                        cartContainer.removeView(item);
                        priceTotalNumber -= priceItem;
                        priceTotal.setText(String.valueOf(priceTotalNumber) + " k");
                    }

                    @Override
                    public void onError(String errorMessage) {
                    }
                });
    }

    private void showConfirmationDialog(View item, LinearLayout
            cartContainer, TextView priceTotal, int index, int priceItem, int
                                                idItemCart) {
        AlertDialog.Builder builder = new
                AlertDialog.Builder(getActivity());
        builder.setTitle("Xác nhận xóa");
        builder.setMessage("Bạn có chắc chắn muốn xóa sản phẩm khỏi đơn hàng ? ");
        builder.setPositiveButton("OK", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(item, cartContainer, priceTotal, index, priceItem, idItemCart);
                    }
                });
        builder.setNegativeButton("Hủy", new
                DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Dismiss dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}