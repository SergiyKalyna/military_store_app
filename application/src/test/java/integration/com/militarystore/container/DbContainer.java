package com.militarystore.container;

import org.jooq.DSLContext;
import org.testcontainers.containers.PostgreSQLContainer;

import static com.militarystore.jooq.Tables.CATEGORIES;
import static com.militarystore.jooq.Tables.DELIVERY_DETAILS;
import static com.militarystore.jooq.Tables.DISCOUNTS;
import static com.militarystore.jooq.Tables.ORDERS;
import static com.militarystore.jooq.Tables.PRODUCTS;
import static com.militarystore.jooq.Tables.USERS;

public class DbContainer extends PostgreSQLContainer<DbContainer> {

    private static final Integer DEFAULT_PORT = 5431;

    public DbContainer() {
        super("postgres:15.2");
        addExposedPort(DEFAULT_PORT);
        withCreateContainerCmdModifier(cmd -> cmd.withName("postgres-it-container"));
    }

    public static void clearTable(DSLContext dslContext) {
        dslContext.truncate(CATEGORIES).cascade().execute();
        dslContext.truncate(PRODUCTS).cascade().execute();
        dslContext.truncate(USERS).cascade().execute();
        dslContext.truncate(ORDERS).cascade().execute();
        dslContext.truncate(DISCOUNTS).execute();
        dslContext.truncate(DELIVERY_DETAILS).execute();
    }
}
