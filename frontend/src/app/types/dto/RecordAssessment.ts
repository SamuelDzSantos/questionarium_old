import { Question } from './Question';

export interface RecordAssessment {
    id: number;
    appliedAssessmentId: number;
    instanceIndex: number;
    studentName: string;
    questionOrder: number[];
    questionSnapshots: Question[];
    totalScore: number;
    obtainedScore: number;
    correctAnswerKeyLetter: string[];
    studentAnswerKey: string[];
    creationDateTime: string;
    updateDateTime: string;
    active: boolean;
}
