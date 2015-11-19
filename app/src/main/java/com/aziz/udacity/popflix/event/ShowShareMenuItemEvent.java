package com.aziz.udacity.popflix.event;


/**
 * Event used to show the share menu item.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class ShowShareMenuItemEvent {
    private final String mTrailerMsg;
    private final String mTrailerUrl;

    public ShowShareMenuItemEvent(String trailerMsg, String trailerUrl) {
        mTrailerMsg = trailerMsg;
        mTrailerUrl = trailerUrl;
    }

    public String getTrailerUrl() {
        return mTrailerUrl;
    }

    public String getTrailerMsg() {
        return mTrailerMsg;
    }
}
