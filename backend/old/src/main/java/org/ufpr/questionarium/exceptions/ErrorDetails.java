package org.ufpr.questionarium.exceptions;

import java.util.Date;

import lombok.Data;

@Data
public class ErrorDetails {
    private final Date timestamp;
    private final String message;
    private final String details;
}
