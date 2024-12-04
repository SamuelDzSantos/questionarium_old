package br.com.questionarium.assessment_service.controller;

@RestController
@RequestMapping("/applied-assessment")
public class AppliedAssessmentController {

    @Autowired
    private AppliedAssessmentService appliedAssessmentService;

    @PostMapping
    public ResponseEntity<String> createAppliedAssessment(
        @RequestParam Long id,
        @RequestParam int quantity,
        @RequestParam boolean shuffle,
        @RequestParam LocalDate applicationDate) {
            try {
                appliedAssessmentService.createAppliedAssessment(id, quantity, shuffle, applicationDate);
                return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Avaliação aplicada criada com sucesso!");
                } catch (RuntimeException e) {
                    // Tratamento para erros, retornando 404 caso algo não seja encontrado
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body(e.getMessage());
                } catch (Exception e) {
                    // Tratamento genérico para outros erros
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body("Erro ao criar avaliação aplicada: " + e.getMessage());
                }
        }

    
}
