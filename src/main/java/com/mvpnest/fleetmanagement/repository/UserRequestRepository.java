package com.mvpnest.fleetmanagement.repository;

import com.mvpnest.fleetmanagement.entity.UserRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRequestRepository extends JpaRepository<UserRequest, UUID> {

    List<UserRequest> findByRequesterId(UUID requesterId);

    List<UserRequest> findByRequesterAdminId(UUID adminId);

    Optional<UserRequest> findTopByOrderByRequestNumberDesc();

}