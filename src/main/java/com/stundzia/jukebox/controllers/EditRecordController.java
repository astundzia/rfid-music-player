package com.stundzia.jukebox.controllers;

import com.stundzia.jukebox.components.SpotifyComponent;
import com.stundzia.jukebox.entities.CardEntity;
import com.stundzia.jukebox.pojo.CardRecord;
import com.stundzia.jukebox.repositories.CardRepositoryPageable;
import jdk.nashorn.internal.runtime.options.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Controller
public class EditRecordController {

    @Autowired
    CardRepositoryPageable cardRepository;

    @Autowired
    SpotifyComponent spotifyComponent;

    @RequestMapping(value = "/edit/{uuid}", method = RequestMethod.GET)
    public String editRecord(@PathVariable("uuid") String cardUuid, ModelMap model) {
        Optional<CardEntity> record = cardRepository.findById(cardUuid);
        if (record.isPresent()) {
            model.put("card", record.get());
            return "editrecord";
        } else {
            return "redirect:/";
        }
    }

    @RequestMapping(value = "/edit/{uuid}", method = RequestMethod.POST)
    public String save(CardRecord card, Model model) {
        Optional<CardEntity> record = cardRepository.findById(card.getCardUuid());
        if (record.isPresent()) {
            CardEntity entity = record.get();
            entity.setLastUpdated(new Date(Calendar.getInstance().getTime().getTime()));
            entity.setSpotifyAlbum(card.getSpotifyAlbum());
            entity.setSpotifyArtist(card.getSpotifyArtist());
            entity.setSpotifyUri(card.getSpotifyUri());
            cardRepository.save(entity);
        }
        return "redirect:/";
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String delete(CardRecord card, Model model) {
        Optional<CardEntity> record = cardRepository.findById(card.getCardUuid());
        if (record.isPresent()) {
            CardEntity entity = record.get();
            cardRepository.delete(entity);
        }
        return "redirect:/";
    }
}
