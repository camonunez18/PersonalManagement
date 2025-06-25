package com.personal.management.service;

import com.personal.management.service.model.request.PersonRequest;
import com.personal.management.service.model.response.Profession;
import com.personal.management.service.repository.ProfessionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static reactor.core.publisher.Mono.empty;

@Service
@RequiredArgsConstructor
public class ManagementProfessionService {

    private final ProfessionRepository professionRepository;

    //TODO save Profession
    public Mono<String> saveProfession(PersonRequest personRequest) {
        return empty();
    }

    //TODO update Profession
    public Mono<String> updateProfession(String id, PersonRequest personRequest) {
        return empty();
    }

    public Mono<List<Profession>> getAllProfessions() {
        return professionRepository.findAll()
                .map(professionEntity -> Profession.builder()
                        .id(professionEntity.getProfessionEntityId())
                        .name(professionEntity.getDescription())
                        .salary(professionEntity.getSalary())
                        .build())
                .collectList();
    }

    //TODO delete Profession
    public Mono<String> deleteProfession(String id) {
        return empty();
    }
}
