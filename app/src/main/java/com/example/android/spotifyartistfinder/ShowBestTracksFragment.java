package com.example.android.spotifyartistfinder;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Tracks;


/**
 * A placeholder fragment containing a simple view.
 */
public class ShowBestTracksFragment extends Fragment {

    private ArrayAdapter<TrackItem> track_list;

    public ShowBestTracksFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_show_best_tracks, container, false);

        this.track_list = new TopTracksArrayAdapter(
                getActivity(),
                R.layout.advanced_track_item,
                new ArrayList<TrackItem>());

        ListView list_view = (ListView) rootView.findViewById(R.id.list_tracks);
        list_view.setAdapter(this.track_list);
        return rootView;
    }




    @Override
     public void onStart() {
        super.onStart();
        new FetchTracksFromSpotify().execute(getActivity().getIntent().getStringExtra(Intent.EXTRA_TEXT));
    }

    public class FetchTracksFromSpotify  extends AsyncTask<String, Void, TrackItem[]> {
        private final static String LOCATION_ARGUMENT_LABEL="country";
        @Override
        protected TrackItem[] doInBackground(String... params) {
            if (params.length==0 || params[0].isEmpty())
                return null;

            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();

            Log.d(FetchTracksFromSpotify.class.getName(), "Artist id: "+params[0]);

            Map<String,Object> queryParameters = new HashMap<>();
            String country_code;
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            country_code=sharedPreferences.getString(getString(R.string.pref_location_key),getString(R.string.pref_location_default));

            queryParameters.put(LOCATION_ARGUMENT_LABEL,country_code);
            Tracks artistTopTrack = spotify.getArtistTopTrack(params[0],queryParameters);

            TrackItem [] result = new TrackItem[(int)Math.min(10,artistTopTrack.tracks.size())];



            for (int i = 0; i < (int)Math.min(10,artistTopTrack.tracks.size()); i++) {
                String name = artistTopTrack.tracks.get(i).name;
                String album =  artistTopTrack.tracks.get(i).album.name;
                Image image = null;
                if (artistTopTrack.tracks.get(i).album.images.size()>0)
                    image = artistTopTrack.tracks.get(i).album.images.get(0);

                result[i] = new TrackItem(name,album,image);
            }
            return result;
        }

        @Override
        protected void onPostExecute(TrackItem[] result) {
            if (result == null || result.length==0) {
                track_list.add(new TrackItem("No tracks found","",null));
            } else {
                track_list.clear();

                for (TrackItem track : result) {
                    track_list.add(track);
                }
            }
        }
    }

}
