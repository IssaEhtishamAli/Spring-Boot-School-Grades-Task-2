package com.school.management.Services;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class FileImportService {

    private final Map<String, String> importStatus = new ConcurrentHashMap<>();

    @Async
    public CompletableFuture<String> importFile(MultipartFile file, String jobId) {
        importStatus.put(jobId, "IN_PROGRESS");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            int count = 0;
            while ((line = reader.readLine()) != null) {
                // TODO: parse and save data to DB
                Thread.sleep(50); // simulate processing
                count++;
                importStatus.put(jobId, "Processed " + count + " rows");
            }
            importStatus.put(jobId, "COMPLETED");
        } catch (Exception e) {
            importStatus.put(jobId, "FAILED: " + e.getMessage());
        }
        return CompletableFuture.completedFuture(jobId);
    }


    public String getStatus(String jobId) {
        return importStatus.getOrDefault(jobId, "Job ID not found");
    }
}
