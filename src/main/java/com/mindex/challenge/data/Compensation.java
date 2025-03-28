package com.mindex.challenge.data;

import java.util.Date;

/**
 * Compensation package associated with an individual Employee.
 * 
 * Thoughts: Should it be a comprehensive description of all payment and benefits of an employee?
 *  Need at least `salary` and `effectiveDate`, I can also imagine compensation including a set of benefits.
 *  It seems like the `salary` field implies that this is for full-time salaried employees only. If it was 
 *  `rate` instead, I would probably want to include options for hourly, contract, etc. 
 * 
 *  I'm going to avoid adding Benefits as a domain entity for now, that seems like a separate
 *      discussion with separate requirements. I'll leave the design open to the concept for the future. 
 * 
 *  Relation to Employee: 1 to 1 relationship, but separate conceptually
 *      Compensation should contain a reference to an Employee, get Compensation via employee ID
 */
public class Compensation {
    private String compensationId;
    private String employeeId;
    private double salary;
    private Date effectiveDate;

    public Compensation() {}

    public Compensation(String compensationId, String employeeId, double salary, Date effectiveDate) {
        this.compensationId = compensationId;
        this.employeeId = employeeId;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public String getCompensationId() {
        return compensationId;
    }

    public void setCompensationId(String compensationId) {
        this.compensationId = compensationId;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public Date getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(Date effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
