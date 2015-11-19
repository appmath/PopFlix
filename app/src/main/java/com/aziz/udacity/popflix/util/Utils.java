package com.aziz.udacity.popflix.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import au.com.jtribe.shelly.Shelly;
import com.aziz.udacity.popflix.data.Repository;
import com.aziz.udacity.popflix.model.DetailWrapper;
import com.aziz.udacity.popflix.model.Flick;
import com.aziz.udacity.popflix.model.FlixWrapper;
import com.aziz.udacity.popflix.model.Review;
import com.aziz.udacity.popflix.model.Trailer;

import java.io.ByteArrayOutputStream;

import static com.aziz.udacity.popflix.data.FlixContract.*;

/**
 * Typical utility class.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public final class Utils {
    public static final double ASPECT_RATIO = 1.5;


    public static boolean isNetworkAvailable(Context ctx) {
        ConnectivityManager connectivityManager
            = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public static boolean isNetworkNotAvailable(Context ctx) {
        return !isNetworkAvailable(ctx);
    }

    public static FlixWrapper retrieveFlixFromDb(Context ctx) {
        Cursor cursor = Repository.retrieveAllFlix(ctx);
        FlixWrapper wrapper = new FlixWrapper();
        if (nonEmptyCursor(cursor)) {
            Flick flick;
            cursor.moveToFirst();
            do {
                flick = new Flick();
                flick.id = Integer.valueOf(getCursorStr(cursor, FlickEntry._ID)).intValue();
                byte[] posterBytes = cursor.getBlob(cursor.getColumnIndex(FlickEntry.COLUMN_POSTER_BLOB));
                flick.originalTitle = getCursorStr(cursor, FlickEntry.COLUMN_TITLE);
                wrapper.add(flick, posterBytes);
            } while (cursor.moveToNext());
        }
        return wrapper;
    }

    private static boolean nonEmptyCursor(Cursor cursor) {
        return cursor != null && cursor.getCount() > 0;
    }

    public static DetailWrapper retrieveFlickDetailsFromDb(Context ctx, Flick flick) {
        DetailWrapper wrapper = new DetailWrapper();

        Cursor cursor = Repository.retrieveFlickById(ctx, String.valueOf(flick.id));
        if (nonEmptyCursor(cursor)) {
            cursor.moveToFirst();
            do {
                wrapper.setTitle(cursor.getString(cursor.getColumnIndex(FlickEntry.COLUMN_TITLE)));
                wrapper.setReleaseYear(cursor.getString(cursor.getColumnIndex(FlickEntry.COLUMN_RELEASE_YEAR)));
                wrapper.setLength(cursor.getString(cursor.getColumnIndex(FlickEntry.COLUMN_LENGTH)));
                wrapper.setSynopsis(cursor.getString(cursor.getColumnIndex(FlickEntry.COLUMN_PLOT_SYNOPSIS)));
                wrapper.setUserRating(cursor.getString(cursor.getColumnIndex(FlickEntry.COLUMN_RATING)));
            } while (cursor.moveToNext());
        }

        cursor = Repository.retrieveReviewsByFlickId(ctx, String.valueOf(flick.id));
        if (nonEmptyCursor(cursor)) {
            cursor.moveToFirst();
            do {
                Review review = new Review();
                review.id = cursor.getString(cursor.getColumnIndex(ReviewEntry._ID));
                review.author = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_AUTHOR));
                review.content = cursor.getString(cursor.getColumnIndex(ReviewEntry.COLUMN_CONTENT));
                wrapper.addReview(review);
            } while (cursor.moveToNext());
        }

        cursor = Repository.retrieveTrailersByFlickId(ctx, String.valueOf(flick.id));
        if (nonEmptyCursor(cursor)) {
            cursor.moveToFirst();
            do {
                Trailer trailer = new Trailer();
                trailer.id = cursor.getString(cursor.getColumnIndex(TrailerEntry._ID));
                trailer.name = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_NAME));
                trailer.key = cursor.getString(cursor.getColumnIndex(TrailerEntry.COLUMN_KEY));
                wrapper.addTrailer(trailer);
            } while (cursor.moveToNext());
        }
        return wrapper;
    }


    private static String getCursorStr(Cursor cursor, String name) {
        return cursor.getString(cursor.getColumnIndex(name));
    }

    // convert from bitmap to byte array
    public static byte[] convertToBytes(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    // convert from bitmap to byte array
    public static byte[] convertToBytes(Drawable drawable) {
        Bitmap bitmap = convertToBitmap(drawable);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    public static Bitmap convertToBitmap(Drawable drawable) {
        Bitmap bitmap;

        if (drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            if (bitmapDrawable.getBitmap() != null) {
                return bitmapDrawable.getBitmap();
            }
        }

        if (drawable.getIntrinsicWidth() <= 0 || drawable.getIntrinsicHeight() <= 0) {
            bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888); // Single color bitmap will be created of 1x1 pixel
        } else {
            bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    public static Bitmap convertBytesToScaledBitmap(byte[] bytes, int width) {
        Bitmap source = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int newWidth = width / 2;
        int newHeight = (int) (newWidth * ASPECT_RATIO);

        return Bitmap.createScaledBitmap(source, newWidth, newHeight, true);
    }

    public static void shareTrailer(Context context, String message, String url) {
        Shelly.share(context)
            .text(message)
            .url(url)
            .send();
    }
}
