package org.camunda.bpm.springboot.api;

import java.util.Objects;

public class Data {

    private String businessKey = null;

    private int simpleInteger = 0;

    public Data() {
    }

    public Data(String businessKey, int simpleInteger) {
        this.businessKey = businessKey;
        this.simpleInteger = simpleInteger;
    }


    public String getBusinessKey() {
        return businessKey;
    }

    public void setBusinessKey(String businessKey) {
        this.businessKey = businessKey;
    }

    public int getSimpleInteger() {
        return simpleInteger;
    }

    public void setSimpleInteger(int simpleInteger) {
        this.simpleInteger = simpleInteger;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Data data = (Data) o;
        return simpleInteger == data.simpleInteger && Objects.equals(businessKey, data.businessKey);
    }

    @Override
    public int hashCode() {
        return Objects.hash(businessKey, simpleInteger);
    }

    @Override
    public String toString() {
        return "Data{" +
            "businessKey='" + businessKey + '\'' +
            ", simpleInteger=" + simpleInteger +
            '}';
    }
}
