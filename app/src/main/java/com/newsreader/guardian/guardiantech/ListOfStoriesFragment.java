package com.newsreader.guardian.guardiantech;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import static com.newsreader.guardian.guardiantech.BuildConfig.API_KEY;

/**
 * Initiates a loader to get JSON from The Guardian
 * updates UI with info when the data has returned
 */

public class ListOfStoriesFragment extends Fragment
        implements LoaderManager.LoaderCallbacks<List<NewsStory>> {
    /** String for tabs */
    public static final String ARG_PAGE = "ARG_PAGE";
    private int mPage;

    private static final String TAG = ListOfStoriesFragment.class.getName();

    /** URL for earthquake data from the USGS dataset */
    private static final String GUARDIAN_REQUEST_URL =
            "http://content.guardianapis.com/search";

    /**
     * Constant value for the news story loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEWSSTORY_LOADER_ID = 1;

    /** Adapter for the list of newsStories */
    private NewsStoryAdapter mAdapter;

    /** TextView that is displayed when the list is empty */
    private TextView mEmptyStateTextView;

    /** more tabs stuff */
    public static ListOfStoriesFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        ListOfStoriesFragment fragment = new ListOfStoriesFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /** needed for tabs?! */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_stories, container, false);

        // Find a reference to the {@link ListView} in the layout
        ListView newsStoryListView = (ListView) rootView.findViewById(R.id.list);

        mEmptyStateTextView = (TextView) rootView.findViewById(R.id.empty_view);
        newsStoryListView.setEmptyView(mEmptyStateTextView);

        // Create a new adapter that takes an empty list of NewsStory as input
        mAdapter = new NewsStoryAdapter(getContext(), new ArrayList<NewsStory>());

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        newsStoryListView.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to a web browser
        // to open a website with more information about the selected NewsStory.
        newsStoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                // Find the current newsStory that was clicked on
                NewsStory currentNewsStory = mAdapter.getItem(position);

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsStoryUri = Uri.parse(currentNewsStory.getUrl());

                // Create a new intent to view the earthquake URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsStoryUri);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of network connectivity
        ConnectivityManager connMgr = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            // because this activity implements the LoaderCallbacks interface).
            //loaderManager.initLoader(NEWSSTORY_LOADER_ID, null, this);

            // number the loaderManager with mPage as may be requesting up to three lots of JSON for each tab
            loaderManager.initLoader(mPage, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            View loadingIndicator = rootView.findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int i, Bundle bundle) {

        String searchTerm;
        switch(mPage) {
            case 0:
                searchTerm = "windows";
                break;
            case 1:
                searchTerm = "android";
                break;
            default:
                searchTerm = "android wear";
        }

        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchTerm);
        uriBuilder.appendQueryParameter("api-key", API_KEY);
        uriBuilder.build();

        Log.wtf(TAG, uriBuilder.toString());
        Loader<List<NewsStory>> newsStories = new StoriesLoader(getContext(), uriBuilder.toString());
        return newsStories;
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        // Hide loading indicator because the data has been loaded
        View loadingIndicator = getView().findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No stories found."
        mEmptyStateTextView.setText(R.string.no_news_stories);

        // Clear the adapter of previous newsStories
        mAdapter.clear();

        // If there is a valid list of {@link NewsStory}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsStories != null && !newsStories.isEmpty()) {
            mAdapter.addAll(newsStories);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        // Loader reset, so we can clear out our existing data.
        mAdapter.clear();
    }


}
