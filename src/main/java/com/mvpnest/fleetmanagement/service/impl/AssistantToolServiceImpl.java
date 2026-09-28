package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
import com.mvpnest.fleetmanagement.dto.mission.MissionDTO;
import com.mvpnest.fleetmanagement.dto.missiondocument.MissionDocumentDTO;
import com.mvpnest.fleetmanagement.dto.missionvehicleinspection.MissionVehicleInspectionDTO;
import com.mvpnest.fleetmanagement.dto.user.UserDTO;
import com.mvpnest.fleetmanagement.dto.vehicle.VehicleDTO;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.User;
import com.mvpnest.fleetmanagement.enums.*;
import com.mvpnest.fleetmanagement.service.*;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class AssistantToolServiceImpl implements AssistantToolService {

    private final UserService userService;
    private final VehicleService vehicleService;
    private final MissionService missionService;
    private final DriverDocumentService driverDocumentService;
    private final VehicleDocumentService vehicleDocumentService;
    private final MissionDocumentService missionDocumentService;

    @Override
    @Tool(description = """
            Search users/drivers accessible to the currently authenticated user.
            
            Optional filters:
            - name: first name, last name, or full name
            - email: email address or partial email
            - phone: phone number or partial phone
            - role: role such as DRIVER, ADMIN, FLEET_MANAGER, or SUPER_ADMIN
            - adminName: responsible administrator name
            
            Use this tool for questions about drivers or users.
            Leave filters empty when they are not needed.
            """)
    public List<UserDTO> searchDrivers(@ToolParam(description = "First name, last name, or full name. Leave empty when not needed.") String name,

                                       @ToolParam(description = "Email address or partial email. Leave empty when not needed.") String email,

                                       @ToolParam(description = "Phone number or partial phone. Leave empty when not needed.") String phone,

                                       @ToolParam(description = "User role such as DRIVER, ADMIN, FLEET_MANAGER, or SUPER_ADMIN. Leave empty when not needed.") String role,

                                       @ToolParam(description = "Administrator name responsible for the user. Leave empty when not needed.") String adminName) {
        getAuthenticatedUser();

        List<UserDTO> users = userService.getAllUsers();

        Stream<UserDTO> result = users.stream();

        if (name != null && !name.isBlank()) {
            String normalizedName = normalize(name);

            result = result.filter(user -> normalize(user.getFirstName()).contains(normalizedName) || normalize(user.getLastName()).contains(normalizedName) || normalize(safe(user.getFirstName()) + " " + safe(user.getLastName())).contains(normalizedName));
        }

        if (email != null && !email.isBlank()) {
            String normalizedEmail = normalize(email);

            result = result.filter(user -> normalize(user.getEmail()).contains(normalizedEmail));
        }

        if (phone != null && !phone.isBlank()) {
            String normalizedPhone = normalize(phone);

            result = result.filter(user -> normalize(user.getPhone()).contains(normalizedPhone));
        }

        if (role != null && !role.isBlank()) {
            RoleType requestedRole = parseRole(role);

            result = result.filter(user -> user.getRole() == requestedRole);
        }

        if (adminName != null && !adminName.isBlank()) {
            String normalizedAdminName = normalize(adminName);

            result = result.filter(user -> normalize(user.getAdminName()).contains(normalizedAdminName));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search vehicles accessible to the currently authenticated user.
            
            Optional filters:
            - plateNumber: registration/plate number
            - brand: vehicle brand
            - model: vehicle model
            - year: vehicle manufacturing year
            - adminName: responsible administrator
            
            Use this tool for vehicle information and vehicle searches.
            Leave filters empty when they are not needed.
            """)
    public List<VehicleDTO> searchVehicles(@ToolParam(description = "Vehicle plate number or partial plate. Leave empty when not needed.") String plateNumber,

                                           @ToolParam(description = "Vehicle brand or partial brand. Leave empty when not needed.") String brand,

                                           @ToolParam(description = "Vehicle model or partial model. Leave empty when not needed.") String model,

                                           @ToolParam(description = "Vehicle manufacturing year as a number, for example 2020. Leave empty when not needed.") String year,

                                           @ToolParam(description = "Responsible administrator name. Leave empty when not needed.") String adminName) {
        getAuthenticatedUser();

        List<VehicleDTO> vehicles = vehicleService.getAllVehicles();

        Stream<VehicleDTO> result = vehicles.stream();

        if (plateNumber != null && !plateNumber.isBlank()) {
            String normalizedPlate = normalize(plateNumber);

            result = result.filter(vehicle -> normalize(vehicle.getPlateNumber()).contains(normalizedPlate));
        }

        if (brand != null && !brand.isBlank()) {
            String normalizedBrand = normalize(brand);

            result = result.filter(vehicle -> normalize(vehicle.getBrand()).contains(normalizedBrand));
        }

        if (model != null && !model.isBlank()) {
            String normalizedModel = normalize(model);

            result = result.filter(vehicle -> normalize(vehicle.getModel()).contains(normalizedModel));
        }

        Integer parsedYear = parseInt(year);

        if (parsedYear != null) {
            result = result.filter(vehicle -> Objects.equals(vehicle.getYear(), parsedYear));
        }

        if (adminName != null && !adminName.isBlank()) {
            String normalizedAdminName = normalize(adminName);

            result = result.filter(vehicle -> normalize(vehicle.getAdminName()).contains(normalizedAdminName));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search missions in Nest-Car using optional filters.
            
            Optional filters:
            - status: PLANNED, ONGOING, COMPLETED, or CANCELLED
            - departureLocation
            - destinationLocation
            - driverName
            - vehiclePlateNumber
            - startDateFrom
            - startDateTo
            - endDateFrom
            - endDateTo
            
            Dates must use YYYY-MM-DD.
            
            Multiple filters can be supplied together and all supplied filters
            are applied.
            
            Use this tool for mission questions involving dates, locations,
            drivers, vehicles, status, mission details, inspections, or
            mission document relationships.
            """)
    public List<MissionDTO> searchMissions(@ToolParam(description = "Mission status such as PLANNED, ONGOING, COMPLETED, or CANCELLED. Leave empty when not needed.") String status,

                                           @ToolParam(description = "Departure location or partial location. Leave empty when not needed.") String departureLocation,

                                           @ToolParam(description = "Destination location or partial location. Leave empty when not needed.") String destinationLocation,

                                           @ToolParam(description = "Driver name or partial name. Leave empty when not needed.") String driverName,

                                           @ToolParam(description = "Vehicle plate number or partial plate. Leave empty when not needed.") String vehiclePlateNumber,

                                           @ToolParam(description = "Earliest mission start date in YYYY-MM-DD format. Leave empty when not needed.") String startDateFrom,

                                           @ToolParam(description = "Latest mission start date in YYYY-MM-DD format. Leave empty when not needed.") String startDateTo,

                                           @ToolParam(description = "Earliest mission end date in YYYY-MM-DD format. Leave empty when not needed.") String endDateFrom,

                                           @ToolParam(description = "Latest mission end date in YYYY-MM-DD format. Leave empty when not needed.") String endDateTo) {
        getAuthenticatedUser();

        List<MissionDTO> missions = missionService.getAllMissions();

        Stream<MissionDTO> result = missions.stream();

        if (status != null && !status.isBlank()) {
            MissionStatus requestedStatus = parseMissionStatus(status);

            result = result.filter(mission -> mission.getStatus() == requestedStatus);
        }

        if (departureLocation != null && !departureLocation.isBlank()) {
            String normalizedDeparture = normalize(departureLocation);

            result = result.filter(mission -> normalize(mission.getDepartureLocation()).contains(normalizedDeparture));
        }

        if (destinationLocation != null && !destinationLocation.isBlank()) {
            String normalizedDestination = normalize(destinationLocation);

            result = result.filter(mission -> normalize(mission.getDestinationLocation()).contains(normalizedDestination));
        }

        if (driverName != null && !driverName.isBlank()) {
            String normalizedDriverName = normalize(driverName);

            result = result.filter(mission -> normalize(mission.getDriverName()).contains(normalizedDriverName));
        }

        if (vehiclePlateNumber != null && !vehiclePlateNumber.isBlank()) {
            String normalizedPlate = normalize(vehiclePlateNumber);

            result = result.filter(mission -> normalize(mission.getVehiclePlateNumber()).contains(normalizedPlate));
        }

        LocalDate parsedStartFrom = parseDate(startDateFrom);

        LocalDate parsedStartTo = parseDate(startDateTo);

        LocalDate parsedEndFrom = parseDate(endDateFrom);

        LocalDate parsedEndTo = parseDate(endDateTo);

        if (parsedStartFrom != null) {
            result = result.filter(mission -> mission.getStartDate() != null && !mission.getStartDate().toLocalDate().isBefore(parsedStartFrom));
        }

        if (parsedStartTo != null) {
            result = result.filter(mission -> mission.getStartDate() != null && !mission.getStartDate().toLocalDate().isAfter(parsedStartTo));
        }

        if (parsedEndFrom != null) {
            result = result.filter(mission -> mission.getEndDate() != null && !mission.getEndDate().toLocalDate().isBefore(parsedEndFrom));
        }

        if (parsedEndTo != null) {
            result = result.filter(mission -> mission.getEndDate() != null && !mission.getEndDate().toLocalDate().isAfter(parsedEndTo));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search accessible driver documents.
            
            Optional filters:
            - title
            - type: driver document type
            - status: driver document status
            - driverName
            - driverId
            - expiryDateFrom
            - expiryDateTo
            
            Dates must use YYYY-MM-DD.
            
            Use this tool for questions about driver documents, document types,
            statuses, expiry dates, uploaded/validated information, and the
            driver associated with a document.
            """)
    public List<DriverDocumentDTO> searchDriverDocuments(@ToolParam(description = "Document title or partial title. Leave empty when not needed.") String title,

                                                         @ToolParam(description = "Driver document type such as DRIVER_LICENSE or ID_CARD. Leave empty when not needed.") String type,

                                                         @ToolParam(description = "Driver document status such as APPROVED, PENDING, or REJECTED. Leave empty when not needed.") String status,

                                                         @ToolParam(description = "Driver name or partial name. Leave empty when not needed.") String driverName,

                                                         @ToolParam(description = "Driver UUID. Leave empty when not needed.") String driverId,

                                                         @ToolParam(description = "Earliest expiry date in YYYY-MM-DD format. Leave empty when not needed.") String expiryDateFrom,

                                                         @ToolParam(description = "Latest expiry date in YYYY-MM-DD format. Leave empty when not needed.") String expiryDateTo) {
        User currentUser = getAuthenticatedUser();

        List<DriverDocumentDTO> documents = driverDocumentService.getAllDocuments(currentUser);

        Stream<DriverDocumentDTO> result = documents.stream();

        if (title != null && !title.isBlank()) {
            String normalizedTitle = normalize(title);

            result = result.filter(document -> normalize(document.getTitle()).contains(normalizedTitle));
        }

        if (type != null && !type.isBlank()) {
            DriverDocumentType requestedType = parseDriverDocumentType(type);

            result = result.filter(document -> document.getType() == requestedType);
        }

        if (status != null && !status.isBlank()) {
            DriverDocumentStatus requestedStatus = parseDriverDocumentStatus(status);

            result = result.filter(document -> document.getStatus() == requestedStatus);
        }

        if (driverName != null && !driverName.isBlank()) {
            String normalizedDriverName = normalize(driverName);

            result = result.filter(document -> normalize(document.getDriverName()).contains(normalizedDriverName));
        }

        UUID parsedDriverId = parseUuid(driverId);

        if (parsedDriverId != null) {
            result = result.filter(document -> Objects.equals(document.getDriverId(), parsedDriverId));
        }

        LocalDate parsedExpiryFrom = parseDate(expiryDateFrom);

        LocalDate parsedExpiryTo = parseDate(expiryDateTo);

        if (parsedExpiryFrom != null) {
            result = result.filter(document -> document.getExpiryDate() != null && !document.getExpiryDate().isBefore(parsedExpiryFrom));
        }

        if (parsedExpiryTo != null) {
            result = result.filter(document -> document.getExpiryDate() != null && !document.getExpiryDate().isAfter(parsedExpiryTo));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search accessible vehicle documents.
            
            Optional filters:
            - title
            - type: vehicle document type
            - vehiclePlateNumber
            - vehicleId
            - expiryDateFrom
            - expiryDateTo
            - uploadedByName
            
            Dates must use YYYY-MM-DD.
            
            Use this tool for questions about vehicle documents, document types,
            expiry dates, vehicles associated with documents, and upload information.
            """)
    public List<VehicleDocumentDTO> searchVehicleDocuments(@ToolParam(description = "Document title or partial title. Leave empty when not needed.") String title,

                                                           @ToolParam(description = "Vehicle document type such as LICENSE, TECHNICAL_CHECK, INSURANCE, or VEHICLE_TAX. Leave empty when not needed.") String type,

                                                           @ToolParam(description = "Vehicle plate number or partial plate. Leave empty when not needed.") String vehiclePlateNumber,

                                                           @ToolParam(description = "Vehicle UUID. Leave empty when not needed.") String vehicleId,

                                                           @ToolParam(description = "Earliest expiry date in YYYY-MM-DD format. Leave empty when not needed.") String expiryDateFrom,

                                                           @ToolParam(description = "Latest expiry date in YYYY-MM-DD format. Leave empty when not needed.") String expiryDateTo,

                                                           @ToolParam(description = "Uploader name or partial name. Leave empty when not needed.") String uploadedByName) {
        User currentUser = getAuthenticatedUser();

        List<VehicleDocumentDTO> documents = vehicleDocumentService.getAllDocuments(currentUser);

        Stream<VehicleDocumentDTO> result = documents.stream();

        if (title != null && !title.isBlank()) {
            String normalizedTitle = normalize(title);

            result = result.filter(document -> normalize(document.getTitle()).contains(normalizedTitle));
        }

        if (type != null && !type.isBlank()) {
            VehicleDocumentType requestedType = parseVehicleDocumentType(type);

            result = result.filter(document -> document.getType() == requestedType);
        }

        if (vehiclePlateNumber != null && !vehiclePlateNumber.isBlank()) {
            String normalizedPlate = normalize(vehiclePlateNumber);

            result = result.filter(document -> normalize(document.getVehiclePlateNumber()).contains(normalizedPlate));
        }

        UUID parsedVehicleId = parseUuid(vehicleId);

        if (parsedVehicleId != null) {
            result = result.filter(document -> Objects.equals(document.getVehicleId(), parsedVehicleId));
        }

        LocalDate parsedExpiryFrom = parseDate(expiryDateFrom);

        LocalDate parsedExpiryTo = parseDate(expiryDateTo);

        if (parsedExpiryFrom != null) {
            result = result.filter(document -> document.getExpiryDate() != null && !document.getExpiryDate().isBefore(parsedExpiryFrom));
        }

        if (parsedExpiryTo != null) {
            result = result.filter(document -> document.getExpiryDate() != null && !document.getExpiryDate().isAfter(parsedExpiryTo));
        }

        if (uploadedByName != null && !uploadedByName.isBlank()) {
            String normalizedUploader = normalize(uploadedByName);

            result = result.filter(document -> normalize(document.getUploadedByName()).contains(normalizedUploader));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search accessible mission documents.
            
            Optional filters:
            - title: document title
            - missionTitle: associated mission title
            - missionId: associated mission UUID
            - uploadedByName
            - uploadedById
            
            Use this tool for questions about mission documents, their associated
            missions, titles, and uploader information.
            Leave filters empty when they are not needed.
            """)
    public List<MissionDocumentDTO> searchMissionDocuments(@ToolParam(description = "Document title or partial title. Leave empty when not needed.") String title,

                                                           @ToolParam(description = "Mission title or partial title. Leave empty when not needed.") String missionTitle,

                                                           @ToolParam(description = "Mission UUID. Leave empty when not needed.") String missionId,

                                                           @ToolParam(description = "Uploader name or partial name. Leave empty when not needed.") String uploadedByName,

                                                           @ToolParam(description = "Uploader UUID. Leave empty when not needed.") String uploadedById) {
        User currentUser = getAuthenticatedUser();

        List<MissionDocumentDTO> documents = missionDocumentService.getAllDocuments(currentUser);

        Stream<MissionDocumentDTO> result = documents.stream();

        if (title != null && !title.isBlank()) {
            String normalizedTitle = normalize(title);

            result = result.filter(document -> normalize(document.getTitle()).contains(normalizedTitle));
        }

        if (missionTitle != null && !missionTitle.isBlank()) {
            String normalizedMissionTitle = normalize(missionTitle);

            result = result.filter(document -> normalize(document.getMissionTitle()).contains(normalizedMissionTitle));
        }

        UUID parsedMissionId = parseUuid(missionId);

        if (parsedMissionId != null) {
            result = result.filter(document -> Objects.equals(document.getMissionId(), parsedMissionId));
        }

        if (uploadedByName != null && !uploadedByName.isBlank()) {
            String normalizedUploader = normalize(uploadedByName);

            result = result.filter(document -> normalize(document.getUploadedByName()).contains(normalizedUploader));
        }

        UUID parsedUploaderId = parseUuid(uploadedById);

        if (parsedUploaderId != null) {
            result = result.filter(document -> Objects.equals(document.getUploadedById(), parsedUploaderId));
        }

        return result.toList();
    }

    @Override
    @Tool(description = """
            Search vehicle inspection information.
            
            Optional filters:
            - inspectionType: BEFORE or AFTER
            - inspectionDateFrom
            - inspectionDateTo
            - mileageFrom
            - mileageTo
            - fuelLevelFrom
            - fuelLevelTo
            - repairStatus
            - accidentOccurred
            - missionId
            - vehiclePlateNumber
            
            Dates must use YYYY-MM-DD.
            
            Inspection data includes inspection date, mileage, fuel level,
            tire pressure, oil change, water check, parts condition,
            repair status, accident information, and notes.
            
            Inspection photos are intentionally excluded from assistant functionality.
            """)
    public List<MissionVehicleInspectionDTO> searchInspections(@ToolParam(description = "Inspection type such as BEFORE or AFTER. Leave empty when not needed.") String inspectionType,

                                                               @ToolParam(description = "Earliest inspection date in YYYY-MM-DD format. Leave empty when not needed.") String inspectionDateFrom,

                                                               @ToolParam(description = "Latest inspection date in YYYY-MM-DD format. Leave empty when not needed.") String inspectionDateTo,

                                                               @ToolParam(description = "Minimum mileage as a number. Leave empty when not needed.") String mileageFrom,

                                                               @ToolParam(description = "Maximum mileage as a number. Leave empty when not needed.") String mileageTo,

                                                               @ToolParam(description = "Minimum fuel level as a number. Leave empty when not needed.") String fuelLevelFrom,

                                                               @ToolParam(description = "Maximum fuel level as a number. Leave empty when not needed.") String fuelLevelTo,

                                                               @ToolParam(description = "Repair status or partial repair status. Leave empty when not needed.") String repairStatus,

                                                               @ToolParam(description = "Whether an accident occurred: true or false. Leave empty when not needed.") String accidentOccurred,

                                                               @ToolParam(description = "Mission UUID associated with the inspection. Leave empty when not needed.") String missionId,

                                                               @ToolParam(description = "Vehicle plate number associated with the mission. Leave empty when not needed.") String vehiclePlateNumber) {
        getAuthenticatedUser();

        UUID parsedMissionId = parseUuid(missionId);

        String normalizedPlate = normalize(vehiclePlateNumber);

        Integer parsedMileageFrom = parseInt(mileageFrom);
        Integer parsedMileageTo = parseInt(mileageTo);
        Integer parsedFuelFrom = parseInt(fuelLevelFrom);
        Integer parsedFuelTo = parseInt(fuelLevelTo);
        Boolean parsedAccident = parseBoolean(accidentOccurred);

        Stream<MissionVehicleInspectionDTO> result = missionService.getAllMissions().stream().filter(Objects::nonNull).filter(mission -> mission.getVehicleInspections() != null).filter(mission -> parsedMissionId == null || Objects.equals(mission.getId(), parsedMissionId)).filter(mission -> normalizedPlate.isBlank() || normalize(mission.getVehiclePlateNumber()).contains(normalizedPlate)).flatMap(mission -> mission.getVehicleInspections().stream());

        if (inspectionType != null && !inspectionType.isBlank()) {
            InspectionType requestedType = parseInspectionType(inspectionType);

            result = result.filter(inspection -> inspection.getInspectionType() == requestedType);
        }

        LocalDate parsedDateFrom = parseDate(inspectionDateFrom);

        LocalDate parsedDateTo = parseDate(inspectionDateTo);

        if (parsedDateFrom != null) {
            result = result.filter(inspection -> inspection.getInspectionDate() != null && !inspection.getInspectionDate().toLocalDate().isBefore(parsedDateFrom));
        }

        if (parsedDateTo != null) {
            result = result.filter(inspection -> inspection.getInspectionDate() != null && !inspection.getInspectionDate().toLocalDate().isAfter(parsedDateTo));
        }

        if (parsedMileageFrom != null) {
            result = result.filter(inspection -> inspection.getMileage() != null && inspection.getMileage() >= parsedMileageFrom);
        }

        if (parsedMileageTo != null) {
            result = result.filter(inspection -> inspection.getMileage() != null && inspection.getMileage() <= parsedMileageTo);
        }

        if (parsedFuelFrom != null) {
            result = result.filter(inspection -> inspection.getFuelLevel() != null && inspection.getFuelLevel() >= parsedFuelFrom);
        }

        if (parsedFuelTo != null) {
            result = result.filter(inspection -> inspection.getFuelLevel() != null && inspection.getFuelLevel() <= parsedFuelTo);
        }

        if (repairStatus != null && !repairStatus.isBlank()) {
            String normalizedRepairStatus = normalize(repairStatus);

            result = result.filter(inspection -> normalize(inspection.getRepairStatus()).contains(normalizedRepairStatus));
        }

        if (parsedAccident != null) {
            result = result.filter(inspection -> Objects.equals(inspection.getAccidentOccurred(), parsedAccident));
        }

        return result.toList();
    }

    private User getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof User user)) {

            throw new IllegalStateException("No authenticated user found");
        }

        return user;
    }

    private MissionStatus parseMissionStatus(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return switch (normalized) {

            case "PLANNED", "PLAN", "SCHEDULED" -> MissionStatus.PLANNED;

            case "ONGOING", "IN_PROGRESS", "ACTIVE", "CURRENT" -> MissionStatus.ONGOING;

            case "COMPLETED", "COMPLETE", "DONE", "FINISHED" -> MissionStatus.COMPLETED;

            case "CANCELLED", "CANCELED" -> MissionStatus.CANCELLED;

            default -> MissionStatus.valueOf(normalized);
        };
    }

    private RoleType parseRole(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return RoleType.valueOf(normalized);
    }

    private DriverDocumentType parseDriverDocumentType(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return DriverDocumentType.valueOf(normalized);
    }

    private DriverDocumentStatus parseDriverDocumentStatus(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return DriverDocumentStatus.valueOf(normalized);
    }

    private VehicleDocumentType parseVehicleDocumentType(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return VehicleDocumentType.valueOf(normalized);
    }

    private InspectionType parseInspectionType(String value) {

        String normalized = normalize(value).replace(" ", "_").replace("-", "_").toUpperCase(Locale.ROOT);

        return InspectionType.valueOf(normalized);
    }

    private LocalDate parseDate(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return LocalDate.parse(value.trim());
        } catch (Exception exception) {
            return null;
        }
    }

    private UUID parseUuid(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return UUID.fromString(value.trim());
        } catch (Exception exception) {
            return null;
        }
    }

    private Integer parseInt(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        try {
            return Integer.valueOf(value.trim());
        } catch (Exception exception) {
            return null;
        }
    }

    private Boolean parseBoolean(String value) {

        if (value == null || value.isBlank()) {
            return null;
        }

        return Boolean.valueOf(value.trim());
    }

    private String normalize(String value) {

        if (value == null) {
            return "";
        }

        return value.trim().toLowerCase(Locale.ROOT);
    }

    private String safe(String value) {
        return value == null ? "" : value;
    }
}