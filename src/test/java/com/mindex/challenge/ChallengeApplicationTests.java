package com.mindex.challenge;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;

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
		testEmployees = new Employee[] {
			new Employee("1", "A", "Smith", "Entry", "Dev"),
			new Employee("2", "B", "Jane", "Intermediate", "Dev"),
			new Employee("3", "C", "Simpson", "Entry", "QA"),
			new Employee("4", "D", "Atreides", "Senior", "QA"),
			new Employee("5", "E", "Holmes", "Senior", "Management")
		};

		// A directly reports to B
		List<Employee> listB = new ArrayList<>();
		listB.add(testEmployees[0]);
		testEmployees[1].setDirectReports(listB);

		// B and C directly report to D
		List<Employee> listD = new ArrayList<>();
		listD.add(testEmployees[1]);
		listD.add(testEmployees[2]);
		testEmployees[3].setDirectReports(listD);

		// B and D directly report to E
		List<Employee> listE = new ArrayList<>();
		listE.add(testEmployees[1]);
		listD.add(testEmployees[3]);
		testEmployees[4].setDirectReports(listE);
	}

	@Test
	public void contextLoads() {}

	@Test
	public void testReportingStructureTopLevel() {
		Employee topEmployee = testEmployees[4];
		ReportingStructure reportingStructure = new ReportingStructure(topEmployee);
		
		assertNotNull(reportingStructure);
		assertEquals(5, reportingStructure.getNumberOfReports());
	}
}
