import { QuestionDTO } from "./QuestionDTO";

export interface CreateAssessmentModelRequestDTO {
    description: string;
    institution: string;
    course: string;
    classroom: string;
    professor: string;
    instructions: string;
    image: string;
    questions: QuestionDTO[]

}