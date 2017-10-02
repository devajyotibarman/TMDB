package edureka.devajyoti.com.tmdb.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

import edureka.devajyoti.com.tmdb.R;
import edureka.devajyoti.com.tmdb.adapter.MoviesAdapter;
import edureka.devajyoti.com.tmdb.model.Movie;
import edureka.devajyoti.com.tmdb.model.MoviesResponse;
import edureka.devajyoti.com.tmdb.rest.ApiClient;
import edureka.devajyoti.com.tmdb.rest.ApiInterface;
import edureka.devajyoti.com.tmdb.support.DividerItemDecoration;
import edureka.devajyoti.com.tmdb.support.RecyclerTouchListener;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private final static String API_KEY = "55957fcf3ba81b137f8fc01ac5a31fb5";
    public static String MOVIE_ID = "MOVIE_ID";
    public static  String LAST_SEARCH = null;
    List<Movie> movies;
    Call<MoviesResponse> call_movie_list;
    android.support.v7.app.ActionBar ab;
    ApiInterface apiService;
    Spinner spinner;
    private int api_slot = 0;
    private RecyclerView recyclerView;
    private int LAST_POS = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ab = getSupportActionBar();
        assert ab != null;
        ab.setDisplayUseLogoEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        ab.setLogo(R.drawable.res91x81);

        assert ab != null;
        ab.setSubtitle("Now Playing");

        if (API_KEY.isEmpty()) {
            Toast.makeText(getApplicationContext(), "Please obtain your API KEY from themoviedb.org first!", Toast.LENGTH_LONG).show();
            return;
        }

        recyclerView = (RecyclerView) findViewById(R.id.movies_recycler_view);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = movies.get(position);
                Log.e(TAG, movie.getTitle() + " is selected!");
                Intent intent = new Intent(MainActivity.this, MovieDetailActivity.class);
                intent.putExtra(MOVIE_ID, movie.getId());
                startActivity(intent);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        apiService = ApiClient.getClient().create(ApiInterface.class);
        startCall(api_slot, null, false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.android_action_bar_spinner_menu, menu);

        MenuItem item = menu.findItem(R.id.spinner);
        spinner = (Spinner) MenuItemCompat.getActionView(item);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.dropdownarray, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                api_slot = position;
                if (position == 3) {
                    startSearchDialog();
                } else if (position == 4) {
                    if (LAST_SEARCH == null) {
                        spinner.setSelection(LAST_POS);
                        Toast.makeText(MainActivity.this, "Nothing to search", Toast.LENGTH_LONG).show();
                    } else {
                        ab.setSubtitle("Results");
                        startCall(3, LAST_SEARCH, true);
                    }
                } else {
                    startCall(api_slot, null, false);
                }
                LAST_POS = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return true;
    }

    private void startSearchDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.search_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.search_input);

        //dialogBuilder.setTitle("Search Movie");
        dialogBuilder.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                startCall(3, edt.getText().toString(), true);
                LAST_SEARCH = edt.getText().toString();
                spinner.setSelection(4);
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
//                spinner.setSelection(0);
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void startCall(int id, String query, boolean search) {

        switch (id) {
            case 0:
                ab.setSubtitle("Now Playing");
                call_movie_list = apiService.getMoviesNowPlaying(API_KEY);
                break;
            case 1:
                ab.setSubtitle("Upcoming");
                call_movie_list = apiService.getMoviesUpcoming(API_KEY);
                break;
            case 2:
                ab.setSubtitle("Top Rated");
                call_movie_list = apiService.getTopRatedMovies(API_KEY);
                break;
            case 3:
                ab.setSubtitle("Search");
                call_movie_list = apiService.searchMovie(query, API_KEY);
                break;
            default:
                ab.setSubtitle("Now Playing");
                call_movie_list = apiService.getMoviesNowPlaying(API_KEY);
        }

        call_movie_list.enqueue(new Callback<MoviesResponse>() {
            @Override
            public void onResponse(Call<MoviesResponse> call, Response<MoviesResponse> response) {
                int statusCode = response.code();
                Log.e(TAG, "Status Code: " + statusCode);
                if (statusCode != 0) {
                    movies = response.body().getResults();
                    recyclerView.setAdapter(new MoviesAdapter(movies, R.layout.movie_list, getApplicationContext()));
                }
            }

            @Override
            public void onFailure(Call<MoviesResponse> call, Throwable t) {
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }

}
