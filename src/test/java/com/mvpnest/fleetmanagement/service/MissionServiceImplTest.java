package com.mvpnest.fleetmanagement.service;

import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.entity.Mission;
import com.mvpnest.fleetmanagement.exception.ResourceNotFoundException;
import com.mvpnest.fleetmanagement.mapper.MissionMapper;
import com.mvpnest.fleetmanagement.repository.MissionRepository;
import com.mvpnest.fleetmanagement.service.impl.MissionServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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

        Mission mission = Mission.builder()
                .id(id)
                .build();

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

        Mission mission = Mission.builder()
                .id(id)
                .title("Delivery")
                .build();

        MissionDTO dto = new MissionDTO();

        when(missionRepository.findById(id)).thenReturn(Optional.of(mission));
        when(missionMapper.toDTO(mission)).thenReturn(dto);

        assertThat(missionService.getMissionById(id)).isSameAs(dto);
    }

    @Test
    void getMissionById_whenNotFound_throwsException() {

        UUID id = UUID.randomUUID();

        when(missionRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> missionService.getMissionById(id))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Mission not found");
    }

    @Test
    void getAllMissions_returnsMappedMissions() {

        Mission mission = Mission.builder()
                .id(UUID.randomUUID())
                .title("Delivery")
                .build();

        MissionDTO dto = new MissionDTO();

        when(missionRepository.findAll()).thenReturn(List.of(mission));
        when(missionMapper.toDTO(mission)).thenReturn(dto);

        List<MissionDTO> result = missionService.getAllMissions();

        assertThat(result).hasSize(1);
        assertThat(result.get(0)).isSameAs(dto);
    }

}