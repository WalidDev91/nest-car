package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.enums.DriverDocumentType;
import com.mvpnest.fleetmanagement.service.DriverDocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DriverDocumentControllerTest {

    @Mock
    private DriverDocumentService driverDocumentService;

    @InjectMocks
    private DriverDocumentController controller;

    @Test
    void upload_callsService() {

        MockMultipartFile file = new MockMultipartFile("file", "license.pdf", "application/pdf", "test".getBytes());

        UUID driverId = UUID.randomUUID();
        LocalDate expiryDate = LocalDate.of(2027, 9, 3);

        DriverDocumentDTO dto = new DriverDocumentDTO();

        when(driverDocumentService.uploadDocument(file, "Driver License", DriverDocumentType.DRIVER_LICENSE, expiryDate, driverId)).thenReturn(dto);

        DriverDocumentDTO result = controller.upload(file, "Driver License", DriverDocumentType.DRIVER_LICENSE, expiryDate, driverId);

        verify(driverDocumentService).uploadDocument(file, "Driver License", DriverDocumentType.DRIVER_LICENSE, expiryDate, driverId);
    }
}