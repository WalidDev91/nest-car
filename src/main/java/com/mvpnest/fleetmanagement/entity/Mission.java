package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "missions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class Mission extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    // ================== ASSOCIATIONS ==================

    // 1️⃣ Mission (*) → User (1) "drivers"
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;
    // fix: each mission is assigned to one driver

    // 2️⃣ Mission (*) → Vehicle (1)
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    // fix: each mission uses one vehicle

    // 3️⃣ Mission (1) → MissionDocument (*)
    @OneToMany(mappedBy = "mission")
    private List<MissionDocument> missionDocuments;
    // fix: one mission can have multiple mission documents

    // 4️⃣ Mission (1) → MissionVehicleInspection (1)
    @OneToOne(mappedBy = "mission", cascade = CascadeType.ALL)
    private MissionVehicleInspection vehicleInspection;
    // fix: each mission has exactly one vehicle inspection
}
