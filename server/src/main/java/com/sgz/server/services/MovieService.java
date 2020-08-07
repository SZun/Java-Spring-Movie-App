package com.sgz.server.services;

import com.sgz.server.entities.Movie;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.MovieRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepo movieRepo;

    @Autowired

    public MovieService(MovieRepo movieRepo) {
        this.movieRepo = movieRepo;
    }

    public List<Movie> getAllMovies() throws NoItemsException {
        List<Movie> allMovies = movieRepo.findAll();

        if(allMovies.isEmpty()) throw new NoItemsException("No Items");

        return allMovies;
    }

    public List<Movie> getAllByGenreName(String name) throws InvalidEntityException, NoItemsException {
        if(name == null || name.trim().length() < 5
                || name.trim().length() > 50){
            throw new InvalidEntityException("Invalid Name");
        }

        List<Movie> allMovies = movieRepo.findAllByGenre_Name(name);

        if(allMovies.isEmpty()) throw new NoItemsException("No Items");

        return allMovies;
    }

    public Movie getMovieById(UUID id) throws InvalidIdException {
        Optional<Movie> toGet = movieRepo.findById(id);

        if(!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public Movie createMovie(Movie toAdd) throws InvalidEntityException {
        validateMovie(toAdd);

        return movieRepo.save(toAdd);
    }

    public Movie updateMovie(Movie toEdit) throws InvalidEntityException, InvalidIdException {
        validateMovie(toEdit);
        checkExistsById(toEdit.getId());

        return movieRepo.save(toEdit);
    }

    public void deleteMovieById(UUID id) throws InvalidIdException {
        checkExistsById(id);

        movieRepo.deleteById(id);
    }

    private void validateMovie(Movie toUpsert) throws InvalidEntityException {
        if(toUpsert == null || toUpsert.getTitle().trim().length() < 5
                            || toUpsert.getTitle().trim().length() > 255
                            || toUpsert.getGenre() == null
                            || toUpsert.getQty() > 99999
                            || toUpsert.getQty() < 0
                            || toUpsert.getDailyRate().compareTo(new BigDecimal("99.991")) == 1
                            || toUpsert.getDailyRate().compareTo(new BigDecimal("00.00")) == -1)
            throw new InvalidEntityException("Invalid Entity");
    }

    private void checkExistsById(UUID id) throws InvalidIdException {
        if(!movieRepo.existsById(id)) throw new InvalidIdException("Invalid Id");
    }
}
