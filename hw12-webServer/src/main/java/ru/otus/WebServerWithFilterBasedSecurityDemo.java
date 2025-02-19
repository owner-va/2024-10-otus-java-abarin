package ru.otus;

import com.google.gson.GsonBuilder;
import org.hibernate.cfg.Configuration;
import ru.otus.db.dbmigrations.MigrationsExecutorFlyway;
import ru.otus.db.repository.DataTemplateHibernate;
import ru.otus.db.repository.HibernateUtils;
import ru.otus.db.sessionmanager.TransactionManagerHibernate;
import ru.otus.model.Address;
import ru.otus.model.Client;
import ru.otus.model.Phone;
import ru.otus.model.User;
import ru.otus.server.UsersWebServer;
import ru.otus.server.UsersWebServerWithFilterBasedSecurity;
import ru.otus.services.ServiceClientImpl;
import ru.otus.services.TemplateProcessorImpl;
import ru.otus.services.UserAuthServiceImpl;
import ru.otus.services.UserServiceImpl;

/*
    Полезные для демо ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/users

    // REST сервис
    http://localhost:8080/api/user/3
*/
public class WebServerWithFilterBasedSecurityDemo {
    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);
        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, User.class, Client.class, Address.class, Phone.class);
        runMigration(configuration);
        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        UsersWebServer usersWebServer = getUsersWebServer(transactionManager);
        usersWebServer.start();
        usersWebServer.join();
    }

    private static void runMigration(Configuration configuration) {
        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");
        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();
    }

    private static UsersWebServer getUsersWebServer(TransactionManagerHibernate transactionManager) {
        var templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        var userTemplate = new DataTemplateHibernate<>(User.class);
        var userService = new UserServiceImpl(userTemplate, transactionManager);
        var authService = new UserAuthServiceImpl(userService);

        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        var dbServiceClient = new ServiceClientImpl(transactionManager, clientTemplate);

        var gson = new GsonBuilder().serializeNulls().setPrettyPrinting().create();
        return new UsersWebServerWithFilterBasedSecurity(
                WEB_SERVER_PORT, authService, userService, dbServiceClient, gson, templateProcessor);
    }
}
