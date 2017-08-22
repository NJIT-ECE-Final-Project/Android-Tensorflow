package com.njit.ece.senior_project.medical_sensor.activtytrackerfrontend.util;

import android.view.View;
import android.widget.ListView;

/**
 * Created by David Etler on 8/22/2017.
 */

public class ViewUtil {

    // from https://stackoverflow.com/questions/25821744/get-all-listview-itemsrows
    public static View getViewByPosition(int position, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (position < firstListItemPosition || position > lastListItemPosition ) {
            return listView.getAdapter().getView(position, listView.getChildAt(position), listView);
        } else {
            final int childIndex = position - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }

}
