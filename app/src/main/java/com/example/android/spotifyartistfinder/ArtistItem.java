package com.example.android.spotifyartistfinder;

import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Juanjo
 */
public class ArtistItem {

    private Image image;
    private String name;
    private String id;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArtistItem(Image image, String name, String id) {
        this.image = image;
        this.name = name;
        this.id = id;
    }
}
