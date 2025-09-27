package com.school.management.Controller;

import com.school.management.Models.Generic.mGeneric;
import com.school.management.Services.FileImportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/import")
public class FileImportController {

    private final FileImportService importService;

    public FileImportController(FileImportService importService) {
        this.importService = importService;
    }

    @Operation(summary = "Upload CSV file for historical import")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File accepted"),
            @ApiResponse(responseCode = "500", description = "Error uploading file")
    })
    @PostMapping(value = "/import", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<mGeneric.mAPIResponse<String>> uploadFile(
            @Parameter(description = "CSV file to upload")
            @RequestPart("file") MultipartFile file) {
        String jobId = UUID.randomUUID().toString();
        importService.importFile(file, jobId);
        return ResponseEntity.ok(new mGeneric.mAPIResponse<>(200, "File upload started", jobId));
    }


    @GetMapping("/status/{jobId}")
    public ResponseEntity<Map<String, String>> getStatus(@PathVariable String jobId) {
        String status = importService.getStatus(jobId);
        return ResponseEntity.ok(Map.of("jobId", jobId, "status", status));
    }
}
