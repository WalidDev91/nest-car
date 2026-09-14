package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.InspectionType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "mission_vehicle_inspections", uniqueConstraints = {@UniqueConstraint(name = "uk_mission_inspection_type", columnNames = {"mission_id", "inspection_type"})})
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

    @Enumerated(EnumType.STRING)
    @Column(name = "inspection_type", nullable = false)
    private InspectionType inspectionType;

    @Column(nullable = false)
    private LocalDateTime inspectionDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false)
    private Integer mileage;

    @Column(nullable = false)
    private Integer fuelLevel;

    @Column(nullable = false)
    private String tirePressure;

    @Column(nullable = false)
    private String oilChange;

    @Column(nullable = false)
    private String waterCheck;

    @Column(nullable = false)
    private String partsCondition;

    @Column(nullable = false)
    private String repairStatus;

    @Column(nullable = false)
    private Boolean accidentOccurred;

    // Inspection (*) → Mission (1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mission_id", nullable = false)
    private Mission mission;

    // Inspection (1) → MissionVehiclePhoto (*)
    @OneToMany(mappedBy = "inspection", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<MissionVehiclePhoto> photos = new ArrayList<>();

}