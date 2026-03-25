package com.mvpnest.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mission_vehicle_inspections")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class MissionVehicleInspection extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private LocalDateTime inspectionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Integer fuelLevel;

    // ================== ASSOCIATIONS ==================

    // 1️⃣ MissionVehicleInspection (1) → Mission (1)
    @OneToOne
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;
    // fix: each inspection belongs to exactly one mission

    // 2️⃣ MissionVehicleInspection (1) → MissionVehiclePhoto (*)
    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL)
    private List<MissionVehiclePhoto> photos;
    // fix: one inspection can have multiple photos

}
