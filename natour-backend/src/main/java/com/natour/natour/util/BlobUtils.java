package com.natour.natour.util;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.Base64;

import javax.sql.rowset.serial.SerialBlob;

import org.springframework.transaction.annotation.Transactional;


public abstract class BlobUtils {
    
    @Transactional
    public static byte[] getBytesFromBlob(Blob blob) {
        try {
            return blob.getBinaryStream().readAllBytes();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error converting blob to bytes " + e.getMessage());
        }
    }

    @Transactional
    public static Blob getBlobFromBytes(byte[] bytes) {
        try {
            return new SerialBlob(bytes);
        } catch (Exception e) {
            throw new RuntimeException("Error converting bytes to blob " + e.getMessage());
        }
    }

    @Transactional
    public static String decodeBlob(Blob blob) {
        if (blob == null) return null;
        byte[] blobBytes = getBytesFromBlob(blob);
        return Base64.getEncoder().encodeToString(blobBytes);
    }
}
