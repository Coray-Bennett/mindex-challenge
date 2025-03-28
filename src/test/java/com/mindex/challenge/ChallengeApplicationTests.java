package com.mindex.challenge;

import java.util.ArrayList;
import java.util.List;

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
			new Employee("0", "A", "Smith", "Entry", "Dev"),
			new Employee("1", "B", "Jane", "Intermediate", "Dev"),
			new Employee("2", "C", "Simpson", "Entry", "QA"),
			new Employee("3", "D", "Atreides", "Senior", "QA"),
			new Employee("4", "E", "Holmes", "Senior", "Management")
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
		listE.add(testEmployees[3]);
		testEmployees[4].setDirectReports(listE);
		
		// Visual of Employee Graph
        //            E
        //          /   \
        //  	   B --> D
        //        /       \
        //       A		   C
	}

	@Test
	public void contextLoads() {}

	/* ReportingStructure Tests */

	@Test
	public void testReportingStructureTopLevel() {
		Employee topEmployee = testEmployees[4]; //E
		ReportingStructure reportingStructure = new ReportingStructure(topEmployee);
		
		assertEquals(4, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureMidLevel() {
		Employee midEmployee = testEmployees[3]; //D
		ReportingStructure reportingStructure = new ReportingStructure(midEmployee);
		
		assertEquals(3, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureLowLevel() {
		Employee lowEmployee = testEmployees[2]; //C
		ReportingStructure reportingStructure = new ReportingStructure(lowEmployee);
		
		assertEquals(0, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureCycle() {
		// Add E as a direct report of A, creates a cycle
		List<Employee> listA = new ArrayList<>();
		listA.add(testEmployees[4]);
		testEmployees[0].setDirectReports(listA);
		
		Employee loopEmployee = testEmployees[4];
		ReportingStructure reportingStructure = new ReportingStructure(loopEmployee);

		assertEquals(4, reportingStructure.getNumberOfReports());
	}
}
