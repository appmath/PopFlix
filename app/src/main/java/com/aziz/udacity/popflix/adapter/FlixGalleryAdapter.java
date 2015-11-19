package com.aziz.udacity.popflix.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.aziz.udacity.popflix.model.Flick;
import com.aziz.udacity.popflix.model.FlixWrapper;
import com.aziz.udacity.popflix.ui.FlixSortType;
import com.aziz.udacity.popflix.ui.MovieSelectedListener;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.util.Utils;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.lang.ref.WeakReference;

/**
 * Adapter for displaying all the movies.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlixGalleryAdapter extends RecyclerView.Adapter<FlixViewHolder> {

    private final WeakReference<Activity> mWeakActivityRef;
    private final MovieSelectedListener mMovieSelectedListener;
    private final FlixSortType mSortType;
    private final GridLayoutManager mGridLayoutManager;
    private FlixWrapper mFlixWrapper;

    public FlixGalleryAdapter(Activity activity, MovieSelectedListener movieSelectedListener, FlixSortType sortType,
                              GridLayoutManager gridLayoutManager) {
        mGridLayoutManager = gridLayoutManager;
        mWeakActivityRef = new WeakReference<>(activity);
        mMovieSelectedListener = movieSelectedListener;
        mSortType = sortType;
    }

    @Override
    public FlixViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.flick_item, parent, false);
        return new FlixViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final FlixViewHolder holder, final int position) {
        Transformation transformation = new Transformation() {

            @Override
            public Bitmap transform(Bitmap source) {
                int targetWidth = holder.mPosterImageView.getWidth();

                double aspectRatio = (double) source.getHeight() / (double) source.getWidth();
                int targetHeight = (int) (targetWidth * aspectRatio);
                Bitmap result = Bitmap.createScaledBitmap(source, targetWidth, targetHeight, false);
                if (result != source) {
                    // Same bitmap is returned if sizes are the same
                    source.recycle();
                }
                return result;
            }

            @Override
            public String key() {
                return "transformation" + " desiredWidth";
            }
        };

        final Flick flick = mFlixWrapper.getFlix().get(position);

        if (mSortType != FlixSortType.FAVORITES) {
            // Retrieve the poster from https://www.themoviedb.org/
            Picasso.with(mWeakActivityRef.get())
                .load(flick.getPosterUrl())
                .placeholder(R.drawable.empty_movie)
                .transform(transformation)
                .into(holder.mPosterImageView);

            holder.mPosterImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mMovieSelectedListener.movieSelected(flick);
                }
            });
        } else { // Retrieve the poster from the DB
            final byte[] posterBytes = mFlixWrapper.getPosterBytes(flick.id);
            holder.mPosterImageView.setImageBitmap(Utils.convertBytesToScaledBitmap(posterBytes,
                mGridLayoutManager.getWidth()));

            holder.mPosterImageView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    mMovieSelectedListener.movieSelected(flick, posterBytes);
                }
            });
        }
    }


    @Override
    public int getItemCount() {
        return mFlixWrapper != null ? mFlixWrapper.getFlix().size() : 0;
    }

    public void addFlix(FlixWrapper wrapper) {
        mFlixWrapper = wrapper;
        notifyDataSetChanged();
    }
    public void removeFlick(Flick flick) {
        mFlixWrapper.getFlix().remove(flick);
        notifyDataSetChanged();
    }

    public boolean isEmpty() {
        return getItemCount() < 1;
    }


}

class FlixViewHolder extends RecyclerView.ViewHolder {

    final ImageView mPosterImageView;
    final LinearLayout mRecyclerLinearLayout;

    public FlixViewHolder(View itemView) {
        super(itemView);
        mPosterImageView = (ImageView) itemView.findViewById(R.id.poster_image_view);
        mRecyclerLinearLayout = (LinearLayout) itemView.findViewById(R.id.recyclerLinearLayout);

    }
}
