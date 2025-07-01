export interface QuestionDTO {
    id: number | null;
    multipleChoice: boolean;
    numberLines: number;
    educationLevel: number;
    userId: number;
    header: string;
    header_image?: string | null;
    answerId: number | null;
    enable: boolean;
    accessLevel: number;
    tagIds: number[] | null;
    creationDateTime: Date;
    updateDateTime: Date;
    // alternatives: Alternative[];
}


/*
  ENSINO_FUNDAMENTAL(0),
    ENSINO_MEDIO(1),
    ENSINO_SUPERIOR(2);

*/