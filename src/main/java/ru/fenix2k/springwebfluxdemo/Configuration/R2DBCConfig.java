package ru.fenix2k.springwebfluxdemo.Configuration;

import io.r2dbc.h2.H2ConnectionConfiguration;
import io.r2dbc.h2.H2ConnectionFactory;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.r2dbc.connection.init.CompositeDatabasePopulator;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;
import org.springframework.transaction.ReactiveTransactionManager;

@Configuration
@EnableR2dbcRepositories
public class R2DBCConfig extends AbstractR2dbcConfiguration {

    @Value("${spring.r2dbc.url:}")
    private String dbUrl;
    @Value("${spring.r2dbc.username:sa}")
    private String dbUsername;
    @Value("${spring.r2dbc.password:}")
    private String dbUserPassword;
    @Value("${spring.r2dbc.schema:schema.sql}")
    private String dbSchemaScript;
    @Value("${spring.r2dbc.data:data.sql}")
    private String dbDataScript;

    @Override
    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        return new H2ConnectionFactory(
                H2ConnectionConfiguration.builder()
                        .url(dbUrl)
                        .username(dbUsername)
                        .password(dbUserPassword)
                        .build()
        );
    }

    @Bean
    public ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {
        ConnectionFactoryInitializer initializer = new ConnectionFactoryInitializer();
        initializer.setConnectionFactory(connectionFactory);

        CompositeDatabasePopulator populator = new CompositeDatabasePopulator();

        ClassPathResource pathSchemaResource = new ClassPathResource(dbSchemaScript);
        if (pathSchemaResource.exists()) {
            populator.addPopulators(new ResourceDatabasePopulator(pathSchemaResource));
        }

        ClassPathResource pathDataResource = new ClassPathResource(dbDataScript);
        if (pathDataResource.exists()) {
            populator.addPopulators(new ResourceDatabasePopulator(pathDataResource));
        }

        initializer.setDatabasePopulator(populator);

        return initializer;
    }

    @Bean
    ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new R2dbcTransactionManager(connectionFactory);
    }

}
