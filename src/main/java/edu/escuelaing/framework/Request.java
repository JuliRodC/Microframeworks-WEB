package edu.escuelaing.framework;

import com.sun.net.httpserver.HttpExchange;
import java.util.HashMap;
import java.util.Map;

public class Request {
    private Map<String, String> queryParams = new HashMap<>();

    public Request(HttpExchange exchange) {
        String query = exchange.getRequestURI().getQuery();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] pair = param.split("=");
                if (pair.length == 2) {
                    queryParams.put(pair[0], pair[1]);
                }
            }
        }
    }

    public String getValues(String key) {
        return queryParams.getOrDefault(key, "");
    }
}