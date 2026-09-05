package com.mvpnest.fleetmanagement.dto.userrequest;

import com.mvpnest.fleetmanagement.enums.RequestStatus;
import com.mvpnest.fleetmanagement.enums.RequestType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRequestDTO {

    private UUID id;

    private RequestType type;

    private String subject;

    private String description;

    private RequestStatus status;

    private String adminResponse;

    private UUID requesterId;

    private String requesterName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}