package com.aziz.udacity.popflix.service;

import com.aziz.udacity.popflix.model.FlickDetail;
import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Retrofit interface for accessing specific moviedb.org services.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public interface TheMovieDbService {
    /**
     * Retrieves all the movies that are filtered by the sort_by parameter, like Most Popular.
     *
     * @param sortBy this is more like a filter.
     * @param apiKey the API KEY that's required by the moviedb.org.
     * @return a response the contains a list of movies.
     * @see com.aziz.udacity.popflix.ui.FlixSortType
     */
    @GET("/3/discover/movie")
    Call<FlickResultResponse> getFlickResponse(@Query("sort_by") String sortBy, @Query("api_key") String apiKey);

    /**
     * Retrieves all trailers associated with a movie.
     *
     * @param sortBy this is more like a filter.
     * @param apiKey the API KEY that's required by the moviedb.org.
     * @return a response the contains a list of movie trailers.
     */
    @GET("/3/movie/{id}/videos")
    Call<TrailerResultResponse> getTrailerResponse(@Path("id") int id, @Query("api_key") String apiKey);

    /**
     * Retrieves all the reviews associated with a movie.
     * @param sortBy this is more like a filter.
     * @param apiKey the API KEY that's required by the moviedb.org.
     * @return a response the contains a list of movie reviews.
     */
    @GET("/3/movie/{id}/reviews")
    Call<ReviewResultResponse> getReviewResponse(@Path("id") int id, @Query("api_key") String apiKey);

    /**
     * Retrieves the details associated with a movie.
     * @param sortBy this is more like a filter.
     * @param apiKey the API KEY that's required by the moviedb.org.
     * @return a response the contains the details for a movie.
     */
    @GET("/3/movie/{id}")
    Call<FlickDetail> getFlickDetail(@Path("id") int id, @Query("api_key") String apiKey);

}
