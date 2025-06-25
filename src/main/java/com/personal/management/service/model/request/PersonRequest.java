package com.personal.management.service.model.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonRequest {

    String name;
    String lastname;
    Integer age;
    String profession;

}
