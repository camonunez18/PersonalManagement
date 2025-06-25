package com.personal.management.utils;

import com.personal.management.service.model.request.PersonRequest;
import com.personal.management.service.repository.entities.PersonEntity;
import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class RequestMapper {

    public static PersonEntity getPersonEntity(PersonRequest request, String professionId) {
        return PersonEntity.builder()
                .name(request.getName())
                .lastname(request.getLastname())
                .age(request.getAge())
                .idProfession(professionId)
                .build();
    }

}
