import { Alternative } from "./Alternative";
import { QuestionHeader } from "./QuestionHeader";

export interface Question {
    id: number;
    multipleChoice: boolean;
    numberLines: number;
    personId: number;
    header: QuestionHeader;
    answerId: number;
    difficultyLevel: number;
    enable: boolean;
    educationLevel: number;
    accessLevel: number;
    tagIds: number[];
    alternatives: Alternative[];
}