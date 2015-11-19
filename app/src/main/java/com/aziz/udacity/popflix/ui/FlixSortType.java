package com.aziz.udacity.popflix.ui;

import com.aziz.udacity.popflix.R;

/**
 * Sort order for movies.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public enum FlixSortType {
    MOST_POPULAR("popularity.desc", R.string.sort_by_popularity, 0),
    HIGHEST_RATED("vote_average.desc", R.string.sort_by_highest_rated, 1),
    FAVORITES("none", R.string.favorites, 2);

    private final String mSortOrder;
    private final int mTitle;
    private final int mSelection;

    FlixSortType(String sortOrder, int title, int selection) {
        mSortOrder = sortOrder;
        mTitle = title;
        mSelection = selection;
    }

    public String getSortOrder() {
        return mSortOrder;
    }

    public int getTitle() {
        return mTitle;
    }

    public int getSelection() {
        return mSelection;
    }
}
