package com.stundzia.jukebox.components;

import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.exceptions.SpotifyWebApiException;
import com.wrapper.spotify.model_objects.credentials.ClientCredentials;
import com.wrapper.spotify.model_objects.specification.AlbumSimplified;
import com.wrapper.spotify.model_objects.specification.Artist;
import com.wrapper.spotify.model_objects.specification.ArtistSimplified;
import com.wrapper.spotify.model_objects.specification.Paging;
import com.wrapper.spotify.requests.authorization.client_credentials.ClientCredentialsRequest;
import com.wrapper.spotify.requests.data.albums.GetSeveralAlbumsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchAlbumsRequest;
import com.wrapper.spotify.requests.data.search.simplified.SearchArtistsRequest;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class SpotifyComponent {

    @Value("${spotify.clientId}")
    String clientId;

    @Value("${spotify.clientSecret}")
    String clientSecret;

    SpotifyApi spotifyApi;

    Long tokenExpiryTime;

    @Synchronized
    public SpotifyApi getSpotifyApi() {
        if (spotifyApi == null || tokenExpired() != false) {
            SpotifyApi newApi = new SpotifyApi.Builder()
                    .setClientId(clientId)
                    .setClientSecret(clientSecret)
                    .build();
            ClientCredentialsRequest clientCredentialsRequest = newApi.clientCredentials().build();
            try {
                final ClientCredentials clientCredentials = clientCredentialsRequest.execute();

                // Set access token for further "spotifyApi" object usage
                newApi.setAccessToken(clientCredentials.getAccessToken());
                log.debug("Spotify token expires in: " + clientCredentials.getExpiresIn());
                tokenExpiryTime = System.currentTimeMillis() + (clientCredentials.getExpiresIn() * 1000);
                spotifyApi = newApi;
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
        return spotifyApi;
    }

        public AlbumSimplified[] searchAlbum (String query){
            SearchAlbumsRequest searchAlbumsRequest = getSpotifyApi().searchAlbums(query).limit(50).build();
            try {
                final Paging<AlbumSimplified> albumSimplifiedPaging = searchAlbumsRequest.execute();
                return albumSimplifiedPaging.getItems();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return new AlbumSimplified[]{};
        }

        public Artist[] searchArtist (String query){
            SearchArtistsRequest searchArtistsRequest = getSpotifyApi().searchArtists(query).limit(50).build();
            try {
                final Paging<Artist> artistPaging = searchArtistsRequest.execute();
                return artistPaging.getItems();
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
            return new Artist[]{};
        }

    private boolean tokenExpired() {
        if (tokenExpiryTime!=null && tokenExpiryTime-System.currentTimeMillis()>2000) {
            return false;
        }
        log.info("Spotify token expired.");
        return true;
    }
}
