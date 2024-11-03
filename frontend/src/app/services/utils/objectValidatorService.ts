import { Injectable } from "@angular/core";


export type WithNonNullableProperties<T, K extends keyof T> = T & {
    [X in K]-?: NonNullable<T[X]>;
};

export const hasNonNullableProperties = <T, K extends keyof T>(
    obj: T,
    ...keys: readonly K[]
): obj is WithNonNullableProperties<T, K> => {
    return keys.every(key => obj[key] != null);
};


@Injectable({
    providedIn: "root"
})
export class ObjectValidatorService {

    public hasEmptyOrBlankAttributes(object: Object) {

        for (let key in object) {
            let value = object[key as keyof object];
            if (value == null || value == "" || value == undefined) {
                return true;
            }
        }
        return false;
    }

    public hasNonNullableProperties = <T, K extends keyof T>(
        obj: T,
        ...keys: readonly K[]
    ): obj is WithNonNullableProperties<T, K> => {
        return keys.every(key => obj[key] != null);
    };

}