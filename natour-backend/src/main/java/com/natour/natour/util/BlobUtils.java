package com.natour.natour.util;

import java.io.IOException;
import java.sql.Blob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

public abstract class BlobUtils {
    public static Blob createBlobFromMultipartFile(
        final MultipartFile multipartFile,
        final String contentType
    ) {
        if (multipartFile.getContentType().matches(contentType)) {
            throw new IllegalArgumentException(
                "The content type must be: " + contentType);
        }
        try {
            return BlobProxy.generateProxy(multipartFile.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("Error in creating the blob: " + contentType);
        }
    }
}
