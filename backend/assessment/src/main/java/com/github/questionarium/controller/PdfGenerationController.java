package com.github.questionarium.controller;

import java.io.IOException;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.service.PdfService;
import com.google.zxing.WriterException;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfGenerationController {

    private final PdfService pdfService;

    @GetMapping("/html")
    public String getHtml(@RequestParam Long id, @RequestParam Long userId,
            Model model, HttpServletResponse response) throws IOException, WriterException {
        String html = pdfService.getHtmlPage(id, userId, model);
        return html;
    }

    @GetMapping("/footer")
    public String getFooterHtml(@RequestParam Long id, @RequestParam Long userId,
            Model model, HttpServletResponse response) throws IOException, WriterException {
        String html = pdfService.generateFooter(id, userId, model);
        return html;
    }

    @GetMapping
    public ResponseEntity<ByteArrayResource> generatePdf(@RequestParam Long appliedId,
            @RequestHeader("X-User-Id") Long userId) {
        var resourse = pdfService.generatePdf(appliedId, userId);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .body(resourse);

    }
}
