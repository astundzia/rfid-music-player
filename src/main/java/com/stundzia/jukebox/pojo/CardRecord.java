package com.stundzia.jukebox.pojo;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardRecord {
    String cardUuid;
    String spotifyUri;
    String spotifyArtist;
    String spotifyAlbum;
}
