package accounts;

import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.grizzly.http.server.NetworkListener;
import org.glassfish.grizzly.threadpool.ThreadPoolConfig;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.net.URI;

/**
 * @author Jakub Kalinowski-Zajdak
 */
public class AccountsMain{

    private static final String HOST = "http://localhost";
    private static final String PORT = "8080";
    private static final String REST_PATH = "rest";
    private static final String BASE_URI = HOST + ":" + PORT + "/" + REST_PATH;

    /**
     * Starts Grizzly HTTP server exposing JAX-RS resources defined in this application.
     * @return Grizzly HTTP server.
     */
    public static HttpServer startServer() {
        ResourceConfig rc = new ResourceConfig().packages("accounts.domain.boundary");
        HttpServer server = GrizzlyHttpServerFactory.createHttpServer(URI.create(BASE_URI), rc);
        NetworkListener grizzlyListener = server.getListener("grizzly");
        grizzlyListener.getTransport().setSelectorRunnersCount(4);
        grizzlyListener.getTransport().setWorkerThreadPoolConfig(
                ThreadPoolConfig.defaultConfig().setCorePoolSize(16).setMaxPoolSize(16));
        return server;
    }

    /**
     * Main method.
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        final HttpServer server = startServer();
        System.out.println("Accounts server has been started. Press any key to stop it.");
        System.in.read();
        server.stop();
    }

}

