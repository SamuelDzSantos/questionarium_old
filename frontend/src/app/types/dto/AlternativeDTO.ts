export interface AlternativeDTO {
    id: number | null;
    description: string;
    imagePath: string;
    isCorrect: boolean;
    explanation: string;
    alternativeOrder?: number;
}

