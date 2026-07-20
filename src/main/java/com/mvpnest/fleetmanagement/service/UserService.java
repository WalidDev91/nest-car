package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.user.CreateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UpdateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.enums.RoleType;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserService {

    UserDTO createUser(CreateUserRequest request);

    UserDTO getUserById(UUID id);

    List<UserDTO> getAllUsers();

    List<UserDTO> getUsersByRole(RoleType role);

    List<UserDTO> getUsersByAdmin(UUID adminId);

    UserDTO updateUser(UUID id, UpdateUserRequest request);

    void changeRole(UUID id, RoleType role);

    void activateUser(UUID id);

    void deactivateUser(UUID id);

    void deleteUser(UUID id);

    UserDTO uploadImage(UUID id, MultipartFile file);

    UserDTO deleteImage(UUID id);

}
