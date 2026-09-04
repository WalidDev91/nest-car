package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.impl.VehicleDocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleDocumentServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private VehicleDocumentRepository vehicleDocumentRepository;

    @Mock
    private VehicleDocumentMapper mapper;

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private VehicleDocumentServiceImpl vehicleDocumentService;

    @Test
    void getDocumentById_whenExists_returnsDto() {

        UUID id = UUID.randomUUID();
        VehicleDocument document = VehicleDocument.builder()
                .id(id)
                .title("Insurance")
                .build();

        VehicleDocumentDTO dto = new VehicleDocumentDTO();
        dto.setId(id);
        dto.setTitle("Insurance");

        when(vehicleDocumentRepository.findById(id))
                .thenReturn(Optional.of(document));
        when(mapper.toDTO(document))
                .thenReturn(dto);

        VehicleDocumentDTO result =
                vehicleDocumentService.getDocumentById(id);

        assertThat(result.getTitle()).isEqualTo("Insurance");
    }

    @Test
    void getDocumentsByVehicleId_returnsMappedDocuments() {

        UUID vehicleId = UUID.randomUUID();

        VehicleDocument document = VehicleDocument.builder()
                .id(UUID.randomUUID())
                .build();

        when(vehicleDocumentRepository.findByVehicleId(vehicleId))
                .thenReturn(List.of(document));

        when(mapper.toDTO(document))
                .thenReturn(new VehicleDocumentDTO());

        List<VehicleDocumentDTO> result =
                vehicleDocumentService.getDocumentsByVehicleId(vehicleId);

        assertThat(result).hasSize(1);
    }

    @Test
    void getAllDocuments_superAdmin_returnsAll() {

        User user = User.builder()
                .id(UUID.randomUUID())
                .role(RoleType.SUPER_ADMIN)
                .build();

        VehicleDocument document = VehicleDocument.builder()
                .id(UUID.randomUUID())
                .build();

        when(vehicleDocumentRepository.findAll())
                .thenReturn(List.of(document));

        when(mapper.toDTO(document))
                .thenReturn(new VehicleDocumentDTO());

        List<VehicleDocumentDTO> result =
                vehicleDocumentService.getAllDocuments(user);

        assertThat(result).hasSize(1);
    }

    @Test
    void uploadDocument_whenFileIsEmpty_throwsException() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "test.pdf",
                "application/pdf",
                new byte[0]
        );

        UploadVehicleDocumentRequest request =
                new UploadVehicleDocumentRequest();

        assertThatThrownBy(() ->
                vehicleDocumentService.uploadDocument(
                        file,
                        request,
                        User.builder().build()
                )
        ).isInstanceOf(RuntimeException.class)
                .hasMessage("File is empty");
    }

    @Test
    void uploadDocument_whenValid_savesDocument() throws Exception {

        Path tempDir = Files.createTempDirectory("vehicle-doc-test");

        ReflectionTestUtils.setField(
                vehicleDocumentService,
                "uploadDir",
                tempDir.toString()
        );

        UUID vehicleId = UUID.randomUUID();

        Vehicle vehicle = Vehicle.builder()
                .id(vehicleId)
                .build();

        User user = User.builder()
                .id(UUID.randomUUID())
                .build();

        UploadVehicleDocumentRequest request =
                new UploadVehicleDocumentRequest();

        request.setTitle("Vehicle Tax");
        request.setType(VehicleDocumentType.VEHICLE_TAX);
        request.setExpiryDate(LocalDate.of(2027, 9, 3));
        request.setVehicleId(vehicleId);

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "tax.jpg",
                "image/jpeg",
                "test".getBytes()
        );

        when(vehicleRepository.findById(vehicleId))
                .thenReturn(Optional.of(vehicle));

        when(vehicleDocumentRepository.save(any(VehicleDocument.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        when(mapper.toDTO(any(VehicleDocument.class)))
                .thenReturn(new VehicleDocumentDTO());

        vehicleDocumentService.uploadDocument(file, request, user);

        verify(vehicleDocumentRepository).save(argThat(document ->
                document.getUploadedBy().equals(user)
                        && document.getExpiryDate()
                        .equals(LocalDate.of(2027, 9, 3))
        ));
    }

    @Test
    void updateDocument_updatesFields() {

        UUID id = UUID.randomUUID();

        VehicleDocument document = VehicleDocument.builder()
                .id(id)
                .title("Old")
                .type(VehicleDocumentType.LICENSE)
                .build();

        UpdateVehicleDocumentRequest request =
                new UpdateVehicleDocumentRequest();

        request.setTitle("New");
        request.setType(VehicleDocumentType.VEHICLE_TAX);
        request.setExpiryDate(LocalDate.of(2027, 9, 3));

        when(vehicleDocumentRepository.findById(id))
                .thenReturn(Optional.of(document));

        when(vehicleDocumentRepository.save(document))
                .thenReturn(document);

        when(mapper.toDTO(document))
                .thenReturn(new VehicleDocumentDTO());

        vehicleDocumentService.updateDocument(id, request);

        assertThat(document.getTitle()).isEqualTo("New");
        assertThat(document.getType())
                .isEqualTo(VehicleDocumentType.VEHICLE_TAX);
        assertThat(document.getExpiryDate())
                .isEqualTo(LocalDate.of(2027, 9, 3));

        verify(vehicleDocumentRepository).save(document);
    }

    @Test
    void deleteDocument_whenExists_deletesIt() {

        UUID id = UUID.randomUUID();

        VehicleDocument document = VehicleDocument.builder()
                .id(id)
                .build();

        when(vehicleDocumentRepository.findById(id))
                .thenReturn(Optional.of(document));

        vehicleDocumentService.deleteDocument(id);

        verify(vehicleDocumentRepository).delete(document);
    }
}