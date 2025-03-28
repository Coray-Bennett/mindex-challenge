package com.mindex.challenge.service.impl;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mindex.challenge.dao.CompensationRepository;
import com.mindex.challenge.data.Compensation;
import com.mindex.challenge.service.CompensationService;

@Service
public class CompensationServiceImpl implements CompensationService {

    private static final Logger LOG = LoggerFactory.getLogger(EmployeeServiceImpl.class);

    @Autowired
    private CompensationRepository compensationRepository;

    //Constructor to inject CompensationRepository for mocking
    public CompensationServiceImpl(CompensationRepository compensationRepository) {
        this.compensationRepository = compensationRepository;
    }

    @Override
    public Compensation create(Compensation compensation) {
        LOG.debug("Creating compenssation [{}]", compensation);
        compensation.setCompensationId(UUID.randomUUID().toString());
        compensationRepository.insert(compensation);

        return compensation;
    }

    @Override
    public Compensation read(String employeeId) {
        LOG.debug("Reading compensation for employeeId [{}]", employeeId);
        Compensation compensation = compensationRepository.findByEmployeeId(employeeId);

        if(compensation == null) {
            throw new RuntimeException("Could not find compensation for employeeId: " + employeeId);
        }

        return compensation;
    }
    
}
