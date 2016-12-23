package com.newsreader.guardian.guardiantech;

import android.content.Context;

import java.util.List;

/**
 * Loads a list of news stories by using an AsyncTask to perform the
 * network request to the given URL.
 */
public class StoriesLoader extends android.support.v4.content.AsyncTaskLoader<List<NewsStory>> { //AsyncTaskLoader<List<NewsStory>> {

    /** Tag for log messages */
    private static final String LOG_TAG = StoriesLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link StoriesLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public StoriesLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * This is on a background thread.
     */
    @Override
    public List<NewsStory> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Perform the network request, parse the response, and extract a list of news stories.
        List<NewsStory> newsStories = QueryUtils.fetchNewsStoryData(mUrl);
        return newsStories;
    }
}
