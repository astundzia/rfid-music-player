package com.stundzia.jukebox.pojo;

import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AlbumItem {

    private String id;
    private String text;
    private ArtistSimplified[] artist;
    private String uri;

}