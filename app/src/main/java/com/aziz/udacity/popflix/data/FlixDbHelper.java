package com.aziz.udacity.popflix.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.aziz.udacity.popflix.data.FlixContract.*;
import static com.aziz.udacity.popflix.data.FlixContract.FlickEntry;

/**
 * A helper class for creating and managing Flix db.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlixDbHelper extends SQLiteOpenHelper {

    public static final int DB_VERSION = 2;

    public static final String DB_NAME = "flix.db";


    public FlixDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_FLICK_TABLE = "CREATE TABLE "
            + FlickEntry.TABLE_NAME
            + " ("
            + FlickEntry._ID + " INTEGER PRIMARY KEY, "
            + FlickEntry.COLUMN_TITLE + " TEXT NOT NULL, "
            + FlickEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, "
            + FlickEntry.COLUMN_RELEASE_YEAR + " STRING NOT NULL, "
            + FlickEntry.COLUMN_LENGTH + " STRING NOT NULL, "
            + FlickEntry.COLUMN_RATING + " REAL NOT NULL, "
            + FlickEntry.COLUMN_PLOT_SYNOPSIS + " STRING NOT NULL, "
            + FlickEntry.COLUMN_POSTER_BLOB + " BLOB NOT NULL"
            + ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE "
            + ReviewEntry.TABLE_NAME
            + " ("
            + ReviewEntry._ID + " STRING PRIMARY KEY, "
            + ReviewEntry.COLUMN_FLICK_ID + " INTEGER NOT NULL, "
            + ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, "
            + ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, "

            + " FOREIGN KEY (" + ReviewEntry.COLUMN_FLICK_ID + ") REFERENCES "
            + FlickEntry.TABLE_NAME + " (" + FlickEntry._ID + ")"
            + ");";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE "
            + TrailerEntry.TABLE_NAME
            + " ("
            + TrailerEntry._ID + " STRING PRIMARY KEY, "
            + TrailerEntry.COLUMN_FLICK_ID + " INTEGER NOT NULL, "
            + TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, "
            + TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, "
            + TrailerEntry.COLUMN_TYPE + " TEXT NOT NULL, "

            + " FOREIGN KEY (" + TrailerEntry.COLUMN_FLICK_ID + ") REFERENCES "
            + FlickEntry.TABLE_NAME + " (" + FlickEntry._ID + ")"
            + ");";

        db.execSQL(SQL_CREATE_FLICK_TABLE);
        db.execSQL(SQL_CREATE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_TRAILER_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FlickEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TrailerEntry.TABLE_NAME);
        onCreate(db);


    }
}
