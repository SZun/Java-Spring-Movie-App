package com.sgz.server.repos;

import com.sgz.server.entities.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface GenreRepo extends JpaRepository<Genre, UUID> {

    boolean existsByName(String name);

    Optional<Genre> findByName(String name);

}
