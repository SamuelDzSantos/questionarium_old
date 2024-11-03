import { UserService } from "./user.service";


export function startupServiceFactory(userService: UserService){
    return ()=> userService.initialize();
}