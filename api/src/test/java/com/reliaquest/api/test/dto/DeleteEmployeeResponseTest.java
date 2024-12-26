package com.reliaquest.api.test.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
