import { OnInit } from '@angular/core';
import { Injectable } from '@angular/core';
import { Headers, Http } from '@angular/http';
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
	
	login(email:string, password:string): void {
		var url = (this.server.getUrl()+"/login");
		var data = {
			email: email,
			password: password
		}

		this.http.post(url,data)
			.toPromise()
			.then(response => {
				console.log("response");
			})
			.catch(this.handleError);
	}

	isLoggedIn(): boolean {
		console.log("isLoggedIn");

		if (this.sessionId) {
			return true;
		} else {
			return false;
		}
	}

	getSessionId(): string {
		return this.sessionId;
	}

	private handleError(error: any): Promise<any> {
		console.error('An error occurred', error); // for demo purposes only
		return Promise.reject(error.message || error);
	}	

}