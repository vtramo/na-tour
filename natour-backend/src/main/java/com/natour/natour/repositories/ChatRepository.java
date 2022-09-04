package com.natour.natour.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.natour.natour.model.entity.Chat;

@Repository
public interface ChatRepository extends CrudRepository<Chat, Long> {}
