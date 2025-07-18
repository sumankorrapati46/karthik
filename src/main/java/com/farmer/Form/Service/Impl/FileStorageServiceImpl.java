package com.farmer.Form.Service.Impl;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.farmer.Form.Service.FileStorageService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    private final String baseDir = "uploads";

    @Override
    public String storeFile(MultipartFile file, String folder) throws IOException {
        if (file == null || file.isEmpty()) return null;

        // Generate a safe and unique filename
        String originalFilename = file.getOriginalFilename().replaceAll("[^a-zA-Z0-9\\.\\-]", "_");
        String filename = UUID.randomUUID() + "_" + originalFilename;

        // ✅ Create directory safely
        Path dirPath = Paths.get(baseDir, folder).toAbsolutePath().normalize();
        Files.createDirectories(dirPath);

        // ✅ Full path to save the file
        Path filePath = dirPath.resolve(filename);
        file.transferTo(filePath.toFile());

        // ✅ Return relative path (for DB or frontend if needed)
        return folder + "/" + filename;
    }
}
