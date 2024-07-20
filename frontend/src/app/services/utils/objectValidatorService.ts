import { Injectable } from "@angular/core";


@Injectable({
    providedIn:"root"
})
export class ObjectValidatorService{

    public hasEmptyOrBlankAttributes(object : Object) : boolean{
        
        for(let key in object){
            let value = object[key as keyof object];
            if(value == null || value == "" || value == undefined){
                return true;
            }
        }
        return false;
    }

}