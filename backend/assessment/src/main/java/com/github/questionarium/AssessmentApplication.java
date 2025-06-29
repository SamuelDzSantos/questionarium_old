package com.github.questionarium;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.pdftron.pdf.PDFNet;

@SpringBootApplication
public class AssessmentApplication {
    public static void main(String[] args) {
        PDFNet.initialize("demo:1751122921672:61ac5f54030000000013227849cc03819a690071769847b7c82216f1cc");
        SpringApplication.run(AssessmentApplication.class, args);
    }
}