package com.mvpnest.fleetmanagement.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @Column(name = "departure_location", nullable = false)
    private String departureLocation;

    @Column(name = "destination_location", nullable = false)
    private String destinationLocation;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MissionStatus status;

    // ================== ASSOCIATIONS ==================

    // Mission (*) → User (1)
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    // Mission (*) → Vehicle (1)
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

    // Mission (1) → MissionDocument (*)
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<MissionDocument> missionDocuments = new ArrayList<>();

    // Mission (1) → MissionVehicleInspection (*)
    @OneToMany(mappedBy = "mission", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<MissionVehicleInspection> vehicleInspections = new ArrayList<>();

    @Column(nullable = true)
    @Builder.Default
    private Boolean documentsVerified = false;

    private LocalDateTime documentsVerificationDate;
}