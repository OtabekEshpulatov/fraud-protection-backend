package com.otabekjan.fraud_protection.controller;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.service.FileService;
import io.jmix.core.FileRef;
import io.jmix.core.FileStorage;
import io.jmix.core.common.util.URLEncodeUtils;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);


    private final FileStorage fileStorage;
    private final FileService fileService;

    @GetMapping("/{fileRef}")
    public void file(HttpServletResponse response, @PathVariable String fileRef) {
        try {
            FileRef file = fileService.decode(fileRef);
            InputStream inputStream = fileStorage.openStream(file);
            String fileName = URLEncodeUtils.encodeUtf8(file.getFileName());

            response.setHeader(HttpHeaders.CACHE_CONTROL, "Cache-Control: public, max-age=31536000");
            response.setHeader("Content-disposition", "attachment; filename=" + fileName);
            response.setHeader(HttpHeaders.CONTENT_TYPE, file.getContentType());

            downloadFromMiddleware(inputStream, response);

        } catch (Exception e) {
            log.warn("FileController.file: {}", e.getMessage());
            error(response);
        }
    }

    @PostMapping
    public ResponseEntity<?> putFile(@RequestParam(name = "file") MultipartFile file) {
        FileRef fileRef = fileService.saveFile(file);
        if (fileRef != null) {
            record responseFile(String fileId, String fileUrl) {
            }
            return ResponseEntity.ok(new responseFile(fileService.encode(fileRef), $.makeFileUrl(fileRef)));
        }

        return ResponseEntity.badRequest().body("cannot save file");
    }

    protected void downloadFromMiddleware(InputStream inputStream, HttpServletResponse response) throws IOException {
        ServletOutputStream os = response.getOutputStream();
        try (InputStream is = inputStream) {
            IOUtils.copy(is, os);
            os.flush();
        } catch (Exception e) {
            log.warn("FileController.downloadFromMiddleware: {}", e.getMessage());
            error(response);
        }
    }

    @SneakyThrows
    protected void error(HttpServletResponse response) {
        if (!response.isCommitted()) response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }
}