package ru.otus;

import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.cachehw.HwCache;
import ru.otus.cachehw.HwListener;
import ru.otus.cachehw.MyCache;
import ru.otus.core.repository.executor.DbExecutorImpl;
import ru.otus.core.sessionmanager.TransactionRunnerJdbc;
import ru.otus.crm.datasource.DriverManagerDataSource;
import ru.otus.crm.model.Client;
import ru.otus.crm.service.DbServiceClientImpl;
import ru.otus.jdbc.mapper.DataTemplateJdbc;
import ru.otus.jdbc.mapper.EntityClassMetaData;
import ru.otus.jdbc.mapper.EntitySQLMetaData;
import ru.otus.jdbc.mapper.impl.EntityClassMetaDataImpl;
import ru.otus.jdbc.mapper.impl.EntitySQLMetaDataImpl;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S125", "java:S1481"})
public class HomeWorkCache {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWorkCache.class);

    public static void main(String[] args) throws NoSuchMethodException, InterruptedException {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        // Работа с клиентом
        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        var dataTemplateClient = new DataTemplateJdbc<>(dbExecutor,
                entitySQLMetaDataClient,
                entityClassMetaDataClient); // реализация DataTemplate, универсальная

        HwCache<Long, Client> cache = new MyCache<>();
        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient, cache);

        var listener = new HwListener<Long, Client>() {
            @Override
            public void notify(Long key, Client value, String action) {
                log.info("key:{}, value:{}, action: {}", key, value, action);
            }
        };
        cache.addListener(listener);

        List<Long> ids = new ArrayList<>();
        for (int i = 0; i < 500; i++) {
            ids.add(dbServiceClient.saveClient(new Client("client" + i)).getId());
        }

        var timeStartWithCacheBeforeGC = LocalTime.now();
        for (Long id : ids) {
            dbServiceClient
                    .getClient(id)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + id));
        }
        var timeEndWithCacheBeforeGC = LocalTime.now();
        System.gc();
        Thread.sleep(1000);
        var timeStartWithoutCacheAfterGC = LocalTime.now();
        for (Long id : ids) {
            dbServiceClient
                    .getClient(id)
                    .orElseThrow(() -> new RuntimeException("Client not found, id:" + id));
        }
        var timeEndWithoutCacheAfterGC = LocalTime.now();

        log.info("Time with cache: {}", Duration.between(timeStartWithCacheBeforeGC, timeEndWithCacheBeforeGC));
        log.info("Time without cache: {}", Duration.between(timeStartWithoutCacheAfterGC, timeEndWithoutCacheAfterGC));

        cache.removeListener(listener);
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
