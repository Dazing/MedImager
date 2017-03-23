import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { UserService } from '../service/user.service';

@Injectable()
export class UserGuard implements CanActivate {

	constructor(private userService: UserService) {}

	canActivate(): boolean {
		console.log("CanActivate");
		
		return this.userService.isLoggedIn();
	}
}