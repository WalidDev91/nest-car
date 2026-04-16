package com.mvpnest.fleetmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "vehicles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Vehicle extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String plateNumber;

    @Column(nullable = false)
    private String brand;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer year;

    // ================== ASSOCIATIONS ==================

    // 1/ Vehicle (*) → User (1) "admin"
    @ManyToOne
    @JoinColumn(name = "admin_id")
    @JsonIgnore
    private User admin;
    // fix: each vehicle is managed by one admin

    // 2/ Vehicle (1) → Mission (*)
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<Mission> missions;
    // fix: one vehicle can be used in multiple missions

    // 3/ Vehicle (1) → VehicleDocument (*)
    @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    private List<VehicleDocument> vehicleDocuments;
    // fix: one vehicle has multiple documents
}
