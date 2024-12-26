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

    @Test
    void testDefaultConstructor() {
        // Act
        Employee employee = new Employee();

        // Assert
        assertNull(employee.getId());
        assertNull(employee.getName());
        assertNull(employee.getSalary());
        assertNull(employee.getAge());
        assertNull(employee.getTitle());
        assertNull(employee.getEmail());
    }

    @Test
    void testBuilderWithNoParameters() {
        // Act
        Employee employee = Employee.builder().build();

        // Assert
        assertNull(employee.getId());
        assertNull(employee.getName());
        assertNull(employee.getSalary());
        assertNull(employee.getAge());
        assertNull(employee.getTitle());
        assertNull(employee.getEmail());
    }

    @Test
    void testEqualsAndHashCode() {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee employee1 = new Employee(id, "John Doe", 50000, 30, "Software Engineer", "john.doe@example.com");
        Employee employee2 = new Employee(id, "John Doe", 50000, 30, "Software Engineer", "john.doe@example.com");

        // Assert
        assertEquals(employee1, employee2);
        assertEquals(employee1.hashCode(), employee2.hashCode());
    }

    @Test
    void testToString() {
        // Arrange
        UUID id = UUID.randomUUID();
        Employee employee = new Employee(id, "John Doe", 50000, 30, "Software Engineer", "john.doe@example.com");

        // Act
        String expectedString = "Employee(id=" + id
                + ", name=John Doe, salary=50000, age=30, title=Software Engineer, email=john.doe@example.com)";

        // Assert
        assertEquals(expectedString, employee.toString());
    }

    @Test
    void testInvalidSalary() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            new Employee(UUID.randomUUID(), "John Doe", -50000, 30, "Software Engineer", "john.doe@example.com");
        });
    }

    @Test
    void testBuilderImmutability() {
        // Arrange
        Employee original =
                new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Software Engineer", "john.doe@example.com");

        // Act
        Employee modified = original.toBuilder().name("Jane Doe").build();

        // Assert
        assertNotEquals(original.getName(), modified.getName());
        assertEquals("John Doe", original.getName());
    }

    @Test
    void testNullValues() {
        // Act
        Employee employee = new Employee(UUID.randomUUID(), null, null, null, null, null);

        // Assert
        assertNull(employee.getName());
        assertNull(employee.getSalary());
        assertNull(employee.getAge());
        assertNull(employee.getTitle());
        assertNull(employee.getEmail());
    }
}
