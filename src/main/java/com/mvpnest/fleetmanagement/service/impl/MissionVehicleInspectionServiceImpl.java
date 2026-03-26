package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.entity.MissionVehicleInspection;
import com.mvpnest.fleetmanagement.repository.MissionVehicleInspectionRepository;
import com.mvpnest.fleetmanagement.service.MissionVehicleInspectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MissionVehicleInspectionServiceImpl implements MissionVehicleInspectionService {

    private final MissionVehicleInspectionRepository repository;

    @Override
    public MissionVehicleInspection createInspection(MissionVehicleInspection inspection) {
        return repository.save(inspection);
    }

    @Override
    public MissionVehicleInspection getInspectionById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inspection not found with id: " + id));
    }

    @Override
    public List<MissionVehicleInspection> getAllInspections() {
        return repository.findAll();
    }

    @Override
    public MissionVehicleInspection getByMissionId(UUID missionId) {
        return repository.findByMissionId(missionId);
    }

    @Override
    public MissionVehicleInspection updateInspection(UUID id, MissionVehicleInspection inspection) {
        MissionVehicleInspection existing = getInspectionById(id);

        existing.setInspectionDate(inspection.getInspectionDate());
        existing.setNotes(inspection.getNotes());
        existing.setMileage(inspection.getMileage());
        existing.setFuelLevel(inspection.getFuelLevel());
        existing.setMission(inspection.getMission());

        return repository.save(existing);
    }

    @Override
    public void deleteInspection(UUID id) {
        MissionVehicleInspection inspection = getInspectionById(id);
        repository.delete(inspection);
    }
}
