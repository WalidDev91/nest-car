package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.user.CreateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UpdateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
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

    @PostMapping
    public UserDTO create(
            @RequestBody CreateUserRequest request
    ) {
        return userService.createUser(request);
    }

    @GetMapping("/{id}")
    public UserDTO getById(
            @PathVariable UUID id
    ) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserDTO> getAll(
            @RequestParam(required = false) RoleType role
    ) {
        if (role != null) {
            return userService.getUsersByRole(role);
        }

        return userService.getAllUsers();
    }

    @PutMapping("/{id}")
    public UserDTO update(
            @PathVariable UUID id,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable UUID id
    ) {
        userService.deleteUser(id);
    }
}
