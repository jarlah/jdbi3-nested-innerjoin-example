package com.example.jdbi3;

import lombok.Data;
import org.jdbi.v3.core.mapper.Nested;

@Data
public class Stuff {
    String id;
    String name;
    @Nested
    NestedStuff nestedStuff;
}
