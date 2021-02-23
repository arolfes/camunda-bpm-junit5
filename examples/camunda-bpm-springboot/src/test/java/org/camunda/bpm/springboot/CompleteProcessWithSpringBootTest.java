package org.camunda.bpm.springboot;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.springboot.delegate.FirstDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.AbstractAssertions.init;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.processInstanceQuery;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;


@SpringBootTest(
    properties = {
        "camunda.bpm.generate-unique-process-engine-name=true",
        // this is only needed if a SpringBootProcessApplication
        // is used for the test
        "camunda.bpm.generate-unique-process-application-name=true",
        "spring.datasource.generate-unique-name=true"
    }
)
public class CompleteProcessWithSpringBootTest {

    @Autowired
    private ApplicationContext appContext;

    @Autowired
    private ProcessEngine processEngine;

    @BeforeEach
    public void setUp() {
        init(processEngine);
    }

    @Test
    public void completeProcess() {
        FirstDelegate firstDelegate = appContext.getBean("firstDelegate", FirstDelegate.class);
        assertThat(firstDelegate).isNotNull();

        Map<String, Object> variables = new HashMap<>();
        variables.put("simpleInteger", 1);
        // Given we create a new process instance
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", "businessKey-1", variables);
        // Then it should be active
        assertThat(processInstance).isActive();
        // And it should be the only instance
        assertThat(processInstanceQuery().count()).isEqualTo(1);

        // And there should exist just a single task within that process instance
        assertThat(task(processInstance)).isNotNull();

        // When we complete that task
        complete(task(processInstance));
        // Then the process instance should be ended
        assertThat(processInstance).isEnded();
    }
}
