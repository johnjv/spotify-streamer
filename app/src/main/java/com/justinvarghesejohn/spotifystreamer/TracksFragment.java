package com.justinvarghesejohn.spotifystreamer;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;


/**
 * A placeholder fragment containing a simple view.
 */

public class TracksFragment extends Fragment {

    private static final String LOG_TAG = Fragment.class.getSimpleName();

    private List<Track> searchResults = new ArrayList<Track>();
    private ListView tracksListView;
    private String artistId;
    private TracksAdapter tracksAdapter;
    private static final String ARTIST_ID = "artistId";
    static final String DETAIL_DATA = "DF_DATA";
    private String[] spotifyData;


    public TracksFragment() {
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(ARTIST_ID, artistId);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        Bundle arguments = getArguments();
        if (arguments != null) {
            String[] spotifyId = arguments.getStringArray(DETAIL_DATA);
            artistId = spotifyId[1];
        }

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            // Restore Spotify artist id from saved instance
            artistId = savedInstanceState.getString(ARTIST_ID);
            Log.i(LOG_TAG, artistId);
        }
//        else if (getActivity().getIntent() != null) {
//            String[] spotifyId = getActivity().getIntent().getStringArrayExtra(Intent.EXTRA_TEXT);
//            artistId = spotifyId[1];
//        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView =  inflater.inflate(R.layout.fragment_tracks, container, false);

        tracksListView = (ListView) rootView.findViewById(R.id.listview_tracks);
        tracksListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // TODO in part 2
            }
        });

        searchTracksByArtist(artistId);

        return rootView;
    }

    /**
     * Helper method to create and execute background task
     */
    private void searchTracksByArtist(String query) {
        SpotifySearchTask task = new SpotifySearchTask();
        task.execute(query);
    }

    /**
     * Background task for retrieving artists from Spotify API
     */
    public class SpotifySearchTask extends AsyncTask<String, Void, List<Track>>
    {
        @Override
        protected List<Track> doInBackground(String... params) {
            SpotifyApi api = new SpotifyApi();
            // api.setAccessToken("myAccessToken");
            SpotifyService service = api.getService();

            Map<String, Object> countryCode = new HashMap<String, Object>();
            countryCode.put("country", "us");

            artistId = params[0];

            try {

                Tracks results = service.getArtistTopTrack(artistId, countryCode);
                return results.tracks;
            }
            catch (RetrofitError error) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(List<Track> results) {
            if (results != null) {
                for (int i = 0; i < results.size(); i++) {
                    Track track = results.get(i);
                    Log.i(LOG_TAG, i + " " + track.name);
                }
                tracksListView.setVisibility(View.VISIBLE);

                searchResults = results;

                tracksAdapter = new TracksAdapter(getActivity(), results);
                tracksListView.setAdapter(tracksAdapter);

            }
        }
    }



}
