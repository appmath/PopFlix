package com.aziz.udacity.popflix.ui;

import com.aziz.udacity.popflix.model.Flick;

/**
 * Call back for movie selection.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public interface MovieSelectedListener {
    void movieSelected(Flick flick);

    void movieSelected(Flick flick, byte[] posterBytes);
}
