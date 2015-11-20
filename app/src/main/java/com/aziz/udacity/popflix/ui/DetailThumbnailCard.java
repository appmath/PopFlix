package com.aziz.udacity.popflix.ui;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.data.Repository;
import com.aziz.udacity.popflix.event.RefreshEvent;
import com.aziz.udacity.popflix.util.Utils;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;
import com.squareup.picasso.Picasso;
import de.greenrobot.event.EventBus;
import it.gmariotti.cardslib.library.internal.Card;
import timber.log.Timber;

import java.lang.ref.WeakReference;

/**
 * Card to display movie details like title and poster.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 * <p/>
 * Thanks to Gabriele Mariotti (gabri.mariotti@gmail.com)
 *
 * @author Aziz Kadhi
 */

public class DetailThumbnailCard extends Card {
    private final FlickBundleWrapper mWrapper;
    private final CardAdapterCallBack mCallBack;
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


    @Bind(R.id.flick_synopsis_text_view)
    TextView mFlickSynopsisTextView;
    
    @Bind(R.id.fav_image_view)
    ImageView mFavImageView;

    private WeakReference<Context> mWeakCtxRef;
    private IconicsDrawable mFlickFavUnselectedDrawable;
    private IconicsDrawable mFlickFavSelectedDrawable;


    /**
     * Constructor with a custom inner layout
     *
     * @param flickBundleWrapper
     * @param ctx
     * @param flickDetailFragment
     */
    public DetailThumbnailCard(Context ctx, FlickBundleWrapper wrapper, CardAdapterCallBack callBack) {
        super(ctx, R.layout.detail_thumbnail_card);
        this.mWrapper = wrapper;
        mCallBack = callBack;
        mWeakCtxRef = new WeakReference<Context>(ctx);

        mFlickFavUnselectedDrawable = createIconicsDrawable(ctx, FontAwesome.Icon.faw_heart_o);
        mFlickFavSelectedDrawable = createIconicsDrawable(ctx, FontAwesome.Icon.faw_heart);


    }

    private IconicsDrawable createIconicsDrawable(Context ctx, FontAwesome.Icon icon) {
        return new IconicsDrawable(ctx)
            .icon(icon)
            .color(Color.RED)
            .sizeDp(48);
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


    @SuppressWarnings("unused")
    @OnClick(R.id.fav_image_view)
    public void favIconicsClicked() {
        final Bundle args = mWrapper.getBundle();
        if (args != null) {
            FlickBundleWrapper wrapper = new FlickBundleWrapper(args);
            switchIcons();
            boolean isPressed = isPressed();
            Timber.d("isPressed " + isPressed);
            if (isPressed) {
                // Add this flick to the DB and update the icon
                try {
                    wrapper.posterBytes(Utils.convertToBytes(mPosterDetailImageView.getDrawable()));
                    Repository.persistFlickWithDetails(mWeakCtxRef.get(), wrapper);
                    Timber.d("Persisted flick " + wrapper.getTitle());
                } catch (Exception e) {
                    Timber.e("Failed to persist flick with details: " + wrapper.getTitle(), e);
                }
                mFavImageView.setImageDrawable(mFlickFavSelectedDrawable);
            } else {
                // Remove this flick from the DB and update the icon
                try {
                    Repository.deleteFlickWithDetails(mWeakCtxRef.get(), wrapper);
                    EventBus.getDefault().post(new RefreshEvent());
                    Timber.d("Removed flick " + wrapper.getTitle());
                } catch (Exception e) {
                    Timber.e("Failed to delete flick with details: " + wrapper.getTitle(), e);
                }
                mFavImageView.setImageDrawable(mFlickFavUnselectedDrawable);
            }
            mWrapper.isPersisted(isPressed);
            mCallBack.updateCard();

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

        mFlickSynopsisTextView.setText(mWrapper.getSynopsis());


        if (mWrapper.getIsPersisted()) {
            mFavImageView.setImageDrawable(mFlickFavSelectedDrawable);
        } else {
            mFavImageView.setImageDrawable(mFlickFavUnselectedDrawable);

        }

    }

    private boolean isPressed() {
        return mFavImageView.getDrawable() == mFlickFavSelectedDrawable;
    }

    public void switchIcons() {
        if (isPressed()) {
            mFavImageView.setImageDrawable(mFlickFavUnselectedDrawable);
        } else {
            mFavImageView.setImageDrawable(mFlickFavSelectedDrawable);
        }
    }


}
