export interface Alternative{
    id: number | null;
    option: string;
    description: string;
    imagePath: string;
    isCorrect: boolean;
    explanation: string;
    question_id: number | null;
}

    