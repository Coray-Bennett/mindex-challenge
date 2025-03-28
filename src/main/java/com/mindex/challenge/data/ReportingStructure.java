package com.mindex.challenge.data;

import java.util.HashSet;
import java.util.Set;

import com.mindex.challenge.service.EmployeeService;

/**
 * Determines the number of direct and indirect reports of a given employee upon instantiation. 
 * 
 * Thinking: What is a valid structure of direct reports? If B directly reports to A, 
 *  and C directly reports to B, is it possible for C to also directly report to A?
 *  Personally, I think yes, it's technically possible. However, it probably ISN'T possible
 *  for A to ALSO report to B or C. In other words, no cycles. Will have to account for that.
 * 
 *  vvv Since each employee must be counted once, cycles would simply be ignored if reached. This is acceptable logic,
 *  since it will never prevent legitimate reports from being counted.
 * 
 * Each employee must only be counted for a report once, even if they connect to multiple other reports in the tree.
 * 
 * Big realization hit -- traversal is based solely on employee ID looking at employee database, need to fetch each
 *  direct report from the repository
 */
public class ReportingStructure {
    private final Employee employee;
    private final int numberOfReports;

    private final EmployeeService employeeService;

    // Dependency Injection for `employeeService`
    public ReportingStructure(EmployeeService employeeService, Employee employee) {
        this.employee = employee;
        this.employeeService = employeeService;
        this.numberOfReports = this.calculateNumberOfReports();
    }

    public Employee getEmployee() {
        return this.employee;
    }

    public int getNumberOfReports() {
        return this.numberOfReports;
    }

    /**
     * Overridden method with no parameters, 
     * calls `calculateNumberOfReports` with an empty `visited` set and the employee in the reporting structure.
     * @return The total number of reports (direct and indirect) of the employee in the reporting structure.
     */
    private int calculateNumberOfReports() {
        return this.calculateNumberOfReports(this.employee, new HashSet<>());
    }

    /**
     * @param employee The employee whose number of reports will be calculated.
     * @param visited Set containing employees that have already been counted as a report.
     * @return The number of reports of `employee`, ignoring `visited` employees in the tree.
     */
    private int calculateNumberOfReports(Employee employee, Set<String> visited) {
        int reportsCount = 0;
        visited.add(employee.getEmployeeId());

        if(employee.getDirectReports() == null) {
            return 0;
        }

        for(Employee report : employee.getDirectReports()) {
            String id = report.getEmployeeId();
            if(!visited.contains(id)) {
                visited.add(id);
                reportsCount++; // add the direct report to the count

                //fetch full employee data from employee service
                Employee fullReportee;
                try {
                    fullReportee = this.employeeService.read(id);
                } catch (RuntimeException e) {
                    continue;
                }
                
                reportsCount += this.calculateNumberOfReports(fullReportee, visited); // recursively add indirect reports
            }
        }

        return reportsCount;
    }
}
