package com.aziz.udacity.popflix.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Flick that can be fetched from either a moviedb.org site or from the local DB.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */
public class FlixWrapper implements Parcelable{

    private ArrayList<Flick> mFlix = new ArrayList<>();
    private Map<Integer, byte[]> posterMap = new HashMap<>();

    public FlixWrapper() { }


    public FlixWrapper(List<Flick> flix) {
        mFlix.addAll(flix);
    }


    protected FlixWrapper(Parcel in) {
        mFlix = in.createTypedArrayList(Flick.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mFlix);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<FlixWrapper> CREATOR = new Creator<FlixWrapper>() {
        @Override
        public FlixWrapper createFromParcel(Parcel in) {
            return new FlixWrapper(in);
        }

        @Override
        public FlixWrapper[] newArray(int size) {
            return new FlixWrapper[size];
        }
    };

    public ArrayList<Flick> getFlix() {
        return mFlix;
    }

    public byte[] getPosterBytes(Integer id) {
        return posterMap.get(id);
    }

    public void add(Flick flick, byte[] posterBytes) {
        mFlix.add(flick);
        posterMap.put(flick.id, posterBytes);

    }
}
