package com.sgz.server.services;

import com.sgz.server.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuthService {

    private final UserRepo userRepo;

    @Autowired
    public AuthService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    public UUID getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepo.findByUsername(username).get().getId();
    }

}
