package com.justinvarghesejohn.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;

import java.util.List;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */

public class SearchFragment extends Fragment {

    private SearchAdapter searchAdapter;
    private List<Artist> searchResults;
    private View view;
    private ListView artistsListView;
    private EditText searchBar;
    private String artistName;
    private SpotifySearchTask spotifySearch;

    public SearchFragment() {
    }

    private static final String LOG_TAG = Fragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_search, container, false);

        searchAdapter = new SearchAdapter(getActivity(), searchResults);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_search);
        listView.setAdapter(searchAdapter);

        return rootView;
    }

    /**
     * Helper method to create and execute background task
     */
    private void searchArtists(String query) {
        SpotifySearchTask task = new SpotifySearchTask();
        task.execute(query);
    }

    /**
     * Background task for retrieving artists from Spotify API
     */
    public class SpotifySearchTask extends AsyncTask<String, Void, List<Artist>>
    {
        @Override
        protected List<Artist> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            // api.setAccessToken("myAccessToken");
            SpotifyService service = api.getService();

            try {
                ArtistsPager results = service.searchArtists(params[0]);
                return results.artists.items;
            }
            catch (RetrofitError error) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Artist> results) {
            if (results != null) {
                for(int i = 0; i < results.size(); i++) {
                    Artist artist = results.get(i);
                    Log.i(LOG_TAG, i + " " + artist.name);
                }
            }
            else {

            }
        }
    }

}
