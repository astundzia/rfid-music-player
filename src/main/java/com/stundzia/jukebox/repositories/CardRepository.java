package com.stundzia.jukebox.repositories;

import com.stundzia.jukebox.entities.CardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepository extends CrudRepository<CardEntity, String> {
}