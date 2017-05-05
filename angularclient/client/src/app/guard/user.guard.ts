import { Injectable } from '@angular/core';
import { CanActivate } from '@angular/router';
import { UserService } from '../service/user.service';
import { Router } from '@angular/router';


@Injectable()
export class UserGuard implements CanActivate {

	constructor(
		private userService: UserService,
		private router: Router
	) {

	}

	canActivate(): boolean {
		if (sessionStorage.getItem('currentUser')) {
			return true;
		}
		else {
			this.router.navigate(['/']);
			return false;
		}	
	}
}