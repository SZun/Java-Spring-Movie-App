package com.sgz.server.repos;

import com.sgz.server.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RentalRepo extends JpaRepository<Rental, UUID> {

    List<Rental> findAllByUser_Id(UUID id);

}
