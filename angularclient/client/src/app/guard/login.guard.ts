import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';


@Injectable()
export class LoginGuard implements CanActivate {

	constructor(
		private userService: UserService,
		private router: Router
	) {

	}

	canActivate(): boolean {
		if (sessionStorage.getItem('currentUser')) {
			return false;
		}
		else {
			this.router.navigate(['/search']);
			return true;
		}	
	}
}