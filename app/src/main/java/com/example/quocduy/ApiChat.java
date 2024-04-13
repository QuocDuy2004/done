package com.example.quocduy;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ApiChat {
    private RequestQueue requestQueue;
    private static ApiChat instance;
    private static Context ctx;

    private ApiChat(Context context) {
        ctx = context.getApplicationContext();
        requestQueue = getRequestQueue();
    }

    public static synchronized ApiChat getInstance(Context context) {
        if (instance == null) {
            instance = new ApiChat(context);
        }
        return instance;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(ctx);
        }
        return requestQueue;
    }

    public void makeStringRequest(String url, final ApiResponseListener<String> listener) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listener.onSuccess(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
        addToRequestQueue(stringRequest);
    }

    public void makeJsonArrayRequest(String url, final ApiResponseListener<String> listener) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                listener.onSuccess(String.valueOf(response));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
        addToRequestQueue(jsonArrayRequest);
    }

    public interface ApiResponseListener<T> {
        void onSuccess(T response);

        void onError(String errorMessage);
    }

    public JSONArray stringToJsonArray(String jsonString) {
        JSONArray jsonArray = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            jsonArray = jsonObject.getJSONArray("content");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonArray;
    }

    public void addChatMessage(String username, String chat, String photo, final ApiResponseListener<String> listener) {
        String addChatUrl = "YOUR_API_ENDPOINT_FOR_ADDING_CHAT_MESSAGES"; // Thay thế YOUR_API_ENDPOINT_FOR_ADDING_CHAT_MESSAGES bằng đường dẫn API thích hợp
        JSONObject chatJson = new JSONObject();
        try {
            chatJson.put("username", username);
            chatJson.put("chat", chat);
            chatJson.put("photo", photo);
        } catch (JSONException e) {
            e.printStackTrace();
            listener.onError(e.getMessage());
            return;
        }
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, addChatUrl, chatJson, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                listener.onSuccess(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                listener.onError(error.getMessage());
            }
        });
        addToRequestQueue(jsonObjectRequest);
    }

    private void addToRequestQueue(Request<?> req) {
        getRequestQueue().add(req);
    }
}
