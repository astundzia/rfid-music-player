package com.stundzia.jukebox.controllers;

import com.stundzia.jukebox.repositories.CardRepositoryPageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({ "/", "/index" })
public class IndexController {

    @Autowired
    CardRepositoryPageable cardRepository;

    @GetMapping
    public String index(ModelMap model, @SortDefault("spotifyArtist") Pageable pageable){
        model.addAttribute("page", cardRepository.findAll(pageable));
        return "index";
    }
}
