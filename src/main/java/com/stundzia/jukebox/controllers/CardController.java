package com.stundzia.jukebox.controllers;

import com.stundzia.jukebox.repositories.CardRepository;
import com.stundzia.jukebox.entities.CardEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@RestController
public class CardController {

    @Autowired
    CardRepository cardRepository;

    @GetMapping("/cards")
    public List<CardEntity> getCards() {
        List<CardEntity> result = StreamSupport.stream(cardRepository.findAll().spliterator(), false).collect(Collectors.toList());
        return result;
    }

}