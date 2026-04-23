package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // CREATE user
    @PostMapping
    public User create(@RequestBody User user) {
        return userService.createUser(user);
    }

    // GET user by ID
    @GetMapping("/{id}")
    public User getById(@PathVariable UUID id) {
        return userService.getUserById(id);
    }

    // GET all users (or filter by role optional)
    @GetMapping
    public List<User> getAll(@RequestParam(required = false) RoleType role) {
        if (role != null) {
            return userService.getUsersByRole(role);
        }
        return userService.getAllUsers();
    }

    // UPDATE user
    @PutMapping("/{id}")
    public User update(@PathVariable UUID id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    // DELETE user
    @DeleteMapping("/{id}")
    public void delete(@PathVariable UUID id) {
        userService.deleteUser(id);
    }
}
