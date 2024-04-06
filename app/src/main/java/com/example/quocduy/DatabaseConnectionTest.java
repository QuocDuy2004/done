package com.example.quocduy;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

public class DatabaseConnectionTest {

    private static final String BASE_URL = "http://192.168.1.7/api/";
    private static final String TEST_ENDPOINT = "testConnection";

    public void testDatabaseConnection(Context context) {
        String testUrl = BASE_URL + TEST_ENDPOINT;

        // Tạo một StringRequest để kiểm tra kết nối
        StringRequest stringRequest = new StringRequest(Request.Method.GET, testUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Nếu nhận được phản hồi, kết nối thành công
                        Log.d("Database Connection", "Connected successfully!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Nếu xảy ra lỗi, in ra lỗi để debug
                Log.e("Database Connection", "Error: " + error.getMessage());
            }
        });

        // Thêm request vào hàng đợi của Volley
        ApiCaller.getInstance(context).addToRequestQueue(stringRequest);
        DatabaseConnectionTest databaseConnectionTest = new DatabaseConnectionTest();
        databaseConnectionTest.testDatabaseConnection(context.getApplicationContext());
    }
}
