package com.reliaquest.api.test.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.reliaquest.api.dto.CreateEmployeeResponse;
import com.reliaquest.api.dto.DeleteEmployeeResponse;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.model.Employee;
import com.reliaquest.api.service.EmployeeService;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class EmployeeServiceIntegrationTest {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() {
        // Arrange
        Employee employee1 = new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Developer", "john@example.com");
        Employee employee2 = new Employee(UUID.randomUUID(), "Jane Smith", 60000, 28, "Designer", "jane@example.com");
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        response.setData(Arrays.asList(employee1, employee2));

        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees);
        assertEquals(2, employees.size());
        assertEquals("John Doe", employees.get(0).getName());
        assertEquals("Jane Smith", employees.get(1).getName());
    }

    @Test
    public void testGetEmployeeById() {
        // Arrange
        UUID employeeId = UUID.randomUUID();
        Employee employee = new Employee(employeeId, "John Doe", 50000, 30, "Developer", "john@example.com");
        GetEmployeeResponse response = new GetEmployeeResponse();
        response.setData(employee);

        when(restTemplate.getForEntity(any(String.class), eq(GetEmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Act
        Employee foundEmployee = employeeService.getEmployeeById(employeeId.toString());

        // Assert
        assertNotNull(foundEmployee);
        assertEquals("John Doe", foundEmployee.getName());
    }

    @Test
    public void testGetHighestSalaryOfEmployees() {
        // Arrange
        Employee employee1 = new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Developer", "john@example.com");
        Employee employee2 = new Employee(UUID.randomUUID(), "Jane Smith", 60000, 28, "Designer", "jane@example.com");
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        response.setData(Arrays.asList(employee1, employee2));

        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Act
        Integer highestSalary = employeeService.getHighestSalaryOfEmployees();

        // Assert
        assertEquals(60000, highestSalary);
    }

    @Test
    public void testGetTopTenHighestEarningEmployeeNames() {
        // Arrange
        Employee employee1 = new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Developer", "john@example.com");
        Employee employee2 = new Employee(UUID.randomUUID(), "Jane Smith", 60000, 28, "Designer", "jane@example.com");
        Employee employee3 =
                new Employee(UUID.randomUUID(), "Alice Johnson", 70000, 35, "Manager", "alice@example.com");
        GetAllEmployeesResponse response = new GetAllEmployeesResponse();
        response.setData(Arrays.asList(employee1, employee2, employee3));

        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Act
        List<String> topEarningNames = employeeService.getTopTenHighestEarningEmployeeNames();

        // Assert
        assertNotNull(topEarningNames);
        assertEquals(3, topEarningNames.size());
        assertEquals("Alice Johnson", topEarningNames.get(0)); // Highest salary
        assertEquals("Jane Smith", topEarningNames.get(1));
        assertEquals("John Doe", topEarningNames.get(2));
    }

    @Test
    public void testCreateEmployee() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO("John Doe", 50000, 30, "Developer");
        CreateEmployeeResponse response = new CreateEmployeeResponse();
        Employee createdEmployee =
                new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Developer", "john@example.com");
        response.setData(createdEmployee);

        when(restTemplate.postForEntity(any(String.class), any(HttpEntity.class), eq(CreateEmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.CREATED));

        // Act
        Employee result = employeeService.createEmployee(employeeDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
    }

    @Test
    public void testDeleteEmployeeById() {
        // Arrange
        UUID employeeId = UUID.randomUUID();
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();
        response.setSuccess(true);

        when(restTemplate.exchange(any(String.class), eq(HttpMethod.DELETE), any(), eq(DeleteEmployeeResponse.class)))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        // Act
        boolean result = employeeService.deleteEmployeeById(employeeId.toString());

        // Assert
        assertTrue(result);
    }

    @Test
    public void testRetryAspect() {
        // Arrange
        Employee employee1 = new Employee(UUID.randomUUID(), "John Doe", 50000, 30, "Developer", "john@example.com");
        Employee employee2 = new Employee(UUID.randomUUID(), "Jane Smith", 60000, 28, "Designer", "jane@example.com");

        GetAllEmployeesResponse successfulResponse = new GetAllEmployeesResponse();
        successfulResponse.setData(Arrays.asList(employee1, employee2)); // Set the data

        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)) // First attempt
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR)) // Second attempt
                .thenReturn(new ResponseEntity<>(successfulResponse, HttpStatus.OK)); // Third attempt

        // Act
        List<Employee> employees = employeeService.getAllEmployees();

        // Assert
        assertNotNull(employees); // This should now pass
        assertEquals(2, employees.size()); // Check if the correct number of employees is returned
        verify(restTemplate, times(3)).exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class));
    }

    @Test
    public void testDefaultThreeRetries() {
        // Arrange
        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> employeeService.getAllEmployees());
        verify(restTemplate, times(20)).exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class));
    }

    @Test
    public void testRetryAndErrorHandlingAspectForGetAllEmployees() {
        // Arrange
        when(restTemplate.exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act and Assert
        // We want to call the method which has the AOP aspects
        assertThrows(HttpClientErrorException.class, () -> {
            employeeService.getAllEmployees();
        });

        // Verify that the method was retried the expected number of times
        verify(restTemplate, times(20)).exchange(any(String.class), any(), any(), eq(GetAllEmployeesResponse.class));
    }

    @Test
    public void testRetryAndErrorHandlingAspectForCreateEmployee() {
        // Arrange
        EmployeeDTO employeeDTO = new EmployeeDTO("John Doe", 50000, 30, "Developer");

        // Simulating bad request response
        when(restTemplate.postForEntity(
                        eq("http://localhost:8112/api/v1/employee"),
                        any(HttpEntity.class),
                        eq(CreateEmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            // This should trigger the retry logic
            employeeService.createEmployee(employeeDTO);
        });

        // Verify that the method was retried the expected number of times
        verify(restTemplate, times(20))
                .postForEntity(any(String.class), any(HttpEntity.class), eq(CreateEmployeeResponse.class));
    }

    @Test
    public void testErrorHandlingAspectForDeleteEmployee() {
        // Arrange
        String employeeId = UUID.randomUUID().toString(); // Generate a random UUID for the employee ID
        when(restTemplate.exchange(
                        eq("http://localhost:8112/api/v1/employee/" + employeeId), // Prepare for the specific endpoint
                        eq(HttpMethod.DELETE),
                        any(),
                        eq(DeleteEmployeeResponse.class)))
                .thenThrow(new HttpClientErrorException(
                        HttpStatus.INTERNAL_SERVER_ERROR)); // Simulate an internal server error

        // Act and Assert
        assertThrows(HttpClientErrorException.class, () -> {
            // This should trigger the retry logic if it's implemented in EmployeeService
            employeeService.deleteEmployeeById(employeeId); // Correctly reference the method
        });

        // Verify that the method was retried the expected number of times
        verify(restTemplate, times(20))
                .exchange(any(String.class), eq(HttpMethod.DELETE), any(), eq(DeleteEmployeeResponse.class));
    }
}
