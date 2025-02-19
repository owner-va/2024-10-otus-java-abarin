package ru.otus.server;

import com.google.gson.Gson;
import org.eclipse.jetty.ee10.servlet.ServletContextHandler;
import org.eclipse.jetty.ee10.servlet.ServletHolder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import ru.otus.helpers.FileSystemHelper;
import ru.otus.services.ServiceClient;
import ru.otus.services.TemplateProcessor;
import ru.otus.services.UserService;
import ru.otus.servlet.ClientServlet;
import ru.otus.servlet.UsersApiServlet;
import ru.otus.servlet.UsersServlet;

public class UsersWebServerSimple implements UsersWebServer {
    private static final String START_PAGE_NAME = "index.html";
    private static final String COMMON_RESOURCES_DIR = "static";

    private final UserService userService;
    private final Gson gson;
    protected final TemplateProcessor templateProcessor;
    private final Server server;
    private final ServiceClient clientService;

    public UsersWebServerSimple(
            int port,
            UserService userService,
            ServiceClient serviceClient,
            Gson gson,
            TemplateProcessor templateProcessor) {
        this.clientService = serviceClient;
        this.userService = userService;
        this.gson = gson;
        this.templateProcessor = templateProcessor;
        server = new Server(port);
    }

    @Override
    public void start() throws Exception {
        if (server.getHandlers().isEmpty()) {
            initContext();
        }
        server.start();
    }

    @Override
    public void join() throws Exception {
        server.join();
    }

    @Override
    public void stop() throws Exception {
        server.stop();
    }

    private void initContext() {

        ResourceHandler resourceHandler = createResourceHandler();
        ServletContextHandler servletContextHandler = createServletContextHandler();

        Handler.Sequence sequence = new Handler.Sequence();
        sequence.addHandler(resourceHandler);
        sequence.addHandler(applySecurity(servletContextHandler, "/admin/*", "/users", "/api/user/*"));

        server.setHandler(sequence);
    }

    @SuppressWarnings({"squid:S1172"})
    protected Handler applySecurity(ServletContextHandler servletContextHandler, String... paths) {
        return servletContextHandler;
    }

    private ResourceHandler createResourceHandler() {
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirAllowed(false);
        resourceHandler.setWelcomeFiles(START_PAGE_NAME);
        resourceHandler.setBaseResourceAsString(
                FileSystemHelper.localFileNameOrResourceNameToFullPath(COMMON_RESOURCES_DIR));
        return resourceHandler;
    }

    private ServletContextHandler createServletContextHandler() {
        ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        servletContextHandler.addServlet(new ServletHolder(new UsersServlet(templateProcessor, userService)), "/users");
        servletContextHandler.addServlet(new ServletHolder(new UsersApiServlet(userService, gson)), "/api/user/*");
        servletContextHandler.addServlet(
                new ServletHolder(new ClientServlet(templateProcessor, clientService)), "/admin/clients");
        return servletContextHandler;
    }
}
