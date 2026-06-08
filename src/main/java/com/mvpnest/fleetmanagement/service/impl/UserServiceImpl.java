package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.user.CreateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UpdateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.mapper.UserMapper;
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
    private final UserMapper userMapper;

    @Override
    public UserDTO createUser(CreateUserRequest request) {

        User admin = null;

        if (request.getAdminId() != null) {
            admin = userRepository.findById(request.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
        }

        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(request.getPassword())
                .phone(request.getPhone())
                .role(request.getRole())
                .admin(admin)
                .build();

        return userMapper.toDTO(
                userRepository.save(user)
        );
    }

    @Override
    public UserDTO getUserById(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return userMapper.toDTO(user);
    }

    @Override
    public List<UserDTO> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public List<UserDTO> getUsersByRole(RoleType role) {

        return userRepository.findByRole(role)
                .stream()
                .map(userMapper::toDTO)
                .toList();
    }

    @Override
    public UserDTO updateUser(UUID id, UpdateUserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        User admin = null;

        if (request.getAdminId() != null) {
            admin = userRepository.findById(request.getAdminId())
                    .orElseThrow(() -> new RuntimeException("Admin not found"));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmail(request.getEmail());
        user.setPhone(request.getPhone());
        user.setRole(request.getRole());
        user.setValidate(request.isValidate());
        user.setAdmin(admin);

        return userMapper.toDTO(
                userRepository.save(user)
        );
    }

    @Override
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        userRepository.delete(user);
    }
}
