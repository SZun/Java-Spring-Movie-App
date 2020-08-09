package com.sgz.server.repos;

import com.sgz.server.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MovieRepo extends JpaRepository<Movie, UUID> {

    List<Movie> findAllByGenre_Id(UUID id);

}
