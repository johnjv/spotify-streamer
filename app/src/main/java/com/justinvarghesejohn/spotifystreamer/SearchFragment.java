package com.justinvarghesejohn.spotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
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
    private List<Artist> searchResults = new ArrayList<Artist>();
    private ListView artistsListView;
    private EditText searchBar;
    private String artistName;

    public SearchFragment() {
    }

    private static final String LOG_TAG = Fragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_search, container, false);

        searchBar = (EditText) rootView.findViewById(R.id.search_bar);
        searchBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    artistsListView.setVisibility(View.GONE);

                    artistName = searchBar.getText().toString();
                    searchArtists(artistName);

                    // dismiss keyboard
                    InputMethodManager imm = (InputMethodManager) getActivity().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
                    searchBar.setText("");
                    return true;
                }
                return false;
            }
        });

        artistsListView = (ListView) rootView.findViewById(R.id.listview_search);
        artistsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Artist currentArtist = searchResults.get(position);
                String[] spotifyData = { currentArtist.name, currentArtist.id };
                Intent tracksIntent = new Intent(getActivity(), TracksActivity.class);
                tracksIntent.putExtra(Intent.EXTRA_TEXT, spotifyData);
                startActivity(tracksIntent);
            }
        });

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
                artistsListView.setVisibility(View.VISIBLE);

                searchResults = results;
                searchAdapter = new SearchAdapter(getActivity(), results);
                artistsListView.setAdapter(searchAdapter);

            }
        }
    }



}
