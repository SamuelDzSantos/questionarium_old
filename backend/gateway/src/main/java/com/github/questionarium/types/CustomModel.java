package com.github.questionarium.types;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomModel {

    private String description;
    private String institution;
    private String department;
    private String course;
    private String classroom;
    private String professor;
    private String instructions;
    private String image;
    private List<CustomQuestion> questions;

}
