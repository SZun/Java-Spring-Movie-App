package com.sgz.server.models;

import com.sgz.server.entities.User;
import lombok.Getter;

import java.util.UUID;

public class UserVM {

    @Getter
    private UUID id;

    @Getter
    private String username;

    public UserVM(User user) {
        id = user.getId();
        username = user.getUsername();
    }
}