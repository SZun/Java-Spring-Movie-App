package com.sgz.server.controllers;

import com.sgz.server.entities.Role;
import com.sgz.server.entities.User;
import com.sgz.server.exceptions.InvalidEntityException;
import com.sgz.server.exceptions.InvalidIdException;
import com.sgz.server.services.AdminService;
import com.sgz.server.services.RoleService;
import com.sgz.server.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private UserService userService;

    @PutMapping("/users/{id}/roles")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<User> editUserRoles(@PathVariable UUID id, @RequestBody Set<UUID> roleIds) throws InvalidIdException, InvalidEntityException {
        User toEdit = userService.getUserById(id);
        Set<Role> roles = new HashSet<>();
        for (UUID roleId : roleIds) {
            roles.add(roleService.getRoleById(roleId));
        }
        toEdit.setRoles(roles);
        return ResponseEntity.ok(adminService.updateUserRoles(toEdit));
    }

}
