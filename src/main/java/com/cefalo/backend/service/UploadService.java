package com.cefalo.backend.service;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

@Service
public class UploadService {
    public boolean consumeFile(MultipartFile uploadedFile, String fileName) {
        String filePath = "src/main/resources/static/images/post" + fileName + ".png";
        try (InputStream is = uploadedFile.getInputStream()) {
            FileUtils.copyInputStreamToFile(is, new File(filePath));
        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());
        }
        return true;
    }
}
