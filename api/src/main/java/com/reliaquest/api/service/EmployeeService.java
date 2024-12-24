package com.reliaquest.api.service;

import com.reliaquest.api.model.Employee;
import com.reliaquest.api.model.EmployeeDTO;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {

    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;

    public EmployeeService() {
        this.restTemplate = new RestTemplate();
    }

    // Method to retrieve all employees
    public List<Employee> getAllEmployees() {
        ResponseEntity<Employee[]> response = restTemplate.getForEntity(BASE_URL, Employee[].class);
        if (response.getStatusCode() == HttpStatus.OK) {
            return List.of(response.getBody());
        }
        return List.of();
    }

    // Method to search employees by name
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        List<Employee> employees = getAllEmployees();
        return employees.stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());
    }

    // Method to retrieve an employee by ID
    public Employee getEmployeeById(String id) {
        try {
            ResponseEntity<Employee> response = restTemplate.getForEntity(BASE_URL + "/" + id, Employee.class);
            if (response.getStatusCode() == HttpStatus.OK) {
                return response.getBody();
            }
        } catch (HttpClientErrorException e) {
            // Handle not found exception
            System.err.println("Employee not found: " + e.getMessage());
        }
        return null;
    }

    // Method to get the highest salary among employees
    public Integer getHighestSalaryOfEmployees() {
        List<Employee> employees = getAllEmployees();
        return employees.stream()
                .map(Employee::getSalary)
                .max(Integer::compare)
                .orElse(0); // Return 0 if no employees found
    }

    // Method to get the top ten highest earning employee names
    public List<String> getTopTenHighestEarningEmployeeNames() {
        List<Employee> employees = getAllEmployees();
        return employees.stream()
                .sorted((e1, e2) -> e2.getSalary().compareTo(e1.getSalary())) // Sort in descending order
                .limit(10) // Limit to top 10
                .map(Employee::getName)
                .collect(Collectors.toList());
    }

    // Method to create a new employee
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(employeeDTO);
        ResponseEntity<EmployeeDTO> response = restTemplate.postForEntity(BASE_URL, requestEntity, EmployeeDTO.class);
        if (response.getStatusCode() == HttpStatus.CREATED) {
            employeeDTO = response.getBody();
            Employee employee = new Employee();
            //          employee.setId(employeeDTO.getId());
            employee.setName(employeeDTO.getName());
            employee.setTitle(employeeDTO.getTitle());
            employee.setSalary(employeeDTO.getSalary());
            employee.setAge(employeeDTO.getAge());
            //          employee.setEmail() = employeeDTO.getEmail();
            return employee;
        }
        return null; // Return null if creation failed
    }

    // Method to delete an employee by ID
    public void deleteEmployeeById(String id) {
        try {
            restTemplate.delete(BASE_URL + "/" + id);
        } catch (HttpClientErrorException e) {
            // Handle not found exception
            System.err.println("Error deleting employee: " + e.getMessage());
        }
    }
}
