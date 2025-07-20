package ca.georgiancollege.comp3025_assignment1;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.annotation.NonNull;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieViewModel extends ViewModel {
    
    private MutableLiveData<List<Movie>> movieData;
    
    public MovieViewModel() {
        movieData = new MutableLiveData<>();
        movieData.setValue(new ArrayList<>());
    }
    
    public MutableLiveData<List<Movie>> getMovieData() {
        return movieData;
    }
    
    public void searchMovies(String searchQuery) {
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
                    
                    Log.i("tag", "Movies data: " + responseData);
                    parseMovieData(responseData);
                }
            }
        });
    }
    
    private void parseMovieData(String responseData) {
        try {
            JSONObject json = new JSONObject(responseData);
            
            if (json.getString("Response").equals("True")) {
                JSONArray searchResults = json.getJSONArray("Search");
                List<Movie> movieList = new ArrayList<>();
                
                for (int i = 0; i < searchResults.length(); i++) {
                    JSONObject movieJson = searchResults.getJSONObject(i);
                    
                    Movie movie = new Movie();
                    movie.setTitle(movieJson.getString("Title"));
                    movie.setYear(movieJson.getString("Year"));
                    movie.setImdbID(movieJson.getString("imdbID"));
                    movie.setType(movieJson.getString("Type"));
                    movie.setPoster(movieJson.getString("Poster"));
                    
                    movieList.add(movie);
                    
                    Log.i("tag", "Movie: " + movie.getTitle() + " (" + movie.getYear() + ")");
                }
                
                movieData.postValue(movieList);
                Log.i("tag", "Found " + movieList.size() + " movies");
            } else {
                movieData.postValue(new ArrayList<>());
                Log.i("tag", "No movies found");
            }
            
        } catch (JSONException e) {
            Log.e("tag", "JSON parsing error: " + e.getMessage());
        }
    }
} 