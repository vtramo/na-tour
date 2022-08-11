package com.natour.natour.util;

import java.io.IOException;
import java.sql.Blob;

import org.hibernate.engine.jdbc.BlobProxy;
import org.springframework.web.multipart.MultipartFile;

public abstract class MultipartFileUtils {
    public static Blob createBlobFromMultipartFile(
        final MultipartFile multipartFile,
        final String contentType
    ) {
        if (multipartFile.getContentType().matches(contentType)) {
            throw new IllegalArgumentException(
                "The content type must be: " + contentType);
        }
        return BlobProxy.generateProxy(
            null
        );
    }

    public static byte[] getBytesFromMultipartFile(
        final MultipartFile multipartFile,
        final String contentType
    ) {
        if (multipartFile.getContentType().matches(contentType)) {
            throw new IllegalArgumentException(
                "The content type must be: " + contentType);
        }
        try {
            return multipartFile.getBytes();
        } catch (IOException e) {
            throw new RuntimeException("An error occurred in getting the contents " + 
                " of the file as a byte array.");
        }
    }
}
