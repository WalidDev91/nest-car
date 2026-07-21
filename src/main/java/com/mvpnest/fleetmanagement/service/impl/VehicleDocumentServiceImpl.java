package com.mvpnest.fleetmanagement.service.impl;

import com.mvpnest.fleetmanagement.dto.vehicledocument.UpdateVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.UploadVehicleDocumentRequest;
import com.mvpnest.fleetmanagement.dto.vehicledocument.VehicleDocumentDTO;
import com.mvpnest.fleetmanagement.entity.Vehicle;
import com.mvpnest.fleetmanagement.entity.VehicleDocument;
import com.mvpnest.fleetmanagement.mapper.VehicleDocumentMapper;
import com.mvpnest.fleetmanagement.repository.VehicleDocumentRepository;
import com.mvpnest.fleetmanagement.repository.VehicleRepository;
import com.mvpnest.fleetmanagement.service.VehicleDocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VehicleDocumentServiceImpl implements VehicleDocumentService {


    private final VehicleRepository vehicleRepository;
    private final VehicleDocumentRepository vehicleDocumentRepository;
    private final VehicleDocumentMapper mapper;


    @Value("${app.upload.dir}")
    private String uploadDir;



    // =====================================================
    // GET BY ID
    // =====================================================

    @Override
    public VehicleDocumentDTO getDocumentById(UUID id) {

        VehicleDocument document =
                vehicleDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Vehicle document not found")
                        );

        return mapper.toDTO(document);
    }



    // =====================================================
    // GET ALL
    // =====================================================

    @Override
    public List<VehicleDocumentDTO> getAllDocuments() {

        return vehicleDocumentRepository.findAll()
                .stream()
                .map(mapper::toDTO)
                .toList();
    }



    // =====================================================
    // GET BY VEHICLE
    // =====================================================

    @Override
    public List<VehicleDocumentDTO> getDocumentsByVehicleId(UUID vehicleId) {

        return vehicleDocumentRepository.findByVehicleId(vehicleId)
                .stream()
                .map(mapper::toDTO)
                .toList();
    }




    // =====================================================
    // UPLOAD DOCUMENT
    // =====================================================

    @Override
    public VehicleDocumentDTO uploadDocument(
            MultipartFile file,
            UploadVehicleDocumentRequest request
    ) {

        try {

            if (file.isEmpty()) {
                throw new RuntimeException("File is empty");
            }


            Vehicle vehicle =
                    vehicleRepository.findById(request.getVehicleId())
                            .orElseThrow(() ->
                                    new RuntimeException("Vehicle not found")
                            );


            Path uploadPath =
                    Paths.get(uploadDir, "vehicle-documents");


            Files.createDirectories(uploadPath);



            String filename =
                    UUID.randomUUID()
                            + "_"
                            + file.getOriginalFilename();



            Path filePath =
                    uploadPath.resolve(filename);



            Files.copy(
                    file.getInputStream(),
                    filePath,
                    StandardCopyOption.REPLACE_EXISTING
            );



            VehicleDocument document =
                    VehicleDocument.builder()
                            .title(request.getTitle())
                            .type(request.getType())
                            .year(request.getYear())
                            .fileUrl(filename)
                            .vehicle(vehicle)
                            .build();



            return mapper.toDTO(
                    vehicleDocumentRepository.save(document)
            );


        } catch (IOException e) {

            throw new RuntimeException(
                    "Upload failed: " + e.getMessage()
            );
        }
    }





    // =====================================================
    // UPDATE METADATA
    // =====================================================

    @Override
    public VehicleDocumentDTO updateDocument(
            UUID id,
            UpdateVehicleDocumentRequest request
    ) {


        VehicleDocument document =
                vehicleDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Vehicle document not found")
                        );



        document.setTitle(request.getTitle());

        document.setType(request.getType());

        document.setYear(request.getYear());



        return mapper.toDTO(
                vehicleDocumentRepository.save(document)
        );
    }





    // =====================================================
    // DELETE
    // =====================================================

    @Override
    public void deleteDocument(UUID id) {


        VehicleDocument document =
                vehicleDocumentRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("Vehicle document not found")
                        );


        vehicleDocumentRepository.delete(document);
    }





    // =====================================================
    // DOWNLOAD
    // =====================================================

    @Override
    public ResponseEntity<Resource> downloadDocument(UUID id) {


        try {


            VehicleDocument document =
                    vehicleDocumentRepository.findById(id)
                            .orElseThrow(() ->
                                    new RuntimeException("Document not found")
                            );



            Path filePath =
                    Paths.get(
                            uploadDir,
                            "vehicle-documents",
                            document.getFileUrl()
                    ).normalize();



            Resource resource =
                    new UrlResource(filePath.toUri());



            if (!resource.exists()
                    || !resource.isReadable()) {

                throw new RuntimeException(
                        "File not found"
                );
            }



            return ResponseEntity.ok()

                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\""
                                    + resource.getFilename()
                                    + "\""
                    )

                    .contentType(
                            MediaType.APPLICATION_OCTET_STREAM
                    )

                    .body(resource);



        } catch (Exception e) {


            throw new RuntimeException(
                    "Download failed: "
                            + e.getMessage()
            );
        }
    }

}