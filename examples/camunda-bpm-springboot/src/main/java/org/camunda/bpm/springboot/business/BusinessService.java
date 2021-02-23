package org.camunda.bpm.springboot.business;

import org.springframework.stereotype.Service;

@Service
public class BusinessService {

    public boolean isPrime(int simpleInteger) {
        boolean flag = false;
        for (int i = 2; i <= simpleInteger / 2; ++i) {
            // condition for nonprime number
            if (simpleInteger % i == 0) {
                flag = true;
                break;
            }
        }
        return flag;
    }
}
