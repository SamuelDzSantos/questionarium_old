import { Question } from './Question';

export interface RecordAssessmentPublic {
  studentName: string;
  questionOrder: number[];
  questionSnapshots: Question[];
  correctAnswerKeyLetter: string[];
  studentAnswerKey: string[];
  obtainedScore: number;
}