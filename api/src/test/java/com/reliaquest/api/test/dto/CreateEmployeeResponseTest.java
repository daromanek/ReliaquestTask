package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.dto.CreateEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class CreateEmployeeResponseTest {

    @Test
    public void testSetAndGetData() {
        // Arrange
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        Employee employee = new Employee(); // Assuming Employee has a default constructor
        employee.setId(UUID.randomUUID()); // Set a random UUID
        employee.setName("John Doe"); // Assuming Employee has a name field and a setter

        // Act
        response.setData(employee);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertEquals(employee.getId(), retrievedEmployee.getId());
        assertEquals(employee.getName(), retrievedEmployee.getName());
    }

    @Test
    public void testSetDataNull() {
        // Arrange
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("John Doe");

        // Act
        response.setData(employee);
        response.setData(null); // Set data to null

        // Assert
        assertNull(response.getData());
    }

    @Test
    public void testGetDataWithoutSetting() {
        // Arrange
        CreateEmployeeResponse response = new CreateEmployeeResponse();

        // Act
        Employee retrievedEmployee = response.getData();

        // Assert
        assertNull(retrievedEmployee); // Should be null since we haven't set any data
    }

    @Test
    public void testSetAndGetDataWithDifferentEmployee() {
        // Arrange
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        Employee employee1 = new Employee();
        employee1.setId(UUID.randomUUID());
        employee1.setName("John Doe");

        Employee employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
        employee2.setName("Jane Smith");

        // Act
        response.setData(employee1);
        Employee retrievedEmployee1 = response.getData();

        // Assert
        assertEquals(employee1.getId(), retrievedEmployee1.getId());
        assertEquals(employee1.getName(), retrievedEmployee1.getName());

        // Act
        response.setData(employee2);
        Employee retrievedEmployee2 = response.getData();

        // Assert
        assertEquals(employee2.getId(), retrievedEmployee2.getId());
        assertEquals(employee2.getName(), retrievedEmployee2.getName());
        assertNotEquals(retrievedEmployee1.getId(), retrievedEmployee2.getId()); // Ensure they are different
    }

    @Test
    public void testDataConsistency() {
        // Arrange
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        Employee employee = new Employee();
        UUID id = UUID.randomUUID();
        employee.setId(id);
        employee.setName("John Doe");

        // Act
        response.setData(employee);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertSame(employee, retrievedEmployee); // Ensure the same instance is returned
    }
}
