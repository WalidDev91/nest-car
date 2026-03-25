package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "driver_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class DriverDocument {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverDocumentType type;

    @Column(nullable = false)
    private String fileUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverDocumentStatus status;

    @Column(nullable = false)
    private LocalDateTime uploadedAt;

    private LocalDateTime validatedAt;

    // ================== ASSOCIATION ==================

    // DriverDocument (*) → User (1) labeled 'drivers'
    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;
    // fix: each driver document belongs to one driver

}
