package com.reliaquest.api.service;

import com.reliaquest.api.dto.CreateEmployeeResponse;
import com.reliaquest.api.dto.DeleteEmployeeResponse;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.model.Employee;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;

    // Constructor injection of RestTemplate
    public EmployeeService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate; // Use the configured RestTemplate
    }

    // Method to retrieve all employees
    public List<Employee> getAllEmployees() {
        ResponseEntity<GetAllEmployeesResponse> response =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null, GetAllEmployeesResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody().getData();
        }
        return List.of(); // Return an empty list if the response is not OK
    }

    // Method to search employees by name
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        // Call the getAllEmployees method to retrieve all employees
        List<Employee> employees = getAllEmployees();

        // Filter the employees based on the search string (case-insensitive)
        return employees.stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Method to retrieve an employee by ID
    public Employee getEmployeeById(String id) {
        try {
            ResponseEntity<GetEmployeeResponse> response =
                    restTemplate.getForEntity(BASE_URL + "/" + id, GetEmployeeResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return response.getBody().getData(); // Return the Employee object
            }
        } catch (HttpClientErrorException e) {
            // Handle not found exception
            System.err.println("Employee not found: " + e.getMessage());
        }
        return null; // Return null if the employee is not found or an error occurs
    }

    // Method to get the highest salary among employees
    public Integer getHighestSalaryOfEmployees() {
        // Retrieve the list of all employees
        List<Employee> employees = getAllEmployees();

        // Use a stream to find the maximum salary
        return employees.stream()
                .map(Employee::getSalary) // Map to the salary of each employee
                .max(Integer::compare) // Find the maximum salary
                .orElse(0); // Return 0 if no employees found
    }

    // Method to get the top ten highest earning employee names
    public List<String> getTopTenHighestEarningEmployeeNames() {
        // Retrieve the list of all employees
        List<Employee> employees = getAllEmployees();

        // Use a stream to sort, limit, and collect the names of the top ten highest earners
        return employees.stream()
                .sorted((e1, e2) -> e2.getSalary().compareTo(e1.getSalary())) // Sort in descending order by salary
                .limit(10) // Limit to the top 10 highest earners
                .map(Employee::getName) // Map to employee names
                .collect(Collectors.toList()); // Collect the names into a list
    }

    // Method to create a new employee
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(employeeDTO);
        ResponseEntity<CreateEmployeeResponse> response =
                restTemplate.postForEntity(BASE_URL, requestEntity, CreateEmployeeResponse.class);

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            return response.getBody().getData(); // Return the created Employee object
        }
        return null; // Return null if creation failed
    }

    // Method to delete an employee by ID
    public boolean deleteEmployeeById(String id) {
        try {
            ResponseEntity<DeleteEmployeeResponse> response =
                    restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, null, DeleteEmployeeResponse.class);
            return response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().isSuccess();
        } catch (HttpClientErrorException e) {
            // Handle not found exception
            System.err.println("Error deleting employee: " + e.getMessage());
            return false; // Return false if deletion failed
        }
    }
}
