package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.VehicleDocumentType;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleDocumentControllerTest {

    @Mock
    private VehicleDocumentService vehicleDocumentService;

    @InjectMocks
    private VehicleDocumentController controller;

    @Test
    void upload_callsService() {

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "insurance.pdf",
                "application/pdf",
                "test".getBytes()
        );

        UploadVehicleDocumentRequest request =
                new UploadVehicleDocumentRequest();

        request.setTitle("Insurance");
        request.setType(VehicleDocumentType.INSURANCE);
        request.setExpiryDate(LocalDate.of(2027, 9, 3));
        request.setVehicleId(UUID.randomUUID());

        User user = User.builder()
                .id(UUID.randomUUID())
                .email("admin@test.com")
                .build();

        VehicleDocumentDTO dto = new VehicleDocumentDTO();

        when(vehicleDocumentService.uploadDocument(file, request, user))
                .thenReturn(dto);

        controller.upload(file, request, user);

        verify(vehicleDocumentService)
                .uploadDocument(file, request, user);
    }
}