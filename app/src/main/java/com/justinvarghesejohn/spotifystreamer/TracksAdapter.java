package com.justinvarghesejohn.spotifystreamer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import kaaes.spotify.webapi.android.models.Track;

public class TracksAdapter extends ArrayAdapter<Track> {

    private static final String LOG_TAG = TracksAdapter.class.getSimpleName();

    /**
     * This is a custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the List is the data we want
     * to populate into the lists
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param searchResults A List of Spotify Artist objects to display in a list
     */
    public TracksAdapter(Context context, List<Track> searchResults) {
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
        // gets the Track Object from ArrayAdapter at correct position
        Track result = getItem(position);
        String trackName = result.name;
        String trackAlbumName = result.album.name;
        String imageUrl = null;

        // check in case the album has no images available
        if (result.album.images != null && result.album.images.size() > 0) {
            imageUrl = result.album.images.get(0).url;
        }

        // If this is a new View object we're getting, then inflate the layout.
        // If not, this view already has the layout inflated from a previous call to getView,
        // and we modify the View widgets as usual.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_track, parent, false);
        }

        ImageView iconView = (ImageView) convertView.findViewById(R.id.list_item_track_album_img);
        Picasso.with(this.getContext()).load(imageUrl).into(iconView);

        TextView trackNameView = (TextView) convertView.findViewById(R.id.list_item_track_text);
        trackNameView.setText(trackName);

        TextView albumNameView = (TextView) convertView.findViewById(R.id.list_item_track_album_text);
        albumNameView.setText(trackAlbumName);

        return convertView;
    }

}
