package com.reliaquest.api.service;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.dto.CreateEmployeeResponse;
import com.reliaquest.api.dto.DeleteEmployeeResponse;
import com.reliaquest.api.dto.DeleteMockEmployeeInput;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.logger.AppLogger;
import com.reliaquest.api.model.Employee;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class EmployeeService {
    private final String BASE_URL = "http://localhost:8112/api/v1/employee";
    private final RestTemplate restTemplate;
    protected final AppLogger logger; // Logger instance for logging messages
    private final RetryTemplate retryTemplate; // Inject RetryTemplate

    // Constructor injection of RestTemplate and AppLoggerProperties
    public EmployeeService(
            RestTemplate restTemplate, AppLoggerProperties loggerProperties, RetryTemplate retryTemplate) {
        this.restTemplate = restTemplate; // Use the configured RestTemplate
        this.retryTemplate = retryTemplate; // Use the configured RestTemplate
        this.logger = new AppLogger(EmployeeService.class); // Create a new logger for this service
        // Set the log level based on the configuration property
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));
    }

    public List<Employee> getAllEmployees() {
        logger.debug("Entering getAllEmployees method");
        ResponseEntity<GetAllEmployeesResponse> response =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null, GetAllEmployeesResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            logger.info("Exiting getAllEmployees method with success");
            List<Employee> employees = response.getBody().getData();
            return response.getBody().getData();
        }
        logger.info("Exiting getAllEmployees method with no employees found");
        return List.of(); // Return an empty list if the response is not OK
    }

    // Method to search employees by name
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        logger.info("Entering getEmployeesByNameSearch method with searchString: " + searchString);
        List<Employee> employees = getAllEmployees();

        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());

        logger.info("Exiting getEmployeesByNameSearch method with results: " + filteredEmployees);
        return filteredEmployees;
    }

    // Method to retrieve an employee by ID
    public Employee getEmployeeById(String id) {
        logger.debug("Entering getEmployeeById method with id: " + id);
        try {
            ResponseEntity<GetEmployeeResponse> response =
                    restTemplate.getForEntity(BASE_URL + "/" + id, GetEmployeeResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.info("Exiting getEmployeeById method with success");
                return response.getBody().getData(); // Return the Employee object
            }
        } catch (HttpClientErrorException e) {
            logger.error("Employee not found: {}" + e.getMessage(), e);
        }
        logger.info("Exiting getEmployeeById method with no employee found");
        return null; // Return null if the employee is not found or an error occurs
    }

    // Method to get the highest salary among employees
    public Integer getHighestSalaryOfEmployees() {
        logger.debug("Entering getHighestSalaryOfEmployees method");
        List<Employee> employees = getAllEmployees();
        Integer highestSalary = employees.stream()
                .map(Employee::getSalary)
                .max(Integer::compare)
                .orElse(0); // Return 0 if no employees found
        logger.info("Exiting getHighestSalaryOfEmployees method with highest salary: " + highestSalary);
        return highestSalary;
    }

    // Method to get the top ten highest earning employee names
    public List<String> getTopTenHighestEarningEmployeeNames() {
        logger.debug("Entering getTopTenHighestEarningEmployeeNames method");
        List<Employee> employees = getAllEmployees();
        List<String> topTenNames = employees.stream()
                .sorted((e1, e2) -> e2.getSalary().compareTo(e1.getSalary()))
                .limit(10)
                .map(Employee::getName)
                .collect(Collectors.toList());
        logger.info("Exiting getTopTenHighestEarningEmployeeNames method with # of names: " + topTenNames);
        return topTenNames;
    }

    // Method to create a new employee
    @CacheEvict(value = "employees", allEntries = true) // Invalidate the cache
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        logger.debug("Entering createEmployee method with employeeDTO: " + employeeDTO);
        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(employeeDTO);
        logger.debug("CreateEmployee requestEntity: " + requestEntity);
        ResponseEntity<CreateEmployeeResponse> response =
                restTemplate.postForEntity(BASE_URL, requestEntity, CreateEmployeeResponse.class);

        // We are getting an incorrect response code of OK instead of CREATED so while it is incorrect I have corrected
        // for it on the api side
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            logger.info("Exiting createEmployee method with created employee:"
                    + response.getBody().getData());
            return response.getBody().getData(); // Return the created Employee object
        }
        logger.error(
                "Failed to create employee. Status: " + response.getStatusCode() + ", Body: " + response.getBody());
        return null; // Return null if creation failed
    }

    // Method to delete an employee by ID
    // Sent in the employee id in the request body to match the MockEmployeeController rather than as a request
    // paramater which is the standard way of doing this
    @CacheEvict(value = "employees", allEntries = true) // Invalidate the cache
    public boolean deleteEmployeeById(String id) {
        logger.error("Entering deleteEmployeeById method with id: " + id);

        // Fetch all employees
        List<Employee> employees = getAllEmployees();

        // Find the employee by ID
        Employee employeeToDelete = employees.stream()
                .filter(employee -> employee.getId().toString().equals(id)) // Assuming ID is a UUID
                .findFirst()
                .orElse(null);

        if (employeeToDelete == null) {
            logger.error("Employee with ID " + id + " not found.");
            return false; // Employee not found, handle as needed
        }

        // Create the input DTO for deletion
        DeleteMockEmployeeInput input = new DeleteMockEmployeeInput();
        input.setName(employeeToDelete.getName()); // Set the name in the DTO

        // Call the MockEmployeeController's delete endpoint
        ResponseEntity<DeleteEmployeeResponse> response = restTemplate.exchange(
                BASE_URL, HttpMethod.DELETE, new HttpEntity<>(input), DeleteEmployeeResponse.class);

        boolean success = response.getStatusCode() == HttpStatus.OK
                && response.getBody() != null
                && response.getBody().isSuccess();

        logger.info("Exiting deleteEmployeeById method with success: " + success);
        return success;
    }

    // Method to manually enforce clearing of the cache....this is useful for tests but could be useful in other
    // scenarios as well
    @CacheEvict(value = "employees", allEntries = true)
    public void evictEmployeeCache() {
        // This method can be empty; its purpose is to trigger the cache eviction
    }
}
