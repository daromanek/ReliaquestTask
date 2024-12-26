package com.reliaquest.api.dto;

import com.reliaquest.api.model.Employee;
import java.util.List;

public class GetAllEmployeesResponse {
    private List<Employee> data;

    // Getters and Setters
    public List<Employee> getData() {
        return data;
    }

    public void setData(List<Employee> data) {
        this.data = data;
    }
}
