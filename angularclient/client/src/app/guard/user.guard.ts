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
		if (localStorage.getItem('currentUser')) {
			return true;
		}
		else {
			this.router.navigate(['/login']);
			return false;
		}	
	}
}