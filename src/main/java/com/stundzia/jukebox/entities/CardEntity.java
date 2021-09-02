package com.stundzia.jukebox.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CardEntity {

    @Id
    private String cardUuid;

    private String spotifyUri;

    private String spotifyArtist;

    private String spotifyAlbum;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastScanned;

    private Integer timesPlayed;

    public void incrementTimesPlayed() {
        if (timesPlayed==null) {
            timesPlayed = 0;
        }
        timesPlayed = timesPlayed + 1;
    }
}
