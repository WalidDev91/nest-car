package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.VehicleType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "vehicle_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class VehicleDocument extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VehicleType type;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private Integer year;

    // ================== ASSOCIATION ==================

    // VehicleDocument (*) → Vehicle (1)
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    // fix: each document belongs to one vehicle

}
