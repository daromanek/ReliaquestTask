package com.reliaquest.api.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class EmployeeDTO {
    private String name;
    private Integer salary;
    private Integer age;
    private String title;
}
