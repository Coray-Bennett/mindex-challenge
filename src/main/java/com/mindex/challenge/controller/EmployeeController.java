package com.mindex.challenge.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Employee;
import com.mindex.challenge.data.ReportingStructure;
import com.mindex.challenge.service.EmployeeService;

@RestController
public class EmployeeController {
    private static final Logger LOG = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/employee")
    public Employee create(@RequestBody Employee employee) {
        LOG.debug("Received employee create request for [{}]", employee);

        return employeeService.create(employee);
    }

    @GetMapping("/employee/{id}")
    public Employee read(@PathVariable String id) {
        LOG.debug("Received employee read request for id [{}]", id); // this originally said 'create'

        return employeeService.read(id);
    }

    @PutMapping("/employee/{id}")
    public Employee update(@PathVariable String id, @RequestBody Employee employee) {
        LOG.debug("Received employee update request for id [{}] and employee [{}]", id, employee); // this originally said 'create'

        employee.setEmployeeId(id);
        return employeeService.update(employee);
    }

    /**
     * @param String id : employeeId of the employee whose reportingStructure will be returned
     * @returns ResponseEntity<?> containing the `ReportingStructure` if successful, or an HTTP error code. 
     * 
     * @note Hypothetically, in a production system this endpoint could be cached,
     *   assuming your churn rate of employees is low enough that the values are usually the same.
     */
    @GetMapping("/employee/reports/{id}")
    public ResponseEntity<?> reportingStructure(@PathVariable String id) {
        LOG.debug("Received request for employee reporting structure for id[{}]", id);
        try {
            Employee employee = employeeService.read(id);
            ReportingStructure reportingStructure = new ReportingStructure(employeeService, employee);
            return new ResponseEntity<>(reportingStructure, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error:" + e.getMessage());
        }
    }
}
