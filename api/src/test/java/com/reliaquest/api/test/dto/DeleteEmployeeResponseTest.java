package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.reliaquest.api.dto.DeleteEmployeeResponse;
import org.junit.jupiter.api.Test;

public class DeleteEmployeeResponseTest {

    @Test
    public void testSetAndGetSuccess() {
        // Arrange
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();

        // Act
        response.setSuccess(true);
        boolean retrievedSuccess = response.isSuccess();

        // Assert
        assertEquals(true, retrievedSuccess);

        // Act again with a different value
        response.setSuccess(false);
        retrievedSuccess = response.isSuccess();

        // Assert again
        assertEquals(false, retrievedSuccess);
    }

    @Test
    public void testDefaultConstructor() {
        // Act
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();

        // Assert
        assertNotNull(response);
        assertEquals(false, response.isSuccess()); // Default value should be false
    }

    @Test
    public void testSetSuccessToTrue() {
        // Arrange
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();

        // Act
        response.setSuccess(true);

        // Assert
        assertEquals(true, response.isSuccess());
    }

    @Test
    public void testSetSuccessToFalse() {
        // Arrange
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();

        // Act
        response.setSuccess(false);

        // Assert
        assertEquals(false, response.isSuccess());
    }

    @Test
    public void testMultipleSetCalls() {
        // Arrange
        DeleteEmployeeResponse response = new DeleteEmployeeResponse();

        // Act
        response.setSuccess(true);
        response.setSuccess(false);
        response.setSuccess(true);

        // Assert
        assertEquals(true, response.isSuccess());
    }
}
