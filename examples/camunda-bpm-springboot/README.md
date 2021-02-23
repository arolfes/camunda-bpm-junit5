# Camunda BPM Assert Example for JUnit 5

This project contains an example of how to test a Spring Boot Camunda BPM Application with JUnit 5.

The project contains the following files:

```
src
├── main
│   ├── java
│   │   └── org
│   │       └── camunda
│   │           └── bpm
│   │               └── springboot
│   │                   ├── WebappExampleProcessApplication.java
│   │                   ├── api
│   │                   │   ├── Controller.java
│   │                   │   └── Data.java
│   │                   ├── business
│   │                   │   └── BusinessService.java
│   │                   └── delegate
│   │                       ├── FirstDelegate.java
│   │                       └── SecondDelegate.java
│   └── resources
│       ├── application.yml
│       └── testProcess.bpmn (5)
└── test
    ├── java
    │   └── org
    │       └── camunda
    │           └── bpm
    │               └── springboot
    │                   ├── CompleteProcessWithExtensionTest.java (1)
    │                   ├── CompleteProcessWithSpringBootAndExtensionTest.java (2)
    │                   ├── CompleteProcessWithSpringBootTest.java (3)
    │                   └── WebappExampleProcessApplicationTest.java
    └── resources
        ├── camunda.cfg.xml (4)
        └── postman
            └── example-camunda-bpm-springboot-junit5.postman_collection.json
```

Explanation:

* (1) A java class containing a JUnit Test. It uses the `ProcessEngineExtension` for bootstrapping the process engine,
  as well as [camunda-bpm-assert][assert] to make your test life easier.
* (2) A java class with SpringBootTest and `ProcessEngineExtension`. Currently __disabled__ because
  the `ProcessEngineExtension` doesn't know something about the Beans from SpringBoot
* (3) A SpringBoot Test with complete process. Every dependency comes from Spring
* (4) Configuration file for the process engine from `ProcessEngineExtension`.
* (5) A process file with 2 Delegates (using Expression) and an UserTask.

## Running the spring boot application with maven

```
../../mvnw spring-boot:run

# you can start processes with Postman and the postman-collection from src/test/resources/postman
```

## Running the test with maven

In order to run the testsuite with maven you can say:

```
mvn clean test
```

## License

The source files in this repository are made available under the [Apache License Version 2.0](./LICENSE).