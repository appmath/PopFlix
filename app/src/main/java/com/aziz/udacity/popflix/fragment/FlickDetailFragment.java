package com.aziz.udacity.popflix.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import butterknife.Bind;
import butterknife.ButterKnife;
import com.aziz.udacity.popflix.R;
import com.aziz.udacity.popflix.event.ShowShareMenuItemEvent;
import com.aziz.udacity.popflix.model.Review;
import com.aziz.udacity.popflix.model.Trailer;
import com.aziz.udacity.popflix.ui.DetailThumbnailCard;
import com.aziz.udacity.popflix.ui.FlickBundleWrapper;
import com.aziz.udacity.popflix.ui.FlickReviewsExpandCard;
import com.aziz.udacity.popflix.ui.YouTubeThumbnailCard;
import com.aziz.udacity.popflix.util.Utils;
import de.greenrobot.event.EventBus;
import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.view.CardListView;
import timber.log.Timber;

import java.util.ArrayList;


/**
 * Fragment for displaying the details for a movie.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */

public class FlickDetailFragment extends Fragment {

    public static final String FRAG_NAME = FlickDetailFragment.class.getSimpleName();

    public static final String YOUTUBE = "http://www.youtube.com/watch?v=";


    @Bind(R.id.trailer_card_list_view)
    CardListView mTrailerCardListView;

    private boolean mDualPane;
    private MenuItem mMenuShareItem;
    private boolean mHasTrailers;
    private String mTrailerUrl;
    private String mTrailerMsg;


    public FlickDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @param bundle used to create a new FlickBundleWrapper.
     * @return a new FlickDetailFragment instance.
     */
    public static FlickDetailFragment newInstance(Bundle bundle) {
        FlickDetailFragment fragment = new FlickDetailFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_flick_detail, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        final Bundle args = getBundle();
        if (args != null) {
            ArrayList<Card> cards = new ArrayList<>();

            FlickBundleWrapper wrapper = new FlickBundleWrapper(args);

            // Add card that contains the movie details like title, poster, etc
            DetailThumbnailCard detailCard = new DetailThumbnailCard(getActivity(), wrapper);
            cards.add(detailCard);

            mDualPane = wrapper.getDualPane();

            // Add cards with the trailer titles and YouTube links
            if (wrapper.hasTrailers()) {
                addTrailerCards(cards, wrapper);
            }

            // Add card that contains the reviews
            if (wrapper.hasReviews()) {
                addReviewsCard(wrapper.getReviews(), cards);
            }

            CardArrayAdapter mCardArrayAdapter = new CardArrayAdapter(getActivity(), cards);
            if (mTrailerCardListView != null) {
                mTrailerCardListView.setAdapter(mCardArrayAdapter);
            }
        }
        return view;
    }

    private void addTrailerCards(ArrayList<Card> cards, FlickBundleWrapper wrapper) {
        final ArrayList<Trailer> trailerList = wrapper.getTrailers();

        for (final Trailer trailer : trailerList) {
            YouTubeThumbnailCard youTubeCard = new YouTubeThumbnailCard(getActivity(), trailer.name);
            youTubeCard.setOnClickListener(new Card.OnCardClickListener() {
                @Override
                public void onClick(Card card, View view) {
                    getContext().startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(YOUTUBE + trailer.key)));
                }
            });
            cards.add(youTubeCard);
        }

        mHasTrailers = true;
        mTrailerMsg = getString(R.string.trailer_for) + wrapper.getTitle();
        mTrailerUrl = YOUTUBE + trailerList.get(0).key;
    }


    /**
     * Share menu item can be displayed on the main activity (FlixGalleryActivity) if its a tablet (dualPane-mode) or
     * on FlickDetailActivity if it's a phone.
     */
    private void showShareMenuItem() {
        if (mDualPane) {
            // Show the share menu item on the FlixGalleryActivity
            EventBus.getDefault().post(new ShowShareMenuItemEvent(mTrailerMsg, mTrailerUrl)); // menu should be created inside the fragment
        } else { // Show it on the parent activity.
            if (mMenuShareItem != null && mHasTrailers) {
                mMenuShareItem.setVisible(true);
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (mDualPane) {
            super.onCreateOptionsMenu(menu, inflater);
        } else {
            inflater.inflate(R.menu.menu_detail_flix, menu);
            mMenuShareItem = menu.findItem(R.id.menu_detail_share);
        }
        showShareMenuItem();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_detail_share: // share the trailer.
                Utils.shareTrailer(getActivity(), mTrailerMsg, mTrailerUrl);
                Timber.d("Share!");
                return true;
            case android.R.id.home:      // back arrow was clicked close the activity
                getActivity().finish();
                return true;
        }
        return false;
    }

    private Bundle getBundle() {
        return getArguments() != null ? getArguments() : getActivity().getIntent().getExtras();
    }


    private void addReviewsCard(final ArrayList<Review> reviewList, ArrayList<Card> cards) {
        StringBuilder builder = new StringBuilder();
        for (Review review : reviewList) {
            builder.append(review.author).append(":").append("\n").append(review.content).append("\n\n");
        }
        if (builder.length() > 0) {
            String reviewTitle = reviewList.size() == 1 ? " review" : " reviews";
            cards.add(createReviewsExpandableCard("Reviews", reviewList.size() + reviewTitle, builder.toString()));
        }
    }

    /**
     * This method builds a standard header with a custom expand/collpase
     */
    private Card createReviewsExpandableCard(String titleHeader, String title, String message) {

        //Create a Card
        Card card = new Card(getActivity());

        //Create a CardHeader
        CardHeader header = new CardHeader(getActivity());

        //Set the header title
        header.setTitle(titleHeader);

        //Set visible the expand/collapse button
        header.setButtonExpandVisible(true);

        //Add Header to card
        card.addCardHeader(header);

        //This provides a simple (and useless) expand area
        FlickReviewsExpandCard expand = new FlickReviewsExpandCard(getActivity(), title, message);
        //Add Expand Area to Card
        card.addCardExpand(expand);

        //Swipe
        card.setSwipeable(true);

        //Animator listener
        card.setOnExpandAnimatorEndListener(new Card.OnExpandAnimatorEndListener() {
            @Override
            public void onExpandEnd(Card card) {

            }
        });

        card.setOnCollapseAnimatorEndListener(new Card.OnCollapseAnimatorEndListener() {
            @Override
            public void onCollapseEnd(Card card) {

            }
        });
        return card;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}
