package com.github.questionarium.service;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.github.questionarium.model.AppliedAssessment;
import com.github.questionarium.model.QuestionSnapshot;
import com.github.questionarium.model.RecordAssessment;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.pdftron.common.PDFNetException;
import com.pdftron.pdf.HTML2PDF;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.pdf.PrinterMode;
import com.pdftron.sdf.SDFDoc;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PdfService {

    private final RecordAssessmentService recordService;
    private final SpringTemplateEngine templateEngine;
    private final RecordAssessmentService recordAssessmentService;

    public String getHtmlPage(Long id, Long userId, Model model) throws IOException, WriterException {

        RecordAssessment record = recordService.findById(id, userId, true);
        AppliedAssessment assessment = record.getAppliedAssessment();

        System.out.println(record.getQuestionOrder());

        List<QuestionSnapshot> questions = assessment.getQuestionSnapshots();

        List<QuestionSnapshot> ordered = new ArrayList<>();

        for (Long orderId : record.getQuestionOrder()) {

            QuestionSnapshot q = questions.stream().filter(q2 -> q2.getQuestion().equals(orderId)).findFirst()
                    .orElse(null);
            ordered.add(q);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> instructions = List.of(assessment.getInstructions());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String codigo = padLeftZeros(record.getId().toString(), 4);

        BitMatrix bitMatrix = qrCodeWriter.encode(record.getId().toString(), BarcodeFormat.QR_CODE, 500, 350);
        var image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        String imagemBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        model.addAttribute("qrcode", imagemBase64);

        model.addAttribute("logoUrl", "https://picsum.photos/200/300");
        model.addAttribute("instituicao", assessment.getInstitution());
        model.addAttribute("curso", assessment.getCourse());
        model.addAttribute("classe", assessment.getClassroom());
        model.addAttribute("professor", assessment.getProfessor());
        model.addAttribute("departamento", assessment.getDepartment());
        model.addAttribute("data", LocalDateTime.now().format(formatter));
        model.addAttribute("instrucoes", instructions);
        model.addAttribute("instrucoes_footer", "Boa prova!");
        model.addAttribute("questoes", ordered);
        model.addAttribute("codigo", codigo);
        String html = renderHtml("Modelo", model);

        return html;
    }

    public String generateFooter(Long id, Long userId, Model model) throws IOException, WriterException {

        RecordAssessment record = recordService.findById(id, userId, true);
        AppliedAssessment assessment = record.getAppliedAssessment();

        System.out.println(record.getQuestionOrder());

        List<QuestionSnapshot> questions = assessment.getQuestionSnapshots();
        System.out.println("_---------------------------------_");
        System.out.println(questions);
        System.out.println("Questions");

        List<QuestionSnapshot> ordered = new ArrayList<>();

        for (Long orderId : record.getQuestionOrder()) {

            QuestionSnapshot q = questions.stream().filter(q2 -> q2.getQuestion().equals(orderId)).findFirst()
                    .orElse(null);
            ordered.add(q);
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        List<String> instructions = List.of(assessment.getInstructions());

        QRCodeWriter qrCodeWriter = new QRCodeWriter();

        String codigo = padLeftZeros(record.getId().toString(), 4);

        BitMatrix bitMatrix = qrCodeWriter.encode(record.getId().toString(), BarcodeFormat.QR_CODE, 500, 350);
        var image = MatrixToImageWriter.toBufferedImage(bitMatrix);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, "png", baos);

        String imagemBase64 = Base64.getEncoder().encodeToString(baos.toByteArray());

        model.addAttribute("qrcode", imagemBase64);
        model.addAttribute("quantidade", ordered.size());
        model.addAttribute("logoUrl", "https://picsum.photos/200/300");
        model.addAttribute("instituicao", assessment.getInstitution());
        model.addAttribute("curso", assessment.getCourse());
        model.addAttribute("classe", assessment.getClassroom());
        model.addAttribute("professor", assessment.getProfessor());
        model.addAttribute("departamento", assessment.getDepartment());
        model.addAttribute("data", LocalDateTime.now().format(formatter));
        model.addAttribute("instrucoes", instructions);
        model.addAttribute("instrucoes_footer", "Boa prova!");
        model.addAttribute("questoes", ordered);
        model.addAttribute("codigo", codigo);
        String html = renderHtml("Footer", model);

        return html;

    }

    public ByteArrayResource generatePdf(Long appliedId, Long userId) {

        // AppliedAssessment assessment = appliedAssessmentService.findById(appliedId,
        // userId, true);
        List<RecordAssessment> records = recordAssessmentService.findByAppliedAssessment(appliedId, userId, true);

        String output_path = "../pdfs/html2pdf_example";
        String host = "http://localhost:14000";

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
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        try (PDFDoc doc = new PDFDoc()) {
            // now convert a web page, sending generated PDF pages to doc

            try (HTML2PDF converter = new HTML2PDF()) {

                for (RecordAssessment record : records) {

                    String page0 = "/pdf/html?id=" + record.getId() + "&userId=" + userId;
                    String page1 = "/pdf/footer?id=" + record.getId() + "&userId=" + userId;

                    converter.insertFromURL(host + page0);
                    converter.setPaperSize(PrinterMode.e_A4);
                    converter.convert(doc);

                    converter.insertFromURL(host + page1);
                    converter.setPaperSize(PrinterMode.e_A4);
                    converter.convert(doc);
                }

            }

            // HTML2PDF.convert(doc, host + page0);
            doc.save(output_path + "_02.pdf", SDFDoc.SaveMode.LINEARIZED, null);

            File file = new File("../pdfs/html2pdf_example_02.pdf"); // Adjust path as needed

            ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(file.toPath()));
            return resource;
        } catch (PDFNetException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private String renderHtml(String templateName, Model model) {
        Context context = new Context();
        context.setVariables(model.asMap());
        return templateEngine.process(templateName, context);
    }

    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }

}
