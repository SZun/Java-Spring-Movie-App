package com.sgz.server.repos;

import com.sgz.server.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepo extends JpaRepository<Customer, UUID> {

    boolean existsByName(String name);

    Optional<Customer> findByName(String name);

}
