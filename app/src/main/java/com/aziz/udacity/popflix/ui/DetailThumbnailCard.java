
package com.aziz.udacity.popflix.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.data.Repository;
import com.aziz.udacity.popflix.event.RefreshEvent;
import com.aziz.udacity.popflix.model.Review;
import com.aziz.udacity.popflix.model.Trailer;
import com.aziz.udacity.popflix.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardHeader;
import timber.log.Timber;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Card to display movie details like title and poster.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * Thanks to Gabriele Mariotti (gabri.mariotti@gmail.com)
 *
 * @author Aziz Kadhi
 */

public class DetailThumbnailCard extends Card {
    private final FlickBundleWrapper mWrapper;
    private WeakReference<Context> mWeakCtxRef;
    private IconicsDrawable mFlickFavUnselectedDrawable;
    private IconicsDrawable mFlickFavSelectedDrawable;

    @Bind(R.id.flick_title_text_view)
    TextView mFlickTitleTextView;

    @Bind(R.id.poster_detail_image_view)
    ImageView mPosterDetailImageView;

    @Bind(R.id.flick_year_text_view)
    TextView mFlickYearTextView;

    @Bind(R.id.flick_time_length_text_view)
    TextView mFlickTimeLengthTextView;

    @Bind(R.id.flick_rating_text_view)
    TextView mFlickRatingTextView;

    @Bind(R.id.flick_favorite_button)
    ToggleButton mFlickFavoriteButton;

    @Bind(R.id.flick_synopsis_text_view)
    TextView mFlickSynopsisTextView;


    /**
     * Constructor with a custom inner layout
     *
     * @param ctx
     * @param flickBundleWrapper
     */
    public DetailThumbnailCard(Context ctx, FlickBundleWrapper wrapper) {
        super(ctx, R.layout.detail_thumbnail_card);
        this.mWrapper = wrapper;
        mWeakCtxRef = new WeakReference<Context>(ctx);
        mFlickFavUnselectedDrawable = new IconicsDrawable(ctx)
            .icon(FontAwesome.Icon.faw_heart_o)
            .color(Color.RED)
            .sizeDp(16);

        mFlickFavSelectedDrawable = new IconicsDrawable(ctx)
            .icon(FontAwesome.Icon.faw_heart)
            .color(Color.RED)
            .sizeDp(16);


    }

    private void setPosterImageView(FlickBundleWrapper wrapper, Context ctx) {
        if (wrapper.isFavoriteMode()) {
            mPosterDetailImageView.setImageBitmap(Utils.convertBytesToScaledBitmap(wrapper.getPosterBytes(),
                wrapper.getImagewidth()));
        } else {
            Picasso.with(ctx)
                .load(wrapper.getPosterUrl())
                .placeholder(R.drawable.empty_movie)
                .into(mPosterDetailImageView);
        }
    }

    private void updateFavButtonImage() {
        if (mFlickFavoriteButton.isChecked()) {
            mFlickFavoriteButton.setBackgroundDrawable(mFlickFavSelectedDrawable);
        } else {
            mFlickFavoriteButton.setBackgroundDrawable(mFlickFavUnselectedDrawable);
        }
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.flick_favorite_button)
    public void favButtonClicked() {
        final Bundle args = mWrapper.getBundle();
        if (args != null) {

            FlickBundleWrapper wrapper = new FlickBundleWrapper(args);
            wrapper.posterBytes(Utils.convertToBytes(mPosterDetailImageView.getDrawable()));
            Timber.d("mFlickFavoriteButton.isChecked(): " + mFlickFavoriteButton.isChecked());
            updateFavButtonImage();
            if (mFlickFavoriteButton.isChecked()) {
                try {
                    Repository.persistFlickWithDetails(mWeakCtxRef.get(), wrapper);
                    Timber.d("Persisted flick " + wrapper.getTitle());
                } catch (Exception e) {
                    Timber.e("Failed to persist flick with details: " + wrapper.getTitle(), e);
                }
            } else {
                try {
                    Repository.deleteFlickWithDetails(mWeakCtxRef.get(), wrapper);
                    EventBus.getDefault().post(new RefreshEvent());

                    Timber.d("Removed flick " + wrapper.getTitle());
                } catch (Exception e) {
                    Timber.e("Failed to delete flick with details: " + wrapper.getTitle(), e);
                }
            }
        }
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        ButterKnife.bind(this, parent);

        Context ctx = mWeakCtxRef.get();

        mFlickTitleTextView.setText(mWrapper.getTitle());

        setPosterImageView(mWrapper, ctx);
        mFlickYearTextView.setText(mWrapper.getReleaseDate());
        mFlickTimeLengthTextView.setText(mWrapper.getFlickLength());
        mFlickRatingTextView.setText(ctx.getString(R.string.rating) + mWrapper.getUserRating());
        mFlickFavoriteButton.setChecked(mWrapper.getIsPersisted());
        updateFavButtonImage();
        mFlickSynopsisTextView.setText(mWrapper.getSynopsis());
    }
}
