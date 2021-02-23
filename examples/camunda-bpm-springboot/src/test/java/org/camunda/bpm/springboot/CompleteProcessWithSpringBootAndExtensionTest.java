package org.camunda.bpm.springboot;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.springboot.business.BusinessService;
import org.camunda.bpm.springboot.delegate.FirstDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.processInstanceQuery;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;

@org.springframework.boot.test.context.TestConfiguration
@ComponentScan(basePackageClasses = {FirstDelegate.class, BusinessService.class})
class TestConfiguration {
}

@SpringBootTest(
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        // this is only needed if a SpringBootProcessApplication
        // is used for the test
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true"
    }
)
@ContextConfiguration(classes = {TestConfiguration.class})
@ExtendWith(ProcessEngineExtension.class)
@Disabled("because of org.camunda.bpm.engine.ProcessEngineException: Unknown property used in expression: ${firstDelegate}.")
public class CompleteProcessWithSpringBootAndExtensionTest {

    @MockBean
    private BusinessService businessService;

    @Autowired
    private ApplicationContext appContext;

    /* if these lines are not enabled,
    this exception occurs
    java.lang.IllegalStateException: 2 ProcessEngines initialized. Call BpmnAwareTests.init(ProcessEngine processEngine) first!
	at org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.processEngine(AbstractAssertions.java:56)
    */

    @Autowired
    private ProcessEngine processEngine;

    @BeforeEach
    public void setUp() {
        init(processEngine);
    }

    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void completeProcess() {
        Mockito.when(businessService.isPrime(4)).thenReturn(false);

        FirstDelegate firstDelegate = appContext.getBean("firstDelegate", FirstDelegate.class);
        assertThat(firstDelegate).isNotNull();

        // the processEngine doesn't know something about firstDelegate which is referenced via expression
        // but it is there as you can see the line before
        // org.camunda.bpm.engine.ProcessEngineException: Unknown property used in expression: ${firstDelegate}. Cause: Cannot resolve identifier 'firstDelegate'
        Map<String, Object> variables = new HashMap<>();
        variables.put("simpleInteger", 4);
        // Given we create a new process instance
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", "businessKey-1", variables);
        // Then it should be active
        BpmnAwareTests.assertThat(processInstance).isActive();
        // And it should be the only instance
        assertThat(processInstanceQuery().count()).isEqualTo(1);

        // And there should exist just a single task within that process instance
        BpmnAwareTests.assertThat(task(processInstance)).isNotNull();

        // When we complete that task
        complete(task(processInstance));
        // Then the process instance should be ended
        BpmnAwareTests.assertThat(processInstance).isEnded();
    }

}
