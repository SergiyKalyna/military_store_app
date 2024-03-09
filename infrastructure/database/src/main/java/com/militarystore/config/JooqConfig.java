package com.militarystore.config;

import org.jooq.ConnectionProvider;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.TransactionProvider;
import org.jooq.impl.DSL;
import org.jooq.impl.DefaultConfiguration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JooqConfig {

    @Bean
    public DSLContext dslContext(
            ConnectionProvider connectionProvider,
            TransactionProvider transactionProvider,
            @Value("${spring.jooq.sql-dialect}") SQLDialect sqlDialect
    ) {
        var configuration = new DefaultConfiguration();
        configuration.set(sqlDialect);
        configuration.setConnectionProvider(connectionProvider);
        configuration.set(transactionProvider);

        return DSL.using(configuration);
    }
}
