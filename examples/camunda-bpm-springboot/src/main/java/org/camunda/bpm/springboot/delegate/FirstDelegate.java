package org.camunda.bpm.springboot.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.springboot.business.BusinessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class FirstDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(FirstDelegate.class);

    @Autowired
    private BusinessService businessService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("businessKey={}", delegateExecution.getBusinessKey());
        int simpleInteger = (int) delegateExecution.getVariable("simpleInteger");
        logger.info("simpleInteger={}", simpleInteger);
        delegateExecution.setVariable("currentDateTime", LocalDateTime.now().toString());
        boolean isPrime = businessService.isPrime(simpleInteger);
        delegateExecution.setVariable("isPrime", isPrime);
    }
}
