package com.mindex.challenge.data;

/**
 * Determines the number of direct and indirect reports of a given employee upon instantiation. 
 * 
 * Thinking: What is a valid structure of direct reports? If B directly reports to A, 
 *  and C directly reports to B, is it possible for C to also directly report to A?
 */
public class ReportingStructure {
    private Employee employee;
    private int numberOfReports;

    public ReportingStructure(Employee employee) {

    }

    public int getNumberOfReports() {

        return 0;
    }
}
