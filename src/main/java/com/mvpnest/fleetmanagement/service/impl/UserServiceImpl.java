package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> getUsersByRole(RoleType role) {
        return userRepository.findByRole(role);
    }

    @Override
    public User updateUser(UUID id, User user) {
        User existing = getUserById(id);

        // ⚠️ Update only relevant fields (adjust based on your User entity)
        existing.setFirstName(user.getFirstName());
        existing.setLastName(user.getLastName());
        existing.setEmail(user.getEmail());
        existing.setRole(user.getRole());

        // ⚠️ Be careful with password (don’t overwrite blindly later)
        existing.setPassword(user.getPassword());

        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(UUID id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }
}
