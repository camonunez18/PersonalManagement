package com.personal.management.utils;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ResponseBody<T> {

    String message;
    int status;
    T data;
    long timestamp;

}
