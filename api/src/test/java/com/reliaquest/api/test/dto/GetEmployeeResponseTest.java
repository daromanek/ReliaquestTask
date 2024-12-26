package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.*;

import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GetEmployeeResponseTest {

    @Test
    public void testSetAndGetData() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Jane Doe");

        // Act
        response.setData(employee);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertEquals(employee.getId(), retrievedEmployee.getId());
        assertEquals(employee.getName(), retrievedEmployee.getName());
    }

    @Test
    public void testSetDataToNull() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Jane Doe");
        response.setData(employee);

        // Act
        response.setData(null);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertNull(retrievedEmployee);
    }

    @Test
    public void testGetDataWhenNotSet() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();

        // Act
        Employee retrievedEmployee = response.getData();

        // Assert
        assertNull(retrievedEmployee);
    }

    @Test
    public void testSetDataWithDifferentEmployee() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee1 = new Employee();
        employee1.setId(UUID.randomUUID());
        employee1.setName("Jane Doe");
        response.setData(employee1);

        // Act
        Employee employee2 = new Employee();
        employee2.setId(UUID.randomUUID());
        employee2.setName("John Smith");
        response.setData(employee2);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertEquals(employee2.getId(), retrievedEmployee.getId());
        assertEquals(employee2.getName(), retrievedEmployee.getName());
        assertNotEquals(employee1.getId(), retrievedEmployee.getId());
        assertNotEquals(employee1.getName(), retrievedEmployee.getName());
    }

    @Test
    public void testSetDataWithDefaultEmployee() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee = new Employee(); // Assuming Employee has a default constructor

        // Act
        response.setData(employee);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertNotNull(retrievedEmployee);
        assertNull(retrievedEmployee.getId());
        assertNull(retrievedEmployee.getName());
    }

    @Test
    public void testEqualsAndHashCode() {
        // Arrange
        GetEmployeeResponse response1 = new GetEmployeeResponse();
        GetEmployeeResponse response2 = new GetEmployeeResponse();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Jane Doe");

        // Act
        response1.setData(employee);
        response2.setData(employee);

        // Assert
        assertEquals(response1, response2);
        assertEquals(response1.hashCode(), response2.hashCode());
    }

    @Test
    public void testToString() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID());
        employee.setName("Jane Doe");
        response.setData(employee);

        // Act
        String expectedString = "GetEmployeeResponse(data=Employee(id=" + employee.getId() + ", name="
                + employee.getName() + ", salary=null, age=null, title=null, email=null))";

        // Assert
        assertEquals(expectedString, response.toString());
    }
}
