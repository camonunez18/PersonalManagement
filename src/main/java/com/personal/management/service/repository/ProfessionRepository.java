package com.personal.management.service.repository;

import com.personal.management.service.repository.entities.ProfessionEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface ProfessionRepository extends ReactiveCrudRepository<ProfessionEntity, String> {

    Mono<ProfessionEntity> findByDescription(String description);

}
