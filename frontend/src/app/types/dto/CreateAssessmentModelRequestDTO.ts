import { QuestionDTO } from "./QuestionDTO";
import { QuestionWeight } from "./QuestionWeight";

export interface CreateAssessmentModelRequestDTO {
    description: string;
    institution: string;
    course: string;
    classroom: string;
    professor: string;
    instructions: string;
    image: string;
    questions: QuestionWeight[]

}