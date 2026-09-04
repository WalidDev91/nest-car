package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.entity.DriverDocument;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.mapper.DriverDocumentMapper;
import com.mvpnest.fleetmanagement.repository.DriverDocumentRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.service.impl.DriverDocumentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DriverDocumentServiceImplTest {

    @Mock
    private DriverDocumentRepository driverDocumentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DriverDocumentMapper mapper;

    @InjectMocks
    private DriverDocumentServiceImpl driverDocumentService;

    @TempDir
    Path tempDir;

    @Test
    void uploadDocument_withExpiryDate_savesDocument() {

        ReflectionTestUtils.setField(
                driverDocumentService,
                "uploadDir",
                tempDir.toString()
        );

        UUID driverId = UUID.randomUUID();

        User driver = User.builder()
                .id(driverId)
                .email("driver@test.com")
                .build();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "license.pdf",
                "application/pdf",
                "test".getBytes()
        );

        when(userRepository.findById(driverId))
                .thenReturn(Optional.of(driver));

        DriverDocument saved = DriverDocument.builder()
                .id(UUID.randomUUID())
                .title("Driver License")
                .type(DriverDocumentType.DRIVER_LICENSE)
                .expiryDate(LocalDate.of(2027, 9, 3))
                .driver(driver)
                .build();

        when(driverDocumentRepository.save(any(DriverDocument.class)))
                .thenReturn(saved);

        when(mapper.toDTO(any(DriverDocument.class)))
                .thenReturn(new DriverDocumentDTO());

        driverDocumentService.uploadDocument(
                file,
                "Driver License",
                DriverDocumentType.DRIVER_LICENSE,
                LocalDate.of(2027, 9, 3),
                driverId
        );

        verify(driverDocumentRepository).save(argThat(doc ->
                doc.getExpiryDate().equals(LocalDate.of(2027, 9, 3))
        ));
    }
}