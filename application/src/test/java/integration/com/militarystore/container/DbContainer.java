package com.militarystore.container;

import org.jooq.DSLContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.SUBCATEGORIES;
import static com.militarystore.jooq.Tables.USERS;

public class DbContainer extends PostgreSQLContainer<DbContainer> {

    private static final Integer DEFAULT_PORT = 5432;

    public DbContainer() {
        super("postgres:15.2");
        addExposedPort(DEFAULT_PORT);
        withCreateContainerCmdModifier(cmd -> cmd.withName("postgres-it-container"));
    }

    public static void clearTable(DSLContext dslContext) {
        dslContext.truncate(USERS).execute();
        dslContext.truncate(SUBCATEGORIES).cascade().execute();
        dslContext.truncate(CATEGORIES).cascade().execute();
    }
}
