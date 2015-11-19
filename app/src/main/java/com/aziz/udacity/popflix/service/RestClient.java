package com.aziz.udacity.popflix.service;

import com.aziz.udacity.popflix.BuildConfig;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Retrofit rest client.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class RestClient {

    private final TheMovieDbService mTheMovieDbService;

    private static RestClient ourInstance = new RestClient();

    public static RestClient getInstance() {
        return ourInstance;
    }

    private RestClient() {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)

            .addConverterFactory(GsonConverterFactory.create())
            .build();
        mTheMovieDbService = retrofit.create(TheMovieDbService.class);
    }

    public TheMovieDbService getTheMovieDbService() {
        return mTheMovieDbService;
    }
}
