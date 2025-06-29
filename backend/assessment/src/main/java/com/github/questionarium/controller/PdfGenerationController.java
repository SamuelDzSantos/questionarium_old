package com.github.questionarium.controller;

import java.io.IOException;

import javax.swing.JTable.PrintMode;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.service.AppliedAssessmentService;
import com.github.questionarium.service.PdfService;
import com.google.zxing.WriterException;
import com.pdftron.pdf.HTML2PDF;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PrinterMode;
import com.pdftron.sdf.SDFDoc;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pdf")
public class PdfGenerationController {

    private final PdfService pdfService;
    private final AppliedAssessmentService appliedAssessmentService;

    @GetMapping("/html")
    public String getHtml(@RequestParam Long id, @RequestParam Long userId,
            Model model, HttpServletResponse response) throws IOException, WriterException {
        System.out.println("Chamdada html");
        String html = pdfService.getHtmlPage(id, userId, model);
        return html;
    }

    @GetMapping("/footer")
    public String getFooterHtml() {
        return "Hi";
    }

    @GetMapping()
    public void generatePdf(@RequestParam Long appliedId, @RequestParam Long userId) {

        AppliedAssessment assessment = appliedAssessmentService.findById(appliedId, userId, true);
        System.out.println(assessment);

        String output_path = "../pdfs/html2pdf_example";
        String host = "http://localhost:14000";
        String page0 = "/pdf/html?id=1&userId=1";

        String page1 = "/pdf";

        try {
            HTML2PDF.setModulePath("./lib/HTML2PDFWindows");
            if (!HTML2PDF.isModuleAvailable()) {
                System.out.println();
                System.out.println("Unable to run HTML2PDFTest: Apryse SDK HTML2PDF module not available.");
                System.out.println("---------------------------------------------------------------");
                System.out.println("The HTML2PDF module is an optional add-on, available for download");
                System.out.println(
                        "at https://docs.apryse.com/core/guides/info/modules. If you have already downloaded this");
                System.out.println("module, ensure that the SDK is able to find the required files");
                System.out.println("using the HTML2PDF.setModulePath() function.");
                System.out.println();
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try (PDFDoc doc = new PDFDoc()) {
            // now convert a web page, sending generated PDF pages to doc

            try (HTML2PDF converter = new HTML2PDF()) {
                converter.insertFromURL(host + page0);
                converter.setPaperSize(PrinterMode.e_A4);
                converter.convert(doc);
            }

            // HTML2PDF.convert(doc, host + page0);
            doc.save(output_path + "_02.pdf", SDFDoc.SaveMode.LINEARIZED, null);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

}
