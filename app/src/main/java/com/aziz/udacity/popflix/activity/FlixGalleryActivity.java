package com.aziz.udacity.popflix.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.event.ShowShareMenuItemEvent;
import com.aziz.udacity.popflix.fragment.FlixGalleryFragment;
import com.aziz.udacity.popflix.ui.FlixSortType;
import com.aziz.udacity.popflix.util.Utils;
import de.greenrobot.event.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Description
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */

public class FlixGalleryActivity extends AppCompatActivity {

    public static final int MOST = 0;
    public static final int MOST_POPULAR = 0;
    public static final int HIGHEST_RATED = 1;
    public static final int FAVORITES = 2;
    public static final String SORT_TYPE = "SORT_TYPE";
    public static final String GALLERY_FRAGMENT = "GALLERY_FRAGMENT";
    public static final String TRAILER_URL = "TRAILER_URL";
    public static final String TRAILER_MSG = "TRAILER_MSG";
    private FlixSortType mSortType;
    private MenuItem mMenuShareItem;
    private String mTrailerUrl;
    private String mTrailerMsg;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flix_gallery);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        View spinnerContainer = LayoutInflater.from(this).inflate(R.layout.toolbar_spinner,
            toolbar, false);
        ActionBar.LayoutParams lp = new ActionBar.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        toolbar.addView(spinnerContainer, lp);

        SortSpinnerAdapter spinnerAdapter = new SortSpinnerAdapter();

        Spinner spinner = (Spinner) spinnerContainer.findViewById(R.id.toolbar_spinner);
        spinner.setAdapter(spinnerAdapter);
        if (!Utils.isNetworkAvailable(this)) {
            spinner.setSelection(FAVORITES);
        } else if (mSortType != null) {
            spinner.setSelection(mSortType.getSelection());
        }

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case MOST_POPULAR:
                        launchFragment(FlixSortType.MOST_POPULAR, savedInstanceState);
                        break;
                    case HIGHEST_RATED:
                        launchFragment(FlixSortType.HIGHEST_RATED, savedInstanceState);
                        break;
                    case FAVORITES:
                        launchFragment(FlixSortType.FAVORITES, savedInstanceState);
                        break;
                    default:
                        launchFragment(FlixSortType.MOST_POPULAR, savedInstanceState);
                }
                if (mMenuShareItem != null) {
                    mMenuShareItem.setVisible(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void launchFragment(FlixSortType sortType, Bundle savedInstanceState) {

        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction tx = fm.beginTransaction();
        Fragment fragment;

        if (!sortType.equals(mSortType) || savedInstanceState == null) {
            fragment = FlixGalleryFragment.newInstance(sortType);
            mSortType = sortType;
        } else {
            fragment = fm.findFragmentByTag(GALLERY_FRAGMENT);
        }
        tx.replace(R.id.flick_gallery_container, fragment, GALLERY_FRAGMENT);
        tx.commit();

    }

    /**
     * Called when the EventBus's EventBus.getDefault().post(ShowShareMenuItemEvent) is invoked.
     * Source of the event: FlickDetailFragment
     *
     * @param event
     */

    @SuppressWarnings("unused")
    public void onEventMainThread(ShowShareMenuItemEvent event) {
        mTrailerUrl = event.getTrailerUrl();
        if (mMenuShareItem != null && mTrailerUrl != null) {
            mMenuShareItem.setVisible(true);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_flix, menu);
        mMenuShareItem = menu.findItem(R.id.menu_main_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_main_share) {
            Utils.shareTrailer(this, mTrailerMsg, mTrailerUrl);
            return true;
        }
        return false;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(SORT_TYPE, mSortType);
        outState.putString(TRAILER_URL, mTrailerUrl);
        outState.putString(TRAILER_MSG, mTrailerMsg);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        EventBus.getDefault().register(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    private class SortSpinnerAdapter extends BaseAdapter {
        public static final String SPINNER_ITEM_DROPDOWN = "DROPDOWN";
        public static final String SPINNER_ITEM_ACTIONBAR = "NON_DROPDOWN";
        private List<FlixSortType> mItems = new ArrayList<>();

        public SortSpinnerAdapter() {
            mItems.add(FlixSortType.MOST_POPULAR);
            mItems.add(FlixSortType.HIGHEST_RATED);
            mItems.add(FlixSortType.FAVORITES);
        }


        @Override
        public int getCount() {
            return mItems.size();
        }

        @Override
        public Object getItem(int position) {
            return mItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getDropDownView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals(SPINNER_ITEM_DROPDOWN)) {
                view = getLayoutInflater().inflate(R.layout.toolbar_spinner_item_dropdown, parent, false);
                view.setTag(SPINNER_ITEM_DROPDOWN);
            }

            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));

            return view;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null || !view.getTag().toString().equals(SPINNER_ITEM_ACTIONBAR)) {
                view = getLayoutInflater().inflate(R.layout.
                    toolbar_spinner_item_actionbar, parent, false);
                view.setTag(SPINNER_ITEM_ACTIONBAR);
            }
            TextView textView = (TextView) view.findViewById(android.R.id.text1);
            textView.setText(getTitle(position));
            return view;
        }

        private String getTitle(int position) {
            return position >= MOST && position < mItems.size() ? getString(mItems.get(position).getTitle()) : "";
        }
    }


}
