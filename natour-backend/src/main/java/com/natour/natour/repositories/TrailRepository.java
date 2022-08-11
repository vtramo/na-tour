package com.natour.natour.repositories;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.natour.natour.model.entity.Trail;

@Repository
public interface TrailRepository extends PagingAndSortingRepository<Trail, Long> {
    
}
