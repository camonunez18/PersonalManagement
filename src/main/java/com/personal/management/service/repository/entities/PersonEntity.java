package com.personal.management.service.repository.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table(name = "person")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PersonEntity {

    @Id
    String id;

    @Column("name")
    String name;

    @Column("lastname")
    String lastname;

    @Column("age")
    Integer age;

    @Column("idProfession")
    String idProfession;

}
