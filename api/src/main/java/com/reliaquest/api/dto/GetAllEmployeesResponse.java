package com.reliaquest.api.dto;

import com.reliaquest.api.model.Employee;
import java.util.ArrayList;
import java.util.List;

public class GetAllEmployeesResponse {
    private List<Employee> data;

    // Getters and Setters
    public List<Employee> getData() {
        return data == null ? null : new ArrayList<>(data); // Return a new copy of the list
    }

    public void setData(List<Employee> data) {
        this.data = data == null ? null : new ArrayList<>(data); // Store a copy of the list
    }
}
