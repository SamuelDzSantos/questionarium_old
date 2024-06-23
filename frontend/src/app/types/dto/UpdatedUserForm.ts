

export interface UserPatch{
    name:string 
    email : string 
    password : string | null
    newPassword:string | null
}