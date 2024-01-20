package org.caykhe.interactiveservice.repositories;


import jakarta.validation.constraints.NotNull;

import org.caykhe.interactiveservice.models.Bookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookmarkRepository extends JpaRepository<Bookmark, Integer> {


    Optional<Bookmark> findByUsername(@NotNull String username);

}
