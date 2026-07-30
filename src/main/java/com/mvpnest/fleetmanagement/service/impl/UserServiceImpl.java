package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.user.CreateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UpdateSupervisorRequest;
import com.mvpnest.fleetmanagement.dto.user.UpdateUserRequest;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.mapper.UserMapper;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {


    private final UserRepository userRepository;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;


    @Value("${app.upload.dir}")
    private String uploadDir;


    // ==========================================================
    // CREATE USER
    // ==========================================================

    @Override
    public UserDTO createUser(CreateUserRequest request) {


        User admin = null;


        if (request.getAdminId() != null) {

            admin = userRepository.findById(request.getAdminId()).orElseThrow(() -> new RuntimeException("Admin not found"));
        }


        User user = User.builder()

                .firstName(request.getFirstName())

                .lastName(request.getLastName())

                .email(request.getEmail())

                .password(passwordEncoder.encode(request.getPassword()))

                .phone(request.getPhone())

                .role(request.getRole())

                .admin(admin)

                .isValidate(false)

                .build();


        return userMapper.toDTO(userRepository.save(user));

    }


    // ==========================================================
    // GET BY ID
    // ==========================================================

    @Override
    public UserDTO getUserById(UUID id) {


        User user = userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));


        return userMapper.toDTO(user);

    }


    // ==========================================================
    // GET ALL
    // ==========================================================

    @Override
    public List<UserDTO> getAllUsers() {


        return userRepository.findAll()

                .stream()

                .map(userMapper::toDTO)

                .toList();

    }


    // ==========================================================
    // GET BY ROLE
    // ==========================================================

    @Override
    public List<UserDTO> getUsersByRole(RoleType role) {


        return userRepository.findByRole(role)

                .stream()

                .map(userMapper::toDTO)

                .toList();

    }


    // ==========================================================
    // UPDATE USER
    // ==========================================================

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

        user.setAdmin(admin);


        return userMapper.toDTO(userRepository.save(user));

    }


    // ==========================================================
    // CHANGE ROLE
    // ==========================================================

    @Override
    public void changeRole(UUID id, RoleType role) {


        User user = userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setRole(role);


        userRepository.save(user);

    }


    // ==========================================================
    // ACTIVATE USER
    // ==========================================================

    @Override
    public void activateUser(UUID id) {


        User user = userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setValidate(true);


        userRepository.save(user);

    }


    // ==========================================================
    // DEACTIVATE USER
    // ==========================================================

    @Override
    public void deactivateUser(UUID id) {


        User user = userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));


        user.setValidate(false);


        userRepository.save(user);

    }


    // ==========================================================
    // DELETE USER
    // ==========================================================

    @Override
    public void deleteUser(UUID id) {


        User user = userRepository.findById(id)

                .orElseThrow(() -> new RuntimeException("User not found"));


        userRepository.delete(user);

    }

    @Override
    public UserDTO uploadImage(UUID id, MultipartFile file) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        try {

            Path folder = Paths.get(uploadDir, "users");
            Files.createDirectories(folder);

            // Delete old image if it exists
            if (user.getImageUrl() != null) {
                Files.deleteIfExists(folder.resolve(user.getImageUrl()));
            }

            String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();

            Files.copy(file.getInputStream(), folder.resolve(filename));

            user.setImageUrl(filename);

            userRepository.save(user);

            return userMapper.toDTO(user);

        } catch (IOException e) {
            throw new RuntimeException("Image upload failed");
        }
    }

    @Override
    public UserDTO deleteImage(UUID id) {

        User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));

        try {

            if (user.getImageUrl() != null) {

                Path folder = Paths.get(uploadDir, "users");

                Files.deleteIfExists(folder.resolve(user.getImageUrl()));

                user.setImageUrl(null);

                userRepository.save(user);
            }

            return userMapper.toDTO(user);

        } catch (IOException e) {
            throw new RuntimeException("Image deletion failed");
        }
    }

    @Override
    public List<UserDTO> getUsersByAdmin(UUID adminId) {

        return userRepository.findByAdminId(adminId).stream().map(userMapper::toDTO).toList();

    }


    @Override
    public UserDTO updateSupervisor(UUID userId, UpdateSupervisorRequest request) {

        User user = userRepository.findById(userId).orElseThrow();

        User supervisor = userRepository.findById(request.getAdminId()).orElseThrow();

        user.setAdmin(supervisor);

        userRepository.save(user);

        return userMapper.toDTO(user);

    }

}