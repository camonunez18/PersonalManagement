package com.personal.management.controller;

import com.personal.management.service.ManagementPersonService;
import com.personal.management.service.model.request.PersonRequest;
import com.personal.management.service.model.response.Person;
import com.personal.management.utils.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/person/management")
@RequiredArgsConstructor
public class PersonController {

    private final ManagementPersonService service;

    @PostMapping
    public Mono<ResponseEntity<ResponseBody<String>>> savePeople(
            @RequestBody PersonRequest personRequest
    ) {
        return service.savePerson(personRequest)
                .map(response -> ResponseEntity.ok(buildOkResponse(
                        "Person created successfully", CREATED, response)));
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<ResponseBody<String>>> updatePerson(
            @PathVariable String id,
            @RequestBody PersonRequest personRequest
    ) {
        return service.updatePerson(id, personRequest)
                .map(response -> ResponseEntity.ok(buildOkResponse(
                        "Person updated successfully", OK, response)));
    }

    @GetMapping
    public Mono<ResponseEntity<ResponseBody<List<Person>>>> getAllPeople() {
        return service.getAllPeople()
                .map(response -> ResponseEntity
                        .ok(buildOkResponse("List of people successfully obtained",
                                OK, response)));
    }

    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<ResponseBody<String>>> deletePerson(
            @PathVariable String id
    ) {
        return service.deletePerson(id)
                .map(response -> ResponseEntity
                        .ok(buildOkResponse("Person deleted",
                                OK, response)));
    }

    private <T> ResponseBody<T> buildOkResponse(String message, HttpStatus status, T data) {
        return ResponseBody.<T>builder()
                .message(message)
                .status(status.value())
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
