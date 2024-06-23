package org.ufpr.questionarium.exceptions;

import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class ErrorDetailsMap {
    private final Date timestamp;
    private final String message;
    private final Map<String, String> details;
}
