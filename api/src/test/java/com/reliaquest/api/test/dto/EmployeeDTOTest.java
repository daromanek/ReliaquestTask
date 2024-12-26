package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    public void testDefaultConstructor() {
        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO(null, null, null, null);

        // Assert
        assertNull(employeeDTO.getName());
        assertNull(employeeDTO.getSalary());
        assertNull(employeeDTO.getAge());
        assertNull(employeeDTO.getTitle());
    }

    @Test
    public void testBuilderWithNoParameters() {
        // Act
        EmployeeDTO employeeDTO = EmployeeDTO.builder().build();

        // Assert
        assertNull(employeeDTO.getName());
        assertNull(employeeDTO.getSalary());
        assertNull(employeeDTO.getAge());
        assertNull(employeeDTO.getTitle());
    }

    @Test
    public void testNullValues() {
        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO(null, null, null, null);

        // Assert
        assertNull(employeeDTO.getName());
        assertNull(employeeDTO.getSalary());
        assertNull(employeeDTO.getAge());
        assertNull(employeeDTO.getTitle());
    }

    @Test
    public void testImmutability() {
        // Arrange
        EmployeeDTO original = EmployeeDTO.builder()
                .name("John Doe")
                .salary(50000)
                .age(30)
                .title("Software Engineer")
                .build();

        // Act
        EmployeeDTO modified = original.toBuilder().name("Jane Doe").build();

        // Assert
        assertNotEquals(original.getName(), modified.getName());
        assertEquals("John Doe", original.getName());
        assertEquals(50000, original.getSalary());
        assertEquals(30, original.getAge());
        assertEquals("Software Engineer", original.getTitle());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        EmployeeDTO employee1 = new EmployeeDTO("John Doe", 50000, 30, "Software Engineer");
        EmployeeDTO employee2 = new EmployeeDTO("John Doe", 50000, 30, "Software Engineer");

        // Assert
        assertEquals(employee1, employee2);
        assertEquals(employee1.hashCode(), employee2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO("John Doe", 50000, 30, "Software Engineer");

        // Act
        String expectedString = "EmployeeDTO(name=John Doe, salary=50000, age=30, title=Software Engineer)";

        // Assert
        assertEquals(expectedString, employeeDTO.toString());
    }

    @Test
    public void testNegativeSalary() {
        // Act
        EmployeeDTO employeeDTO = new EmployeeDTO("John Doe", -50000, 30, "Software Engineer");

        // Assert
        assertEquals("John Doe", employeeDTO.getName());
        assertEquals(-50000, employeeDTO.getSalary());
        assertEquals(30, employeeDTO.getAge());
        assertEquals("Software Engineer", employeeDTO.getTitle());
    }

    @Test
    public void testAgeBoundaryValues() {
        // Act
        EmployeeDTO employeeDTO1 = new EmployeeDTO("John Doe", 50000, 0, "Software Engineer");
        EmployeeDTO employeeDTO2 = new EmployeeDTO("Jane Doe", 60000, 150, "Product Manager");

        // Assert
        assertEquals(0, employeeDTO1.getAge());
        assertEquals(150, employeeDTO2.getAge());
    }
}
