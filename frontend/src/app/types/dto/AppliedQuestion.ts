import { Alternative } from "./Alternative";
import { AppliedAssessment } from "./AppliedAssessment";

export interface AppliedQuestion {
    id: number;
    appliedAssessment: AppliedAssessment;
    idQuestion: number;
    order: number;
    multipleChoice: boolean;
    description: string;
    alternatives: Alternative[];
    answer: string;
    weight: number;
  }