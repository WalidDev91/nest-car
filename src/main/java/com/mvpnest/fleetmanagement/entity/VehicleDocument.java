package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
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
    private VehicleDocumentType type;

    @Column(nullable = false)
    private String fileUrl;

    @Column(nullable = false)
    private LocalDate expiryDate;

    // ================== ASSOCIATION ==================

    // VehicleDocument (*) → Vehicle (1)
    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;
    // fix: each document belongs to one vehicle

    @ManyToOne
    @JoinColumn(name = "uploaded_by_id", nullable = false)
    private User uploadedBy;

}