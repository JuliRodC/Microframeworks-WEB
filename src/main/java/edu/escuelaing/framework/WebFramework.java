package edu.escuelaing.framework;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpExchange;
import java.io.*;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class WebFramework {

    private static Map<String, BiFunction<Request, Response, String>> routes = new HashMap<>();
    private static String staticFilesPath = "";

    public static void get(String path, BiFunction<Request, Response, String> handler) {
        routes.put(path, handler);
    }

    public static void staticfiles(String path) {
        staticFilesPath = path;
    }

    public static void start() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/", exchange -> {
            String path = exchange.getRequestURI().getPath();

            // Buscar ruta REST
            if (routes.containsKey(path)) {
                Request req = new Request(exchange);
                Response res = new Response();
                String result = routes.get(path).apply(req, res);
                byte[] bytes = result.getBytes();
                exchange.getResponseHeaders().set("Content-Type", "text/plain");
                exchange.sendResponseHeaders(200, bytes.length);
                OutputStream os = exchange.getResponseBody();
                os.write(bytes);
                os.close();
            } else {
                // Buscar archivo estático
                String resourcePath = staticFilesPath + path;
                InputStream is = WebFramework.class.getResourceAsStream(resourcePath);
                if (is != null) {
                    byte[] bytes = is.readAllBytes();
                    String contentType = getContentType(path);
                    exchange.getResponseHeaders().set("Content-Type", contentType);
                    exchange.sendResponseHeaders(200, bytes.length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(bytes);
                    os.close();
                } else {
                    String response = "404 Not Found";
                    exchange.sendResponseHeaders(404, response.length());
                    OutputStream os = exchange.getResponseBody();
                    os.write(response.getBytes());
                    os.close();
                }
            }
        });

        server.start();
        System.out.println("Server started on port 8080");
    }

    private static String getContentType(String path) {
        if (path.endsWith(".html")) return "text/html";
        if (path.endsWith(".css")) return "text/css";
        if (path.endsWith(".js")) return "application/javascript";
        if (path.endsWith(".png")) return "image/png";
        if (path.endsWith(".jpg")) return "image/jpeg";
        return "text/plain";
    }
    public static Map<String, BiFunction<Request, Response, String>> getRoutes() {
        return routes;
    }

    public static String getStaticFilesPath() {
        return staticFilesPath;
    }
}