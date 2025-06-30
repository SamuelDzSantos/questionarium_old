import { QuestionWeight } from "./QuestionWeight";

export interface CreateAssessmentModelRequest {
  description: string;
  institution: string;
  department: string;
  course: string;
  classroom: string;
  professor: string;
  instructions: string;
  image: string;
  questions: QuestionWeight[];
}