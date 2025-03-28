package com.mindex.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;
import com.mindex.challenge.service.EmployeeService;

@RestController
public class CompensationController {

    @Autowired
    private CompensationService compensationService;

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/compensation/{employeeId}")
    public ResponseEntity<?> read(@PathVariable String employeeId) {
        try {
            employeeService.read(employeeId);
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No employee with id: " + employeeId + " found", HttpStatus.NOT_FOUND);
        }

        try {
            Compensation compensation = compensationService.read(employeeId);
            return new ResponseEntity<>(compensation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error:" + e.getMessage());
        }
    }

    @PostMapping("/compensation")
    public ResponseEntity<?> create(@RequestBody Compensation compensation) {
        switch(validateCompensation(compensation)) {
            case -1 -> { return ResponseEntity.badRequest().body("Compensation is missing required values."); }
            case -2 -> { return ResponseEntity.badRequest().body("Salary must not be a negative number."); }
        }

        try {
            employeeService.read(compensation.getEmployeeId()); // validate employee ID
        } catch (RuntimeException e) {
            return new ResponseEntity<>("No employee with id: " + compensation + " found", HttpStatus.NOT_FOUND);
        }

        try {
            compensationService.read(compensation.getEmployeeId()); // enforce uniqueness
            return new ResponseEntity<>(
                "Compensation for employee with ID " + compensation.getEmployeeId() + " already exists",
                HttpStatus.CONFLICT
            );
        } catch (RuntimeException e) {
            // ONLY continue if compensation does not exist for given employeeId
        }

        try {
            Compensation createdCompensation = compensationService.create(compensation);
            return new ResponseEntity<>(createdCompensation, HttpStatus.OK);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Internal server error:" + e.getMessage());
        }
    }

    // Compensation is only valid if this returns an int > 0
    private int validateCompensation(Compensation compensation) {
        Object[] requiredNotNull = {
            compensation.getEmployeeId(), 
            compensation.getSalary(),
            compensation.getEffectiveDate()
        };

        for(Object req : requiredNotNull) {
            if(req == null) { return -1; }
        }

        return (compensation.getSalary() < 0 ? -2 : 1); // returns -2 if salary is negative, distinct case vs null values
    }
}
