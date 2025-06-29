import { Alternative } from "./Alternative";

export interface Question {
    id: number | null;
    multipleChoice: boolean;
    numberLines: number;
    header: string;
    header_image?: string | null;
    answerId: number | null;
    enable: boolean;
    educationLevel: string;
    accessLevel: number;
    tagIds: number[] | null;
    alternatives: Alternative[];
}