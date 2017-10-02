package edureka.devajyoti.com.tmdb.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.Locale;

import edureka.devajyoti.com.tmdb.R;
import edureka.devajyoti.com.tmdb.model.Credits;
import edureka.devajyoti.com.tmdb.model.MovieDetail;
import edureka.devajyoti.com.tmdb.rest.ApiClient;
import edureka.devajyoti.com.tmdb.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "55957fcf3ba81b137f8fc01ac5a31fb5";
    private android.support.v7.app.ActionBar ab;
    private ImageView movie_poster_iv;
    private TextView movie_title_tv;
    private TextView movie_overview_tv;
    private TextView movie_rating_tv;
    private TextView movie_tagline_tv;
    private TextView movie_budget_tv;
    private TextView movie_release_date_tv;
    private TextView movie_revenue_tv;
    private TextView movie_runtime_tv;
    private TextView movie_genre_tv;
    private TextView movie_language_tv;
    private TextView movie_production_company_tv;
    private TextView movie_cast_tv;
    private TextView movie_crew_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        ab = getSupportActionBar();
        assert ab != null;
        ab.setSubtitle("Details");

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        Intent intent = getIntent();
        int movie_id = intent.getIntExtra(MainActivity.MOVIE_ID, 0);
        Log.e(TAG, "ID is: " + movie_id);

        movie_poster_iv = (ImageView) findViewById(R.id.movie_poster);
        movie_title_tv = (TextView) findViewById(R.id.movie_title);
        movie_overview_tv = (TextView) findViewById(R.id.movie_overview);
        movie_overview_tv.setMovementMethod(new ScrollingMovementMethod());

        movie_rating_tv = (TextView) findViewById(R.id.movie_rating);
        movie_tagline_tv = (TextView) findViewById(R.id.movie_tagline);
        movie_budget_tv = (TextView) findViewById(R.id.movie_budget);
        movie_release_date_tv = (TextView) findViewById(R.id.movie_release_date);
        movie_revenue_tv = (TextView) findViewById(R.id.movie_revenue);
        movie_runtime_tv = (TextView) findViewById(R.id.movie_runtime);
        movie_genre_tv = (TextView) findViewById(R.id.movie_genres);
        movie_language_tv = (TextView) findViewById(R.id.movie_language);
        movie_production_company_tv = (TextView) findViewById(R.id.movie_production_company);
        movie_cast_tv = (TextView) findViewById(R.id.movie_cast);
        movie_crew_tv = (TextView) findViewById(R.id.movie_crew);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        Call<MovieDetail> call_details = apiService.getMovieDetails(movie_id, API_KEY);
        call_details.enqueue(new Callback<MovieDetail>() {
            @Override
            public void onResponse(Call<MovieDetail> call, Response<MovieDetail> response) {
                int statusCode = response.code();
                Log.e(TAG, "Status Code: " + statusCode);
                if (statusCode != 0) {

                    populateMovieDetail(response.body());
                }
            }

            @Override
            public void onFailure(Call<MovieDetail> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

        Call<Credits> call_credits = apiService.getMovieCredits(movie_id, API_KEY);
        call_credits.enqueue(new Callback<Credits>() {
            @Override
            public void onResponse(Call<Credits> call, Response<Credits> response) {
                int statusCode = response.code();
                Log.e(TAG, "Status Code: " + statusCode);
                if (statusCode != 0) {
                    populateCreditsDetail(response.body());
                }
            }

            @Override
            public void onFailure(Call<Credits> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });

    }

    private void populateCreditsDetail(Credits body) {
        Log.e(TAG, "Cast: ");
        for (int i = 0; i < body.getCast().size(); i++) {
            //Log.e(TAG, body.getCast().get(i).getCharacter() + ":" + body.getCast().get(i).getName());
            movie_cast_tv.append("\n\t\t" + body.getCast().get(i).getCharacter() + ": " + body.getCast().get(i).getName());
        }

        for (int i = 0; i < body.getCrew().size(); i++) {
            //Log.e(TAG, body.getCrew().get(i).getJob() + ":" + body.getCrew().get(i).getName());
            movie_crew_tv.append("\n\t\t" + body.getCrew().get(i).getJob() + ": " + body.getCrew().get(i).getName());
        }
    }

    void populateMovieDetail(MovieDetail movie) {
        Log.e(TAG, "Title is: " + movie.getTitle());
        Log.e(TAG, "ID is: " + movie.getId());

        String posterUrl = "https://image.tmdb.org/t/p/w780" + movie.getPosterPath();
        Picasso.with(this).load(posterUrl).into(movie_poster_iv);

        movie_title_tv.setText(movie.getTitle());
        ab.setTitle("TMDB - " + movie.getTitle());
        movie_overview_tv.setText(movie.getOverview());
        movie_rating_tv.append(movie.getVoteAverage().toString());
        movie_tagline_tv.append(movie.getTagline());
        movie_budget_tv.append(String.format(Locale.getDefault(), "%,d", movie.getBudget()));
        movie_release_date_tv.append(movie.getReleaseDate());
        movie_revenue_tv.append(String.format(Locale.getDefault(), "%,d", movie.getRevenue()));
        movie_runtime_tv.append(movie.getRuntime() + " minutes");

        for (int i = 0; i < movie.getGenres().size(); i++) {
            movie_genre_tv.append("\n\t\t" + movie.getGenres().get(i).getName());
        }

        movie_language_tv.append(movie.getOriginalLanguage());

        for (int i = 0; i < movie.getProductionCompanies().size(); i++) {
            movie_production_company_tv.append("\n\t\t" + movie.getProductionCompanies().get(i).getName());
        }

    }
}
