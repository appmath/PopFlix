/*
 * ******************************************************************************
 *   Copyright (c) 2013-2014 Gabriele Mariotti.
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *  *****************************************************************************
 */

package com.aziz.udacity.popflix.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.aziz.udacity.popflix.R;
import it.gmariotti.cardslib.library.internal.CardExpand;

/**
 * Expandable card for displaying movie reviews.
 * <p/>
 * Google field naming convention:
 * Non-public, non-static field names start with m.
 * Static field names start with s.
 * Other fields start with a lower case letter.
 * Public static final fields (constants) are ALL_CAPS_WITH_UNDERSCORES.
 *
 * @author Aziz Kadhi
 */

public class FlickReviewsExpandCard extends CardExpand {

    private final String mTitleHeader;
    private final String mMessage;

    public FlickReviewsExpandCard(Context context, String titleHeader, String message) {
        super(context, R.layout.reviews_expandable_card_inner);
        this.mTitleHeader = titleHeader;
        this.mMessage = message;

    }

    //You can set you properties here (example buttons visibility)

    @Override
    public void setupInnerViewElements(ViewGroup parent, View view) {
        if (view == null) return;
        //Retrieve TextView elements
        TextView headerTextView = (TextView) view.findViewById(R.id.header_expandable_textview);
        if (headerTextView != null) {
            headerTextView.setText(mTitleHeader);
        }
        TextView contentTextView = (TextView) view.findViewById(R.id.message_expandable_textview);
        if (contentTextView != null) {
            contentTextView.setText(mMessage);
        }
    }
}
