package com.personal.management.service;

import com.personal.management.service.model.request.PersonRequest;
import com.personal.management.service.model.response.Person;
import com.personal.management.service.repository.PersonRepository;
import com.personal.management.service.repository.ProfessionRepository;

import com.personal.management.service.repository.entities.PersonEntity;
import com.personal.management.service.repository.entities.ProfessionEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ManagementPersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @Mock
    private ProfessionRepository professionRepository;

    @InjectMocks
    private ManagementPersonService managementPersonservice;

    private String personId;
    private String professionId;
    private PersonRequest personRequest;
    private PersonEntity personEntity;
    private ProfessionEntity professionEntity;
    private Person personResponseDto;

    @BeforeEach
    void setUp() {
        personId = UUID.randomUUID().toString();
        professionId = UUID.randomUUID().toString();

        personRequest = PersonRequest.builder()
                .name("John")
                .lastname("Doe")
                .age(30)
                .profession("Software Developer")
                .build();

        personEntity = PersonEntity.builder()
                .id(personId)
                .name("John")
                .lastname("Doe")
                .age(30)
                .idProfession(professionId)
                .build();

        professionEntity = ProfessionEntity.builder()
                .professionEntityId(professionId)
                .description("Software Developer")
                .salary(75000.00)
                .build();

        personResponseDto = Person.builder()
                .id(personId)
                .name("John")
                .lastName("Doe")
                .age(30)
                .profession("Software Developer")
                .salary(75000.00)
                .build();
    }

    @Test
    void savePerson_Success() {
        when(professionRepository.findByDescription(personRequest.getProfession()))
                .thenReturn(Mono.just(professionEntity));
        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(Mono.just(personEntity));

        StepVerifier.create(managementPersonservice.savePerson(personRequest))
                .expectNext("Person saved")
                .verifyComplete();

        verify(professionRepository, times(1)).findByDescription(personRequest.getProfession());
        verify(personRepository, times(1)).save(any(PersonEntity.class));
    }

    @Test
    void savePerson_ProfessionNotFound_ThrowsException() {
        when(professionRepository.findByDescription(personRequest.getProfession()))
                .thenReturn(Mono.empty()); // No devuelve nada

        StepVerifier.create(managementPersonservice.savePerson(personRequest))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Profession with description 'Software Developer' not found."))
                .verify();

        verify(professionRepository, times(1)).findByDescription(personRequest.getProfession());
        verify(personRepository, never()).save(any(PersonEntity.class));
    }

    @Test
    void updatePerson_Success() {
        String newName = "Jane";
        PersonRequest updatedPersonRequest = PersonRequest.builder()
                .name(newName)
                .lastname(personRequest.getLastname())
                .age(personRequest.getAge())
                .profession(personRequest.getProfession())
                .build();

        PersonEntity updatedPersonEntity = PersonEntity.builder()
                .id(personId)
                .name(newName)
                .lastname(personEntity.getLastname())
                .age(personEntity.getAge())
                .idProfession(personEntity.getIdProfession())
                .build();

        when(personRepository.findById(personId)).thenReturn(Mono.just(personEntity));
        when(professionRepository.findByDescription(updatedPersonRequest.getProfession()))
                .thenReturn(Mono.just(professionEntity));
        when(personRepository.save(any(PersonEntity.class)))
                .thenReturn(Mono.just(updatedPersonEntity));

        StepVerifier.create(managementPersonservice.updatePerson(personId, updatedPersonRequest))
                .expectNext("Person updated")
                .verifyComplete();

        verify(personRepository, times(1)).findById(personId);
        verify(professionRepository, times(1)).findByDescription(updatedPersonRequest.getProfession());
        verify(personRepository, times(1)).save(any(PersonEntity.class));
    }

    @Test
    void updatePerson_PersonNotFound_ThrowsException() {
        when(personRepository.findById(personId)).thenReturn(Mono.empty());

        StepVerifier.create(managementPersonservice.updatePerson(personId, personRequest))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Person with ID '" + personId + "' not found."))
                .verify();

        verify(personRepository, times(1)).findById(personId);
        verify(professionRepository, never()).findByDescription(anyString());
        verify(personRepository, never()).save(any(PersonEntity.class));
    }

    @Test
    void updatePerson_ProfessionNotFound_ThrowsException() {
        when(personRepository.findById(personId)).thenReturn(Mono.just(personEntity));
        when(professionRepository.findByDescription(personRequest.getProfession()))
                .thenReturn(Mono.empty());

        StepVerifier.create(managementPersonservice.updatePerson(personId, personRequest))
                .expectErrorMatches(throwable -> throwable instanceof IllegalArgumentException &&
                        throwable.getMessage().contains("Profession with description 'Software Developer' not found."))
                .verify();

        verify(personRepository, times(1)).findById(personId);
        verify(professionRepository, times(1)).findByDescription(personRequest.getProfession());
        verify(personRepository, never()).save(any(PersonEntity.class));
    }

    @Test
    void getAllPeople_ReturnsListOfPeople() {
        PersonEntity personEntity2 = PersonEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Jane")
                .lastname("Smith")
                .age(25)
                .idProfession(professionId)
                .build();
        Person personResponseDto2 = Person.builder()
                .id(personEntity2.getId())
                .name(personEntity2.getName())
                .lastName(personEntity2.getLastname())
                .age(personEntity2.getAge())
                .profession(professionEntity.getDescription())
                .salary(professionEntity.getSalary())
                .build();

        when(personRepository.findAll()).thenReturn(Flux.just(personEntity, personEntity2));
        when(professionRepository.findById(personEntity.getIdProfession())).thenReturn(Mono.just(professionEntity));
        when(professionRepository.findById(personEntity2.getIdProfession())).thenReturn(Mono.just(professionEntity));


        StepVerifier.create(managementPersonservice.getAllPeople())
                .expectNextMatches(peopleList -> {
                    assertThat(peopleList)
                            .hasSize(2);
                    Person actualPerson = peopleList.get(0);

                    assertThat(actualPerson.getId()).isEqualTo(personResponseDto.getId());
                    assertThat(actualPerson.getName()).isEqualTo(personResponseDto.getName());
                    assertThat(actualPerson.getSalary()).isEqualTo(personResponseDto.getSalary());
                    return true;
                })
                .verifyComplete();

        verify(personRepository, times(1)).findAll();
        verify(professionRepository, times(2)).findById(anyString());
    }

    @Test
    void getAllPeople_ReturnsEmptyListWhenNoPeople() {
        when(personRepository.findAll()).thenReturn(Flux.empty());

        StepVerifier.create(managementPersonservice.getAllPeople())
                .expectNext(List.of()) // Espera una lista vacía
                .verifyComplete();

        verify(personRepository, times(1)).findAll();
        verify(professionRepository, never()).findById(anyString());
    }

    @Test
    void getAllPeople_SkipsPersonIfProfessionNotFound() {
        PersonEntity personWithMissingProfession = PersonEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Mark")
                .lastname("Test")
                .age(40)
                .idProfession(UUID.randomUUID().toString())
                .build();

        when(personRepository.findAll()).thenReturn(Flux.just(personEntity, personWithMissingProfession));
        when(professionRepository.findById(personEntity.getIdProfession())).thenReturn(Mono.just(professionEntity));
        when(professionRepository.findById(personWithMissingProfession.getIdProfession())).thenReturn(Mono.empty());

        StepVerifier.create(managementPersonservice.getAllPeople())
                .expectNextMatches(peopleList -> {
                    assertThat(peopleList).hasSize(1);

                    Person actualPerson = peopleList.get(0);

                    assertThat(actualPerson.getId()).isEqualTo(personResponseDto.getId());
                    assertThat(actualPerson.getName()).isEqualTo(personResponseDto.getName());
                    assertThat(actualPerson.getSalary()).isEqualTo(personResponseDto.getSalary());

                    // If all assertions pass, return true
                    return true;
                })
                .verifyComplete();

        verify(personRepository, times(1)).findAll();
        verify(professionRepository, times(2)).findById(anyString());
    }

    @Test
    void deletePerson_Success() {
        when(personRepository.deleteById(personId)).thenReturn(Mono.empty());

        StepVerifier.create(managementPersonservice.deletePerson(personId))
                .expectNext(String.format("Person deleted with id: %s", personId))
                .verifyComplete();

        verify(personRepository, times(1)).deleteById(personId);
    }

    @Test
    void deletePerson_RepositoryError_PropagatesError() {
        RuntimeException dbError = new RuntimeException("Database error during delete");
        when(personRepository.deleteById(personId)).thenReturn(Mono.error(dbError));

        StepVerifier.create(managementPersonservice.deletePerson(personId))
                .expectErrorMatches(throwable -> throwable instanceof RuntimeException &&
                        throwable.getMessage().equals(dbError.getMessage()))
                .verify();

        verify(personRepository, times(1)).deleteById(personId);
    }
}