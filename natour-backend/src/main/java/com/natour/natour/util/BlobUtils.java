package com.natour.natour.util;

import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

import javax.transaction.Transactional;

public abstract class BlobUtils {
    
    @Transactional
    public static byte[] getBytesFromBlob(Blob blob) {
        try {
            return blob.getBinaryStream().readAllBytes();
        } catch (IOException | SQLException e) {
            throw new RuntimeException("Error reading bytes blob " + e.getMessage());
        }
    }
}
