package com.github.questionarium.model;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "tokens")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Token {

    @Id
    private String id;
    private Long userId;
    private String token;
    private LocalDateTime validUntil;

}
