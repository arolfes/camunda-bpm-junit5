package org.camunda.bpm.springboot;

import org.assertj.core.api.Assertions;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.camunda.bpm.extension.mockito.DelegateExpressions;
import org.camunda.bpm.extension.mockito.mock.FluentJavaDelegateMock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.assertThat;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.complete;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.processInstanceQuery;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.runtimeService;
import static org.camunda.bpm.engine.test.assertions.bpmn.BpmnAwareTests.task;

@ExtendWith(ProcessEngineExtension.class)
public class CompleteProcessWithExtensionTest {

    @Test
    @Deployment(resources = {"testProcess.bpmn"})
    public void shouldExecuteCompleteProcess() {
        FluentJavaDelegateMock firstDelegate = DelegateExpressions.registerJavaDelegateMock("firstDelegate");
        firstDelegate.onExecutionSetVariable("simpleInteger", 1);
        firstDelegate.onExecutionSetVariable("isPrime", true);
        firstDelegate.onExecutionSetVariable("currentDateTime", LocalDateTime.now().toString());

        // this doesn't work
        //DelegateExpressions.autoMock("testProcess.bpmn");
        FluentJavaDelegateMock secondDelegate = DelegateExpressions.registerJavaDelegateMock("secondDelegate");

        Map<String, Object> variables = new HashMap<>();
        variables.put("simpleInteger", 1);
        // Given we create a new process instance
        ProcessInstance processInstance = runtimeService().startProcessInstanceByKey("testProcess", "businessKey-1", variables);

        // Then it should be active
        assertThat(processInstance).isActive();
        // And it should be the only instance
        Assertions.assertThat(processInstanceQuery().count()).isEqualTo(1);
        // And there should exist just a single task within that process instance
        assertThat(task(processInstance)).isNotNull();

        // When we complete that task
        complete(task(processInstance));
        assertThat(processInstance).hasPassed("StartEvent_1", "FirstDelegate", "UserTask_1", "SecondDelegate", "EndEvent_1");
        // Then the process instance should be ended
        assertThat(processInstance).isEnded();

        // this fails
        // DelegateExpressions.verifyJavaDelegateMock(secondDelegate).executed(new Times(1));
        // this fails
        // DelegateExpressions.verifyJavaDelegateMock("secondDelegate").executed(new Times(1));
    }
}
