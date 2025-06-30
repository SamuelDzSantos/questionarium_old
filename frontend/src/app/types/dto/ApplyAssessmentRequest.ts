export interface ApplyAssessmentRequest {
    modelId: number;
    quantity: number;
    applicationDate: string; // ou Date, mas string ISO é mais seguro para o backend Java
    shuffleQuestions: boolean;
}
