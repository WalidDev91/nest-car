package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.entity.User;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
public class HierarchyService {

    public boolean isInHierarchy(User currentUser, User other) {

        if (other == null || currentUser == null) return false;

        if (other.getId().equals(currentUser.getId())) return true;

        if (isSupervisorOf(currentUser.getId(), other)) return true;

        return isSupervisorOf(other.getId(), currentUser);

    }

    private boolean isSupervisorOf(UUID targetId, User start) {

        Set<UUID> visited = new HashSet<>();

        User current = start;

        while (current != null) {

            if (!visited.add(current.getId())) {
                // Cycle detected in the admin chain — stop instead of hanging.
                return false;
            }

            if (current.getId().equals(targetId)) return true;

            current = current.getAdmin();

        }

        return false;
    }
}