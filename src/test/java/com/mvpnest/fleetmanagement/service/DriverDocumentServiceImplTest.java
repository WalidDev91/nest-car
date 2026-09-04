package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentRequest;
import com.mvpnest.fleetmanagement.dto.driverdocument.UpdateDriverDocumentStatusRequest;
import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.mapper.DriverDocumentMapper;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.impl.DriverDocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverDocumentServiceImplTest {

    @TempDir
    Path tempDir;
    @Mock
    private DriverDocumentRepository driverDocumentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private DriverDocumentMapper mapper;
    @InjectMocks
    private DriverDocumentServiceImpl driverDocumentService;

    @Test
    void uploadDocument_withExpiryDate_savesDocument() {

        ReflectionTestUtils.setField(driverDocumentService, "uploadDir", tempDir.toString());

        UUID driverId = UUID.randomUUID();

        User driver = User.builder().id(driverId).email("driver@test.com").build();

        MockMultipartFile file = new MockMultipartFile("file", "license.pdf", "application/pdf", "test".getBytes());

        when(userRepository.findById(driverId)).thenReturn(Optional.of(driver));

        DriverDocument saved = DriverDocument.builder().id(UUID.randomUUID()).title("Driver License").type(DriverDocumentType.DRIVER_LICENSE).expiryDate(LocalDate.of(2027, 9, 3)).driver(driver).build();

        when(driverDocumentRepository.save(any(DriverDocument.class))).thenReturn(saved);

        when(mapper.toDTO(any(DriverDocument.class))).thenReturn(new DriverDocumentDTO());

        driverDocumentService.uploadDocument(file, "Driver License", DriverDocumentType.DRIVER_LICENSE, LocalDate.of(2027, 9, 3), driverId);

        verify(driverDocumentRepository).save(argThat(doc -> doc.getExpiryDate().equals(LocalDate.of(2027, 9, 3))));
    }


    @Test
    void getDocumentById_whenExists_returnsDto() {

        UUID id = UUID.randomUUID();

        DriverDocument document = DriverDocument.builder().id(id).title("License").build();

        DriverDocumentDTO dto = new DriverDocumentDTO();

        when(driverDocumentRepository.findById(id)).thenReturn(Optional.of(document));
        when(mapper.toDTO(document)).thenReturn(dto);

        assertThat(driverDocumentService.getDocumentById(id)).isSameAs(dto);
    }

    @Test
    void getDocumentById_whenNotFound_throwsException() {

        UUID id = UUID.randomUUID();

        when(driverDocumentRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> driverDocumentService.getDocumentById(id)).isInstanceOf(RuntimeException.class).hasMessage("Document not found");
    }

    @Test
    void updateDocument_updatesAllFields() {

        UUID id = UUID.randomUUID();

        DriverDocument document = DriverDocument.builder().id(id).title("Old").type(DriverDocumentType.ID_CARD).expiryDate(LocalDate.of(2026, 1, 1)).build();

        UpdateDriverDocumentRequest request = new UpdateDriverDocumentRequest();
        request.setTitle("New");
        request.setType(DriverDocumentType.DRIVER_LICENSE);
        request.setExpiryDate(LocalDate.of(2027, 9, 3));

        when(driverDocumentRepository.findById(id)).thenReturn(Optional.of(document));
        when(driverDocumentRepository.save(document)).thenReturn(document);
        when(mapper.toDTO(document)).thenReturn(new DriverDocumentDTO());

        driverDocumentService.updateDocument(id, request);

        assertThat(document.getTitle()).isEqualTo("New");
        assertThat(document.getType()).isEqualTo(DriverDocumentType.DRIVER_LICENSE);
        assertThat(document.getExpiryDate()).isEqualTo(LocalDate.of(2027, 9, 3));
    }

    @Test
    void updateStatus_whenPending_clearsValidatedAt() {

        UUID id = UUID.randomUUID();

        DriverDocument document = DriverDocument.builder().id(id).status(DriverDocumentStatus.APPROVED).validatedAt(LocalDateTime.now()).build();

        UpdateDriverDocumentStatusRequest request = new UpdateDriverDocumentStatusRequest();
        request.setStatus(DriverDocumentStatus.PENDING);

        when(driverDocumentRepository.findById(id)).thenReturn(Optional.of(document));
        when(driverDocumentRepository.save(document)).thenReturn(document);
        when(mapper.toDTO(document)).thenReturn(new DriverDocumentDTO());

        driverDocumentService.updateStatus(id, request);

        assertThat(document.getValidatedAt()).isNull();
    }

    @Test
    void updateStatus_whenApproved_setsValidatedAt() {

        UUID id = UUID.randomUUID();

        DriverDocument document = DriverDocument.builder().id(id).build();

        UpdateDriverDocumentStatusRequest request = new UpdateDriverDocumentStatusRequest();
        request.setStatus(DriverDocumentStatus.APPROVED);

        when(driverDocumentRepository.findById(id)).thenReturn(Optional.of(document));
        when(driverDocumentRepository.save(document)).thenReturn(document);
        when(mapper.toDTO(document)).thenReturn(new DriverDocumentDTO());

        driverDocumentService.updateStatus(id, request);

        assertThat(document.getValidatedAt()).isNotNull();
    }
}