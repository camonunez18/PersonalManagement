package com.personal.management.service.repository.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table(name = "profession")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProfessionEntity {

    @Id
    @Column("id")
    String professionEntityId;

    @Column("description")
    String description;

    @Column("salary")
    Double salary;
}
