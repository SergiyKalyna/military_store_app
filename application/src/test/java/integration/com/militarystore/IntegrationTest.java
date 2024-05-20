package com.militarystore;

import com.militarystore.container.DbContainer;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = "spring.cloud.vault.enabled=false"
)
@Testcontainers
public abstract class IntegrationTest {

    @Autowired
    protected DSLContext dslContext;

    @ServiceConnection
    private static final DbContainer DB_CONTAINER;

    static {
        DB_CONTAINER = new DbContainer();
        DB_CONTAINER.start();
    }

    @AfterEach
    void afterEach() {
        DbContainer.clearTable(dslContext);
    }
}
