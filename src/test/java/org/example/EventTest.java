package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventTest {
    Event testevent;
    Scanner scnr= Mockito.mock(Scanner.class);
    @BeforeEach
    void Setup(){
        testevent = new Event(scnr);
    }


    @Test
    void AdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("Admin1").thenReturn("pass1");
        assertTrue(testevent.AdminLogin());
    }
}