import { Component } from '@angular/core';
import { Router } from '@angular/router';

import { UserService } from '../service/user.service';

@Component({
	selector: 'login',
	templateUrl: '../template/login.component.html'
})

export class LoginComponent {
	public error;
	public setRegi: boolean = false;
	public model: any = {}

	constructor (
		private userService: UserService,
		private router: Router
	) {

	}

	ngAfterViewInit(): void {
		this.userService.error.subscribe(error => {
			console.log("Sub on error @LoginComponent");
			
			this.error = error;
			console.log("error in sub:"+this.error);
			
		});
	}

	login():void {
		console.log("Running login@LoginComponent, model:"+ JSON.stringify(this.model,null,1));
		
		if(this.model.username && this.model.password) {
			try {
				console.log("Calling login@UserService");
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
			} catch (error) {
				console.log("Logging error @LoginComponent: "+error);
				
			}
			
		
		
		} else {
			this.error = "Enter email and password"
		}
        
    }

	register():void {
		if(
			this.model.username &&
			this.model.password &&
			this.model.firstname &&
			this.model.surname &&
			this.model.workplace
		) {
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
		}
		else {
			this.error = 'Complete the form';
		}
	}

	setRegister(): void {
		this.error = "";
		this.setRegi = !this.setRegi;
	}

}
