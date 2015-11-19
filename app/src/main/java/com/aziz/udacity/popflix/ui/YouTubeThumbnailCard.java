package com.aziz.udacity.popflix.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aziz.udacity.popflix.R;
import it.gmariotti.cardslib.library.internal.Card;

/**
 * Provides a customized card for with a inner layout to display product info.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 * @author Aziz Kadhi
 */

public class YouTubeThumbnailCard extends Card {
    public static final String YOUTUBE = "http://www.youtube.com/watch?v=";

    private final String mTrailerTitle;
    protected TextView mTitleTextView;


    /**
     * Constructor with a custom inner layout
     *
     * @param context
     */
    public YouTubeThumbnailCard(Context context, final String title) {
        super(context, R.layout.youtube_thumbnail_card);
        mTrailerTitle = title;
    }


    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {

        //Retrieve elements
        mTitleTextView = (TextView) parent.findViewById(R.id.title_textview);

        if (mTitleTextView != null)
            mTitleTextView.setText(mTrailerTitle);

    }
}
