import { UserData } from "./UserData"



export interface AuthResponse {

    userData: UserData,
    accessToken: string,
    refreshToken: string
}