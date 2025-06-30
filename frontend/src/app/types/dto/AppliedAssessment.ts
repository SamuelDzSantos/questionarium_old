import { AppliedQuestion } from "./AppliedQuestion";

export interface AppliedAssessment {
    id: number;
    originalAssessmentId: number;
    description: string;
    userId: number;
    institution?: string;
    department?: string;
    course?: string;
    classroom?: string;
    professor?: string;
    instructions?: string;
    image?: string;
    creationDate: string;
    applicationDate: string;
    quantity: number;
    status: boolean;
    shuffle: boolean;
    appliedQuestions: AppliedQuestion[];
}


