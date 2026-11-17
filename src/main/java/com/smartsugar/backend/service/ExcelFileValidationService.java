
package com.smartsugar.backend.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import java.util.HexFormat;

@Service
public class ExcelFileValidationService {

    @Value("${app.upload.max-file-size-bytes:26214400}")
    private long maxFileSize;

    public String validateAndGetFileName(MultipartFile file)
            throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException(
                    "Please upload a non-empty Excel file."
            );
        }

        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException(
                    "File size exceeds the permitted upload limit."
            );
        }

        String originalName = file.getOriginalFilename();

        if (originalName == null || originalName.isBlank()) {
            throw new IllegalArgumentException(
                    "File name is missing."
            );
        }

        String safeFileName = originalName.replace("\\", "/");

        safeFileName = safeFileName.substring(
                safeFileName.lastIndexOf("/") + 1
        ).trim();

        if (safeFileName.isBlank()
                || safeFileName.length() > 255
                || safeFileName.contains("..")) {

            throw new IllegalArgumentException(
                    "Invalid file name."
            );
        }

        if (!safeFileName.toLowerCase().endsWith(".xlsx")) {
            throw new IllegalArgumentException(
                    "Only .xlsx files are allowed."
            );
        }

        // Check actual XLSX ZIP signature
        try (InputStream input =
                     new BufferedInputStream(file.getInputStream())) {

            byte[] signature = new byte[4];

            int bytesRead = input.read(signature);

            boolean validSignature =
                    bytesRead == 4
                    && signature[0] == 0x50
                    && signature[1] == 0x4B
                    && signature[2] == 0x03
                    && signature[3] == 0x04;

            if (!validSignature) {
                throw new IllegalArgumentException(
                        "The uploaded file does not appear to be a valid XLSX file."
                );
            }
        }

        return safeFileName;
    }

    public String calculateSha256(MultipartFile file)
            throws IOException {

        try {

            MessageDigest digest =
                    MessageDigest.getInstance("SHA-256");

            try (InputStream input = file.getInputStream()) {

                byte[] buffer = new byte[8192];

                int bytesRead;

                while ((bytesRead = input.read(buffer)) != -1) {

                    digest.update(buffer, 0, bytesRead);
                }
            }

            return HexFormat.of().formatHex(digest.digest());

        } catch (NoSuchAlgorithmException e) {

            throw new IllegalStateException(
                    "SHA-256 algorithm is unavailable.",
                    e
            );
        }
    }
}
