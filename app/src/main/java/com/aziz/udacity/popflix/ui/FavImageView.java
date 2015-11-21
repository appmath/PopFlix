package com.aziz.udacity.popflix.ui;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.iconics.IconicsDrawable;

/**
 * A fix the selected/unselected blues.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FavImageView extends ImageView{

    private boolean mPressed;

    private IconicsDrawable mFlickFavUnselectedDrawable;
    private IconicsDrawable mFlickFavSelectedDrawable;

    public FavImageView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        mFlickFavUnselectedDrawable = createIconicsDrawable(context, FontAwesome.Icon.faw_heart_o);
        mFlickFavSelectedDrawable = createIconicsDrawable(context, FontAwesome.Icon.faw_heart);
    }

    public FavImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public FavImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private IconicsDrawable createIconicsDrawable(Context ctx, FontAwesome.Icon icon) {
        return new IconicsDrawable(ctx)
            .icon(icon)
            .color(Color.RED)
            .sizeDp(48);
    }

    public boolean isFavPressed() {
        return mPressed;
    }

    public void setFavPressed(boolean pressed) {
        mPressed = pressed;
        if (pressed) {
            setImageDrawable(mFlickFavSelectedDrawable);
        } else {
            setImageDrawable(mFlickFavUnselectedDrawable);
        }
    }

    public void switchPressedMode() {
        setFavPressed(!mPressed);
    }
}
