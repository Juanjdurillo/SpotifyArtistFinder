package com.example.android.spotifyartistfinder;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistsPager;
import kaaes.spotify.webapi.android.models.Image;


/**
 * A placeholder fragment containing a simple view.
 */
public class ArtistFinderFragment extends Fragment {



    private ArrayAdapter<ArtistItem> artistItems;
    static Map<String, ArtistItem[]> cache = new HashMap<>(); // caching search so not using network
    static List<String> recent_searches    = new LinkedList<>(); // contains the N more recent searches
                                                            // use linked list because ill be adding at
                                                            // the end and removing from beginning
    private static final int CACHE_SIZE    = 10;

    public ArtistFinderFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_artist_finder, container, false);

        this.artistItems = new ArtistArrayAdapter(
                getActivity(),
                R.layout.advanced_list_item_artist,
                new ArrayList<ArtistItem>());

        ListView list_view = (ListView) rootView.findViewById(R.id.list_view_artist);
        list_view.setAdapter(this.artistItems);

        list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String artistId = artistItems.getItem(position).getId();
                Intent intent = (new Intent(getActivity(),
                        ShowBestTracks.class)).putExtra(Intent.EXTRA_TEXT, artistId);
                startActivity(intent);
            }
        });

/*
        final SearchView searchView = (SearchView) rootView.findViewById(R.id.artist_search);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                new FetchArtistOnSpotify().execute(newText);
                return true;
            }
        });
*/
        EditText editText = (EditText) rootView.findViewById(R.id.artist_search);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new FetchArtistOnSpotify().execute(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        return rootView;

    }


    public class FetchArtistOnSpotify  extends AsyncTask<String, Void, ArtistItem[]> {

        @Override
        protected ArtistItem[] doInBackground(String... params) {
            Log.v(FetchArtistOnSpotify.class.getName(),"Application started");
            if (params.length==0)
                return null;

            if (params[0].isEmpty())
                return null;

            // A valid string is introduced, search need to be perform

            // Two options:
            // we have it cache, then we use it
            // we do not have it cache, then we need to use the API. If the cache is full
            // we remove the oldest in the cache (maybe more recently use policy would be
            // a better option here

            // case 1
            if (cache.containsKey(params[0])) {
                Log.v(ArtistFinderFragment.class.getName(),"Using cached search");
                return cache.get(params[0]);
            }

            // case 2
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            ArtistsPager artistPager = spotify.searchArtists(params[0]);

            List<Artist> allArtist = artistPager.artists.items;

            ArtistItem[] resultArray = new ArtistItem[Math.min(10, allArtist.size())];


            for (int i = 0; i < (int)Math.min(10,allArtist.size()); i++) {
                String name = allArtist.get(i).name;
                String id = allArtist.get(i).id;
                Image image;
                if (allArtist.get(i).images.size()>0)
                    image = allArtist.get(i).images.get(0);
                else
                    image = null;
                resultArray[i] = new ArtistItem(image,name,id);
            }

            // cache this search
            cache.put(params[0],resultArray);
            recent_searches.add(params[0]);


            if (recent_searches.size() > CACHE_SIZE)
                cache.remove(recent_searches.remove(0));//remove the oldest search from both cache and recent_search_list


            return resultArray;
        }

        @Override
        protected void onPostExecute(ArtistItem[] result) {
            if (result != null) {

                if (result.length>0) {
                    artistItems.clear();

                    for (ArtistItem artist : result) {
                        artistItems.add(artist);
                    }
                } else if (result.length==0) {
                    artistItems.clear();
                    artistItems.add(new ArtistItem(null, getString(R.string.not_found),null));
                }
            }
        }
    }

}
