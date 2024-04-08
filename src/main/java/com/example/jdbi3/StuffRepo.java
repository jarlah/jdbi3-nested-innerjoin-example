package com.example.jdbi3;

import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.statement.SqlQuery;

import java.util.List;

public interface StuffRepo {

    @SqlQuery("""
        SELECT
            s.id AS s_id,
            s.name AS s_name,
            ns.id AS ns_id,
            ns.name AS ns_name
        FROM stuff s
        INNER JOIN nested_stuff ns ON s.stuff_id = ns.id
    """)
    @RegisterBeanMapper(value = Stuff.class, prefix = "s")
    @RegisterBeanMapper(value = NestedStuff.class, prefix = "ns")
    List<Stuff> listStuff();
}
