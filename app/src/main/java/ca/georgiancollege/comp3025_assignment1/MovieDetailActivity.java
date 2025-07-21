package ca.georgiancollege.comp3025_assignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private Button backButton;
    private ImageView posterImageView;
    private TextView titleTextView;
    private TextView yearTextView;
    private TextView ratingTextView;
    private TextView directorTextView;
    private TextView actorsTextView;
    private TextView genreTextView;
    private TextView runtimeTextView;
    private TextView plotTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        backButton = findViewById(R.id.backButton);
        posterImageView = findViewById(R.id.detailPosterImageView);
        titleTextView = findViewById(R.id.detailTitleTextView);
        yearTextView = findViewById(R.id.detailYearTextView);
        ratingTextView = findViewById(R.id.detailRatingTextView);
        directorTextView = findViewById(R.id.detailDirectorTextView);
        actorsTextView = findViewById(R.id.detailActorsTextView);
        genreTextView = findViewById(R.id.detailGenreTextView);
        runtimeTextView = findViewById(R.id.detailRuntimeTextView);
        plotTextView = findViewById(R.id.detailPlotTextView);

        // Set up back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        String imdbID = getIntent().getStringExtra("imdbID");
        fetchMovieDetails(imdbID);
    }

    private void fetchMovieDetails(String imdbID) {
        String apiUrl = "https://www.omdbapi.com/?apikey=f9e0fd04&i=" + imdbID;

        ApiClient.get(apiUrl, new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.e("tag", "Failed to fetch movie details: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    assert response.body() != null;
                    String responseData = response.body().string();

                    runOnUiThread(() -> {
                        Log.i("tag", "Movie details: " + responseData);
                        parseMovieDetails(responseData);
                    });
                }
            }
        });
    }

    private void parseMovieDetails(String responseData) {
        try {
            JSONObject json = new JSONObject(responseData);

            String title = json.getString("Title");
            String year = json.getString("Year");
            String poster = json.getString("Poster");
            String rating = json.getString("imdbRating");
            String director = json.getString("Director");
            String actors = json.getString("Actors");
            String genre = json.getString("Genre");
            String runtime = json.getString("Runtime");
            String plot = json.getString("Plot");

            titleTextView.setText(title);
            yearTextView.setText(year);
            Picasso.get().load(poster).into(posterImageView);
            ratingTextView.setText("IMDB Rating: " + rating + "/10");
            directorTextView.setText("Director: " + director);
            actorsTextView.setText("Actors: " + actors);
            genreTextView.setText("Genre: " + genre);
            runtimeTextView.setText("Runtime: " + runtime);
            plotTextView.setText("Plot: " + plot);

        } catch (JSONException e) {
            Log.e("tag", "JSON parsing error: " + e.getMessage());
        }
    }
} 