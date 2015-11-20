package com.aziz.udacity.popflix.ui;

/**
 * Necessary callback to fix a (Gabriel Mariotti) card refresh issue in Lollipop
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public interface CardAdapterCallBack {
    /**
     * Force the card to redraw itself by calling CardArrayAdapter.notifyDataSetChanged()
     */
    void updateCard();
}
