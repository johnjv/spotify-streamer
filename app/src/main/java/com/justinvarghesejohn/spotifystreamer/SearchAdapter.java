package com.justinvarghesejohn.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import kaaes.spotify.webapi.android.models.Artist;

public class SearchAdapter extends ArrayAdapter<Artist> {

    private static final String LOG_TAG = SearchAdapter.class.getSimpleName();

    /**
     * This is a custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param searchResults A List of Spotify Artist objects to display in a list
     */
    public SearchAdapter(Context context, List<Artist> searchResults) {
        super(context, 0, searchResults);
    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position    The AdapterView position that is requesting a view
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // gets the Artist Object from ArrayAdapter at correct position
        Artist result = getItem(position);

        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_artist, parent, false);
        }

        // ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_artist_img);
        // iconView.setImageResource(androidFlavor.image);

        TextView versionNameView = (TextView) convertView.findViewById(R.id.list_item_artist_text);
        versionNameView.setText(result.name);

        return convertView;
    }

}
