package com.personal.management.service.repository;

import com.personal.management.service.repository.entities.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<PersonEntity, String> {
}
