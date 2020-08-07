package com.sgz.server.services;

import com.sgz.server.entities.Genre;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.GenreRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class GenreService {

    private final GenreRepo genreRepo;

    @Autowired
    public GenreService(GenreRepo genreRepo) {
        this.genreRepo = genreRepo;
    }

    public List<Genre> getAllGenres() throws NoItemsException {
        List<Genre> allGenres = genreRepo.findAll();

        if (allGenres.isEmpty()) throw new NoItemsException("No Items");

        return allGenres;
    }

    public Genre getGenreById(UUID id) throws InvalidIdException {
        Optional<Genre> toGet = genreRepo.findById(id);

        if (!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public Genre getGenreByName(String name) throws InvalidNameException, InvalidEntityException {
        if(name == null || name.trim().length() < 5
                || name.trim().length() > 50){
            throw new InvalidEntityException("Invalid Name");
        }

        Optional<Genre> toGet = genreRepo.findByName(name);

        if (!toGet.isPresent()) throw new InvalidNameException("Invalid Name");

        return toGet.get();
    }

    public Genre createGenre(Genre toAdd) throws InvalidEntityException, InvalidNameException {
        validateGenre(toAdd);
        checkExistsByName(toAdd.getName());

        return genreRepo.save(toAdd);
    }

    public Genre editGenre(Genre toEdit) throws InvalidEntityException, InvalidIdException {
        validateGenre(toEdit);
        checkExistsById(toEdit.getId());

        return genreRepo.save(toEdit);
    }

    public void deleteGenreById(UUID id) throws InvalidIdException {
        checkExistsById(id);

        genreRepo.deleteById(id);
    }

    private void validateGenre(Genre toUpsert) throws InvalidEntityException {
        if (toUpsert == null || toUpsert.getName().trim().length() < 5
                || toUpsert.getName().trim().length() > 50) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }

    private void checkExistsByName(String name) throws InvalidNameException {
        if (genreRepo.existsByName(name)) throw new InvalidNameException("Name already in use");
    }

    private void checkExistsById(UUID id) throws InvalidIdException {
        if (!genreRepo.existsById(id)) throw new InvalidIdException("Invalid Id");
    }

}
