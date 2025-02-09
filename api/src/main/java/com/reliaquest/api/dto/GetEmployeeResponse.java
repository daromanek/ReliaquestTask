package com.reliaquest.api.dto;

import com.reliaquest.api.model.Employee;
import lombok.Data;

@Data
public class GetEmployeeResponse {
    private Employee data;

    // Getters and Setters
    public Employee getData() {
        return data;
    }

    public void setData(Employee data) {
        this.data = data;
    }
}
