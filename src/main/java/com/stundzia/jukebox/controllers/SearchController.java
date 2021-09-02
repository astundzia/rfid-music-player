package com.stundzia.jukebox.controllers;

import com.stundzia.jukebox.components.SpotifyComponent;
import com.stundzia.jukebox.pojo.AlbumItem;
import com.stundzia.jukebox.pojo.ArtistItem;
import com.stundzia.jukebox.repositories.CardRepositoryPageable;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

@RestController
public class SearchController {

    @Autowired
    CardRepositoryPageable cardRepository;

    @Autowired
    SpotifyComponent spotifyComponent;

    @GetMapping("/search/artist")
    public List<ArtistItem> searchArtist(@RequestParam(value = "q", required = false) String query) {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyList();
        }

        return Arrays.stream(spotifyComponent.searchArtist(query.toLowerCase()))
                .filter(artist -> artist
                        .getName().toLowerCase()
                        .contains(query.toLowerCase()))
                .limit(100)
                .map(this::mapToArtistItem)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/album")
    public List<AlbumItem> searchAlbum(@RequestParam(value = "q", required = false) String query) {
        if (StringUtils.isEmpty(query)) {
            return Collections.emptyList();
        }

        return Arrays.stream(spotifyComponent.searchAlbum(query.toLowerCase()))
                .filter(album -> album
                        .getName().toLowerCase()
                        .contains(query.toLowerCase()))
                .limit(100)
                .map(this::mapToAlbumItem)
                .collect(Collectors.toList());
    }

    private AlbumItem mapToAlbumItem(AlbumSimplified album) {
        return AlbumItem.builder()
                .id(album.getId())
                .text(album.getName())
                .uri(album.getUri())
                .artist(album.getArtists())
                .build();
    }

    private ArtistItem mapToArtistItem(Artist artist) {
        return ArtistItem.builder()
                .id(artist.getId())
                .text(artist.getName())
                .build();
    }
}
