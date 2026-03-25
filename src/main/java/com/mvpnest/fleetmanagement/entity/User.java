package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    @Column(nullable = false)
    private boolean isValidate = true;

    // ================== ASSOCIATIONS ==================

    // 1/ User → User (Admin manages users)
    @ManyToOne
    @JoinColumn(name = "admin_id")
    private User admin;

    @OneToMany(mappedBy = "admin")
    private List<User> drivers;

    // 2/ User (Admin / Fleet Manager) → Vehicles
    @OneToMany(mappedBy = "admin")
    private List<Vehicle> vehicles;

    // 3/ User (Driver) → Missions
    @OneToMany(mappedBy = "driver")
    private List<Mission> missions;

    // 4/ User (Driver) → DriverDocuments
    @OneToMany(mappedBy = "driver")
    private List<DriverDocument> driverDocuments;
}
