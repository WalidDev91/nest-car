package com.mvpnest.fleetmanagement.controller;

import com.mvpnest.fleetmanagement.dto.mission.*;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.service.MissionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionControllerTest {

    @Mock
    private MissionService missionService;

    @InjectMocks
    private MissionController controller;

    @Test
    void create_callsService() {
        CreateMissionRequest request = new CreateMissionRequest();
        MissionDTO dto = new MissionDTO();

        when(missionService.createMission(request)).thenReturn(dto);

        controller.create(request);

        verify(missionService).createMission(request);
    }

    @Test
    void getById_callsService() {
        UUID id = UUID.randomUUID();

        when(missionService.getMissionById(id)).thenReturn(new MissionDTO());

        controller.getById(id);

        verify(missionService).getMissionById(id);
    }

    @Test
    void getAll_callsService() {
        when(missionService.getAllMissions()).thenReturn(List.of());

        controller.getAll();

        verify(missionService).getAllMissions();
    }

    @Test
    void update_callsService() {
        UUID id = UUID.randomUUID();
        UpdateMissionRequest request = new UpdateMissionRequest();

        when(missionService.updateMission(id, request)).thenReturn(new MissionDTO());

        controller.update(id, request);

        verify(missionService).updateMission(id, request);
    }

    @Test
    void delete_callsService() {
        UUID id = UUID.randomUUID();

        controller.delete(id);

        verify(missionService).deleteMission(id);
    }

    @Test
    void getByVehicle_callsService() {
        UUID vehicleId = UUID.randomUUID();

        when(missionService.getMissionsByVehicleId(vehicleId)).thenReturn(List.of());

        controller.getByVehicle(vehicleId);

        verify(missionService).getMissionsByVehicleId(vehicleId);
    }

    @Test
    void getByStatus_callsService() {
        when(missionService.getMissionsByStatus(MissionStatus.PLANNED)).thenReturn(List.of());

        controller.getByStatus(MissionStatus.PLANNED);

        verify(missionService).getMissionsByStatus(MissionStatus.PLANNED);
    }

    @Test
    void getByDeparture_callsService() {
        when(missionService.getMissionsByDepartureLocation("Tunis")).thenReturn(List.of());

        controller.getByDeparture("Tunis");

        verify(missionService).getMissionsByDepartureLocation("Tunis");
    }

    @Test
    void getByDestination_callsService() {
        when(missionService.getMissionsByDestinationLocation("Sfax")).thenReturn(List.of());

        controller.getByDestination("Sfax");

        verify(missionService).getMissionsByDestinationLocation("Sfax");
    }

    @Test
    void assignMission_callsService() {
        UUID id = UUID.randomUUID();
        MissionAssignmentRequest request = new MissionAssignmentRequest();

        when(missionService.assignMission(id, request)).thenReturn(new MissionDTO());

        controller.assignMission(id, request);

        verify(missionService).assignMission(id, request);
    }

    @Test
    void updateDocumentsVerification_callsService() {
        UUID id = UUID.randomUUID();

        when(missionService.updateDocumentsVerification(id, true))
                .thenReturn(new MissionDTO());

        controller.updateDocumentsVerification(id, true);

        verify(missionService).updateDocumentsVerification(id, true);
    }

    @Test
    void saveInspection_callsService() {
        UUID id = UUID.randomUUID();
        MissionInspectionRequest request = new MissionInspectionRequest();

        when(missionService.saveInspection(id, request))
                .thenReturn(new MissionDTO());

        controller.saveInspection(id, request);

        verify(missionService).saveInspection(id, request);
    }

    @Test
    void uploadInspectionPhoto_callsService() {
        UUID id = UUID.randomUUID();

        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                "test".getBytes()
        );

        when(missionService.uploadInspectionPhoto(id, file, "test"))
                .thenReturn(new MissionDTO());

        controller.uploadInspectionPhoto(id, file, "test");

        verify(missionService).uploadInspectionPhoto(id, file, "test");
    }

    @Test
    void deleteInspection_callsService() {
        UUID id = UUID.randomUUID();

        when(missionService.deleteInspection(id))
                .thenReturn(new MissionDTO());

        controller.deleteInspection(id);

        verify(missionService).deleteInspection(id);
    }

    @Test
    void deleteInspectionPhoto_callsService() {
        UUID id = UUID.randomUUID();
        UUID photoId = UUID.randomUUID();

        when(missionService.deleteInspectionPhoto(id, photoId))
                .thenReturn(new MissionDTO());

        controller.deleteInspectionPhoto(id, photoId);

        verify(missionService).deleteInspectionPhoto(id, photoId);
    }
}