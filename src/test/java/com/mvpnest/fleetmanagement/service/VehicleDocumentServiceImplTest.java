package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.impl.VehicleDocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleDocumentServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleDocumentRepository vehicleDocumentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleDocumentMapper mapper;

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private VehicleDocumentServiceImpl vehicleDocumentService;

    @TempDir
    Path tempDir;

    @Test
    void uploadDocument_withExpiryDateAndUploader_savesDocument() {

        ReflectionTestUtils.setField(
                vehicleDocumentService,
                "uploadDir",
                tempDir.toString()
        );

        UUID vehicleId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .plateNumber("123 TUN 456")
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("admin@test.com")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "insurance.pdf",
                "application/pdf",
                "test".getBytes()
        );

        var request =
                new com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest();

        request.setTitle("Insurance");
        request.setType(VehicleDocumentType.INSURANCE);
        request.setExpiryDate(LocalDate.of(2027, 9, 3));
        request.setVehicleId(vehicleId);

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleDocumentRepository.save(any(VehicleDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toDTO(any(VehicleDocument.class)))
                .thenReturn(new VehicleDocumentDTO());

        vehicleDocumentService.uploadDocument(file, request, user);

        verify(vehicleDocumentRepository).save(argThat(doc ->
                doc.getExpiryDate().equals(LocalDate.of(2027, 9, 3))
                        && doc.getUploadedBy().equals(user)
        ));
    }
}