package ca.georgiancollege.comp3025_assignment1;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import ca.georgiancollege.comp3025_assignment1.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    MovieViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);
        
        viewModel.getMovieData().observe(this, movieList -> {
            Log.i("tag", "Update View");
            updateMovieList(movieList);
        });
        
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String movieName = binding.searchEditText.getText().toString();
                if (movieName.isEmpty()) {
                    return;
                }
                viewModel.searchMovies(movieName);
            }
        });
    }

    private void updateMovieList(List<Movie> movieList) {
        for (Movie movie : movieList) {
            Log.i("tag", "Movie in list: " + movie.getTitle() + " (" + movie.getYear() + ")");
        }
        Log.i("tag", "Total movies: " + movieList.size());
    }
}