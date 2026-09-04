package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.service.MissionDocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MissionDocumentControllerTest {

    @Mock
    private MissionDocumentService missionDocumentService;

    @InjectMocks
    private MissionDocumentController controller;

    @Test
    void upload_callsService() {

        MockMultipartFile file = new MockMultipartFile("file", "mission.pdf", "application/pdf", "test".getBytes());

        UploadMissionDocumentRequest request = new UploadMissionDocumentRequest();

        request.setTitle("Mission Document");
        request.setMissionId(UUID.randomUUID());

        User user = User.builder().id(UUID.randomUUID()).email("admin@test.com").build();

        MissionDocumentDTO dto = new MissionDocumentDTO();

        when(missionDocumentService.uploadDocument(file, request, user)).thenReturn(dto);

        controller.upload(file, request, user);

        verify(missionDocumentService).uploadDocument(file, request, user);
    }
}