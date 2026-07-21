//package com.mvpnest.fleetmanagement.controller;
//
//import com.mvpnest.fleetmanagement.dto.driverdocument.DriverDocumentDTO;
//import com.mvpnest.fleetmanagement.entity.User;
//import com.mvpnest.fleetmanagement.enums.DriverDocumentStatus;
//import com.mvpnest.fleetmanagement.enums.RoleType;
//import com.mvpnest.fleetmanagement.service.DriverDocumentService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.core.io.Resource;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.List;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/driver-documents")
//@RequiredArgsConstructor
//public class DriverDocumentController {
//
//    private final DriverDocumentService driverDocumentService;
//
//    // ===================== CREATE / UPLOAD =====================
//    @PostMapping("/upload")
//    public DriverDocumentDTO upload(
//            @RequestPart("file") MultipartFile file,
//            @ModelAttribute DriverDocumentUploadRequest request
//    ) {
//        return driverDocumentService.uploadDocument(
//                file,
//                request.getTitle(),
//                request.getType(),
//                request.getDriverId()
//        );
//    }
//
//    // ===================== READ SINGLE =====================
//    @GetMapping("/{id}")
//    public DriverDocumentDTO getById(@PathVariable UUID id) {
//        return driverDocumentService.getDocumentById(id);
//    }
//
//    // ===================== ROLE-BASED LIST (MAIN ENDPOINT) =====================
//    @GetMapping
//    public List<DriverDocumentDTO> getDocuments(@AuthenticationPrincipal User user) {
//
//        if (user.getRole() == RoleType.ADMIN) {
//            return driverDocumentService.getAllDocuments();
//        }
//
//        return driverDocumentService.getMyDocuments(user.getId());
//    }
//
//    // ===================== DRIVER-SPECIFIC (OPTIONAL DEBUG / INTERNAL USE) =====================
//    @GetMapping("/driver/{driverId}")
//    public List<DriverDocumentDTO> getByDriver(@PathVariable UUID driverId) {
//        return driverDocumentService.getMyDocuments(driverId);
//    }
//
//    // ===================== UPDATE =====================
//    @PutMapping("/{id}")
//    public DriverDocumentDTO update(
//            @PathVariable UUID id,
//            @RequestBody DriverDocumentDTO dto
//    ) {
//        return driverDocumentService.updateDocument(id, dto);
//    }
//
//    // ===================== DELETE =====================
//    @DeleteMapping("/{id}")
//    public void delete(@PathVariable UUID id) {
//        driverDocumentService.deleteDocument(id);
//    }
//
//    // ===================== DOWNLOAD =====================
//    @GetMapping("/{id}/download")
//    public ResponseEntity<Resource> download(@PathVariable UUID id) {
//        return driverDocumentService.downloadDocument(id);
//    }
//
//    // ===================== APPROVAL FLOW =====================
//    @PatchMapping("/{id}/status")
//    public DriverDocumentDTO updateStatus(
//            @PathVariable UUID id,
//            @RequestParam DriverDocumentStatus status
//    ) {
//        return driverDocumentService.updateStatus(id, status);
//    }
//}