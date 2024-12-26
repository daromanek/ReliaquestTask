package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.UUID;
import org.junit.jupiter.api.Test;

public class GetEmployeeResponseTest {

    @Test
    public void testSetAndGetData() {
        // Arrange
        GetEmployeeResponse response = new GetEmployeeResponse();
        Employee employee = new Employee(); // Assuming Employee has a default constructor
        employee.setId(UUID.randomUUID()); // Set a random UUID
        employee.setName("Jane Doe"); // Assuming Employee has a name field and a setter

        // Act
        response.setData(employee);
        Employee retrievedEmployee = response.getData();

        // Assert
        assertEquals(employee.getId(), retrievedEmployee.getId());
        assertEquals(employee.getName(), retrievedEmployee.getName());
    }
}
