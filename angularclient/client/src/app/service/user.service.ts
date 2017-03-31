import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, Response } from '@angular/http';
import { Observable } from 'rxjs';
import { Subject} from 'rxjs/Subject';

import 'rxjs/add/operator/toPromise';
import 'rxjs/add/operator/map';

import { Image } from '../model/image';

import { Server } from '../model/server';



@Injectable()
export class UserService {

	private sessionId:string;

	constructor(private http: Http, private server: Server) {
	}

	register(email:string, password:string): void {
		var url = (this.server.getUrl()+"/register");

		var data = {
			email: email,
			password: password
		}

		this.http.post(url,data)
			.toPromise()
			.then(response => {
				console.log("Register sent succesfully!");
			})
			.catch(this.handleError);
	}
	
	login(email:string, password:string): Observable<Boolean> {
		var url = (this.server.getUrl()+"/login");
		var data = {
			email: email,
			password: password
		}

		return this.http.post('/api/authenticate', JSON.stringify({ email: email, password: password }))
			.map((response: Response) => {
				// login successful if there's a jwt token in the response
				let user = response.json();
				if (user.email && user.token) {
					// store email and jwt token in local storage to keep user logged in between page refreshes
					localStorage.setItem('currentUser', user);
 
					// return true to indicate successful login
					return true;
				} 
				else {
					// return false to indicate failed login
					return false;
				}
		});

		/*this.http.post(url,data)
			.toPromise()
			.then(response => {
				
					Response = {
						email = "email@doman.com",
						token = "cklmdfglskgfnlsdbgkbdsgkudfghrö214345938ujskjfsdkf"
					}
				
				console.log("response");
				var user = response.json();

				if (user.email && user.token) {
					localStorage.setItem('currUser',user);
				}
				else {

				}
			})
			.catch(this.handleError);*/
	}
	
	private handleError(error: any): Promise<any> {
		console.error('An error occurred', error); // for demo purposes only
		return Promise.reject(error.message || error);
	}	

}