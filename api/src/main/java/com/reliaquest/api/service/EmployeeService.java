package com.reliaquest.api.service;

import com.reliaquest.api.config.AppLoggerProperties;
import com.reliaquest.api.dto.CreateEmployeeResponse;
import com.reliaquest.api.dto.DeleteEmployeeResponse;
import com.reliaquest.api.dto.EmployeeDTO;
import com.reliaquest.api.dto.GetAllEmployeesResponse;
import com.reliaquest.api.dto.GetEmployeeResponse;
import com.reliaquest.api.logger.AppLogger;
import com.reliaquest.api.model.Employee;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
    protected final AppLogger logger; // Logger instance for logging messages

    // Constructor injection of RestTemplate and AppLoggerProperties
    public EmployeeService(RestTemplate restTemplate, AppLoggerProperties loggerProperties) {
        this.restTemplate = restTemplate; // Use the configured RestTemplate
        this.logger = new AppLogger(EmployeeService.class); // Create a new logger for this service
        // Set the log level based on the configuration property
        this.logger.setLogLevel(
                AppLogger.LogLevel.valueOf(loggerProperties.getLogLevel().toUpperCase()));
    }

    // Method to retrieve all employees
    // For scalability we could implement pagination on the MockEmployeeController.getEmployees() and
    // MockEmployeeService.getMockEmployees() to support this
    //     then we could use pagination parameters in the EmployeeService's call to the MockEmployeeController'e
    // endpoint
    @Cacheable(value = "employees")
    public List<Employee> getAllEmployees() {
        logger.debug("Entering getAllEmployees method");
        ResponseEntity<GetAllEmployeesResponse> response =
                restTemplate.exchange(BASE_URL, HttpMethod.GET, null, GetAllEmployeesResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            logger.debug("Exiting getAllEmployees method with success");
            return response.getBody().getData();
        }
        logger.debug("Exiting getAllEmployees method with no employees found");
        return List.of(); // Return an empty list if the response is not OK
    }

    // Method to search employees by name
    public List<Employee> getEmployeesByNameSearch(String searchString) {
        logger.debug("Entering getEmployeesByNameSearch method with searchString: " + searchString);
        List<Employee> employees = getAllEmployees();

        List<Employee> filteredEmployees = employees.stream()
                .filter(employee -> employee.getName().toLowerCase().contains(searchString.toLowerCase()))
                .collect(Collectors.toList());

        logger.debug("Exiting getEmployeesByNameSearch method with results: " + filteredEmployees.size());
        return filteredEmployees;
    }

    // Method to retrieve an employee by ID
    public Employee getEmployeeById(String id) {
        logger.debug("Entering getEmployeeById method with id: " + id);
        try {
            ResponseEntity<GetEmployeeResponse> response =
                    restTemplate.getForEntity(BASE_URL + "/" + id, GetEmployeeResponse.class);
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                logger.debug("Exiting getEmployeeById method with success");
                return response.getBody().getData(); // Return the Employee object
            }
        } catch (HttpClientErrorException e) {
            logger.error("Employee not found: {}" + e.getMessage(), e);
        }
        logger.debug("Exiting getEmployeeById method with no employee found");
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
        logger.debug("Exiting getHighestSalaryOfEmployees method with highest salary: " + highestSalary);
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
        logger.debug("Exiting getTopTenHighestEarningEmployeeNames method with # of names: " + topTenNames.size());
        return topTenNames;
    }

    // Method to create a new employee
    @CacheEvict(value = "employees", allEntries = true) // Invalidate the cache
    public Employee createEmployee(EmployeeDTO employeeDTO) {
        logger.debug("Entering createEmployee method with employeeDTO: " + employeeDTO);
        HttpEntity<EmployeeDTO> requestEntity = new HttpEntity<>(employeeDTO);
        ResponseEntity<CreateEmployeeResponse> response =
                restTemplate.postForEntity(BASE_URL, requestEntity, CreateEmployeeResponse.class);

        if (response.getStatusCode() == HttpStatus.CREATED && response.getBody() != null) {
            logger.debug("Exiting createEmployee method with created employee:"
                    + response.getBody().getData());
            return response.getBody().getData(); // Return the created Employee object
        }
        logger.error("Failed to create employee");
        return null; // Return null if creation failed
    }

    // Method to delete an employee by ID
    @CacheEvict(value = "employees", allEntries = true) // Invalidate the cache
    public boolean deleteEmployeeById(String id) {
        logger.debug("Entering deleteEmployeeById method with id: " + id);
        try {
            ResponseEntity<DeleteEmployeeResponse> response =
                    restTemplate.exchange(BASE_URL + "/" + id, HttpMethod.DELETE, null, DeleteEmployeeResponse.class);
            boolean success = response.getStatusCode() == HttpStatus.OK
                    && response.getBody() != null
                    && response.getBody().isSuccess();
            logger.debug("Exiting deleteEmployeeById method with success: " + success);
            return success;
        } catch (HttpClientErrorException e) {
            logger.error("Error deleting employee: " + e.getMessage(), e);
            return false; // Return false if deletion failed
        }
    }
}
