package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.UploadMissionDocumentRequest;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.MissionDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.mapper.MissionDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionDocumentRepository;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.impl.MissionDocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionDocumentServiceImplTest {

    @Mock
    private MissionRepository missionRepository;

    @Mock
    private MissionDocumentRepository missionDocumentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MissionDocumentMapper mapper;

    @InjectMocks
    private MissionDocumentServiceImpl missionDocumentService;

    @TempDir
    Path tempDir;

    @Test
    void uploadDocument_withUploader_savesDocument() {

        ReflectionTestUtils.setField(
                missionDocumentService,
                "uploadDir",
                tempDir.toString()
        );

        UUID missionId = UUID.randomUUID();

        Mission mission = Mission.builder()
                .id(missionId)
                .title("Tunis Delivery")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("admin@test.com")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "mission.pdf",
                "application/pdf",
                "test".getBytes()
        );

        UploadMissionDocumentRequest request =
                new UploadMissionDocumentRequest();

        request.setTitle("Mission Document");
        request.setMissionId(missionId);

        when(missionRepository.findById(missionId))
                .thenReturn(Optional.of(mission));

        when(missionDocumentRepository.save(any(MissionDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toDTO(any(MissionDocument.class)))
                .thenReturn(new MissionDocumentDTO());

        missionDocumentService.uploadDocument(file, request, user);

        verify(missionDocumentRepository).save(argThat(doc ->
                doc.getUploadedBy().equals(user)
        ));
    }
}