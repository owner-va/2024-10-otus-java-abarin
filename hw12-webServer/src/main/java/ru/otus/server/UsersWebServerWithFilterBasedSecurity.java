package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.FilterHolder;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import ru.otus.services.ServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserAuthService;
import ru.otus.servlet.AuthorizationFilter;
import ru.otus.servlet.LoginServlet;

import java.util.Arrays;

public class UsersWebServerWithFilterBasedSecurity extends UsersWebServerSimple {
    private final UserAuthService authService;

    public UsersWebServerWithFilterBasedSecurity(
            int port,
            UserAuthService authService,
            ServiceClient serviceClient,
            Gson gson,
            TemplateProcessor templateProcessor) {
        super(port, serviceClient, gson, templateProcessor);
        this.authService = authService;
    }

    @Override
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        servletContextHandler.addServlet(new ServletHolder(new LoginServlet(templateProcessor, authService)), "/login");
        AuthorizationFilter authorizationFilter = new AuthorizationFilter();
        Arrays.stream(paths)
                .forEachOrdered(
                        path -> servletContextHandler.addFilter(new FilterHolder(authorizationFilter), path, null));
        return servletContextHandler;
    }
}
