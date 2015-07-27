package com.example.android.spotifyartistfinder;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by JuanJose on 6/21/2015.
 */
public class TrackItem {
    private String name;
    private String album;
    private Image image;

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public TrackItem(String trackName, String trackAlbum, Image image) {
        this.name = trackName;
        this.album = trackAlbum;
        this.image = image;
    }


}
