import { CustomQuestion } from "./CustomQuestion";

export interface CustomModel {
    description: string,
    institution: string,
    department: string,
    course: string,
    classroom: string,
    professor: string,
    instructions: string,
    image: string,
    questions: CustomQuestion[]
}