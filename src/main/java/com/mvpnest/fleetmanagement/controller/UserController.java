package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.user.*;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {


    private final UserService userService;


    // ==========================================================
    // CREATE
    // ==========================================================

    @PostMapping
    public UserDTO create(@RequestBody CreateUserRequest request) {

        return userService.createUser(request);

    }


    // ==========================================================
    // GET ONE
    // ==========================================================

    @GetMapping("/{id}")
    public UserDTO getById(@PathVariable UUID id) {

        return userService.getUserById(id);

    }


    // ==========================================================
    // GET ALL / FILTER ROLE
    // ==========================================================

    @GetMapping
    public List<UserDTO> getAll(@RequestParam(required = false) RoleType role, @RequestParam(required = false) UUID adminId) {

        if (adminId != null) {
            return userService.getUsersByAdmin(adminId);
        }

        if (role != null) {
            return userService.getUsersByRole(role);
        }

        return userService.getAllUsers();
    }


    // ==========================================================
    // UPDATE
    // ==========================================================

    @PutMapping("/{id}")
    public UserDTO update(@PathVariable UUID id, @RequestBody UpdateUserRequest request) {

        return userService.updateUser(id, request);
    }


    // ==========================================================
    // ACTIVATE
    // ==========================================================

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(@PathVariable UUID id) {

        userService.activateUser(id);

        return ResponseEntity.ok().build();

    }


    // ==========================================================
    // DEACTIVATE
    // ==========================================================

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(@PathVariable UUID id) {

        userService.deactivateUser(id);

        return ResponseEntity.ok().build();

    }


    // ==========================================================
    // CHANGE ROLE
    // ==========================================================

    @PatchMapping("/{id}/role")
    public ResponseEntity<Void> changeRole(@PathVariable UUID id, @RequestBody RoleRequest request) {

        userService.changeRole(id, request.getRole());

        return ResponseEntity.ok().build();

    }


    // ==========================================================
    // DELETE
    // ==========================================================

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {

        userService.deleteUser(id);
        return ResponseEntity.noContent().build();

    }


    // ==========================================================
    // INNER DTO FOR ROLE CHANGE
    // ==========================================================

    @PostMapping("/{id}/image")
    public UserDTO uploadImage(@PathVariable UUID id, @RequestParam("file") MultipartFile file) {
        return userService.uploadImage(id, file);
    }

    // ==========================================================
// UPLOAD PROFILE IMAGE
// ==========================================================

    @DeleteMapping("/{id}/image")
    public UserDTO deleteImage(@PathVariable UUID id) {
        return userService.deleteImage(id);
    }

// ==========================================================
// DELETE PROFILE IMAGE
// ==========================================================

    // ==========================================================
// UPDATE SUPERVISOR
// ==========================================================
    @PutMapping("/{id}/supervisor")
    public UserDTO assignSupervisor(@PathVariable UUID id, @RequestBody UpdateSupervisorRequest request) {
        return userService.updateSupervisor(id, request);
    }

    @PatchMapping("/me/profile")
    public UserDTO updateOwnProfile(@RequestBody UpdateProfileRequest request, Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        return userService.updateProfile(currentUser.getId(), request);

    }

// ==========================================================
// UPDATE OWN PROFILE (self-service, no role/admin change)
// ==========================================================

    @PatchMapping("/me/password")
    public ResponseEntity<Void> changeOwnPassword(@RequestBody ChangePasswordRequest request, Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        userService.changePassword(currentUser.getId(), request);

        return ResponseEntity.ok().build();

    }

// ==========================================================
// CHANGE OWN PASSWORD
// ==========================================================

    public static class RoleRequest {

        private RoleType role;


        public RoleType getRole() {

            return role;

        }

        public void setRole(RoleType role) {

            this.role = role;
        }

    }

}