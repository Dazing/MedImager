import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http, Response, RequestOptions} from '@angular/http';
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

	register(email:string, password:string): Observable<Boolean> {
		var url = (this.server.getUrl()+"/register");
		var data = {
			username: email,
			password: password
		}

		return this.http.post(url, data)
			.map((response: Response) => {
				// login successful if there's a jwt token in the response
				let user = response.json();
				console.log(JSON.stringify(user,null,1));
				
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
			}).catch((error: any) => {
				return Observable.throw(false);
			});
	}
	
	login(username:string, password:string): Observable<Boolean> {
		var url = (this.server.getUrl()+"/user/login");

		let headers = new Headers();

		headers.append('username', username);
		headers.append('password', password);

		let options = new RequestOptions({ headers: headers });

		let loginSuccess = false;

		console.log(headers.toJSON());
		
		
		return this.http.post(url, null, options)
			.map((response: Response) => {
				console.log("getting response");
				
				// login successful if there's a jwt token in the response
				if (response.toString()) {
					// store email and jwt token in local storage to keep user logged in between page refreshes
					localStorage.setItem('currentUser', response.toString());
 
					// return true to indicate successful login
					return true;
				} 
				else {
					// return false to indicate failed login
					return false;
				}
			}).catch((error: any) => {
				console.log(error);
				
				return Observable.throw(error);
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