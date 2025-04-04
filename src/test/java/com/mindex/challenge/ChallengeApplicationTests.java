package com.mindex.challenge;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThrows;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;
import com.mindex.challenge.service.impl.CompensationServiceImpl;

/**
 * Will place all of my unit tests for the challenge application in this class.
 * In regular development circumstances, I would place each set of tests in a corresponding file 
 * -- ReportingStructureTest.java, etc.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ChallengeApplicationTests {

	private Employee[] testEmployees;
	private final EmployeeService mockEmployeeService = Mockito.mock(EmployeeService.class);

	private Compensation[] testCompensations;
	private final CompensationRepository mockCompensationRepository = Mockito.mock(CompensationRepository.class);
	private final CompensationService compensationService = new CompensationServiceImpl(mockCompensationRepository);

	@Before
	public void initialize() {
		testEmployees = new Employee[] {
			new Employee("A", "A", "Smith", "Entry", "Dev"),
			new Employee("B", "B", "Jane", "Intermediate", "Dev"),
			new Employee("C", "C", "Simpson", "Entry", "QA"),
			new Employee("D", "D", "Atreides", "Senior", "QA"),
			new Employee("E", "E", "Holmes", "Senior", "Management")
		};

		Mockito.when(mockEmployeeService.read("A")).thenReturn(testEmployees[0]);
		Mockito.when(mockEmployeeService.read("B")).thenReturn(testEmployees[1]);
		Mockito.when(mockEmployeeService.read("C")).thenReturn(testEmployees[2]);
		Mockito.when(mockEmployeeService.read("D")).thenReturn(testEmployees[3]);
		Mockito.when(mockEmployeeService.read("E")).thenReturn(testEmployees[4]);
		

		// A directly reports to B
		List<Employee> listB = new ArrayList<>();
		listB.add(new Employee("A"));
		testEmployees[1].setDirectReports(listB);

		// B and C directly report to D
		List<Employee> listD = new ArrayList<>();
		listD.add(new Employee("B"));
		listD.add(new Employee("C"));
		testEmployees[3].setDirectReports(listD);

		// B and D directly report to E
		List<Employee> listE = new ArrayList<>();
		listE.add(new Employee("B"));
		listE.add(new Employee("D"));
		testEmployees[4].setDirectReports(listE);
		// Visual of Employee Graph
        //            E
        //          /   \
        //  	   B --> D
        //        /       \
        //       A		   C


		/* Compensation Test Setup */

		Date date = new GregorianCalendar(2025, Calendar.MARCH, 28).getTime();
		Compensation compA = new Compensation("A", "A", 45000.00, date);
		Compensation compB = new Compensation("B", "B", 55000.00, date);
		Mockito.when(mockCompensationRepository.findByEmployeeId("A")).thenReturn(compA);
		Mockito.when(mockCompensationRepository.findByEmployeeId("B")).thenReturn(compB);
		Mockito.when(mockCompensationRepository.findByEmployeeId("C")).thenReturn(null);

		testCompensations = new Compensation[] {
			compA,
			compB
		};
	}

	@Test
	public void contextLoads() {}

	/* ReportingStructure Tests */

	@Test
	public void testReportingStructureTopLevel() {
		Employee topEmployee = testEmployees[4]; //E
		ReportingStructure reportingStructure = new ReportingStructure(mockEmployeeService, topEmployee);
		
		assertEquals(4, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureMidLevel() {
		Employee midEmployee = testEmployees[3]; //D
		ReportingStructure reportingStructure = new ReportingStructure(mockEmployeeService, midEmployee);
		
		assertEquals(3, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureLowLevel() {
		Employee lowEmployee = testEmployees[2]; //C
		ReportingStructure reportingStructure = new ReportingStructure(mockEmployeeService, lowEmployee);
		
		assertEquals(0, reportingStructure.getNumberOfReports());
	}

	@Test
	public void testReportingStructureCycle() {
		// Add E as a direct report of A, creates a cycle
		List<Employee> listA = new ArrayList<>();
		listA.add(testEmployees[4]);
		testEmployees[0].setDirectReports(listA);
		
		Employee loopEmployee = testEmployees[4];
		ReportingStructure reportingStructure = new ReportingStructure(mockEmployeeService, loopEmployee);

		assertEquals(4, reportingStructure.getNumberOfReports());
	}

	/* Compensation Tests */

	@Test
	public void testCompensationServiceRead() {
		Compensation compensation = compensationService.read("A");

		assertNotNull(compensation);
		assertEquals("A", compensation.getEmployeeId());
		assertCompensationEquivalence(testCompensations[0], compensation);
	}

	@Test
	public void testCompensationServiceReadNull() {
		Exception exception = assertThrows(RuntimeException.class, () ->  {
			compensationService.read("C");
		});

		assertEquals("Could not find compensation for employeeId: C", exception.getMessage());
	}

	@Test
	public void testCompensationServiceCreate() {
		Compensation compensation = testCompensations[1];
		Compensation createdCompensation = compensationService.create(compensation);

		assertNotNull(createdCompensation);
		assertEquals(compensation.getEmployeeId(), createdCompensation.getEmployeeId());
		assertEquals(compensation.getEffectiveDate(), createdCompensation.getEffectiveDate());
		assertEquals(compensation.getSalary(), createdCompensation.getSalary());
	}

	private void assertCompensationEquivalence(Compensation expected, Compensation actual) {
		assertEquals(expected.getCompensationId(), actual.getCompensationId());
		assertEquals(expected.getEmployeeId(), actual.getEmployeeId());
		assertEquals(expected.getEffectiveDate(), actual.getEffectiveDate());
		assertEquals(expected.getSalary(), actual.getSalary());
	} 

}
