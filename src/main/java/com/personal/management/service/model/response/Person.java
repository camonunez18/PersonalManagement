package com.personal.management.service.model.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Person {

    String id;
    String name;
    String lastName;
    Integer age;
    String profession;
    Double salary;

}
