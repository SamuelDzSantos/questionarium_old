import { ReportRecords } from "./report-records";

export interface AppliedReport {
    date: Date, classroom: string, course: string, totalRecords: number,
    correctedRecords: number, pendingRecords: number, tags: string[], records: ReportRecords[]
}
