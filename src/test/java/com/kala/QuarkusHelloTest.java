package com.kala;

public class QuarkusHelloTest {
    @org.junit.jupiter.api.Test
    public void testHello() {
        String greeting = "Hello, Quarkus!";
        org.junit.jupiter.api.Assertions.assertEquals("Hello, Quarkus!", greeting);
    }
}