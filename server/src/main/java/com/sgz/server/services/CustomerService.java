package com.sgz.server.services;


import com.sgz.server.entities.Customer;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.exceptions.InvalidNameException;
import com.sgz.server.exceptions.NoItemsException;
import com.sgz.server.repos.CustomerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CustomerService {

    private final CustomerRepo customerRepo;

    @Autowired
    public CustomerService(CustomerRepo customerRepo) {
        this.customerRepo = customerRepo;
    }

    public List<Customer> getAllCustomer() throws NoItemsException {
        List<Customer> allCustomers = customerRepo.findAll();

        if(allCustomers.isEmpty()) throw new NoItemsException("No Items");

        return allCustomers;
    }

    public Customer getCustomerById(UUID id) throws InvalidIdException {
        Optional<Customer> toGet = customerRepo.findById(id);

        if(!toGet.isPresent()) throw new InvalidIdException("Invalid Id");

        return toGet.get();
    }

    public Customer getCustomerByName(String name) throws InvalidEntityException, InvalidNameException {

        if(name == null || name.trim().length() < 5
                || name.trim().length() > 50){
            throw new InvalidEntityException("Invalid Name");
        }

        Optional<Customer> toGet = customerRepo.findByName(name);

        if(!toGet.isPresent()) throw new InvalidNameException("Invalid Name");

        return toGet.get();
    }

    public Customer createCustomer(Customer toAdd) throws InvalidEntityException, InvalidNameException {
        validateCustomer(toAdd);
        checkExistsByName(toAdd.getName());

        return customerRepo.save(toAdd);
    }

    public Customer updateCustomerById(Customer toEdit, UUID userId) throws InvalidEntityException, InvalidIdException, AccessDeniedException {
        validateCustomer(toEdit);
        checkAuthorization(toEdit.getId(), userId);
        checkExistsById(toEdit.getId());

        return customerRepo.save(toEdit);
    }

    public void deleteCustomerById(UUID writeId, UUID authId) throws AccessDeniedException {
        checkAuthorization(writeId, authId);

        customerRepo.deleteById(writeId);
    }

    private void validateCustomer(Customer toUpsert) throws InvalidEntityException {
        if(toUpsert == null || toUpsert.getPhone().trim().length() != 10
                            || !toUpsert.getPhone().matches("\"\\\\d{10}\"")
                            || toUpsert.getName().trim().length() < 5
                            || toUpsert.getName().trim().length() > 50) {
            throw new InvalidEntityException("Invalid Entity");
        }
    }

    private void checkExistsByName(String name) throws InvalidNameException {
        if(customerRepo.existsByName(name)) throw new InvalidNameException("Invalid Name");
    }

    private void checkExistsById(UUID id) throws InvalidIdException {
        if(!customerRepo.existsById(id)) throw new InvalidIdException("Invalid id");
    }

    private void checkAuthorization(UUID writeId, UUID authId) throws AccessDeniedException {
        if (!writeId.equals(authId)) throw new AccessDeniedException("Access Denied");
    }


}
