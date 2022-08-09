package com.natour.natour.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.natour.natour.model.entity.Trail;

@Repository
public interface TrailRepository extends CrudRepository<Trail, Long> {
    
}
