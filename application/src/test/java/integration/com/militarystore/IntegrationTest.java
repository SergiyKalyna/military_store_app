package com.militarystore;

import com.militarystore.container.DbContainer;
import com.militarystore.port.out.email.SendEmailPort;
import org.jooq.DSLContext;
import org.junit.jupiter.api.AfterEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    properties = {
        "spring.cloud.vault.enabled=false",
        "google.drive.credentials.path=src/test/resources/mock-google-drive-credentials.json",
        "spring.mail.username=test-email@gmail.com"
    }
)
@Testcontainers
public abstract class IntegrationTest {

    @Autowired
    protected DSLContext dslContext;

    @MockBean
    protected SendEmailPort sendEmailPort;

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
