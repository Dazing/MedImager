import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../service/user.service';

@Component({
	selector: 'login',
	templateUrl: '../template/login.component.html'
})

export class LoginComponent {
	private error = '';
	private setRegi: boolean = false;
	private model: any = {}

	constructor (
		private userService: UserService,
		private router: Router
	) {

	}

	login():void {
		if(this.model.mail && this.model.password) {
			this.userService.login(this.model.username, this.model.password)
				.subscribe(result => {
					if (result === true) {
						// login successful
						this.router.navigate(['/']);
					} else {
						// login failed
						this.error = 'Username or password is incorrect';
					}
				});
		} else {
			this.error = "Enter email and password"
		}
        
    }

	register():void {
		console.log("hello");
	}

	setRegister(): void {
		this.error = "";
		this.setRegi = !this.setRegi;
	}

}
