package com.justinvarghesejohn.spotifystreamer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;


public class SearchActivity extends AppCompatActivity implements SearchFragment.Callback {

    private static final String DETAILFRAGMENT_TAG = "DFTAG";
    private boolean mTwoPane;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
//        removing because fragment is already created in the layout
//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .add(R.id.fragment_search, new SearchFragment())
//                    .commit();
//        }
        if (findViewById(R.id.tracks_detail_container) != null) {
            // tracks detail container is present which means we are in two pane mode
            mTwoPane = true;

            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.tracks_detail_container, new TracksFragment(), DETAILFRAGMENT_TAG)
                        .commit();
            }
        } else {
            mTwoPane = false;
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(String[] spotifyData) {
        if (mTwoPane) {
            // if in two-pane mode, show the detail view in this activity by
            // adding or replacing the detail fragment using a
            // fragment transaction.
            Bundle args = new Bundle();
            // putting String arry vs Parceable ?still unsure about this
            args.putStringArray(TracksFragment.DETAIL_DATA, spotifyData);

            TracksFragment fragment = new TracksFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.tracks_detail_container, fragment, DETAILFRAGMENT_TAG)
                    .commit();

        } else {
            Intent tracksIntent = new Intent(this, TracksActivity.class);
            tracksIntent.putExtra(Intent.EXTRA_TEXT, spotifyData);
            startActivity(tracksIntent);
        }
    }
}
