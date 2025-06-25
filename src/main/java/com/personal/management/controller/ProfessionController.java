package com.personal.management.controller;

import com.personal.management.service.ManagementProfessionService;
import com.personal.management.service.model.response.Profession;
import com.personal.management.utils.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/profession/management")
@RequiredArgsConstructor
public class ProfessionController {

    private final ManagementProfessionService service;

    @GetMapping
    public Mono<ResponseEntity<ResponseBody<List<Profession>>>> getAllProfessions() {
        return service.getAllProfessions()
                .map(response -> ResponseEntity
                        .ok(buildOkResponse("List of people successfully obtained",
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
