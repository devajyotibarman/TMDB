package edureka.devajyoti.com.tmdb.rest;

/**
 * Created by devajyoti on 21/9/17.
 */

import edureka.devajyoti.com.tmdb.model.Credits;
import edureka.devajyoti.com.tmdb.model.MovieDetail;
import edureka.devajyoti.com.tmdb.model.MoviesResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @GET("movie/top_rated")
    Call<MoviesResponse> getTopRatedMovies(@Query("api_key") String apiKey);

    @GET("movie/{id}")
    Call<MovieDetail> getMovieDetails(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/{id}/credits")
    Call<Credits> getMovieCredits(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("movie/now_playing")
    Call<MoviesResponse> getMoviesNowPlaying(@Query("api_key") String apiKey);

    @GET("movie/upcoming")
    Call<MoviesResponse> getMoviesUpcoming(@Query("api_key") String apiKey);

    @GET("search/movie")
    Call<MoviesResponse> searchMovie(@Query("query") String query, @Query("api_key") String apiKey);
}