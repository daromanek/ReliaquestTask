package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.model.Employee;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GetAllEmployeesResponseTest {

    @Test
    public void testSetAndGetData() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = new ArrayList<>();

        // Create sample Employee objects
        Employee employee1 = new Employee(); // Use different variable names
        employee1.setId(UUID.randomUUID()); // Set a random UUID
        employee1.setName("John Doe");

        Employee employee2 = new Employee(); // Use different variable names
        employee2.setId(UUID.randomUUID()); // Set a random UUID
        employee2.setName("Jane Smith");

        // Add employees to the list
        employees.add(employee1);
        employees.add(employee2);

        // Act
        response.setData(employees);
        List<Employee> retrievedEmployees = response.getData();

        // Assert
        assertEquals(2, retrievedEmployees.size());
        assertEquals(employee1.getName(), retrievedEmployees.get(0).getName());
        assertEquals(employee2.getName(), retrievedEmployees.get(1).getName());
    }
}
