package edu.escuelaing.app;

import edu.escuelaing.framework.WebFramework;
import static edu.escuelaing.framework.WebFramework.*;

public class App {
    public static void main(String[] args) throws Exception {
        staticfiles("/webroot");

        get("/hello", (req, resp) -> "Hello " + req.getValues("name"));

        get("/pi", (req, resp) -> {
            return String.valueOf(Math.PI);
        });

        start();
    }
}