export interface Alternative{
    id: number | null;
    description: string;
    imagePath: string;
    isCorrect: boolean;
    explanation: string;
    question_id: number | null;
    alternativeOrder: number;
}

    