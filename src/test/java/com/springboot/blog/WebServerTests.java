package com.springboot.blog;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("integration-test")
public class WebServerTests {

    @LocalServerPort
    private int port;

    @Test
    public void testServerPort() {
        System.out.println("Puerto:" + port);
        assertEquals(8181, port, "Server is not running on port 8181");
    }

}