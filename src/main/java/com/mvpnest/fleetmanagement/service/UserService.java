package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;

import java.util.List;
import java.util.UUID;

public interface UserService {

    User createUser(User user);

    User getUserById(UUID id);

    List<User> getAllUsers();

    List<User> getUsersByRole(RoleType role);

    User updateUser(UUID id, User user);

    void deleteUser(UUID id);
}
