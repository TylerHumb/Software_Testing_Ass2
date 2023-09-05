package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EventTest {
    Event testevent;
    Scanner scnr= Mockito.mock(Scanner.class);
    @BeforeEach
    void Setup(){
        testevent = new Event(scnr);
    }
    @Test
    void ValidAdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("Admin1").thenReturn("pass1");
        assertTrue(testevent.AdminLogin());
    }
    @Test
    void InvalidAdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("wrong").thenReturn("pass1");
        assertFalse(testevent.AdminLogin());
    }
    @Test
    void ValidStudentLoginTest() {
        // they used a very annoying way of collecting user input here
        Mockito.when(scnr.nextInt()).thenReturn(7654324);
        Mockito.when(scnr.next()).thenReturn("p7654324#");
        assertTrue(testevent.StudentLogin());
    }
    @Test
    void InvalidStudentLoginTest() {
        Mockito.when(scnr.nextInt()).thenReturn(10);
        Mockito.when(scnr.next()).thenReturn("testers");
        assertFalse(testevent.StudentLogin());
    }
    @Test
    void ViewDetailsTest(){
        assertTrue(testevent.viewStudentDetails());
    }
    @Test
    void ValidSearchDetailsTest(){
        assertTrue(testevent.searchStudentDetails(7654324));
    }
    @Test
    void InvalidSearchDetailsTest(){
        assertFalse(testevent.searchStudentDetails(102030));
    }
    @Test
    void StudentCountTest(){
        assertEquals(testevent.countStudent(),9);
    }
    @Test
    void ValidRemoveStudentTest(){
        int studentcount = testevent.countStudent();
        //need to spy the method in order to skip the writing to student file,
        // as to not affect future tests and alter the record during testing
        Event fakeevent = spy(new Event(scnr));
        doNothing().when(fakeevent).rewriteStudentFile();
        assertAll(
                () -> assertTrue(fakeevent.removeStudent(7654324)),
                () -> assertFalse(fakeevent.searchStudentDetails(7654324)),
                () -> assertEquals(fakeevent.countStudent(),studentcount - 1)
        );
    }
    @Test
    void InvalidRemoveStudentTest(){
        assertFalse(testevent.removeStudent(299332));
    }
    @Test
    void  ValidAddStudentTest(){
        //need to spy the method in order to skip the writing to student file,
        // as to not affect future tests and alter the record during testing
        Event fakeevent = spy(new Event(scnr));
        doNothing().when(fakeevent).rewriteStudentFile();
        Mockito.when(scnr.nextInt()).thenReturn(20202020);
        Mockito.when(scnr.nextLine()).thenReturn("William");
        Mockito.when(scnr.nextLine()).thenReturn("p2222229#");
        assertEquals("Student Added Successfully",fakeevent.AddStudent());
    }
    @Test
    void
}