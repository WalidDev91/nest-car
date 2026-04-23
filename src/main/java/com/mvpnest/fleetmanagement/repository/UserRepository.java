package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(RoleType role);
}
