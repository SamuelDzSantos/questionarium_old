import { QuestionWeight } from "./QuestionWeight";

export interface AssessmentModel {
  id: number;
  description: string;
  userId: number;
  institution: string;
  department: string;
  course: string;
  classroom: string;
  professor: string;
  instructions: string;
  image: string;
  questions: QuestionWeight[];
  creationDateTime: string; // ISO String
  updateDateTime: string; // ISO String
}