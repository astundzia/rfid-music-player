package com.stundzia.jukebox.components;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.RestTemplate;

@Controller
@Slf4j
public class SonosComponent {

    @Value("${sonos.mainSpeaker}")
    String mainSpeaker;

    @Value("${sonos.speakerGroup}")
    String[] speakers;

    @Value("${sonos.volume}")
    int volume;

    @Value("${sonos.setVolume}")
    boolean setVolume;

    public void playSpotifyUri(String uri) {
        try {
            log.debug("Clearing queue.");


            log.debug("Setting a speaker group and clearing the queue.");
            for (String speaker : speakers) {
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        try {
                            String joinSpeakerUrl = "http://localhost:5005/" + speaker + "/join/" + mainSpeaker;
                            RestTemplate restTemplate = new RestTemplate();
                            String result = restTemplate.getForObject(joinSpeakerUrl, String.class);
                            log.debug("Speaker join result: {}", result);
                        } catch (Exception e) {
                            log.error("Unable to join speaker group. Possible already joined.");
                        }
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }

            Thread.sleep(1000);

            String clearQueueUrl = "http://localhost:5005/" + mainSpeaker + "/clearqueue";
            RestTemplate restTemplate = new RestTemplate();
            String result = restTemplate.getForObject(clearQueueUrl, String.class);
            log.debug("Clear queue result: {}", result);
            Thread.sleep(2000);


            if (setVolume) {
                for (String speaker : speakers) {
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            log.debug("Setting speaker {} volume to {}", speaker, volume);
                            String volumeUrl = "http://localhost:5005/" + speaker + "/volume/" + volume;
                            RestTemplate restTemplate = new RestTemplate();
                            String result = restTemplate.getForObject(volumeUrl, String.class);
                            log.debug("Volume set result: {}", result);
                        }
                    };
                    Thread thread = new Thread(runnable);
                    thread.start();
                }

                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        log.debug("Setting speaker {} volume to {}", mainSpeaker, volume);
                        String volumeUrl = "http://localhost:5005/" + mainSpeaker + "/volume/" + volume;
                        RestTemplate restTemplate = new RestTemplate();
                        String result = restTemplate.getForObject(volumeUrl, String.class);
                        log.debug("Volume set result: {}", result);
                    }
                };
                Thread thread = new Thread(runnable);
                thread.start();
            }

            Thread.sleep(5000);

            log.debug("Playing spotify URI: {}", uri);
            String spotifyUrl = "http://localhost:5005/" + mainSpeaker + "/spotify/now/" + uri;
            restTemplate = new RestTemplate();
            result = restTemplate.getForObject(spotifyUrl, String.class);
            log.debug("Play spotify URI result: {}", result);
        } catch (Exception e) {
            log.error("Unable to play spotify URI: {}.", uri, e);
        }
    }
}
