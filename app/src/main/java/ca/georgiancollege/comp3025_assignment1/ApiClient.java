package ca.georgiancollege.comp3025_assignment1;

import okhttp3.*;

public class ApiClient {
    
    private static final OkHttpClient client = new OkHttpClient();
    private static final MediaType JSON = MediaType.get("application/json; charset=utf-8");
    
    public static void get(String url, Callback callback) {
        Request request = new Request.Builder()
                .url(url)
                .build();
                
        client.newCall(request).enqueue(callback);
    }
    
    public static void post(String url, String json, Callback callback) {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
                
        client.newCall(request).enqueue(callback);
    }
} 