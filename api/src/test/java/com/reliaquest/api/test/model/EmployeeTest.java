package com.reliaquest.api.test.model;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.model.Employee;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EmployeeTest {
    @Test
    void testEmployeeConstructorAndGetters() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "John Doe";
        Integer salary = 50000;
        Integer age = 30;
        String title = "Software Engineer";
        String email = "john.doe@example.com";

        // Act
        Employee employee = new Employee(id, name, salary, age, title, email);

        // Assert
        assertEquals(id, employee.getId());
        assertEquals(name, employee.getName());
        assertEquals(salary, employee.getSalary());
        assertEquals(age, employee.getAge());
        assertEquals(title, employee.getTitle());
        assertEquals(email, employee.getEmail());
    }

    @Test
    void testEmployeeSetters() {
        // Arrange
        Employee employee =
                new Employee(UUID.randomUUID(), "Jane Doe", 60000, 28, "Product Manager", "jane.doe@example.com");

        // Act
        employee.setName("Jane Smith");
        employee.setSalary(65000);
        employee.setAge(29);
        employee.setTitle("Senior Product Manager");
        employee.setEmail("jane.smith@example.com");

        // Assert
        assertEquals("Jane Smith", employee.getName());
        assertEquals(65000, employee.getSalary());
        assertEquals(29, employee.getAge());
        assertEquals("Senior Product Manager", employee.getTitle());
        assertEquals("jane.smith@example.com", employee.getEmail());
    }

    @Test
    void testEmployeeBuilder() {
        // Arrange
        UUID id = UUID.randomUUID();
        String name = "Alice Johnson";
        Integer salary = 70000;
        Integer age = 35;
        String title = "Team Lead";
        String email = "alice.johnson@example.com";

        // Act
        Employee employee = Employee.builder()
                .id(id)
                .name(name)
                .salary(salary)
                .age(age)
                .title(title)
                .email(email)
                .build();

        // Assert
        assertEquals(id, employee.getId());
        assertEquals(name, employee.getName());
        assertEquals(salary, employee.getSalary());
        assertEquals(age, employee.getAge());
        assertEquals(title, employee.getTitle());
        assertEquals(email, employee.getEmail());
    }
}
