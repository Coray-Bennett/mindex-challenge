package com.mindex.challenge.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        } catch (Exception e) {
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
    
}
