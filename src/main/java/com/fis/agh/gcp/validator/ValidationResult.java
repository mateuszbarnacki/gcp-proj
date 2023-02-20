package com.fis.agh.gcp.validator;

import java.util.List;

public class ValidationResult {
    private final List<String> validationResults;

    public ValidationResult(List<String> validationResults) {
        this.validationResults = validationResults;
    }

    public boolean validate() {
        return validationResults.isEmpty();
    }

    public String getMessages() {
        return String.join("", validationResults);
    }
}
