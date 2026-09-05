package com.mvpnest.fleetmanagement.entity;

import com.mvpnest.fleetmanagement.enums.RequestStatus;
import com.mvpnest.fleetmanagement.enums.RequestType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "user_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class UserRequest extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType type;

    @Column(nullable = false)
    private String subject;

    @Column(length = 1000, nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RequestStatus status = RequestStatus.PENDING;

    @Column(length = 1000)
    private String adminResponse;

    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

}