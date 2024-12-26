package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.model.Employee;
import java.util.ArrayList;
import java.util.Collections;
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
        Employee employee1 = new Employee();
        employee1.setId(UUID.randomUUID());
        employee1.setName("John Doe");

        Employee employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
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

    @Test
    public void testSetDataWithNull() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();

        // Act
        response.setData(null);

        // Assert
        assertNull(response.getData());
    }

    @Test
    public void testSetDataWithEmptyList() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = new ArrayList<>();

        // Act
        response.setData(employees);

        // Assert
        assertEquals(0, response.getData().size());
    }

    @Test
    public void testSetDataWithSingleEmployee() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Alice Johnson");
        employees.add(employee);

        // Act
        response.setData(employees);

        // Assert
        assertEquals(1, response.getData().size());
        assertEquals(employee.getName(), response.getData().get(0).getName());
    }

    @Test
    public void testSetDataWithImmutableList() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = Collections.singletonList(new Employee());

        // Act
        response.setData(employees);

        // Assert
        assertEquals(1, response.getData().size());
    }

    @Test
    public void testGetDataReturnsNewList() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = new ArrayList<>();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Bob Brown");
        employees.add(employee);
        response.setData(employees);

        // Act
        List<Employee> retrievedEmployees = response.getData();
        retrievedEmployees.add(new Employee()); // Modify the retrieved list

        // Assert
        assertEquals(1, response.getData().size()); // Ensure original list is unchanged
    }

    @Test
    public void testGetDataWithMultipleEmployees() {
        // Arrange
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        List<Employee> employees = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Employee employee = new Employee();
            employee.setId(UUID.randomUUID());
            employee.setName("Employee " + i);
            employees.add(employee);
        }
        response.setData(employees);

        // Act
        List<Employee> retrievedEmployees = response.getData();

        // Assert
        assertEquals(5, retrievedEmployees.size());
        for (int i = 0; i < 5; i++) {
            assertEquals("Employee " + i, retrievedEmployees.get(i).getName());
        }
    }
}
