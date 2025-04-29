package com.otabekjan.fraud_protection.service;

import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class FileService {

    private static final Logger log = LoggerFactory.getLogger(FileService.class);
    private final FileStorage fileStorage;

    public String encode(FileRef file) {
        return Base64.encodeBase64URLSafeString(file.toString().getBytes());
    }

    public FileRef decode(String encodedFileId) {
        try {
            String decoded = new String(Base64.decodeBase64URLSafe(encodedFileId));
            return FileRef.fromString(decoded);
        } catch (Exception ignored) {
        }
        return null;
    }

    public FileRef saveFile(MultipartFile file) {
        if (file == null) return null;

        try (InputStream inputStream = new ByteArrayInputStream(file.getBytes())) {
            return fileStorage.saveStream(Objects.requireNonNullElse(file.getOriginalFilename(), "file"), inputStream);
        } catch (Exception ex) {
            log.warn("File save error", ex);
        }
        return null;
    }
}
