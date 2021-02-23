package org.camunda.bpm.springboot.api;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class Controller {

    @Autowired
    private RuntimeService runtimeService;

    @PostMapping(path = "/startProcess", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void startProcess(@RequestBody Data processStartData) {
        System.out.println(processStartData);

        Map<String, Object> variables = new HashMap<>();
        variables.put("simpleInteger", processStartData.getSimpleInteger());
        runtimeService.startProcessInstanceByKey("testProcess", processStartData.getBusinessKey(), variables);
    }
}
