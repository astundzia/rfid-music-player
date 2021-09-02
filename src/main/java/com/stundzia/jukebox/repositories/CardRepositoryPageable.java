package com.stundzia.jukebox.repositories;

import com.stundzia.jukebox.entities.CardEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CardRepositoryPageable extends PagingAndSortingRepository<CardEntity, String> {
}