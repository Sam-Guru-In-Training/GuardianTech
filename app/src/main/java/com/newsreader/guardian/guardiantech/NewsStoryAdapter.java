package com.newsreader.guardian.guardiantech;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * An {@link NewsStoryAdapter} knows how to create a list item layout for each earthquake
 * in the data source (a list of {@link NewsStory} objects).
 *
 * These list item layouts will be provided to an adapter view like ListView
 * to be displayed to the user.
 */
public class NewsStoryAdapter extends ArrayAdapter<NewsStory> {

    /**
     * The part of the location string from the USGS service that we use to determine
     * whether or not there is a location offset present ("5km N of Cairo, Egypt").
     */
    private static final String LOCATION_SEPARATOR = " of ";

    /**
     * Constructs a new {@link NewsStoryAdapter}.
     *
     * @param context of the app
     * @param newsStories is the list of NewsStory, which is the data source of the adapter
     */
    public NewsStoryAdapter(Context context, List<NewsStory> newsStories) {
        super(context, 0, newsStories);
    }

    /**
     * Returns a list item view that displays information about the earthquake at the given position
     * in the list of earthquakes.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if there is an existing list item view (called convertView) that we can reuse,
        // otherwise, if convertView is null, then inflate a new list item layout.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Find the earthquake at the given position in the list of earthquakes
        NewsStory currentNewsStory = getItem(position);

        TextView sectionView = (TextView) listItemView.findViewById(R.id.sectionName);
        sectionView.setText(currentNewsStory.getSectionName() + "  |  "
                + currentNewsStory.getArticleType());

        TextView titleView = (TextView) listItemView.findViewById(R.id.title);
        titleView.setText(currentNewsStory.getTitle());

//        TextView typeView = (TextView) listItemView.findViewById(R.id.articleType);
//        typeView.setText(currentNewsStory.getArticleType());

        TextView dateView = (TextView) listItemView.findViewById(R.id.date);
        dateView.setText(currentNewsStory.getDate());

        // Set the proper background color on the date circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) dateView.getBackground();
        // Get the appropriate background color based on the current earthquake magnitude
        int magnitudeColor = getDateColor(currentNewsStory.getDaysFromPub());
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(magnitudeColor);

        return listItemView;
    }

    /**
     * Return the color for the date circle based on the time since publication.
     *
     * @param daysFromPub of the article
     */
    private int getDateColor(int daysFromPub) {
        int dateColorResourceId;
        int monthsSincePub = (int) (daysFromPub / 30);
        switch (monthsSincePub) {
            case 0:
                dateColorResourceId = R.color.magnitude10plus;
                break;
            case 1:
                dateColorResourceId = R.color.magnitude9;
                break;
            case 2:
                dateColorResourceId = R.color.magnitude8;
                break;
            case 3:
                dateColorResourceId = R.color.magnitude7;
                break;
            case 4:
                dateColorResourceId = R.color.magnitude6;
                break;
            case 5:
                dateColorResourceId = R.color.magnitude5;
                break;
            case 6:
                dateColorResourceId = R.color.magnitude4;
                break;
            case 7:
                dateColorResourceId = R.color.magnitude3;
                break;
            case 8:
                dateColorResourceId = R.color.magnitude2;
                break;
            default:
                dateColorResourceId = R.color.magnitude1;
                break;
        }

        return ContextCompat.getColor(getContext(), dateColorResourceId);
    }

    /**
     * Return the formatted magnitude string showing 1 decimal place (i.e. "3.2")
     * from a decimal magnitude value.
     */
    private String formatMagnitude(double magnitude) {
        DecimalFormat magnitudeFormat = new DecimalFormat("0.0");
        return magnitudeFormat.format(magnitude);
    }

    /**
     * Return the formatted date string (i.e. "Mar 3, 1984") from a Date object.
     */
    private String formatDate(Date dateObject) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("LLL dd, yyyy");
        return dateFormat.format(dateObject);
    }

    /**
     * Return the formatted date string (i.e. "4:30 PM") from a Date object.
     */
    private String formatTime(Date dateObject) {
        SimpleDateFormat timeFormat = new SimpleDateFormat("h:mm a");
        return timeFormat.format(dateObject);
    }
}
