package com.newsreader.guardian.guardiantech;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Sam on 20/12/2016.
 * Each list item on the main screen displays relevant text and information about the story.

 Required fields include the title of the article and the name of the section that it belongs to.

 Optional fields (if available) : author name , date published

 fields for information about the story
 "type": "interactive",
 "sectionName": "Life and style",
 */

public class NewsStory {
    private final String TAG = NewsStory.class.getSimpleName();
    private String Title;
    private String ArticleType;
    private String SectionName;
    private String URL;
    private String mDateStr;
    private int daysFromPub;

    /**
     * Create a new NewsStory object
     * @param title news story headline
     * @param sectionName part of newspaper the story is from
     * @param articleType article / interactive etc.
     * @param date the story was printed
     */
    public NewsStory(String title, String sectionName, String articleType, String date, String url) {
        Title = title;
        SectionName = sectionName;
        ArticleType = articleType;
        URL = url;
        processDate(date);
    }

    /**
     * works out days between today and pub Date, saves this for display
     * @param dateStr from json in format "2013-05-15T10:00:00-0700"
     */
    private void processDate(String dateStr) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        Date mDate;
        try {
            mDate = dateFormat.parse(dateStr);
            mDateStr = (String) android.text.format.DateFormat.format("dd/MM", mDate);
            //Log.i(TAG, "date is: " + mDateStr);
        } catch (ParseException e) {
            Log.e(TAG, "something has gone wrong converting pub date string into SimpleDateFormat");
            e.printStackTrace();
            return;
        }
        // get today's date     http://stackoverflow.com/questions/2271131/display-the-current-time-and-date-in-an-android-application
        String currentDateTimeString = DateFormat.getDateInstance().format(new Date());
        // create calendar object from mDate
        Calendar pubCal = Calendar.getInstance();
        pubCal.setTime(mDate);
        // get num days difference      http://stackoverflow.com/questions/23323792/android-days-between-two-dates
        long msDiff = Calendar.getInstance().getTimeInMillis() - pubCal.getTimeInMillis();
        daysFromPub = (int) TimeUnit.MILLISECONDS.toDays(msDiff);

    }
    public String getTitle() { return Title; }
    public String getSectionName() { return SectionName; }
    public String getArticleType() { return ArticleType; }
    public int getDaysFromPub() { return daysFromPub; }
    public String getDate() { return mDateStr; }
    public String getUrl() { return URL; }

}
