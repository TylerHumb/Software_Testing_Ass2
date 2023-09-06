package org.example;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
@SpiraTestConfiguration(
        //following are REQUIRED
        url = "https://rmit.spiraservice.net",
        login = "S3947682",
        rssToken = "{DA4EA898-6C4C-4253-9D7A-8733A126142B}",
        projectId = 78

)
class EventTest {
    Event testevent;
    Scanner scnr= Mockito.mock(Scanner.class);
    @BeforeEach
    void Setup(){
        testevent = new Event(scnr);
    }
    @Test
    @SpiraTestCase(testCaseId = 1373)
    public void ValidAdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("Admin1").thenReturn("pass1");
        assertTrue(testevent.AdminLogin(scnr));
    }
    @Test
    @SpiraTestCase(testCaseId = 375)
    public void InvalidAdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("wrong").thenReturn("pass1");
        assertFalse(testevent.AdminLogin(scnr));
    }
    @Test
    @SpiraTestCase(testCaseId = 1413)
    public void ValidStudentLoginTest() {
        // they used a very annoying way of collecting user input here
        Mockito.when(scnr.nextInt()).thenReturn(7654324);
        Mockito.when(scnr.next()).thenReturn("p7654324#");
        assertTrue(testevent.StudentLogin());
    }
    @Test
    @SpiraTestCase(testCaseId = 1414)
    public void InvalidStudentLoginTest() {
        Mockito.when(scnr.nextInt()).thenReturn(10);
        Mockito.when(scnr.next()).thenReturn("testers");
        assertFalse(testevent.StudentLogin());
    }
    @Test
    @SpiraTestCase(testCaseId = 1415)
    public void ViewDetailsTest(){
        assertTrue(testevent.viewStudentDetails());
    }
    @Test
    @SpiraTestCase(testCaseId = 1416)
    public void ValidSearchDetailsTest(){
        assertTrue(testevent.searchStudentDetails(7654324));
    }
    @Test
    @SpiraTestCase(testCaseId = 1417)
    public void InvalidSearchDetailsTest(){
        assertFalse(testevent.searchStudentDetails(102030));
    }
    @Test
    @SpiraTestCase(testCaseId = 1418)
    public void StudentCountTest(){
        assertEquals(testevent.countStudent(),9);
    }
    @Test
    @SpiraTestCase(testCaseId = 1419)
    public void ValidRemoveStudentTest(){
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
    @SpiraTestCase(testCaseId = 1420)
    public void InvalidRemoveStudentTest(){
        assertFalse(testevent.removeStudent(299332));
    }
    @Test
    @SpiraTestCase(testCaseId = 1421)
    public void  ValidAddStudentTest(){
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
    @SpiraTestCase(testCaseId = 1423)
    public void InvalidAddStudentDupeID(){
        Mockito.when(scnr.nextInt()).thenReturn(7654324);
        assertEquals(testevent.AddStudent(),"Student Exists");

    }
    @Test
    @SpiraTestCase(testCaseId = 1424)
    public void InvalidAddStudentPasswords() {
        Event fakeevent = spy(new Event(scnr));
        doNothing().when(fakeevent).rewriteStudentFile();
        assertAll(
                () -> {
                    Mockito.when(scnr.nextInt()).thenReturn(20202020);
                    Mockito.when(scnr.nextLine()).thenReturn("William");
                    Mockito.when(scnr.nextLine()).thenReturn("p22229#");
                    assertEquals("Password length should be 9", fakeevent.AddStudent());
                },
                () -> {
                    Mockito.when(scnr.nextInt()).thenReturn(20202020);
                    Mockito.when(scnr.nextLine()).thenReturn("William");
                    Mockito.when(scnr.nextLine()).thenReturn("22292229#");
                    assertEquals("First letter of the Password should be p", fakeevent.AddStudent());
                },
                () -> {
                    Mockito.when(scnr.nextInt()).thenReturn(20202020);
                    Mockito.when(scnr.nextLine()).thenReturn("William");
                    Mockito.when(scnr.nextLine()).thenReturn("p22222299");
                    assertEquals("Last letter of the password should be #", fakeevent.AddStudent());
                }
        );
    }


    }