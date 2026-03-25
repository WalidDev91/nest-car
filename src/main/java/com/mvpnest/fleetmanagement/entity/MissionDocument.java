package com.mvpnest.fleetmanagement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "mission_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class MissionDocument extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String fileUrl;

    // ================== ASSOCIATION ==================

    // MissionDocument (*) → Mission (1)
    @ManyToOne
    @JoinColumn(name = "mission_id")
    private Mission mission;
    // fix: each mission document belongs to one mission

}
