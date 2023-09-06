package org.example;

import com.inflectra.spiratest.addons.junitextension.SpiraTestCase;
import com.inflectra.spiratest.addons.junitextension.SpiraTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
@SpiraTestConfiguration(
        //following are REQUIRED
        url = "https://rmit.spiraservice.net",
        login = "S3947682",
        rssToken = "{DA4EA898-6C4C-4253-9D7A-8733A126142B}",
        projectId = 78

)
class EventTest {
    Scanner scnr= Mockito.mock(Scanner.class);
    Event testevent;
    @BeforeEach
    void Setup(){
        testevent = new Event(scnr);
    }
    @AfterEach
    void restore(){
        //rewrites the user file for tests that alter it
        try{
            BufferedWriter out_Student = new BufferedWriter (new FileWriter("student.txt"));
            out_Student.write("""
                   Id:7654324, Name:Student1, Password:p7654324#
                   Id:7654325, Name:Student2, Password:p7654325#
                   Id:7654326, Name:Student3, Password:p7654326#
                   Id:7654327, Name:Student4, Password:p7654327#
                   Id:7654328, Name:Student5, Password:p7654328#
                   Id:7654329, Name:Student6, Password:p7654329#
                   Id:7654330, Name:Student7, Password:p7654330#
                   Id:7654331, Name:Student8, Password:p7654331#
                   Id:7654332, Name:Student9, Password:p7654332#""");

            out_Student.close();

        }
        catch(Exception e)
        { }
    }
    @Test
    @SpiraTestCase(testCaseId = 1373)
    public void ValidAdminLoginTest() {
        Mockito.when(scnr.next()).thenReturn("Admin1").thenReturn("pass1");
        assertTrue(testevent.AdminLogin());
    }
    @Test
    @SpiraTestCase(testCaseId = 375)
    public void InvalidAdminLoginTest() {
        Mockito.when(scnr.nextLine()).thenReturn("wrong").thenReturn("pass1");
        assertFalse(testevent.AdminLogin());
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
        assertSame(testevent.countStudent(),9);
    }
    @Test
    @SpiraTestCase(testCaseId = 1419)
    public void ValidRemoveStudentTest(){
        int studentcount = testevent.countStudent();
        assertAll(
                () -> assertTrue(testevent.removeStudent(7654324)),
                () -> assertFalse(testevent.searchStudentDetails(7654324)),
                () -> assertEquals(testevent.countStudent(),studentcount - 1)
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
        Mockito.when(scnr.nextInt()).thenReturn(20202020);
        Mockito.when(scnr.nextLine()).thenReturn("William");
        Mockito.when(scnr.nextLine()).thenReturn("p2222229#");
        assertEquals("Student Added Successfully",testevent.AddStudent());
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