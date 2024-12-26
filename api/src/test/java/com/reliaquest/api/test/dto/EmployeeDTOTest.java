package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reliaquest.api.dto.EmployeeDTO;
import org.junit.jupiter.api.Test;

public class EmployeeDTOTest {

    @Test
    public void testEmployeeDTOBuilder() {
        // Arrange
        String name = "John Doe";
        Integer salary = 50000;
        Integer age = 30;
        String title = "Software Engineer";

        // Act
        EmployeeDTO employeeDTO = EmployeeDTO.builder()
                .name(name)
                .salary(salary)
                .age(age)
                .title(title)
                .build();

        // Assert
        assertEquals(name, employeeDTO.getName());
        assertEquals(salary, employeeDTO.getSalary());
        assertEquals(age, employeeDTO.getAge());
        assertEquals(title, employeeDTO.getTitle());
    }

    @Test
    public void testEmployeeDTOAllArgsConstructor() {
        // Arrange
        String name = "Jane Doe";
        Integer salary = 60000;
        Integer age = 28;
        String title = "Product Manager";

        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO(name, salary, age, title);

        // Assert
        assertEquals(name, employeeDTO.getName());
        assertEquals(salary, employeeDTO.getSalary());
        assertEquals(age, employeeDTO.getAge());
        assertEquals(title, employeeDTO.getTitle());
    }
}
