package edu.escuelaing.framework;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WebFrameworkTest {

    @Test
    void testGetRouteRegistration() {
        WebFramework.get("/test", (req, res) -> "test response");
        assertNotNull(WebFramework.getRoutes().get("/test"));
    }

    @Test
    void testStaticFilesPath() {
        WebFramework.staticfiles("/webroot");
        assertEquals("/webroot", WebFramework.getStaticFilesPath());
    }

    @Test
    void testLambdaResponse() {
        WebFramework.get("/hello-test", (req, res) -> "Hello World");
        var handler = WebFramework.getRoutes().get("/hello-test");
        assertNotNull(handler);
        String result = handler.apply(null, null);
        assertEquals("Hello World", result);
    }

    @Test
    void testPiRoute() {
        WebFramework.get("/pi-test", (req, res) -> String.valueOf(Math.PI));
        var handler = WebFramework.getRoutes().get("/pi-test");
        String result = handler.apply(null, null);
        assertEquals("3.141592653589793", result);
    }
}