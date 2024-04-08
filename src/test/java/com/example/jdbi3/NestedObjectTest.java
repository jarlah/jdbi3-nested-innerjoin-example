package com.example.jdbi3;

import org.jdbi.v3.testing.junit5.JdbiExtension;
import org.jdbi.v3.testing.junit5.tc.JdbiTestcontainersExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.testcontainers.containers.JdbcDatabaseContainer;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Testcontainers
public class NestedObjectTest {
    @Container
    static final JdbcDatabaseContainer MY_SQL_CONTAINER = new MySQLContainer();

    @RegisterExtension
    static final JdbiExtension extension = JdbiTestcontainersExtension.instance(MY_SQL_CONTAINER);

    @Test
    public void test() {
        assertTrue(MY_SQL_CONTAINER.isRunning());
        var jdbi = extension.getJdbi().installPlugins();
        jdbi.useHandle(handle -> {
            handle.createScript("""
                create table nested_stuff (
                    id int primary key,
                    name varchar(100)
                )
            """).execute();
            handle.createScript("""
                CREATE TABLE stuff (
                    id INT PRIMARY KEY,
                    name VARCHAR(100),
                    stuff_id INT,
                    FOREIGN KEY (stuff_id) REFERENCES nested_stuff(id)
                );
            """).execute();
            handle.createUpdate("insert into nested_stuff (id, name) values (1, 'nested_stuff1')").execute();
            handle.createUpdate("insert into stuff (id, name, stuff_id) values (1, 'stuff1', 1)").execute();
            handle.close();
        });
        assertEquals("1", jdbi.onDemand(StuffRepo.class).listStuff().get(0).nestedStuff.id);
    }
}
