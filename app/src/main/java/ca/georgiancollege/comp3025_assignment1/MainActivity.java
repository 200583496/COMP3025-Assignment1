package ca.georgiancollege.comp3025_assignment1;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;

 import okhttp3.Call;
 import okhttp3.Callback;
 import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // test API connection
        fetchMovies("batman");
    }

    private void fetchMovies(String searchQuery) {
        String apiUrl = "https://www.omdbapi.com/?apikey=f9e0fd04&s=" + searchQuery;
        
        ApiClient.get(apiUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "Failed to fetch movies: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();
                    
                    runOnUiThread(() -> {
                        Log.i("tag", "Movies data: " + responseData);
                        try {
                            parseMovieData(responseData);
                        } catch (Exception e) {
                            Log.e("tag", "Error parsing movie data: " + e.getMessage());
                        }
                    });
                }
            }
        });
    }

    private void parseMovieData(String responseData) {
        Log.i("tag", "Parsing movie data: " + responseData);
    }
}