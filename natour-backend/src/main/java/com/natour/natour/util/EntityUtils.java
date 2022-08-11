package com.natour.natour.util;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import lombok.extern.java.Log;

@Log
public abstract class EntityUtils {
    public static <T> T findEntityById(
        CrudRepository<T, Long> repository, 
        Long id,
        String exceptionMessage
    ) {
        final Optional<T> entity = repository.findById(id);
        if (entity.isEmpty()) {
            log.warning(exceptionMessage);
            throw new RuntimeException(exceptionMessage);
        }
        return entity.get();
    }
}
