package com.aziz.udacity.popflix.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.aziz.udacity.popflix.BuildConfig;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.activity.FlickDetailActivity;
import com.aziz.udacity.popflix.adapter.FlixGalleryAdapter;
import com.aziz.udacity.popflix.data.Repository;
import com.aziz.udacity.popflix.event.RefreshEvent;
import com.aziz.udacity.popflix.model.DetailWrapper;
import com.aziz.udacity.popflix.model.Flick;
import com.aziz.udacity.popflix.model.FlickDetail;
import com.aziz.udacity.popflix.model.FlixWrapper;
import com.aziz.udacity.popflix.service.FlickResultResponse;
import com.aziz.udacity.popflix.service.RestClient;
import com.aziz.udacity.popflix.service.ReviewResultResponse;
import com.aziz.udacity.popflix.service.TrailerResultResponse;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import com.aziz.udacity.popflix.ui.FlixSortType;
import com.aziz.udacity.popflix.ui.MovieSelectedListener;
import com.aziz.udacity.popflix.util.Utils;
import de.greenrobot.event.EventBus;
import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;
import timber.log.Timber;
import tr.xip.errorview.ErrorView;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Fragment for displaying a gallery of movies.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */

public class FlixGalleryFragment extends Fragment implements MovieSelectedListener {

    public static final String SORT_TYPE = "SORT_TYPE";
    public static final String FLICK_BUNDLE_WRAPPER = "FLICK_BUNDLE_WRAPPER";
    public static final String FLIX_WRAPPER = "FLIX_WRAPPER";

    @Bind(R.id.progress_bar)
    ProgressBar mProgressBar;

    @Bind(R.id.message_text_view)
    TextView mMessageTextView;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Bind(R.id.error_view)
    ErrorView mErrorView;

    @Nullable
    @Bind(R.id.flick_detail_container)
    FrameLayout mFlickDetailContainer;


    private FlixGalleryAdapter mFlixGalleryAdapter;

    private boolean mDualPane;

    private AtomicInteger mCallCounter;
    private LayoutInflater mInflater;
    private FlixSortType mSortType;
    private FlickBundleWrapper mFlickBundleWrapper;
    private FlixWrapper mFlixWrapper;
    private GridLayoutManager mGridLayoutManager;


    public FlixGalleryFragment() {
    }

    public static FlixGalleryFragment newInstance(FlixSortType sortType) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(SORT_TYPE, sortType);

        final FlixGalleryFragment fragment = new FlixGalleryFragment();
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Timber.d("onCreateView called this: " + this);
        mInflater = inflater;
        View view = mInflater.inflate(R.layout.fragment_flix_gallery, container, false);
        ButterKnife.bind(this, view);


        if (savedInstanceState == null) {
            final Bundle args = getArguments();
            if (args != null) {
                mSortType = ((FlixSortType) args.getSerializable(SORT_TYPE));
            }
        } else {
            mSortType = (FlixSortType) savedInstanceState.getSerializable(SORT_TYPE);
        }
        mGridLayoutManager = new GridLayoutManager(getActivity(), 2);
        mFlixGalleryAdapter = new FlixGalleryAdapter(getActivity(), this, mSortType, mGridLayoutManager);
        mRecyclerView.setAdapter(mFlixGalleryAdapter);
        mDualPane = mFlickDetailContainer != null;
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        checkBeforeFetch(savedInstanceState);
        return view;
    }


    private void checkBeforeFetch(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            if (Utils.isNetworkAvailable(getActivity())) {
                if (mFlixGalleryAdapter.isEmpty()) {
                    if (isNotFavorites()) {
                        fetchMovies();
                    } else {
                        showFavorites();
                    }
                }
            } else if (isFavorites()) { // There is no available network, let's try to display the favorites.
                showFavorites();
            } else {
                showError(getString(R.string.error_network), getString(R.string.check_network));
            }
        } else {
            showFlix(savedInstanceState.<FlixWrapper>getParcelable(FLIX_WRAPPER));
        }
    }

    private void showFavorites() {
        if (Repository.hasFlixData(getActivity())) {
            showFlix(Utils.retrieveFlixFromDb(getActivity()));
        } else {
            showEmptyFavoritesError();
        }
    }

    private boolean isNotFavorites() {
        return mSortType != FlixSortType.FAVORITES;
    }

    private boolean isFavorites() {
        return mSortType == FlixSortType.FAVORITES;
    }

    private void showEmptyFavoritesError() {
        mProgressBar.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setTitle("No favorites were found");
        mErrorView.showRetryButton(false);
    }

    private void showError(String title, String subTitle) {
        mProgressBar.setVisibility(View.GONE);
        mErrorView.setVisibility(View.VISIBLE);
        mErrorView.setTitle(title);
        mErrorView.setSubtitle(subTitle);
        mErrorView.setOnRetryListener(new ErrorView.RetryListener() {
            @Override
            public void onRetry() {
                checkBeforeFetch(null);
            }
        });
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


    private void fetchMovies() {
        final Call<FlickResultResponse> call = RestClient.getInstance().getTheMovieDbService()
            .getFlickResponse(mSortType.getSortOrder(), BuildConfig.API_KEY);
        call.enqueue(new Callback<FlickResultResponse>() {
            @Override
            public void onResponse(Response<FlickResultResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    final FlickResultResponse flickResultResponse = response.body();
                    showFlix(new FlixWrapper(flickResultResponse.flickList));
                } else {
                    showError("Service error", response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.e("Retrofit failure", t);
                showError("Service error", "Retrofit error");
            }
        });
    }

    private void showFlix(FlixWrapper wrapper) {
        mFlixWrapper = wrapper;
        mFlixGalleryAdapter.addFlix(wrapper);
        mProgressBar.setVisibility(View.GONE);
        mErrorView.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    @Override
    public void movieSelected(final Flick flick) {
        Timber.d("Selected this movie = " + flick);
        mCallCounter = new AtomicInteger(0);
        final FlickDetail[] flickDetail = new FlickDetail[1];
        final TrailerResultResponse[] trailerResponse = new TrailerResultResponse[1];
        final ReviewResultResponse[] reviewResponse = new ReviewResultResponse[1];

        final Call<FlickDetail> flickDetailCall = RestClient.getInstance().getTheMovieDbService()
            .getFlickDetail(flick.id, BuildConfig.API_KEY);

        flickDetailCall.enqueue(new Callback<FlickDetail>() {
            @Override
            public void onResponse(Response<FlickDetail> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    flickDetail[0] = response.body();
                    Timber.d("---- FlickDetail - " + flickDetail[0]);
                    if (mCallCounter.incrementAndGet() == 3) {
                        showMovieDetail(flickDetail[0], reviewResponse[0], trailerResponse[0], flick);
                    }
                } else {
                    showError("Service error", response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.e(t, "Retrofit failure");
                showError("Service error", "Retrofit error");
            }
        });

        final Call<ReviewResultResponse> reviewCall = RestClient.getInstance().getTheMovieDbService()
            .getReviewResponse(flick.id, BuildConfig.API_KEY);

        reviewCall.enqueue(new Callback<ReviewResultResponse>() {
            @Override
            public void onResponse(Response<ReviewResultResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    reviewResponse[0] = response.body();
                    Timber.d("---- ReviewResponse - " + reviewResponse[0]);
                    if (mCallCounter.incrementAndGet() == 3) {
                        showMovieDetail(flickDetail[0], reviewResponse[0], trailerResponse[0], flick);
                    }
                } else {
                    showError("Service error", response.message());
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Timber.e(t, "Retrofit failure");
                showError("Service error", "Retrofit error");
            }
        });
        final Call<TrailerResultResponse> trailerCall = RestClient.getInstance().getTheMovieDbService().
            getTrailerResponse(flick.id, BuildConfig.API_KEY);

        trailerCall.enqueue(new Callback<TrailerResultResponse>() {
            @Override
            public void onResponse(Response<TrailerResultResponse> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    trailerResponse[0] = response.body();
                    Timber.d("--- TrailerResponse - " + trailerResponse[0]);
                    if (mCallCounter.incrementAndGet() == 3) {
                        showMovieDetail(flickDetail[0], reviewResponse[0], trailerResponse[0], flick);
                    }
                } else {
                    showError("Service error", response.message());
                }

            }

            @Override
            public void onFailure(Throwable t) {
                Timber.e(t, "Retrofit failure");
                showError("Service error", "Retrofit error");
            }
        });

        Timber.d("Calling RxJava...");
    }

    /**
     * Event for refreshing the UI when a favorite
     * Called when the EventBus's EventBus.getDefault().post(RefreshEvent) is invoked.
     * Source of the event: FlickDetailFragment
     *
     * @param event
     */
    @SuppressWarnings("unused")
    public void onEventMainThread(RefreshEvent event) {
        if (mSortType.equals(FlixSortType.FAVORITES)) {
            if (Repository.hasFlixData(getActivity())) {
                showFlix(Utils.retrieveFlixFromDb(getActivity()));
            } else {
                mRecyclerView.setVisibility(View.GONE);
                showEmptyFavoritesError();

            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            Fragment fragment = fm.findFragmentByTag(FlickDetailFragment.FRAG_NAME);
            if (fragment != null) {
                FragmentTransaction transaction = fm.beginTransaction();
                transaction.remove(fragment);
                transaction.commit();
            }
        }

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mFlickBundleWrapper != null) {
            outState.putParcelable(FLICK_BUNDLE_WRAPPER, mFlickBundleWrapper.getBundle());
        }
        outState.putParcelable(FLIX_WRAPPER, mFlixWrapper);
        outState.putSerializable(SORT_TYPE, mSortType);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void movieSelected(Flick flick, byte[] posterBytes) {
        DetailWrapper detailWrapper = Utils.retrieveFlickDetailsFromDb(getActivity(), flick);

        final FlickBundleWrapper flickWrapper = new FlickBundleWrapper(new Bundle());
        flickWrapper.flickId(String.valueOf(flick.id))
            .dualPane(mDualPane)
            .favoriteMode(true)
            .title(detailWrapper.getTitle())
            .posterBytes(posterBytes)
            .releaseDate(detailWrapper.getReleaseYear())
            .flickLength(detailWrapper.getLength())
            .userRating(detailWrapper.getUserRating())
            .synopsis(detailWrapper.getSynopsis())
            .imageWidth(mGridLayoutManager.getWidth())
            .isPersisted(true)

            .reviews(detailWrapper.getReviewList())
            .trailers(detailWrapper.getTrailerList());

        showDetailView(flickWrapper);
    }

    private void showMovieDetail(final FlickDetail flickDetail, final ReviewResultResponse reviewResultResponse,
                                 final TrailerResultResponse trailerResultResponse, Flick flick) {
        Timber.d("Show Movie Detail!!!!!");
        Timber.d("Flick: " + flick);

        mFlickBundleWrapper = new FlickBundleWrapper(new Bundle());
        final String posterUrl = mDualPane ? flick.getPosterUrl() : flick.getLargePosterUrl();
        mFlickBundleWrapper.flickId(String.valueOf(flick.id))
            .dualPane(mDualPane)
            .favoriteMode(false)
            .title(flickDetail.originalTitle)
            .posterUrl(posterUrl)
            .releaseDate(flickDetail.getYear())
            .flickLength(flickDetail.movieLength)
            .userRating(flick.userRating)
            .synopsis(flickDetail.plotSynopsis)
            .isPersisted(Repository.isPersistedFlick(getActivity(), String.valueOf(flick.id)))
            .reviews(reviewResultResponse.mReviewList)
            .trailers(trailerResultResponse.mTrailerList);

        Timber.d("Created Bundle!");
        showDetailView(mFlickBundleWrapper);

    }

    private void showDetailView(FlickBundleWrapper wrapper) {
        final FlickDetailFragment flickDetailFragment = FlickDetailFragment.newInstance(wrapper.getBundle());

        if (mDualPane) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.flick_detail_container, flickDetailFragment, FlickDetailFragment.FRAG_NAME);
            transaction.commit();

        } else {
            Intent intent = new Intent(getActivity(), FlickDetailActivity.class);
            intent.putExtras(wrapper.getBundle());
            getActivity().startActivity(intent);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
