package com.mindex.challenge;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;

/**
 * Will place all of my unit tests for the challenge application in this class.
 * In regular development circumstances, I would place each set of tests in a corresponding file 
 * -- ReportingStructureTest.java, etc.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

	private Employee[] testEmployees;

	@Before
	public void initialize() {
		Employee[] testEmployees = {
			new Employee("1", "A", "Smith", "Entry", "Dev"),
			new Employee("2", "B", "Jane", "Intermediate", "Dev"),
			new Employee("3", "C", "Simpson", "Entry", "QA"),
			new Employee("4", "D", "Atreides", "Senior", "QA"),
			new Employee("5", "E", "Holmes", "Senior", "Management")
		};
	}

	@Test
	public void contextLoads() {}

	@Test
	public void testReportingStructureCreation() {
		
	}
}
