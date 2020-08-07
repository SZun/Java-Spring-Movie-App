package com.sgz.server.services;

import com.sgz.server.entities.Rental;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.RentalRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class RentalService {

    private final RentalRepo rentalRepo;

    @Autowired
    public RentalService(RentalRepo rentalRepo) {
        this.rentalRepo = rentalRepo;
    }

    public List<Rental> getAllRentals() throws NoItemsException {
        List<Rental> allRentals = rentalRepo.findAll();

        if(allRentals.isEmpty()) throw new NoItemsException("No Items");

        return allRentals;
    }

    public List<Rental> getAllRentalsByCustomerId(UUID customerId, UUID authId) throws NoItemsException, AccessDeniedException {
        checkIsAuthorized(customerId, authId);

        List<Rental> allRentals = rentalRepo.findAllByCustomer_Id(customerId);

        if(allRentals.isEmpty()) throw new NoItemsException("No Items");

        return allRentals;
    }

    public Rental createRental(Rental toAdd) throws InvalidEntityException {
        validateRental(toAdd);
        toAdd.setRentalDate(LocalDate.now());
        toAdd.setReturnDate(LocalDate.now().plusDays(7));

        return rentalRepo.save(toAdd);
    }

    public Rental getRentalById(UUID id, UUID authId) throws InvalidIdException, AccessDeniedException {
        Optional<Rental> toGet = rentalRepo.findById(id);

        if(!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        Rental toReturn = toGet.get();

        checkIsAuthorized(toReturn.getCustomer().getId(), authId);

        return toReturn;
    }

    private void validateRental(Rental toAdd) throws InvalidEntityException {
        if(toAdd == null || toAdd.getMovie() == null || toAdd.getCustomer() == null) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }

    public void checkIsAuthorized(UUID customerId, UUID authId) throws AccessDeniedException {
        if(!customerId.equals(authId)) throw new AccessDeniedException("Access Denied");
    }

}
