package com.stundzia.jukebox.pojo;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ArtistItem {
    private String id;
    private String text;
}