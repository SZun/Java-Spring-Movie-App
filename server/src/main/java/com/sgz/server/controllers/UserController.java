package com.sgz.server.controllers;

import com.google.common.collect.Sets;
import com.sgz.server.entities.User;
import com.sgz.server.exceptions.*;
import com.sgz.server.models.UserVM;
import com.sgz.server.services.AuthService;
import com.sgz.server.services.RoleService;
import com.sgz.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AuthService authService;

    @PostMapping("/create")
    public ResponseEntity<UserVM> createUser(@Valid @RequestBody User toAdd) throws InvalidEntityException, InvalidNameException, InvalidAuthorityException {
        toAdd.setRoles(Sets.newHashSet(roleService.getRoleByAuthority("USER")));

        UserVM toReturn = new UserVM(userService.createUser(toAdd));

        return new ResponseEntity(toReturn, HttpStatus.CREATED);
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<List<UserVM>> getAllUsers() throws NoItemsException {
        List<UserVM> toReturn = new ArrayList<>();

        userService.getAllUsers().forEach(u -> toReturn.add(new UserVM(u)));

        return ResponseEntity.ok(toReturn);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserVM> getUserById(@PathVariable UUID id) throws InvalidIdException {
        UserVM toReturn = new UserVM(userService.getUserById(id));

        return ResponseEntity.ok(toReturn);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UserVM> updateUserById(@PathVariable UUID id, @Valid @RequestBody User toEdit) throws InvalidEntityException, InvalidIdException, InvalidAuthorityException, AccessDeniedException {
        try {
            User toCheck = userService.getUserByName(toEdit.getUsername());

            if (toCheck.getId() != authService.getUserId()) {
                return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }
        } catch (InvalidNameException | InvalidEntityException ex) {
        }

        toEdit.setId(id);
        toEdit.setRoles(Sets.newHashSet(roleService.getRoleByAuthority("USER")));

        UserVM toReturn = new UserVM(userService.editUser(toEdit, authService.getUserId()));

        return ResponseEntity.ok(toReturn);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<UUID> deleteUserById(@PathVariable UUID id) throws InvalidIdException, AccessDeniedException {
        userService.deleteUserById(id, authService.getUserId());

        return ResponseEntity.ok(id);
    }

}
