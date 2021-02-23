package org.camunda.bpm.springboot.delegate;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SecondDelegate implements JavaDelegate {

    private final Logger logger = LoggerFactory.getLogger(SecondDelegate.class);

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        logger.info("businessKey={}", delegateExecution.getBusinessKey());
        logger.info("simpleInteger={}", delegateExecution.getVariable("simpleInteger"));
        logger.info("currentDateTime={}", delegateExecution.getVariable("currentDateTime"));
        logger.info("isPrime={}", delegateExecution.getVariable("isPrime"));
    }
}
