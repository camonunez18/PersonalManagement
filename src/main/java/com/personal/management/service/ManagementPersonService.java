package com.personal.management.service;

import com.personal.management.service.model.request.PersonRequest;
import com.personal.management.service.model.response.Person;
import com.personal.management.service.repository.PersonRepository;
import com.personal.management.service.repository.ProfessionRepository;
import com.personal.management.service.repository.entities.PersonEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

import static com.personal.management.utils.RequestMapper.getPersonEntity;
import static java.lang.String.format;
import static reactor.core.publisher.Mono.error;

@Service
@RequiredArgsConstructor
public class ManagementPersonService {

    private final PersonRepository personRepository;
    private final ProfessionRepository professionRepository;

    public Mono<String> savePerson(PersonRequest personRequest) {
        return professionRepository.findByDescription(personRequest.getProfession())
                .switchIfEmpty(
                        error(new IllegalArgumentException("Profession with description '" +
                                personRequest.getProfession() + "' not found."))
                )
                .flatMap(professionEntity ->
                        personRepository.save(getPersonEntity(personRequest,
                                        professionEntity.getProfessionEntityId()))
                                .map(PersonEntity::getId));
    }

    public Mono<String> updatePerson(String id, PersonRequest personRequest) {
        return personRepository.findById(id)
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Person with ID '" + id + "' not found.")))
                .flatMap(existingPerson ->
                        professionRepository.findByDescription(personRequest.getProfession())
                                .switchIfEmpty(
                                        Mono.error(new IllegalArgumentException("Profession with description '" +
                                                personRequest.getProfession() + "' not found."))
                                )
                                .flatMap(professionEntity -> {
                                    existingPerson.setName(personRequest.getName());
                                    existingPerson.setLastname(personRequest.getLastname());
                                    existingPerson.setAge(personRequest.getAge());
                                    existingPerson.setIdProfession(professionEntity.getProfessionEntityId());
                                    return personRepository.save(existingPerson);
                                })
                )
                .map(ignore -> "Person updated");
    }

    public Mono<List<Person>> getAllPeople() {
        return personRepository.findAll()
                .flatMap(personEntity -> professionRepository
                        .findById(personEntity.getIdProfession())
                        .map(professionEntity -> Person.builder()
                                .id(personEntity.getId())
                                .name(personEntity.getName())
                                .lastName(personEntity.getLastname())
                                .age(personEntity.getAge())
                                .profession(professionEntity.getDescription())
                                .salary(professionEntity.getSalary())
                                .build())
                )
                .collectList();
    }

    public Mono<String> deletePerson(String id) {
        return personRepository.deleteById(id)
                .thenReturn(format("Person deleted with id: %s", id));
    }
}
