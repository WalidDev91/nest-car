package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.mission.CreateMissionRequest;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.mission.UpdateMissionRequest;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.MissionStatus;
import com.mvpnest.fleetmanagement.enums.RoleType;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.mapper.MissionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.repository.UserRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.impl.MissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MissionServiceImplTest {

    @Mock
    private MissionRepository missionRepository;

    @InjectMocks
    private MissionServiceImpl missionService;

    @Mock
    private MissionMapper missionMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private MissionVehicleInspectionRepository inspectionRepository;

    @Mock
    private MissionVehicleInspectionService inspectionService;

    @Mock
    private MissionVehiclePhotoService photoService;

    @Test
    void deleteMission_whenMissionExists_deletesIt() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).title("Tunis Delivery").build();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));

        missionService.deleteMission(id);

        verify(missionRepository, times(1)).delete(mission);

    }

    @Test
    void deleteMission_whenMissionDoesNotExist_throwsResourceNotFoundException() {

        UUID id = UUID.randomUUID();

        when(missionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missionService.deleteMission(id)).isInstanceOf(ResourceNotFoundException.class).hasMessage("Mission not found");

        verify(missionRepository, never()).delete(any());

    }

    @Test
    void updateDocumentsVerification_setsVerificationStatus() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).title("Tunis Delivery").build();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));
        when(missionRepository.save(mission)).thenReturn(mission);
        when(missionMapper.toDTO(mission)).thenReturn(new MissionDTO());

        missionService.updateDocumentsVerification(id, true);

        assertThat(mission.getDocumentsVerified()).isTrue();
        assertThat(mission.getDocumentsVerificationDate()).isNotNull();

        verify(missionRepository).save(mission);
    }


    @Test
    void updateDocumentsVerification_whenNull_clearsVerificationDate() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).build();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));
        when(missionRepository.save(mission)).thenReturn(mission);
        when(missionMapper.toDTO(mission)).thenReturn(new MissionDTO());

        missionService.updateDocumentsVerification(id, null);

        assertThat(mission.getDocumentsVerified()).isNull();
        assertThat(mission.getDocumentsVerificationDate()).isNull();
    }

    @Test
    void getMissionById_whenExists_returnsDto() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).title("Delivery").build();

        MissionDTO dto = new MissionDTO();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));
        when(missionMapper.toDTO(mission)).thenReturn(dto);

        assertThat(missionService.getMissionById(id)).isSameAs(dto);
    }

    @Test
    void getMissionById_whenNotFound_throwsException() {

        UUID id = UUID.randomUUID();

        when(missionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missionService.getMissionById(id)).isInstanceOf(RuntimeException.class).hasMessage("Mission not found");
    }

    @Test
    void getAllMissions_returnsMappedMissions() {

        Mission mission = Mission.builder().id(UUID.randomUUID()).title("Delivery").build();

        MissionDTO dto = new MissionDTO();

        when(missionRepository.findAll()).thenReturn(List.of(mission));
        when(missionMapper.toDTO(mission)).thenReturn(dto);

        List<MissionDTO> result = missionService.getAllMissions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(dto);
    }


    @Test
    void createMission_withoutDriverAndVehicle_usesDefaultStatus() {

        CreateMissionRequest request = new CreateMissionRequest();
        request.setTitle("Test Mission");

        Mission saved = Mission.builder().id(UUID.randomUUID()).title("Test Mission").status(MissionStatus.PLANNED).build();

        when(missionRepository.save(any(Mission.class))).thenReturn(saved);
        when(missionMapper.toDTO(saved)).thenReturn(new MissionDTO());

        missionService.createMission(request);

        verify(missionRepository).save(argThat(mission -> mission.getDriver() == null && mission.getVehicle() == null && mission.getStatus() == MissionStatus.PLANNED));
    }

    @Test
    void createMission_withInvalidDates_throwsException() {

        CreateMissionRequest request = new CreateMissionRequest();
        request.setStartDate(LocalDateTime.of(2026, 9, 10, 10, 0));
        request.setEndDate(LocalDateTime.of(2026, 9, 9, 10, 0));

        assertThatThrownBy(() -> missionService.createMission(request)).isInstanceOf(RuntimeException.class).hasMessage("Start date cannot be after end date");

        verify(missionRepository, never()).save(any());
    }

    @Test
    void createMission_withNonDriver_throwsException() {

        UUID driverId = UUID.randomUUID();

        CreateMissionRequest request = new CreateMissionRequest();
        request.setDriverId(driverId);

        User user = User.builder().id(driverId).role(RoleType.ADMIN).build();

        when(userRepository.findById(driverId)).thenReturn(Optional.of(user));

        assertThatThrownBy(() -> missionService.createMission(request)).isInstanceOf(RuntimeException.class).hasMessage("Selected user is not a driver");
    }

    @Test
    void updateMission_withInvalidDates_throwsException() {

        UUID id = UUID.randomUUID();

        UpdateMissionRequest request = new UpdateMissionRequest();
        request.setStartDate(LocalDateTime.of(2026, 9, 10, 10, 0));
        request.setEndDate(LocalDateTime.of(2026, 9, 9, 10, 0));

        assertThatThrownBy(() -> missionService.updateMission(id, request)).isInstanceOf(RuntimeException.class).hasMessage("Start date cannot be after end date");

        verify(missionRepository, never()).findById(id);
    }

    @Test
    void updateDocumentsVerification_whenFalse_setsDate() {

        UUID id = UUID.randomUUID();

        Mission mission = Mission.builder().id(id).build();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));
        when(missionRepository.save(mission)).thenReturn(mission);
        when(missionMapper.toDTO(mission)).thenReturn(new MissionDTO());

        missionService.updateDocumentsVerification(id, false);

        assertThat(mission.getDocumentsVerified()).isFalse();
        assertThat(mission.getDocumentsVerificationDate()).isNotNull();
    }

}