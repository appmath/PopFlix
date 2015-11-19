package com.aziz.udacity.popflix.data;

import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import com.aziz.udacity.popflix.exception.RepositoryException;
import timber.log.Timber;

import java.util.ArrayList;
import java.util.Arrays;

import static com.aziz.udacity.popflix.data.FlixContract.*;


/**
 * Sweet one-liner persistence and query helper.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class Repository {



    public static Uri persistFlick(Context ctx, ContentValues cv) {
        return ctx.getContentResolver().insert(FlickEntry.CONTENT_URI, cv);
    }


    public static Uri persistReview(Context ctx, ContentValues cv) {
        return ctx.getContentResolver().insert(ReviewEntry.CONTENT_URI, cv);
    }


    public static Uri persistTrailer(Context ctx, ContentValues cv) {
        return ctx.getContentResolver().insert(TrailerEntry.CONTENT_URI, cv);
    }


    /**
     * Persists reviews.
     *
     * @param ctx
     * @param cvs
     * @return number of newly created rows.
     */
    public static int persistReviews(Context ctx, ContentValues[] cvs) {
        return ctx.getContentResolver().bulkInsert(ReviewEntry.CONTENT_URI, cvs);
    }


    /**
     * Persists trailers.
     *
     * @param ctx
     * @param cvs
     * @return number of newly created rows.
     */
    public static int persistTrailers(Context ctx, ContentValues[] cvs) {
        return ctx.getContentResolver().bulkInsert(TrailerEntry.CONTENT_URI, cvs);
    }

    public static void persistFlickWithDetails(Context ctx, FlickBundleWrapper wrapper) {
        String errorMessage = null;
        try {
            ContentValues flickCV = wrapper.getFlickContentValues();
            Uri uri = persistFlick(ctx, flickCV);
            if (ContentUris.parseId(uri) == -1) {
                errorMessage = "Failed to persist flick: " + flickCV;
            }
            errorMessage = persistReview(ctx, wrapper, errorMessage);
            errorMessage = persistTrailer(ctx, wrapper, errorMessage);
        } catch (Exception e) {
            throw new RepositoryException("Failed to persist a flick and its details", e);
        }

        if (errorMessage != null) {
            throw new RepositoryException(errorMessage);
        }
    }


    private static String persistReview(Context ctx, FlickBundleWrapper wrapper, String errorMsg) {
        String errorMessage = errorMsg;
        ContentValues[] reviewCVs = wrapper.getReviewContentValues();
        if (reviewCVs.length == 1) {
            Uri uri = persistReview(ctx, reviewCVs[0]);
            if (ContentUris.parseId(uri) == -1) {
                if (errorMessage == null) {
                    errorMessage = "Failed to persist review: " + Arrays.toString(reviewCVs);
                } else {
                    errorMessage = errorMessage + ", Failed to persist review: " + Arrays.toString(reviewCVs);
                }

            }
        } else if (reviewCVs.length > 1) {
            final int numberOfReviewsSaved = persistReviews(ctx, reviewCVs);
            if (numberOfReviewsSaved != reviewCVs.length) {
                if (errorMessage == null) {
                    errorMessage = "Failed to persist reviews: " + Arrays.toString(reviewCVs);
                } else {
                    errorMessage = errorMessage + ", Failed to persist reviews: " + Arrays.toString(reviewCVs);
                }
            }
        } else {
            Timber.w("There were no reviews to persist");
        }
        return errorMessage;
    }
    
    
    private static String persistTrailer(Context ctx, FlickBundleWrapper wrapper, String errorMsg) {
        String errorMessage = errorMsg;
        ContentValues[] trailerCVs = wrapper.getTrailerContentValues();
        if (trailerCVs.length == 1) {
            Uri uri = persistTrailer(ctx, trailerCVs[0]);
            if (ContentUris.parseId(uri) == -1) {
                if (errorMessage == null) {
                    errorMessage = "Failed to persist trailer: " + Arrays.toString(trailerCVs);
                } else {
                    errorMessage = errorMessage + ", Failed to persist trailer: " + Arrays.toString(trailerCVs);
                }

            }
        } else if (trailerCVs.length > 1) {
            final int numberOfTrailersSaved = persistTrailers(ctx, trailerCVs);
            if (numberOfTrailersSaved != trailerCVs.length) {
                if (errorMessage == null) {
                    errorMessage = "Failed to persist trailers: " + Arrays.toString(trailerCVs);
                } else {
                    errorMessage = errorMessage + ", Failed to persist trailers: " + Arrays.toString(trailerCVs);
                }
            }
        } else {
            Timber.w("There were no trailers to persist");
        }
        return errorMessage;
    }


    public static Cursor retrieveFlickById(Context ctx, String id) {
        return ctx.getContentResolver().query(
            FlickEntry.CONTENT_URI,
            null,
            FlickEntry.ID_SELECTION,
            new String[]{id},
            null
        );
    }
    public static boolean isPersistedFlick(Context ctx, String id) {
        Cursor cursor = ctx.getContentResolver().query(
            FlickEntry.CONTENT_URI,
            null,
            FlickEntry.ID_SELECTION,
            new String[]{id},
            null
        );
        if (cursor != null && cursor.getCount() > 0) {
            cursor.moveToFirst();
            return id.equals(cursor.getString(cursor.getColumnIndex(FlickEntry._ID)));
        }
        return false;
    }


    public static Cursor retrieveTrailersByFlickId(Context ctx, String id) {
        return ctx.getContentResolver().query(
            TrailerEntry.CONTENT_URI,
            TrailerEntry.PROJECTION,
            null,
            new String[]{id},
            null
        );
    }

    public static Cursor retrieveReviewsByFlickId(Context ctx, String id) {
        return ctx.getContentResolver().query(
            ReviewEntry.CONTENT_URI,
            ReviewEntry.PROJECTION,
            null,
            new String[]{id},
            null
        );
    }

    public static Cursor retrieveAllFlix(Context ctx) {
        return ctx.getContentResolver().query(
            FlickEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );
    }

    public static Cursor retrieveAllTrailers(Context ctx) {
        return ctx.getContentResolver().query(
            TrailerEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );
    }

    public static Cursor retrieveAllReviews(Context ctx) {
        return ctx.getContentResolver().query(
            ReviewEntry.CONTENT_URI,
            null,
            null,
            null,
            null
        );
    }

    public static int deleteFlick(Context ctx, String id) {
        return ctx.getContentResolver().delete(FlickEntry.CONTENT_URI, FlickEntry.ID_SELECTION, new String[]{id});
    }

    public static void deleteFlickWithDetails(Context ctx, FlickBundleWrapper wrapper) {
        StringBuilder errorBuilder = new StringBuilder();

        try {
            if (wrapper.hasReviews()) {
                String[] reviewIds = wrapper.getReviewIds();
                int deletedRows = deleteReviews(ctx, reviewIds);
                if (deletedRows != reviewIds.length) {
                    errorBuilder.append("Failed to delete these reviews ").append(Arrays.toString(reviewIds)).append("\n");
                }
            }

            if (wrapper.hasTrailers()) {
                String[] trailerIds = wrapper.getTrailerIds();
                int deletedRows = deleteTrailers(ctx, trailerIds);
                if (deletedRows != trailerIds.length) {
                    errorBuilder.append("Failed to delete these trailers ").append(Arrays.toString(trailerIds)).append("\n");
                }
            }

            final int expectOne = deleteFlick(ctx, wrapper.getFlickId());
            if (expectOne != 1) {
                errorBuilder.append("Failed to delete this flick ").append(wrapper.getFlickId());

            }
        } catch (Exception e) {
            throw new RepositoryException("Failed to delete all or part of flick " + wrapper.getTitle() + " with id: "
                + wrapper.getFlickId(), e);
        }

        if (errorBuilder.length() > 0) {
            throw new RepositoryException(errorBuilder.toString());
        }
    }

    /**
     *
     * @param ctx
     * @param trailerIds
     * @return
     */
    public static int deleteTrailers(Context ctx, String[] trailerIds) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentProviderOperation operation;
        for (String id : trailerIds) {
            operation = ContentProviderOperation
                .newDelete(TrailerEntry.CONTENT_URI)
                .withSelection(FlixBaseColumns.ID_SELECTION, new String[]{id})
                .build();

            operations.add(operation);
        }

        try {
            final ContentProviderResult[] contentProviderResults = ctx.getContentResolver().applyBatch(FlixContract.CONTENT_AUTHORITY, operations);
            return contentProviderResults.length;

        } catch (Exception e) {
            Timber.e("Failed to delete trailers with these id's: " + Arrays.toString(trailerIds), e);
        }
        return 0;
    }

    public static int deleteReviews(Context ctx, String[] reviewIds) {
        ArrayList<ContentProviderOperation> operations = new ArrayList<>();
        ContentProviderOperation operation;
        for (String id : reviewIds) {
            operation = ContentProviderOperation
                .newDelete(ReviewEntry.CONTENT_URI)
                .withSelection(FlixBaseColumns.ID_SELECTION, new String[]{id})
                .build();

            operations.add(operation);
        }

        try {
            final ContentProviderResult[] contentProviderResults = ctx.getContentResolver().applyBatch(FlixContract.CONTENT_AUTHORITY, operations);
            return contentProviderResults.length;

        } catch (Exception e) {
            Timber.e("Failed to delete reviews with these id's: " + Arrays.toString(reviewIds), e);
        }
        return 0;
    }

    public static boolean hasFlixData(Context ctx) {
        Cursor cursor = retrieveAllFlix(ctx);
        return cursor != null && cursor.getCount() > 0;
    }


}
