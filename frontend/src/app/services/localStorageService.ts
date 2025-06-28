import { Injectable } from "@angular/core";
import { UserData } from "../types/dto/UserData";


@Injectable({
    providedIn:"root",
})
export class LocalStorageService{

    public setUserToken(userToken:string){
        localStorage.setItem("token",userToken);
    }

    public getUserToken() : string | null {
        return localStorage.getItem("token");
    }

    public clearUserToken(){
        localStorage.removeItem("token");
    }

    public getUserId() : number | null {
        return Number(localStorage.getItem("userId"));
    }
}