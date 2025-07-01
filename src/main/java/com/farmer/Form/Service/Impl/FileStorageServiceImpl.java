package com.farmer.Form.Service.Impl;


import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.farmer.Form.Service.FileStorageService;

import java.io.File;
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

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path dirPath = Paths.get(baseDir, folder);
        Files.createDirectories(dirPath);
        Path filePath = dirPath.resolve(filename);
        file.transferTo(filePath);
        return filename;
    }
}
