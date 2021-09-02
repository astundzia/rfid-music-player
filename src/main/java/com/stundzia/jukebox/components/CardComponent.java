package com.stundzia.jukebox.components;

import com.stundzia.jukebox.repositories.CardRepository;
import com.stundzia.jukebox.entities.CardEntity;
import lombok.Synchronized;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.smartcardio.*;
import java.math.BigInteger;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Slf4j
@Component
public class CardComponent {

    @Autowired
    CardRepository cardRepository;

    @Autowired
    SpotifyComponent spotifyComponent;

    @Autowired
    SonosComponent sonosComponent;

    public static Boolean isRunning = false;

    private static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length * 2) + "X", new BigInteger(1, data));
    }

    public String scanCard() {
        Card card = null;
        try {
            TerminalFactory factory = TerminalFactory.getDefault();
            List<CardTerminal> terminals = factory.terminals().list();
            CardTerminal terminal = terminals.get(0);
            card = terminal.connect("*");
            CardChannel channel = card.getBasicChannel();
            ResponseAPDU response = channel.transmit(new CommandAPDU(new byte[]{(byte) 0xFF, (byte) 0xCA, (byte) 0x00, (byte) 0x00, (byte) 0x00}));
            return bin2hex(response.getData());
        } catch (Exception e) {
            log.debug("No card detected.");
            try {
                Thread.sleep(50);
            } catch (InterruptedException interruptedException) {
                log.error("Thread interrupted.", interruptedException);
            }
        } finally {
            if (card != null) {
                try {
                    card.disconnect(false);
                } catch (CardException e) {
                    log.debug("No card detected.");
                }
            }
        }
        return null;
    }

    public void startScanner() {
        Runnable scannerRunnable = () -> {
            isRunning = true;
            while (isRunning) {
                String card = scanCard();
                if (card != null) {
                    log.debug("Scanned card: {}", card);
                    processCard(card);
                }
            }
        };
        Thread scannerThread = new Thread(scannerRunnable);
        scannerThread.start();
    }

    @Synchronized
    private void processCard(String card) {
        log.info("Processing card {}", card);
        Optional<CardEntity> record = cardRepository.findById(card);
        if (record.isPresent()) {
            CardEntity cardEntity = record.get();
            log.debug("Found card in database, card: {} ", cardEntity.getCardUuid());
            if (cardEntity.getSpotifyUri() != null && cardEntity.getSpotifyUri().length() > 5) {
                log.info("Playing {} by {}", cardEntity.getSpotifyAlbum(), cardEntity.getSpotifyArtist());
                cardEntity.setLastScanned(new Date(Calendar.getInstance().getTime().getTime()));
                cardEntity.incrementTimesPlayed();
                cardRepository.save(cardEntity);
                sonosComponent.playSpotifyUri(cardEntity.getSpotifyUri());
            }
        } else {
            log.info("Didn't find card in database. Saving card: {}", card);
            CardEntity cardEntity = new CardEntity();
            cardEntity.setCardUuid(card);
            cardEntity.setLastUpdated(new Date(Calendar.getInstance().getTime().getTime()));
            cardEntity.setLastScanned(new Date(Calendar.getInstance().getTime().getTime()));
            cardRepository.save(cardEntity);
        }
    }
}