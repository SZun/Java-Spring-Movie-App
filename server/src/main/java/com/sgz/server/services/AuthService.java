package com.sgz.server.services;

import com.sgz.server.repos.CustomerRepo;
import com.sgz.server.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepo userRepo;
    private final CustomerRepo customerRepo;

    @Autowired
    public AuthService(UserRepo userRepo, CustomerRepo customerRepo) {
        this.userRepo = userRepo;
        this.customerRepo = customerRepo;
    }

    public UUID getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).get().getId();
    }

    public UUID getCustomerId() {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        return customerRepo.findByName(name).get().getId();
    }

}
