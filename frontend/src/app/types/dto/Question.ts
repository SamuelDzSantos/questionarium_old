import { Alternative } from "./Alternative";
import { QuestionHeader } from "./QuestionHeader";

export interface Question {
    id: number | null;
    multipleChoice: boolean;
    numberLines: number;
    personId: number;
    header: QuestionHeader;
    answerId: number | null;
    difficultyLevel: number;
    enable: boolean;
    educationLevel: number;
    accessLevel: number;
    tagIds: number[] | null;
    alternatives: Alternative[];
}