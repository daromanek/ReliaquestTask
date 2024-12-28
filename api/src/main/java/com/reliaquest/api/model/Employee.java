package com.reliaquest.api.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder(toBuilder = true)
@JsonNaming(Employee.PrefixNamingStrategy.class) // Using the same naming strategy
public class Employee {

    private UUID id;
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
    private String email;

    // Constructor with validation
    public Employee(UUID id, String name, Integer salary, Integer age, String title, String email) {
        if (salary != null && salary < 0) {
            throw new IllegalArgumentException("Salary cannot be negative");
        }
        this.id = id;
        this.name = name;
        this.salary = salary;
        this.age = age;
        this.title = title;
        this.email = email;
    }

    // Added the same prefix naming strategy as the MockEmployee has so that we can convert to an Employee in the
    // response
    static class PrefixNamingStrategy extends PropertyNamingStrategies.NamingBase {

        @Override
        public String translate(String propertyName) {
            if ("id".equals(propertyName)) {
                return propertyName;
            }
            return "employee_" + propertyName;
        }
    }
}
